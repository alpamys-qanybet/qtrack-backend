package kz.essc.qtrack.kiosk.display;

import kz.essc.qtrack.client.*;
import kz.essc.qtrack.client.Process;
import kz.essc.qtrack.operator.OperatorWrapper;
import kz.essc.qtrack.process.ProcessWrapper;
import kz.essc.qtrack.sc.user.Role;
import kz.essc.qtrack.sc.user.User;
import org.codehaus.jettison.json.JSONObject;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;

@RequestScoped
public class DisplayBean {
    @PersistenceContext
    EntityManager em;

    public DisplayWrapper get(int display) {
        List<User> list = em.createQuery("select u from User u join u.roles r " +
                                        "where r.name = :role " +
                                        "and u.display = :display")
                                        .setParameter("role", Role.Name.OPERATOR.toString())
                                        .setParameter("display", display)
                                        .getResultList();
        long max = 0L;
        String code = null;
        String status = null;
        // the last evented client code and status of operators
        for (User operator: list) {
            if (operator.getClient() != null) {
                Client client = operator.getClient();
                if (client.getStatus().equals(Client.Status.CALLED.toString()) || client.getStatus().equals(Client.Status.IN_PROCESS.toString())) {
                    long time = client.getEvent().getTime();

                    if (time >= max) {
                        max = time;
                        code = client.getCode();
                        status = client.getStatus();
                    }
                }
            }
        }

        DisplayWrapper w = new DisplayWrapper();
        w.setCode(code);
        w.setStatus(status);

        return w;
    }
}
