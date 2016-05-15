package kz.essc.qtrack.websocket.infotable;

import kz.essc.qtrack.websocket.operator.OperatorEventSocketClient;

import javax.enterprise.context.RequestScoped;
import java.net.URI;
import java.net.URISyntaxException;

@RequestScoped
public class WsInfotableBean {

    private OperatorEventSocketClient client;
    private final String wsAddress = "ws://alpamys-samsung:8080/api/infotable";
//    private final String wsAddress = "ws://192.168.1.108:8080/api/infotable";

    private void initWS(String clientId) throws URISyntaxException {
//        System.out.println("REST service: open websocket client at " + wsAddress);
        client = new OperatorEventSocketClient(new URI(wsAddress + "/"+clientId));
        // add listener
        client.addMessageHandler(new OperatorEventSocketClient.MessageHandler() {
            public void handleMessage(String message) {
//                System.out.println("messagehandler in REST service - process message " + message);
            }
        });
    }

    public void sendMessageOverSocket(String message, String clientId) {
        if (client == null) {
            try {
                initWS(clientId);
            } catch (URISyntaxException e) {
//                e.printStackTrace();
            }
        }
        client.sendMessage(message);
    }
}
