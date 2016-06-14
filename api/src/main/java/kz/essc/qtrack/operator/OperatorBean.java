package kz.essc.qtrack.operator;

import kz.essc.qtrack.sc.user.Role;
import kz.essc.qtrack.sc.user.User;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@RequestScoped
public class OperatorBean {
    @PersistenceContext
    EntityManager em;

    public List<User> get() {

        try {
            return em.createQuery("select u from User u join u.roles r " +
                    "where r.name = :role " +
                    "order by u.cabinet")
                    .setParameter("role", Role.Name.OPERATOR.toString())
                    .getResultList();
        }
        catch (Exception e) {
//            e.printStackTrace();
            return null;
        }
    }

    public User edit(Long id, OperatorWrapper wrapper) {
        try {
            User operator = (User) em.find(User.class, id);
            operator.setCabinet(wrapper.getCabinet());
            operator.setPosition(wrapper.getPosition());
            operator.setEnabled(wrapper.getEnabled());
//            operator.setOnline(operatorWrapper.getOnline());
            operator.setDisplay(wrapper.getDisplay());
            return em.merge(operator);
        }
        catch (Exception e) {
//            e.printStackTrace();
            return null;
        }
    }

    public List<User> getAvailable() {
        try {
            return em.createQuery("select u from User u join u.roles r " +
                    "where r.name = :role " +
                    "and u.line is null")
                    .setParameter("role", Role.Name.OPERATOR.toString())
                    .getResultList();
        }
        catch (Exception e) {
//            e.printStackTrace();
            return null;
        }
    }
}
