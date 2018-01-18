package carbonx;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import fr.lifl.carbon.wse.WSE;
import utilities.Log;

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
            Log.i("IDE now linked with CarbonX");

            // Dès que la carbonx est effectuée, on rejoint la session NOM_SESSION
            finalWse.joinSession(NOM_SESSION);

            // On écoute les messages envoyés sur la session NOM_SESSION
            finalWse.addListener(new CarbonXListener(), NOM_SESSION);
        });

        finalWse.onConnectionError(objects -> Log.e("Connection error"));
        finalWse.onConnectionTimeout(objects -> Log.e("Connection timeout"));
        finalWse.onReconnectFailed(objects -> Log.e("Reconnection failed"));
        finalWse.onReconnectError(objects -> Log.e("Reconnection timeout"));
        finalWse.onError(objects -> Log.e("Error"));
        finalWse.onDisconnect(objects -> Log.e("Disconnected !"));

        // On effectue la connexion avec WSE
        finalWse.connect();
    }

}
