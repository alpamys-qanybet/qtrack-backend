package kz.essc.qtrack.kiosk;

import kz.essc.qtrack.client.*;
import kz.essc.qtrack.client.Process;
import kz.essc.qtrack.core.LangBean;
import kz.essc.qtrack.line.*;
import kz.essc.qtrack.operator.OperatorBean;
import kz.essc.qtrack.operator.OperatorWrapper;
import kz.essc.qtrack.process.ProcessWrapper;
import kz.essc.qtrack.sc.user.User;
import kz.essc.qtrack.user.UserBean;
//import kz.essc.qtrack.websocket.monitoring.WsMonitoringBean;
//import kz.essc.qtrack.websocket.operator.WsOperatorBean;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@RequestScoped
public class KioskBean {

    public enum Event {
        LAUNCH_OPERATOR_LINE_INFO, LAUNCH_OPERATOR_DISPLAY_INFO, ADD_CLIENT_TO_LINE, OPERATOR_CALL_CLIENT,
        OPERATOR_START_PROCESS, OPERATOR_STOP_PROCESS, SKIP_CLIENT, SEND_CLIENT_TO_LINE,
        LAUNCH_INFOTABLE_INFO, REMOVE_CLIENT
    }

    @PersistenceContext
    EntityManager em;

    @Inject
    LineBean lineBean;

    @Inject
    ClientBean clientBean;

    @Inject
    OperatorBean operatorBean;

    @Inject
    UserBean userBean;

//    @Inject
//    WsOperatorBean wsOperatorBean;

//    @Inject
//    WsMonitoringBean wsMonitoringBean;

    @Inject
    LangBean langBean;

    public List<TicketWrapper> translated(List<TicketWrapper> list) {
        for (TicketWrapper wrapper: list)
            translated(wrapper);

        return list;
    }

    public TicketWrapper translated(TicketWrapper wrapper) {
        try {
            String name = "line." + wrapper.getLineId() + ".name";
            wrapper.setLineNameKz(langBean.getMessage(name, LangBean.Code.kz.toString()));
            wrapper.setLineNameEn(langBean.getMessage(name, LangBean.Code.en.toString()));
            wrapper.setLineNameRu(langBean.getMessage(name, LangBean.Code.ru.toString()));

            String nameTicket = "line.ticket." + wrapper.getLineId() + ".name";
            wrapper.setTicketNameKz(langBean.getMessage(nameTicket, LangBean.Code.kz.toString()));
            wrapper.setTicketNameEn(langBean.getMessage(nameTicket, LangBean.Code.en.toString()));
            wrapper.setTicketNameRu(langBean.getMessage(nameTicket, LangBean.Code.ru.toString()));
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }

        return wrapper;
    }

    public String messageOnLaunchOperator() throws JSONException {
        JSONObject json = new JSONObject();
        JSONArray list = new JSONArray();

        for (Line line: lineBean.get()) {
            JSONArray operatorsJson = new JSONArray();
            for (User operator: line.getOperators()) {
                JSONObject operatorJson = new JSONObject();
                operatorJson.put("operator", new JSONObject(OperatorWrapper.wrapInherited(operator).toString()));

                clientBean.checkOldDayClient(operator);
                if (operator.getClient() == null) {
                    operatorJson.put("current", "null");
                }
                else {
                    operatorJson.put("current", new JSONObject(ClientWrapper.wrap(operator.getClient()).toString()));
                    if (operator.getClient().getStatus().equals(Client.Status.IN_PROCESS.toString())) {
//                        System.out.println("client id " + operator.getClient().getId());
//                        System.out.println("operator id " + operator.getId());

                        try{
                            Process process = (Process) em.createQuery("select p from Process p " +
                                                            "where p.clientId = :clientId " +
                                                            "and p.operatorId = :operatorId " +
                                                            "and p.end is null")
                                    .setParameter("clientId", operator.getClient().getId())
                                    .setParameter("operatorId", operator.getId())
                                    .getSingleResult();

                            operatorJson.put("process", new JSONObject(ProcessWrapper.wrap(process).toString()));
                        }
                        catch(NoResultException nre) {
//                            nre.printStackTrace();
                        }
                    }
                }

                operatorsJson.put(operatorJson);
            }
            JSONObject lineInfoJson = new JSONObject();
            lineInfoJson.put("line", new JSONObject(LineWrapper.wrap(line).toString()));
            lineInfoJson.put("clients", new JSONArray(ClientWrapper.wrap(lineBean.getAvailableClients(line.getId())).toString()));
            lineInfoJson.put("operators", operatorsJson);
            list.put(lineInfoJson);
        }

        json.put("event", Event.LAUNCH_OPERATOR_LINE_INFO.toString());
        json.put("list", list);

        return json.toString();
    }

    public String noWsOperatorRest(Long operatorId) throws JSONException{
        JSONObject json = new JSONObject();

        User operator = userBean.get(operatorId);

        JSONObject operatorJson = new JSONObject();
        operatorJson.put("operator", new JSONObject(OperatorWrapper.wrapInherited(operator).toString()));

        clientBean.checkOldDayClient(operator);
        /*Date now = new Date();
        Date d2300 = new Date();
        d2300.setHours(23);
        d2300.setMinutes(0);
        Date d2350 = new Date();
        d2350.setHours(23);
        d2350.setMinutes(50);
        if ( (clientBean.toMinutes(d2300) < clientBean.toMinutes(now)) &&  (clientBean.toMinutes(now) < clientBean.toMinutes(d2350)) )
            clientBean.checkOldDayClient(operator);

        Date d600 = new Date();
        d600.setHours(6);
        d600.setMinutes(0);
        Date d650 = new Date();
        d650.setHours(6);
        d650.setMinutes(50);
        if ( (clientBean.toMinutes(d600) < clientBean.toMinutes(now)) &&  (clientBean.toMinutes(now) < clientBean.toMinutes(d650)) )
            clientBean.checkOldDayClient(operator);*/

        if (operator.getClient() == null) {
            operatorJson.put("current", "null");
        }
        else {
            operatorJson.put("current", new JSONObject(ClientWrapper.wrap(operator.getClient()).toString()));
            if (operator.getClient().getStatus().equals(Client.Status.IN_PROCESS.toString())) {
                try{
                    Process process = (Process) em.createQuery("select p from Process p " +
                            "where p.clientId = :clientId " +
                            "and p.operatorId = :operatorId " +
                            "and p.end is null")
                            .setParameter("clientId", operator.getClient().getId())
                            .setParameter("operatorId", operator.getId())
                            .getSingleResult();

                    operatorJson.put("process", new JSONObject(ProcessWrapper.wrap(process).toString()));
                }
                catch(NoResultException nre) {
                }
            }
        }
        json.put("operator", operatorJson);
        List<LineWrapper> lines = LineWrapper.wrap(lineBean.get());
        Line line = operator.getLine();
        json.put("line", new JSONObject(LineWrapper.wrap(line).toString()));
        json.put("clients", new JSONArray(ClientWrapper.wrap(lineBean.getAvailableClients(line.getId())).toString()));
        json.put("lines", new JSONArray(lines.toString()));

        return json.toString();
    }

    public String messageOnLaunchOperatorDisplay() throws JSONException {
        JSONObject json = new JSONObject();

        JSONArray operatorsJson = new JSONArray();
        for (User operator: operatorBean.get()) {
            JSONObject operatorJson = new JSONObject();
            operatorJson.put("operator", new JSONObject(OperatorWrapper.wrapInherited(operator).toString()));
            clientBean.checkOldDayClient(operator);
            if (operator.getClient() == null) {
                operatorJson.put("current", "null");
            }
            else {
                operatorJson.put("current", new JSONObject(ClientWrapper.wrap(operator.getClient()).toString()));
                if (operator.getClient().getStatus().equals(Client.Status.IN_PROCESS.toString())) {
                    try{
                        Process process = (Process) em.createQuery("select p from Process p " +
                                "where p.clientId = :clientId " +
                                "and p.operatorId = :operatorId " +
                                "and p.end is null")
                                .setParameter("clientId", operator.getClient().getId())
                                .setParameter("operatorId", operator.getId())
                                .getSingleResult();

                        operatorJson.put("process", new JSONObject(ProcessWrapper.wrap(process).toString()));
                    }
                    catch(NoResultException nre) {
//                            nre.printStackTrace();
                    }
                }
            }

            operatorsJson.put(operatorJson);
        }
        json.put("operators", operatorsJson);
        json.put("event", Event.LAUNCH_OPERATOR_DISPLAY_INFO.toString());

        return json.toString();
    }

    public String messageOnLaunchInfotable() throws JSONException {
        JSONObject json = new JSONObject();
        List<ClientWrapper> list = ClientWrapper.wrap(clientBean.getCalledAndInprocessClients());
        List<LineWrapper> lines = LineWrapper.wrap(lineBean.get());
        List<OperatorWrapper> operators = OperatorWrapper.wrapInherited(operatorBean.get());

        json.put("event", Event.LAUNCH_INFOTABLE_INFO.toString());
        json.put("list", new JSONArray(list.toString()));
        json.put("lines", new JSONArray(lines.toString()));
        json.put("operators", new JSONArray(operators.toString()));

        return json.toString();
    }

    public long createClient(Long lineId, ClientWrapper clientWrapper) {
        Client client = clientBean.add(lineId, clientWrapper);

        if (client == null)
            return -1L;

//        Line line = lineBean.get(lineId);

//        int begin = clientBean.toMinutes(line.getBegin());
//        int end = clientBean.toMinutes(line.getEnd());
//        int now = clientBean.toMinutes(new Date());

//        if (now < begin)
//            return -1L;
//        if (now > end)
//            return -2L;

//        if (begin <= now && now <= end) {
//            Long clients = (Long) em.createQuery("select count(c) from Client c "+
//                    "where c.line = :line "+)
//                    .getSingleResult();
//        }

//        try {
//            JSONObject jsonObj = new JSONObject();
//
//            jsonObj.put("event", KioskBean.Event.ADD_CLIENT_TO_LINE.toString());
//            jsonObj.put("client", new JSONObject(ClientWrapper.wrap(client).toString()));
//            jsonObj.put("line", new JSONObject(LineWrapper.wrap(line).toString()));
//            jsonObj.put("clients", new JSONArray(ClientWrapper.wrap(lineBean.getAvailableClients(line.getId())).toString()));
//
//            jsonObj.put("initiator", "kiosk");

//            wsOperatorBean.sendMessageOverSocket(jsonObj.toString(), "1");
//            wsMonitoringBean.sendMessageOverSocket(jsonObj.toString(), "1");
//        }
//        catch (JSONException e) {
////            e.printStackTrace();
//        }

        return client.getId();
    }

    public boolean initLineCounter(Long lineId) {
        lineBean.initLineCounter(lineId);
        return true;
    }

    // make the method in lineBean public and use it
    public boolean genLP() {
//        lineBean.generatePrefix();
        return true;
    }
}
