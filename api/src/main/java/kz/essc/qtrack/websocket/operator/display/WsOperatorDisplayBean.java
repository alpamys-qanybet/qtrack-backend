package kz.essc.qtrack.websocket.operator.display;

import kz.essc.qtrack.core.resource.ResourceBean;
import kz.essc.qtrack.websocket.operator.OperatorEventSocketClient;

import javax.enterprise.context.RequestScoped;
import java.net.URI;
import java.net.URISyntaxException;

@RequestScoped
public class WsOperatorDisplayBean {

    private OperatorEventSocketClient client;
    // alpamys-samsung 192.168.0.103
    private final String wsAddress = "ws://"+ ResourceBean.getHost()+"/api/operatordisplay";

    private void initWS(String clientId) throws URISyntaxException {
        client = new OperatorEventSocketClient(new URI(wsAddress + "/"+clientId));
        client.addMessageHandler(new OperatorEventSocketClient.MessageHandler() {
            public void handleMessage(String message) {
            }
        });
    }

    public void sendMessageOverSocket(String message, String clientId) {
        if (client == null) {
            try {
                initWS(clientId);
            } catch (URISyntaxException e) {}
        }
        client.sendMessage(message);
    }
}
