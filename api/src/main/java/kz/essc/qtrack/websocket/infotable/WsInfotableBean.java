//package kz.essc.qtrack.websocket.infotable;
//
//import kz.essc.qtrack.core.resource.ResourceBean;
//import kz.essc.qtrack.websocket.operator.OperatorEventSocketClient;
//
//import javax.enterprise.context.RequestScoped;
//import java.net.URI;
//import java.net.URISyntaxException;
//
//@RequestScoped
//public class WsInfotableBean {
//
//    private OperatorEventSocketClient client;
//    private final String wsAddress = "ws://"+ ResourceBean.getHost()+"/api/infotable";
//
//    private void initWS(String clientId) throws URISyntaxException {
//        client = new OperatorEventSocketClient(new URI(wsAddress + "/"+clientId));
//        client.addMessageHandler(new OperatorEventSocketClient.MessageHandler() {
//            public void handleMessage(String message) {
//            }
//        });
//    }
//
//    public void sendMessageOverSocket(String message, String clientId) {
//        if (client == null) {
//            try {
//                initWS(clientId);
//            } catch (URISyntaxException e) {
//                e.printStackTrace();
//            }
//        }
//        client.sendMessage(message);
//    }
//}
