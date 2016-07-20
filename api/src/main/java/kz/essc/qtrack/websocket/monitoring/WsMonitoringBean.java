//package kz.essc.qtrack.websocket.monitoring;
//
//import kz.essc.qtrack.core.resource.ResourceBean;
////import kz.essc.qtrack.websocket.operator.OperatorEventSocketClient;
//
//import javax.enterprise.context.RequestScoped;
//import java.net.URI;
//import java.net.URISyntaxException;
//
//@RequestScoped
//public class WsMonitoringBean {
//
//    private MonitoringEventSocketClient client;
//    private final String wsAddress = "ws://"+ ResourceBean.getHost()+"/api/monitoring";
////    private final String wsAddress = "ws://192.168.1.108:8080/api/monitoring";
//
//    private void initWS(String clientId) throws URISyntaxException {
////        System.out.println("REST service: open websocket client at " + wsAddress);
//        client = new MonitoringEventSocketClient(new URI(wsAddress + "/"+clientId));
//        // add listener
//        client.addMessageHandler(new MonitoringEventSocketClient.MessageHandler() {
//            public void handleMessage(String message) {
////                System.out.println("messagehandler in REST service - process message " + message);
//            }
//        });
//    }
//
//    public void sendMessageOverSocket(String message, String clientId) {
//        if (client == null) {
//            try {
//                initWS(clientId);
//            } catch (URISyntaxException e) {
////                e.printStackTrace();
//            }
//        }
//        client.sendMessage(message);
//    }
//}
