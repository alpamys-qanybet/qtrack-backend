package kz.essc.qtrack.websocket.operator.display;

import kz.essc.qtrack.kiosk.KioskBean;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint("/operatordisplay/{client-id}")
public class OperatorDisplayEventSocketMediator {

    private static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());

    @Inject
    KioskBean kioskBean;

    @OnMessage
    public String onMessage(String message, Session session, @PathParam("client-id") String clientId) {
        try {
            JSONObject jObj = new JSONObject(message);

            for (Session s : peers) {
                try {
                    s.getBasicRemote().sendText(message);
                } catch (IOException e) {
                }
            }
        } catch (JSONException e) {
        }
        return "message was received by socket mediator and processed: " + message;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("client-id") String clientId) {
        peers.add(session);

        try {
            session.getBasicRemote().sendText("good to be in touch Operator Display " + clientId);
            session.getBasicRemote().sendText(kioskBean.messageOnLaunchOperatorDisplay());
        } catch (IOException | JSONException e) {
//            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session, @PathParam("client-id") String clientId) {
        peers.remove(session);
    }
}