package connection;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import fr.lifl.carbon.wse.WSE;
import fr.lifl.carbon.wse.WSEListener;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class Connect extends AnAction {

    private final static String NOM_SESSION = "pfe";

    public Connect() {
        super("Connexion");
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        WSE wse = null;

        try {
            wse = new WSE("http://vps131654.ovh.net:80");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        assert wse != null;

        WSE finalWse = wse;
        finalWse.onConnection(objects -> {
            finalWse.joinSession(NOM_SESSION);
            finalWse.addListener(new WSEListener() {
                @Override
                public void onMessage(JSONObject msg) {
                    System.out.println("Message reçu !!!");
                    try {
                        System.out.println("ID: " + msg.get("id"));
                        System.out.println("Source: " + msg.get("source"));
                        System.out.println("Timestamp: " + msg.get("timestamp"));
                        System.out.println("Session: " + msg.get("session"));
                        System.out.println("Data: " + msg.get("data"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, NOM_SESSION);

            JSONObject startMessage = new JSONObject();

            try {
                startMessage.put("application", "AndroidStudio");
                startMessage.put("action", "started");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

            finalWse.sendMessage(startMessage, NOM_SESSION);
        });

        finalWse.setSessionJoinedListener(objects ->
                System.out.println("Quelqu'un s'est connecté !")
        );

        finalWse.connect();
    }

}
