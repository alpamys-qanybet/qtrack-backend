package kz.essc.qtrack.client;

import kz.essc.qtrack.jms.CallingMessageProducer;
import kz.essc.qtrack.kiosk.KioskBean;
import kz.essc.qtrack.line.*;
import kz.essc.qtrack.operator.OperatorBean;
import kz.essc.qtrack.operator.OperatorWrapper;
import kz.essc.qtrack.sc.user.User;
import kz.essc.qtrack.user.UserBean;
//import kz.essc.qtrack.websocket.infotable.WsInfotableBean;
//import kz.essc.qtrack.websocket.monitoring.WsMonitoringBean;
//import kz.essc.qtrack.websocket.operator.WsOperatorBean;
//import kz.essc.qtrack.websocket.operator.display.WsOperatorDisplayBean;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.*;

@RequestScoped
public class ClientBean {

    @PersistenceContext
    EntityManager em;

//    @Inject
//    WsOperatorBean wsOperatorBean;

//    @Inject
//    WsInfotableBean wsInfotableBean;

//    @Inject
//    WsOperatorDisplayBean wsOperatorDisplayBean;

//    @Inject
//    WsMonitoringBean wsMonitoringBean;

    @Inject
    LineBean lineBean;

    @Inject
    UserBean userBean;

    @Inject
    CallingMessageProducer messageSender;

    @Inject
    OperatorBean operatorBean;

    public List<Client> get() {

        try {
            return em.createQuery("select c from Client c").getResultList();
        }
        catch (Exception e) {
//            e.printStackTrace();
            return null;
        }
    }

    public Client get(Long id) {
        try {
            return (Client) em.find(Client.class, id);
        }
        catch (Exception e) {
//            e.printStackTrace();
            return null;
        }
    }

    public Client add(Long lineId, ClientWrapper wrapper) {
        try {
            Line line = (Line) em.find(Line.class, lineId);
            Client client = new Client();
            Date now = new Date();

            for (User op: line.getOperators()) {
//                System.out.println("check old day client op " + op.getShortname());
                checkOldDayClient(op);
            }

//            for (User op: operatorBean.get()) {
//                System.out.println("check old day client op " + op.getShortname());
//                checkOldDayClient(op);
//            }

            if (line.getIsRaw()) {
                Date todayBegin = new Date();
                todayBegin.setHours(0);
                todayBegin.setMinutes(0);
                Date todayEnd = new Date();
                todayEnd.setHours(23);
                todayEnd.setMinutes(59);

                Long countTodayClients = (Long) em.createQuery("select count(c) from Client c " +
                                                            "where c.date >= :begin and c.date <= :end " +
                                                            "and c.line.id = :lineId")
                    .setParameter("begin", todayBegin)
                    .setParameter("end", todayEnd)
                    .setParameter("lineId", line.getId())
                    .getSingleResult();

                Long countTodayProcesses = (Long) em.createQuery("select count(p) from Process p "+
                                                                 "where p.begin >= :begin "+ // and p.begin <= :end
                                                                 "and p.lineId = :lineId")
                    .setParameter("begin", todayBegin)
//                    .setParameter("end", todayEnd)
                    .setParameter("lineId", line.getId())
                    .getSingleResult();

//                System.out.println("today clients " + (countTodayClients + countTodayProcesses));

                if ((countTodayClients + countTodayProcesses) == 0L) { // no clients today except deleted
//                    line.setCounter(line.getCounterBegin());
//                    line.setLimit(0);
                    line.setLimitAdditional(0);
//                    line.setClients(new ArrayList<Client>());
                    line.setSize(0);
                }

                if (line.getSize() >= line.getLimit() + line.getLimitAdditional())
                    return null;

                if ((countTodayClients + countTodayProcesses) == 0L) { // no clients today except deleted
                    line.setCounter(line.getCounterBegin());
                }
                else if (line.getCounter() == line.getCounterEnd() || line.getCounter() == 0L) // 999
                    line.setCounter(line.getCounterBegin()); // 0

                int counter = line.getCounter() + 1;
//                String code = generateCode(line.getPrefix(), counter);
                String code = generateCode(counter);

                int length = line.getLength() + 1;
                line.setSize(line.getSize()+1);

                client.setDate(now);
                client.setStatus(Client.Status.WAITING.toString());
                client.setLine(line);
                client.setCode(code);
                client.setOrder(length);
                client.setEvent(now);
                client.setLang(wrapper.getLang());

                em.persist(client);

                line.getClients().add(client);
                line.setCounter(counter);
                line.setLength(length);

                em.merge(line);
            }
            else {
                Date date = wrapper.getDate(); // specified date that come from UI

                Calendar cal1 = Calendar.getInstance();
                cal1.setTime(date);
                cal1.set(Calendar.SECOND, 0);
                cal1.set(Calendar.MILLISECOND, 0);

                Date dateCheckBegin = cal1.getTime();

                Calendar cal2 = Calendar.getInstance();
                cal2.setTime(date);
                cal2.set(Calendar.SECOND, 59);
                cal2.set(Calendar.MILLISECOND, 999);

                Date dateCheckEnd = cal2.getTime();

                Long count = (Long) em.createQuery("select count(c) from Client c "+
                        "where c.date >= :begin and c.date <= :end " +
                        "and c.line.id = :lineId")
                        .setParameter("begin", dateCheckBegin)
                        .setParameter("end", dateCheckEnd)
                        .setParameter("lineId", line.getId())
                        .getSingleResult();

                if (count > 0)
                    return null;

                LineAppointment la = lineBean.getLA(line.getId(), date);

                if (la.getCounter() == line.getCounterEnd() || la.getCounter() == 0) // 999
                    la.setCounter(line.getCounterBegin()); // 0

                int counter = la.getCounter() + 1;
//                String code = generateCode(line.getPrefix(), counter);
                String code = generateCode(counter);

                int length = la.getLength() + 1;

                client.setDate(date);
                client.setStatus(Client.Status.WAITING.toString());
                client.setLine(line);
                client.setCode(code);
                client.setOrder(length);
                client.setEvent(now);
                client.setLang(wrapper.getLang());

                em.persist(client);

                line.getClients().add(client);
                em.merge(line);

                la.setCounter(counter);
                la.setLength(length);
                em.merge(la);

                lineBean.orderAppointmentClients(line.getId(), date);
            }

            return client;
        }
        catch (Exception e) {
//            e.printStackTrace();

            return null;
        }
    }

    // not used
    public Client edit(Long id, ClientWrapper clientWrapper) {
        try {
            Client client = (Client) em.find(Client.class, id);
            return em.merge(client);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Client call(Long id, ClientWrapper wrapper, String login) {
        try {
            Date now = new Date();
            Client client = (Client) em.find(Client.class, id);
            User operator = (User) em.find(User.class, wrapper.getOperatorId());
            client.setOperator(operator);

            client.setStatus(Client.Status.CALLED.toString());
            Line line = client.getLine();

            List<Client> waitingClients = lineBean.getAvailableClients(line.getId());
            if (!waitingClients.isEmpty()) {
                int nextOrder = waitingClients.get(0).getOrder();
                if ( (nextOrder-1) != client.getOrder() ) {
                    client.setOrder(nextOrder - 1);
                }
            }
            client.setEvent(now);
            client = em.merge(client);

            operator.setClient(client);
            em.merge(operator);

//            JSONObject json = new JSONObject();
//
//            json.put("event", KioskBean.Event.OPERATOR_CALL_CLIENT.toString());
//            json.put("client", new JSONObject(ClientWrapper.wrap(client).toString()));
//            json.put("line", new JSONObject(LineWrapper.wrap(line).toString()));
//            json.put("clients", new JSONArray(ClientWrapper.wrap(lineBean.getAvailableClients(line.getId())).toString()));
//
//            User initiator = userBean.getUserByLogin(login);
//            json.put("initiator", new JSONObject(OperatorWrapper.wrapInherited(initiator).toString()));

//            wsOperatorBean.sendMessageOverSocket(json.toString(), "1");

//            wsMonitoringBean.sendMessageOverSocket(json.toString(), "1");

//            JSONObject jsonInfo = new JSONObject();
//            jsonInfo.put("event", KioskBean.Event.OPERATOR_CALL_CLIENT.toString());
//            List<ClientWrapper> list = ClientWrapper.wrap(getCalledAndInprocessClients());
//            jsonInfo.put("list", new JSONArray(list.toString()));
//
//            wsInfotableBean.sendMessageOverSocket(jsonInfo.toString(), "1");

//            JSONObject jsonDisplay = new JSONObject();
//            jsonDisplay.put("event", KioskBean.Event.OPERATOR_CALL_CLIENT.toString());
//            jsonDisplay.put("operator", new JSONObject(OperatorWrapper.wrapInherited(initiator).toString()));
//            jsonDisplay.put("client", new JSONObject(ClientWrapper.wrap(client).toString()));

//            wsOperatorDisplayBean.sendMessageOverSocket(jsonDisplay.toString(), "1");

            messageSender.sendMessage(ClientWrapper.wrap(client));

            return client;
        }
        catch (Exception e) {
//            e.printStackTrace();
            return null;
        }
    }

    public void skip(Long id, String login) {
        try {
            Client client = (Client) em.find(Client.class, id);
            Line line = client.getLine();

            List<Client> waitingClients = lineBean.getAvailableClients(line.getId());
            if (!waitingClients.isEmpty()) {
                Client next = (Client) em.find(Client.class, waitingClients.get(0).getId());
                int nextOrder = next.getOrder();
                next.setOrder(client.getOrder());
                client.setOrder(nextOrder);

                em.merge(next);
            }

           /* if (length > 1) {
                Client next = (Client) em.createQuery("select c from Client c where c.order = :order")
                        .setParameter("order", order+1).getSingleResult();
                client.setOrder(order+1);
                next.setOrder(order);
                em.merge(next);
            }*/

            User operator = client.getOperator();
            operator.setClient(null);
            em.merge(operator);

            client.setOperator(null);
            client.setStatus(Client.Status.WAITING.toString());
            em.merge(client);
//            client = em.merge(client);

//            JSONObject json = new JSONObject();
//            json.put("event", KioskBean.Event.SKIP_CLIENT.toString());
//            json.put("client", new JSONObject(ClientWrapper.wrap(client).toString()));
//            json.put("line", new JSONObject(LineWrapper.wrap(line).toString()));
//            json.put("clients", new JSONArray(ClientWrapper.wrap(lineBean.getAvailableClients(line.getId())).toString()));
//
//            User initiator = userBean.getUserByLogin(login);
//            json.put("initiator", new JSONObject(OperatorWrapper.wrapInherited(initiator).toString()));

//            wsOperatorBean.sendMessageOverSocket(json.toString(), "1");

//            wsMonitoringBean.sendMessageOverSocket(json.toString(), "1");

//            JSONObject jsonInfo = new JSONObject();
//            jsonInfo.put("event", KioskBean.Event.SKIP_CLIENT.toString());
//            List<ClientWrapper> list = ClientWrapper.wrap(getCalledAndInprocessClients());
//            jsonInfo.put("list", new JSONArray(list.toString()));
//
//            wsInfotableBean.sendMessageOverSocket(jsonInfo.toString(), "1");
//
//            JSONObject jsonDisplay = new JSONObject();
//            jsonDisplay.put("event", KioskBean.Event.SKIP_CLIENT.toString());
//            jsonDisplay.put("operator", new JSONObject(OperatorWrapper.wrapInherited(initiator).toString()));
//            jsonDisplay.put("client", new JSONObject(ClientWrapper.wrap(client).toString()));

//            wsOperatorDisplayBean.sendMessageOverSocket(jsonDisplay.toString(), "1");
        }
        catch (Exception e) {
//            e.printStackTrace();
        }
    }

    public Process startProcess(Long id, ClientWrapper wrapper) {
        try {
            Date now = new Date();
            Client client = (Client) em.find(Client.class, id);

            if (client.getDate().before(now) && client.getDate().getDate() != now.getDate())
                return null;

            client.setStatus(Client.Status.IN_PROCESS.toString());
            client.setEvent(now);
            em.merge(client);

            Process process = new Process();
            process.setClientCode(client.getCode());
            process.setClientId(client.getId());
            process.setLineId(wrapper.getLineId());
            process.setOperatorId(wrapper.getOperatorId());
            process.setBegin(now);

            int create = toMinutes(client.getDate());
            int start = toMinutes(now);
            int waiting = start - create;
            if (waiting < 1)
                waiting = 1;
            process.setWait(waiting);
            if (Math.abs(now.getSeconds() - client.getDate().getSeconds()) > 20)
                process.setWait(process.getWait() + 1);


            em.persist(process);

            User operator = (User) em.find(User.class, wrapper.getOperatorId());
            operator.setClient(client);

            em.merge(operator);

            return process;
        }
        catch(Exception e) {}


//        try {
//            JSONObject jsonInfo = new JSONObject();
//            jsonInfo.put("event", KioskBean.Event.OPERATOR_START_PROCESS.toString());
//            List<ClientWrapper> list = ClientWrapper.wrap(getCalledAndInprocessClients());
//            jsonInfo.put("list", new JSONArray(list.toString()));
//
//            wsInfotableBean.sendMessageOverSocket(jsonInfo.toString(), "1");

//            JSONObject jsonDisplay = new JSONObject();
//            jsonDisplay.put("event", KioskBean.Event.OPERATOR_START_PROCESS.toString());
//            jsonDisplay.put("operator", new JSONObject(OperatorWrapper.wrapInherited(operator).toString()));
//            jsonDisplay.put("client", new JSONObject(ClientWrapper.wrap(client).toString()));

//            wsOperatorDisplayBean.sendMessageOverSocket(jsonDisplay.toString(), "1");

//            wsMonitoringBean.sendMessageOverSocket(jsonDisplay.toString(), "1");
//        }
//        catch(JSONException je) {
//            je.printStackTrace();
//        }
        return null;
    }

    public void stopProcess(Long id) {
        Date now = new Date();
        Process process = (Process) em.find(Process.class, id);
        process.setEnd(now);

        int begin = toMinutes(process.getBegin());
        int end = toMinutes(now);
        int handling = begin - end;
        if (handling < 1)
            handling = 15;

        process.setHandling(handling);

        if (Math.abs(process.getBegin().getSeconds() - now.getSeconds()) > 20)
            process.setHandling(process.getHandling()+1);

        em.merge(process);

//        try {
//            JSONObject jsonInfo = new JSONObject();
//            jsonInfo.put("event", KioskBean.Event.OPERATOR_STOP_PROCESS.toString());
//            List<ClientWrapper> list = ClientWrapper.wrap(getCalledAndInprocessClients());
//            jsonInfo.put("list", new JSONArray(list.toString()));
//
//            wsInfotableBean.sendMessageOverSocket(jsonInfo.toString(), "1");

//            Client client = (Client) em.find(Client.class, process.getClientId());
//            User operator = (User) em.find(User.class, process.getOperatorId());
//            JSONObject jsonDisplay = new JSONObject();
//            jsonDisplay.put("event", KioskBean.Event.OPERATOR_STOP_PROCESS.toString());
//            jsonDisplay.put("operator", new JSONObject(OperatorWrapper.wrapInherited(operator).toString()));
//            jsonDisplay.put("client", new JSONObject(ClientWrapper.wrap(client).toString()));
//
////            wsOperatorDisplayBean.sendMessageOverSocket(jsonDisplay.toString(), "1");
//
//            wsMonitoringBean.sendMessageOverSocket(jsonDisplay.toString(), "1");
//        }
//        catch(JSONException je) {
////            je.printStackTrace();
//        }
    }

    // sending works to raw lines
    public Client send(Long id, ClientWrapper wrapper, String login) {
        try {
            Client client = (Client) em.find(Client.class, id);
            Line line = (Line) em.find(Line.class, wrapper.getLineId());

            if (line.getSize() == line.getLimit() + line.getLimitAdditional())
                return null;
            else
                line.setSize(line.getSize() + 1);

            Line prev = client.getLine();

            int sentClientOrder = 0;
            prev.getClients().remove(client);

            if (prev.getIsRaw()) {
                prev.setSize(prev.getSize()+1);

                if (prev.getClients().isEmpty())
                    prev.setLength(0);
                else
                    prev.setLength(prev.getLength() - 1);
            }
            else {
                LineAppointment la = lineBean.getLA(prev.getId(), client.getDate());

                if (lineBean.getAvailableClients(prev.getId(), client.getDate()).isEmpty())
                    la.setLength(0);
                else
                    la.setLength(la.getLength()-1);
                em.merge(la);
            }
            em.merge(prev);

            // TODO check if need to remove following two line code ?
            if (line.getCounter() == line.getCounterEnd() || line.getCounter() == 0) // 999
                line.setCounter(line.getCounterBegin()); // 0

            int length = line.getLength() + 1;
            line.getClients().add(client);
            line.setLength(length);

            line = em.merge(line);

            User operator = (User) em.find(User.class, client.getOperator().getId());
            List<Client> waitingClients = lineBean.getAvailableClients(line.getId());

            sentClientOrder = length;
            boolean afterCameFound = false;
            for (Client waiting : waitingClients) {
                if (!afterCameFound) {
                    if (waiting.getDate().after(client.getDate())) {
                        afterCameFound = true;
                        sentClientOrder = waiting.getOrder();
                        waiting.setOrder(waiting.getOrder() + 1);

                        em.merge(waiting);
                    }
                } else {
                    if (waiting.getDate().after(client.getDate())) {
                        waiting.setOrder(waiting.getOrder() + 1);

                        em.merge(waiting);
                    }
                }
            }

            client.setOrder(sentClientOrder);
            client.setOperator(null);
            client.setStatus(Client.Status.WAITING.toString());
            client.setLine(line);

            client = em.merge(client);

//            JSONObject json = new JSONObject();

//            json.put("event", KioskBean.Event.SEND_CLIENT_TO_LINE.toString());
//            json.put("client", new JSONObject(ClientWrapper.wrap(client).toString()));
//            json.put("line", new JSONObject(LineWrapper.wrap(line).toString()));
//            json.put("clients", new JSONArray( ClientWrapper.wrap(lineBean.getAvailableClients(line.getId())).toString() ));
//            json.put("operator", new JSONObject(OperatorWrapper.wrapInherited(operator).toString()));
//
//            User initiator = userBean.getUserByLogin(login);
//            json.put("initiator", new JSONObject(OperatorWrapper.wrapInherited(initiator).toString()));

//            wsOperatorBean.sendMessageOverSocket(json.toString(), "1");

//            wsMonitoringBean.sendMessageOverSocket(json.toString(), "1");

//            JSONObject jsonInfo = new JSONObject();
//            jsonInfo.put("event", KioskBean.Event.SEND_CLIENT_TO_LINE.toString());
//            List<ClientWrapper> list = ClientWrapper.wrap(getCalledAndInprocessClients());
//            jsonInfo.put("list", new JSONArray(list.toString()));
//
//            wsInfotableBean.sendMessageOverSocket(jsonInfo.toString(), "1");
//
//            JSONObject jsonDisplay = new JSONObject();
//            jsonDisplay.put("event", KioskBean.Event.SEND_CLIENT_TO_LINE.toString());
//            jsonDisplay.put("operator", new JSONObject(OperatorWrapper.wrapInherited(initiator).toString()));
//            jsonDisplay.put("client", new JSONObject(ClientWrapper.wrap(client).toString()));

//            wsOperatorDisplayBean.sendMessageOverSocket(jsonDisplay.toString(), "1");

            return client;
        }
        catch (Exception e) {
//            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public boolean delete(Long id) {
//        System.out.println("delete " + id);
        try {
            Client client = (Client) em.find(Client.class, id);

            User operator = client.getOperator();
            operator.setClient(null);

            em.merge(operator);

            Line line = client.getLine();
            line.getClients().remove(client);
            line = em.merge(line);

            if (line.getIsRaw())
                if (line.getClients().isEmpty())
                    line.setLength(0);
                else
                    line.setLength(line.getLength()-1);
            else {
                List<Client> list = lineBean.getAvailableClients(line.getId(), client.getDate());
                LineAppointment la = lineBean.getLA(line.getId(), client.getDate());

                if (list.isEmpty())
                    la.setLength(0);
                else
                    la.setLength(la.getLength()-1);
                em.merge(la);
            }

            em.merge(line);
            em.remove(client);

//            JSONObject jsonInfo = new JSONObject();
//            jsonInfo.put("event", KioskBean.Event.REMOVE_CLIENT.toString());
//            List<ClientWrapper> list = ClientWrapper.wrap(getCalledAndInprocessClients());
//            jsonInfo.put("list", new JSONArray(list.toString()));
//
//            wsInfotableBean.sendMessageOverSocket(jsonInfo.toString(), "1");

//            JSONObject jsonDisplay = new JSONObject();
//            jsonDisplay.put("event", KioskBean.Event.REMOVE_CLIENT.toString());
//            jsonDisplay.put("operator", new JSONObject(OperatorWrapper.wrapInherited(operator).toString()));
//            jsonDisplay.put("client", new JSONObject(ClientWrapper.wrap(client).toString()));

//            wsOperatorDisplayBean.sendMessageOverSocket(jsonDisplay.toString(), "1");

//            wsMonitoringBean.sendMessageOverSocket(jsonDisplay.toString(), "1");

            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String generateCode(int number) { //(String prefix, int number)
    /*
        int third = number%10;
        int second = (number%100)/10;
        int first = number/100;

        return prefix + first + second + third;
    */
        int forth = number%10;
        int third = (number%100)/10;
        int second = (number%1000)/100;
        int first = number/1000;

        return "" +first + second + third + forth;
    }

    public List<Client> getCalledAndInprocessClients(){
        List<Client> list = new ArrayList<>();

        try {
//            List<String> statuses = new List<>();
//            statuses.add("CALLED");
//            statuses.add("IN_PROCESS");

            Date todayBegin = new Date();
            todayBegin.setHours(0);
            todayBegin.setMinutes(0);
            Date todayEnd = new Date();
            todayEnd.setHours(23);
            todayEnd.setMinutes(59);

            list = em.createQuery("select c from Client c " +
                    "where c.status in :statuses " +
                    "and c.date >= :begin and c.date <= :end " +
                    "order by c.event desc ")
                    .setParameter("statuses", Arrays.asList("CALLED", "IN_PROCESS"))
                    .setParameter("begin", todayBegin)
                    .setParameter("end", todayEnd)
                    .setMaxResults(12)
                    .getResultList();
        }
        catch (NoResultException nre) {
//            nre.printStackTrace();
        }

        return list;
    }

    public int toMinutes(Date date){
        int hours = date.getHours();
        int minutes = date.getMinutes();

        return minutes + hours*60;
    }

    @Transactional
    public void checkOldDayClient(User operator) {
//        System.out.println("checkOldDayClient " + operator.getLogin());
//        if (operator.getClient() != null)
//            System.out.println(" c " + operator.getClient().getCode());
        Date today = new Date();
        Client client = operator.getClient();

        if (client == null)
            return;

//        System.out.println("before " + client.getDate().before(today));
//        System.out.println("not today " + client.getDate().getDate() +" "+ today.getDate());
//        System.out.println(client.getDate().before(today) && client.getDate().getDate() != today.getDate());
        if (client.getDate().before(today) && client.getDate().getDate() != today.getDate()) {
//            System.out.println("if before today");
            if (operator.getClient().getStatus().equals(Client.Status.IN_PROCESS.toString())) {
//                System.out.println("IN PROCESS");
                try{
                    List<Process> processes = em.createQuery("select p from Process p " +
                            "where p.clientId = :clientId " +
                            "and p.operatorId = :operatorId " +
                            "and p.end is null")
                            .setParameter("clientId", operator.getClient().getId())
                            .setParameter("operatorId", operator.getId())
                            .getResultList();

                    for (Process process: processes) {
                        Date processEnd = new Date(process.getBegin().getTime());
                        processEnd.setMinutes(processEnd.getMinutes() + 20);
                        process.setEnd(processEnd);

                        em.merge(process);
                    }
                }
                catch(NoResultException e) {
                    e.printStackTrace();
                }
            }
            delete(client.getId());
        }
    }
}
