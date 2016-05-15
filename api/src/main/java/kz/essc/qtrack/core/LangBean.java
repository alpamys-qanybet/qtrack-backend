package kz.essc.qtrack.core;

import kz.essc.qtrack.sc.Language;
import kz.essc.qtrack.sc.Message;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@RequestScoped
public class LangBean {

    @PersistenceContext
    EntityManager em;

    public enum Code {
        kz, en, ru
    }

    public List<Language> getLanguages() {
        return em.createQuery("select l from Language l").getResultList();
    }

    public Language getLanguage(String code) {
        Language lang = (Language) em.createQuery("select l from Language l where l.code = :code")
                .setParameter("code", code)
                .getSingleResult();

        return lang;
    }

    public String getMessage(String name, String code) {
        String value = "";
        Language lang = getLanguage(code);

//        value = (String) em.createQuery("select m.value from Message m join Language l where m.name = :name and l.code = :code")
//                            .setParameter("name", name)
//                            .setParameter("code", code)
//                            .getSingleResult();
        value = (String) em.createQuery("select m.value from Message m where m.name = :name and m.lang = :lang")
                .setParameter("name", name)
                .setParameter("lang", lang)
                .getSingleResult();

        return value;
    }

    public Message getMessageInstance(String name, String code) {
        Language lang = getLanguage(code);

        return (Message) em.createQuery("select m from Message m where m.name = :name and m.lang = :lang")
                                        .setParameter("name", name)
                                        .setParameter("lang", lang)
                                        .getSingleResult();
    }
}
