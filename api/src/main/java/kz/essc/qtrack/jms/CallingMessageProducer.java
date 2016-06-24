package kz.essc.qtrack.jms;

import kz.essc.qtrack.client.ClientWrapper;
import kz.essc.qtrack.sc.user.User;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RequestScoped
public class CallingMessageProducer {
    Context ic = null;

    ConnectionFactory connectionFactory = null;

    Connection connection = null;

    @PersistenceContext
    EntityManager em;

    public void sendMessage(ClientWrapper wrapper) throws NamingException {
        System.out.println("QUEUE sending message");

        ic = new InitialContext();
        connectionFactory = (ConnectionFactory) ic.lookup("jms/GlassFishBookConnectionFactory");

        String destinationName = "jms/GlassFishBookQueue";

        try {
            connection = connectionFactory.createConnection();
            Queue queue = (Queue) ic.lookup(destinationName);
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            javax.jms.MessageProducer publisher = session.createProducer(queue);

            connection.start();

            User operator = (User) em.find(User.class, wrapper.getOperatorId());

            MapMessage m = session.createMapMessage();
            m.setStringProperty("lang", wrapper.getLang());
            m.setStringProperty("code", wrapper.getCode());
            m.setStringProperty("cabinet", operator.getCabinet());
            m.setIntProperty("floor", operator.getFloor());

            publisher.send(m);
            System.out.println("QUEUE sent!");
        } catch (Exception exc) {
//            exc.printStackTrace();
        } finally {

            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
//                    e.printStackTrace();
                }
            }
        }
    }
}
