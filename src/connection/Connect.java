package connection;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import fr.lifl.carbon.wse.WSE;
import fr.lifl.carbon.wse.WSEListener;
import org.json.JSONObject;
import utilities.Log;
import utilities.Message;

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

        // On vérifie que wse est bien initialisé
        assert wse != null;

        WSE finalWse = wse;
        finalWse.onConnection(objects -> {
            // Dès que la connection est effectuée, on rejoint la session NOM_SESSION
            finalWse.joinSession(NOM_SESSION);

            // On écoute les messages envoyés sur la session NOM_SESSION
            finalWse.addListener(new WSEListener() {
                @Override
                public void onMessage(JSONObject object) {
                    Message msg = new Message(object);

                    Log.v(msg);
                }
            }, NOM_SESSION);

            // On envoie ensuite le message de lancement de l'application Android Studio
            Message msg = new Message(new String[][]{
                    {"application", "AndroidStudio"},
                    {"action", "started"},
            });

            finalWse.sendMessage(msg.getMessage(), NOM_SESSION);
        });

        // On effectue la connexion avec WSE
        finalWse.connect();
    }

}
