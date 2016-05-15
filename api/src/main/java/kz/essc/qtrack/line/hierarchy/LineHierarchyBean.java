package kz.essc.qtrack.line.hierarchy;


import kz.essc.qtrack.core.LangBean;
import kz.essc.qtrack.line.Line;
import kz.essc.qtrack.line.LineHierarchy;
import kz.essc.qtrack.line.LineWrapper;
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
public class LineHierarchyBean {

    @PersistenceContext
    EntityManager em;

    @Inject
    LangBean langBean;

    public List<LineHierarchyWrapper> translated(List<LineHierarchyWrapper> list) {
        for (LineHierarchyWrapper wrapper: list)
            translated(wrapper);

        return list;
    }

    public LineHierarchyWrapper translated(LineHierarchyWrapper wrapper) {
        try {
            String name = "line.hierarchy." + wrapper.getId() + ".name";
            wrapper.setNameKz(langBean.getMessage(name, LangBean.Code.kz.toString()));
            wrapper.setNameEn(langBean.getMessage(name, LangBean.Code.en.toString()));
            wrapper.setNameRu(langBean.getMessage(name, LangBean.Code.ru.toString()));
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }

        return wrapper;
    }

    public List<LineHierarchy> all() {
        try {
            return em.createQuery("select l from LineHierarchy l").getResultList();
        }
        catch (Exception e) {
//            e.printStackTrace();
            return null;
        }
    }

    public List<LineHierarchy> get() {
        try {
            return em.createQuery("select l from LineHierarchy l where l.parent is null").getResultList();
        }
        catch (Exception e) {
//            e.printStackTrace();
            return null;
        }
    }

    public LineHierarchy get(Long id) {
        try {
            return (LineHierarchy) em.find(LineHierarchy.class, id);
        }
        catch (Exception e) {
//            e.printStackTrace();
            return null;
        }
    }

    public List<LineHierarchy> getChildren(Long id) {
        try {
            LineHierarchy parent = get(id);
//            parent.getChildren(); ??
            return em.createQuery("select l from LineHierarchy l where l.parent = :parent")
                    .setParameter("parent", parent)
                    .getResultList();
        }
        catch (Exception e) {
//            e.printStackTrace();
            return null;
        }
    }

    public List<Line> getLines(Long id) {
        try {
            LineHierarchy parent = get(id);
            return parent.getLines();
        }
        catch (Exception e) {
//            e.printStackTrace();
            return null;
        }
    }

    public LineHierarchy add(LineHierarchyWrapper wrapper) {
        LineHierarchy hierarchy = new LineHierarchy();
        em.persist(hierarchy);

        if (wrapper.getParentId() != 0) {
            LineHierarchy parent = (LineHierarchy) em.find(LineHierarchy.class, wrapper.getParentId());
            hierarchy.setParent(parent);

            hierarchy = em.merge(hierarchy);

            try {
                parent.getChildren().add(hierarchy);
            }
            catch (NullPointerException e) {
                parent.setChildren(new ArrayList<LineHierarchy>());
                parent.getChildren().add(hierarchy);
            }
            em.merge(parent);
        }

        for (Language lang: langBean.getLanguages()) {
            Message message = new Message();
            message.setLang(lang);
            message.setName("line.hierarchy." + hierarchy.getId() + ".name");

            if (lang.getCode().equals("kz")) message.setValue(wrapper.getNameKz());
            else if (lang.getCode().equals("en")) message.setValue(wrapper.getNameEn());
            else if (lang.getCode().equals("ru")) message.setValue(wrapper.getNameRu());

            em.persist(message);
        }

        return hierarchy;
    }

    public LineHierarchy edit(long id, LineHierarchyWrapper wrapper) {
        LineHierarchy hierarchy = (LineHierarchy) em.find(LineHierarchy.class, id);

        boolean oldIsRoot = hierarchy.getParent() == null;
        boolean newIsRoot = wrapper.getParentId() == 0;
        if ( oldIsRoot && newIsRoot ) {
        }
        else if ( !oldIsRoot && newIsRoot ) {
            hierarchy.getParent().getChildren().remove(hierarchy);
            em.merge(hierarchy.getParent());

            hierarchy.setParent(null);
            em.merge(hierarchy);
        }
        else if ( oldIsRoot && !newIsRoot ) {
            LineHierarchy newParent = (LineHierarchy) em.find(LineHierarchy.class, wrapper.getParentId());
            hierarchy.setParent(newParent);
            em.merge(hierarchy);
        }
        else {
            hierarchy.getParent().getChildren().remove(hierarchy);
            em.merge(hierarchy.getParent());

            LineHierarchy newParent = (LineHierarchy) em.find(LineHierarchy.class, wrapper.getParentId());
            hierarchy.setParent(newParent);
            em.merge(hierarchy);
        }

        for (Language lang: langBean.getLanguages()) {
            Message message = langBean.getMessageInstance("line.hierarchy." + hierarchy.getId() + ".name", lang.getCode());
            message.setLang(lang);

            if (lang.getCode().equals("kz")) message.setValue(wrapper.getNameKz());
            else if (lang.getCode().equals("en")) message.setValue(wrapper.getNameEn());
            else if (lang.getCode().equals("ru")) message.setValue(wrapper.getNameRu());

            em.merge(message);
        }

        return hierarchy;
    }

    public void delete(long id) {
        LineHierarchy hierarchy = (LineHierarchy) em.find(LineHierarchy.class, id);

        if (hierarchy.getParent() != null) {
            hierarchy.getParent().getChildren().remove(hierarchy);
            em.merge(hierarchy.getParent());
        }

        for (LineHierarchy child: hierarchy.getChildren()) {
            child.setParent(null);
            em.merge(child);
        }

        em.remove(hierarchy);
    }

    public boolean addLine(Long id, LineWrapper lineWrapper) {
        try {
            LineHierarchy hierarchy = (LineHierarchy) em.find(LineHierarchy.class, id);
            Line line = (Line) em.find(Line.class, lineWrapper.getId());

            if (!hierarchy.getLines().contains(line)) {
                hierarchy.getLines().add(line);
                em.merge(hierarchy);

                line.setLineHierarchy(hierarchy);
                em.merge(line);
            }
            return true;
        }
        catch (Exception e) {
//            e.printStackTrace();
            return false;
        }
    }

    public boolean removeLine(Long id, Long lineId) {
        try {
            LineHierarchy hierarchy = (LineHierarchy) em.find(LineHierarchy.class, id);
            Line line = (Line) em.find(Line.class, lineId);

            if (hierarchy.getLines().contains(line)) {
                hierarchy.getLines().remove(line);
                em.merge(hierarchy);

                line.setLineHierarchy(null);
                em.merge(line);
            }

            return true;
        }
        catch (Exception e) {
//            e.printStackTrace();
            return false;
        }
    }
}
