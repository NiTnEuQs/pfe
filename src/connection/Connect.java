package connection;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import fr.lifl.carbon.wse.WSE;
import fr.lifl.carbon.wse.WSEListener;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class Connect extends AnAction {

    public Connect() {
        super("Connect");
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
        wse.onConnection(arg0 -> {
            finalWse.joinSession("NOM_SESSION");
            finalWse.addListener(new WSEListener() {
                @Override
                public void onMessage(JSONObject msg) {
                    // Chaque message aura la forme suivante :
                    msg = {
                            "id":000, // généré par le serveur
                            "source":"source_id", // Un id unique pour la personne qui a envoyé le message
                            "timestamp":123456, // Le timestamp du message
                            "session":"NOM_SESSION", // Le nom de la session sur laquelle le message passe
                            "data":{
                        // Tout ce qui a été envoyé par l'utilisateur est compris dans "data"
                    }
                    }
                }
            }, "NOM_SESSION");

            JSONObject startMessage = new JSONObject();

            try {
                startMessage.put("application", "AndroidStudio");
                startMessage.put("action", "started");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

            finalWse.sendMessage(startMessage, "NOM_SESSION");
        });

        wse.connect();
    }

}
