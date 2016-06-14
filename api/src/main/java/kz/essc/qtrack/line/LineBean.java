package kz.essc.qtrack.line;

import kz.essc.qtrack.client.Client;
import kz.essc.qtrack.core.LangBean;
import kz.essc.qtrack.operator.OperatorWrapper;
import kz.essc.qtrack.sc.Language;
import kz.essc.qtrack.sc.Message;
import kz.essc.qtrack.sc.user.User;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

@RequestScoped
public class LineBean {
    @PersistenceContext
    EntityManager em;

    @Inject
    LangBean langBean;

    public List<LineWrapper> translated(List<LineWrapper> list) {
        for (LineWrapper wrapper: list)
            translated(wrapper);

        return list;
    }

    public LineWrapper translated(LineWrapper wrapper) {
        try {
            String name = "line." + wrapper.getId() + ".name";
            wrapper.setNameKz(langBean.getMessage(name, LangBean.Code.kz.toString()));
            wrapper.setNameEn(langBean.getMessage(name, LangBean.Code.en.toString()));
            wrapper.setNameRu(langBean.getMessage(name, LangBean.Code.ru.toString()));
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }

        return wrapper;
    }

    public List<Line> get() {

        try {
            return em.createQuery("select l from Line l").getResultList();
        }
        catch (Exception e) {
//            e.printStackTrace();
            return null;
        }
    }

    public Line get(Long id) {
        try {
            return (Line) em.find(Line.class, id);
        }
        catch (Exception e) {
//            e.printStackTrace();
            return null;
        }
    }

    public List<Line> root() {
        try {
            return em.createQuery("select l from Line l where l.lineHierarchy is null").getResultList();
        }
        catch (Exception e) {
//            e.printStackTrace();
            return null;
        }
    }

    public List<Line> children(Long lineHierarchyId) {
        try {
            LineHierarchy hierarchy = (LineHierarchy) em.find(LineHierarchy.class, lineHierarchyId);

            return em.createQuery("select l from Line l where l.lineHierarchy = :hierarchy")
                    .setParameter("hierarchy", hierarchy)
                    .getResultList();
        }
        catch (Exception e) {
//            e.printStackTrace();
            return null;
        }
    }

    public Line add(LineWrapper wrapper) {
        try {
            Line line = new Line();
            line.setName(wrapper.getNameEn());
            line.setPrefix(wrapper.getPrefix());
//            line.setLimit(wrapper.getLimit());
//            line.setIsRaw();

            em.persist(line);

            for (Language lang: langBean.getLanguages()) {
                Message message = new Message();
                message.setLang(lang);
                message.setName("line." + line.getId() + ".name");

                if (lang.getCode().equals("kz")) message.setValue(wrapper.getNameKz());
                else if (lang.getCode().equals("en")) message.setValue(wrapper.getNameEn());
                else if (lang.getCode().equals("ru")) message.setValue(wrapper.getNameRu());

                em.persist(message);
            }

            return line;
        }
        catch (Exception e) {
//            e.printStackTrace();

            return null;
        }
    }

    public Line edit(long id, LineWrapper wrapper) {
        Line line = (Line) em.find(Line.class, id);

        line.setName(wrapper.getNameKz());
        line.setPrefix(wrapper.getPrefix());
        line.setIsRaw(wrapper.getIsRaw());

        if (wrapper.getBegin() != null) {
            line.setBegin(wrapper.getBegin());
        }
        if (wrapper.getEnd() != null) {
            line.setEnd(wrapper.getEnd());
        }

        if (wrapper.getSuBegin() != null) {
            line.setSuBegin(wrapper.getSuBegin());
        }
        if (wrapper.getSuEnd() != null) {
            line.setSuEnd(wrapper.getSuEnd());
        }

        if (wrapper.getMoBegin() != null) {
            line.setMoBegin(wrapper.getMoBegin());
        }
        if (wrapper.getMoEnd() != null) {
            line.setMoEnd(wrapper.getMoEnd());
        }

        if (wrapper.getTuBegin() != null) {
            line.setTuBegin(wrapper.getTuBegin());
        }
        if (wrapper.getTuEnd() != null) {
            line.setTuEnd(wrapper.getTuEnd());
        }

        if (wrapper.getWeBegin() != null) {
            line.setWeBegin(wrapper.getWeBegin());
        }
        if (wrapper.getWeEnd() != null) {
            line.setWeEnd(wrapper.getWeEnd());
        }

        if (wrapper.getThBegin() != null) {
            line.setThBegin(wrapper.getThBegin());
        }
        if (wrapper.getThEnd() != null) {
            line.setThEnd(wrapper.getThEnd());
        }

        if (wrapper.getFrBegin() != null) {
            line.setFrBegin(wrapper.getFrBegin());
        }
        if (wrapper.getFrEnd() != null) {
            line.setFrEnd(wrapper.getFrEnd());
        }

        if (wrapper.getStBegin() != null) {
            line.setStBegin(wrapper.getStBegin());
        }
        if (wrapper.getStEnd() != null) {
            line.setStEnd(wrapper.getStEnd());
        }

        for (Language lang: langBean.getLanguages()) {
            Message message = langBean.getMessageInstance("line." + line.getId() + ".name", lang.getCode());
            message.setLang(lang);

            if (lang.getCode().equals("kz")) message.setValue(wrapper.getNameKz());
            else if (lang.getCode().equals("en")) message.setValue(wrapper.getNameEn());
            else if (lang.getCode().equals("ru")) message.setValue(wrapper.getNameRu());

            em.merge(message);
        }

        return line;
    }

    public List<User> getOperators(Long id) {
        try {
            Line line = (Line) em.find(Line.class, id);
            return line.getOperators();
        }
        catch (Exception e) {
//            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean addOperator(Long id, OperatorWrapper operatorWrapper) {
        try {
            Line line = (Line) em.find(Line.class, id);
            User operator = (User) em.find(User.class, operatorWrapper.getId());

            if (!line.getOperators().contains(operator)) {
                line.getOperators().add(operator);
                em.merge(line);

                operator.setLine(line);
                em.merge(operator);
            }
            return true;
        }
        catch (Exception e) {
//            e.printStackTrace();
            return false;
        }
    }

    public boolean removeOperator(Long id, Long operatorId) {
        try {
            Line line = (Line) em.find(Line.class, id);
            User operator = (User) em.find(User.class, operatorId);

            if (line.getOperators().contains(operator)) {
                line.getOperators().remove(operator);
                em.merge(line);

                operator.setLine(null);
                em.merge(operator);
            }

            return true;
        }
        catch (Exception e) {
//            e.printStackTrace();
            return false;
        }
    }

    public List<Client> getClients(Long id) {
        try {
            Line line = (Line) em.find(Line.class, id);
            return line.getClients();
        }
        catch (Exception e) {
//            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    public List<Client> getAvailableClients(Long id) {
        try {
//            Line line = (Line) em.find(Line.class, id);
            Date todayBegin = new Date();
            todayBegin.setHours(0);
            todayBegin.setMinutes(0);
            Date todayEnd = new Date();
            todayEnd.setHours(23);
            todayEnd.setMinutes(59);

            return em.createQuery("select c from Client c " +
                    "where c.line.id = :id "+
                    "and c.date >= :begin and c.date <= :end " +
                    "and c.status = :status " +
                    "order by c.order")
                    .setParameter("id", id)
                    .setParameter("status", Client.Status.WAITING.toString())
                    .setParameter("begin", todayBegin)
                    .setParameter("end", todayEnd)
                    .getResultList();
        }
        catch (Exception e) {
//            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Client> getAvailableClients(Long id, FilterWrapper w) {
        return getAvailableClients(id, w.getDate());
    }

    public List<Client> getAvailableClients(Long id, Date date) {
        try {
//            Line line = (Line) em.find(Line.class, id);
            Date begin = new Date();
            begin.setDate(date.getDate());
            begin.setMonth(date.getMonth());
            begin.setYear(date.getYear());
            begin.setHours(0);
            begin.setMinutes(0);
            Date end = new Date();
            end.setDate(begin.getDate());
            end.setMonth(begin.getMonth());
            end.setYear(begin.getYear());
            end.setHours(23);
            end.setMinutes(59);

            return em.createQuery("select c from Client c " +
                    "where c.line.id = :id "+
                    "and c.date >= :begin and c.date <= :end " +
                    "and c.status = :status " +
                    "order by c.order")
                    .setParameter("id", id)
                    .setParameter("status", Client.Status.WAITING.toString())
                    .setParameter("begin", begin)
                    .setParameter("end", end)
                    .getResultList();
        }
        catch (Exception e) {
//            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void orderAppointmentClients(Long id, Date date) {
        try {
//            Line line = (Line) em.find(Line.class, id);
            Date begin = new Date();
            begin.setDate(date.getDate());
            begin.setMonth(date.getMonth());
            begin.setYear(date.getYear());
            begin.setHours(0);
            begin.setMinutes(0);
            Date end = new Date();
            end.setDate(begin.getDate());
            end.setMonth(begin.getMonth());
            end.setYear(begin.getYear());
            end.setHours(23);
            end.setMinutes(59);

            List<Client> clients = em.createQuery("select c from Client c " +
                                    "where c.line.id = :id "+
                                    "and c.date >= :begin and c.date <= :end " +
                                    "and c.status = :status " +
                                    "order by c.date")
                                    .setParameter("id", id)
                                    .setParameter("status", Client.Status.WAITING.toString())
                                    .setParameter("begin", begin)
                                    .setParameter("end", end)
                                    .getResultList();

            for (int i=0; i<clients.size(); i++) {
                Client c = clients.get(i);
                c.setOrder(i);
                em.merge(c);
            }
        }
        catch (Exception e) {
//            e.printStackTrace();
        }
    }

    public List<Line> getAvailable() {
        try {
            return em.createQuery("select l from Line l where l.lineHierarchy is null").getResultList();
        }
        catch (Exception e) {
//            e.printStackTrace();
            return null;
        }
    }

    public LineAppointment getLA(Long lineId, Date date) {
//        Calendar calendar = Calendar.getInstance();
//        int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR); // starts from 1

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_YEAR);
        int year = 1900+date.getYear();

        return (LineAppointment) em.createQuery(
                "select l from LineAppointment l join LineAppointmentTiming t " +
                        "where t.year = :year "+
                        "and t.day = :day " +
                        "and l.timingId = t.id " +
                        "and l.lineId = :line")
                .setParameter("year", year)
                .setParameter("day", day)
                .setParameter("line", lineId)
                .getSingleResult();
    }
}
