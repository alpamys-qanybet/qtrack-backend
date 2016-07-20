//package kz.essc.qtrack.websocket.operator;
//
//import kz.essc.qtrack.kiosk.KioskBean;
//import org.codehaus.jettison.json.JSONException;
//import org.codehaus.jettison.json.JSONObject;
//
//import javax.inject.Inject;
//import javax.websocket.OnClose;
//import javax.websocket.OnMessage;
//import javax.websocket.OnOpen;
//import javax.websocket.Session;
//import javax.websocket.server.PathParam;
//import javax.websocket.server.ServerEndpoint;
//import java.io.IOException;
//import java.util.*;
//
//
//@ServerEndpoint("/operator/{client-id}")
//public class OperatorEventSocketMediator {
//
//    private static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());
//
//    @Inject
//    KioskBean kioskBean;
//
//    @OnMessage
//    public String onMessage(String message, Session session, @PathParam("client-id") String clientId) {
//        try {
//            JSONObject jObj = new JSONObject(message);
////            System.out.println("received message from client " + clientId);
//
//            for (Session s : peers) {
//                try {
//                    s.getBasicRemote().sendText(message);
////                    System.out.println("send message to peer ");
//                } catch (IOException e) {
////                    e.printStackTrace();
//                }
//
//            }
////            session.getBasicRemote().sendText(message);
//        } catch (JSONException e) {
////            e.printStackTrace();
//        }
////        catch (IOException e) {
////        }
//        return "message was received by socket mediator and processed: " + message;
//    }
//
//    @OnOpen
//    public void onOpen(Session session, @PathParam("client-id") String clientId) {
////        System.out.println("mediator: opened websocket channel for client " + clientId);
//        peers.add(session);
//
//        try {
//            session.getBasicRemote().sendText("good to be in touch Operator " + clientId);
//            session.getBasicRemote().sendText(kioskBean.messageOnLaunchOperator());
//        } catch (IOException | JSONException e) {
////            e.printStackTrace();
//        }
//    }
//
//    @OnClose
//    public void onClose(Session session, @PathParam("client-id") String clientId) {
////        System.out.println("mediator: closed websocket channel for client " + clientId);
//        peers.remove(session);
//    }
//}