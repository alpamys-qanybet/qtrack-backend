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
import java.util.ArrayList;
import java.util.List;

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

        if (wrapper.getBegin() != null) {
            line.setBegin(wrapper.getBegin());
        }

        if (wrapper.getEnd() != null) {
            line.setEnd(wrapper.getEnd());
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
            return em.createQuery("select c from Client c " +
                    "where c.line.id = :id "+
                    "and c.status = :status " +
                    "order by c.order")
                    .setParameter("id", id)
                    .setParameter("status", Client.Status.WAITING.toString())
                    .getResultList();
        }
        catch (Exception e) {
//            e.printStackTrace();
            return new ArrayList<>();
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
}
