package kz.essc.qtrack.websocket.operator;

import kz.essc.qtrack.core.resource.ResourceBean;

import javax.enterprise.context.RequestScoped;
import java.net.URI;
import java.net.URISyntaxException;

@RequestScoped
public class WsOperatorBean {

    private OperatorEventSocketClient client;
//    private final String wsOperDispAddress = "ws://192.168.100.14:8080/api/operdisp";
    private final String wsAddress = "ws://"+ ResourceBean.getHost()+"/api/operator";

    private void initWS(String clientId) throws URISyntaxException {
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
