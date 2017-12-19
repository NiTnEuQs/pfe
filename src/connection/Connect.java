package connection;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import fr.lifl.carbon.wse.WSE;
import fr.lifl.carbon.wse.WSEListener;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

/**
 * La classe qui contient l'action de "CarbonX > Connexion"
 * La connexion est effectué avec le serveur distant, une session est rejointe et les messages sont traités
 */
public class Connect extends AnAction {

    private final static String NOM_SESSION = "pfe";

    public Connect() {
        super("Connexion");
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        WSE wse = null;

        try {
            // On initialise la connexion vers le serveur distant
            wse = new WSE("http://vps131654.ovh.net", 80);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        // On vérifie si wse est bien initialisé
        assert wse != null;

        WSE finalWse = wse;
        finalWse.onConnection(objects -> {
            // Dès que la connection est effectuée, on rejoint la session NOM_SESSION
            finalWse.joinSession(NOM_SESSION);

            // On envoie ensuite le message de lancement de l'application Android Studio
            // TODO Attention, le message s'envoie toutes les X secondes
            JSONObject startMessage = new JSONObject();

            try {
                startMessage.put("application", "AndroidStudio");
                startMessage.put("action", "started");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            finalWse.sendMessage(startMessage, NOM_SESSION);
        });

        // On écoute les messages envoyés sur la session NOM_SESSION
        finalWse.addListener(new WSEListener() {
            @Override
            public void onMessage(JSONObject msg) {
                try {
                    System.out.println("-*-*-(" + msg.get("id") + ")-*-*-");
                    System.out.println("Source: " + msg.get("source"));
                    System.out.println("Timestamp: " + msg.get("timestamp"));
                    System.out.println("Session: " + msg.get("session"));
                    System.out.println("Data: " + msg.get("data"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, NOM_SESSION);

        // On effectue la connexion avec WSE
        finalWse.connect();
    }

}
