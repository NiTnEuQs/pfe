package connection;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import fr.lifl.carbon.wse.WSE;
import message.Message;

import java.net.URISyntaxException;

public class CreateFile extends AnAction {

    public CreateFile() {
        super("CreateFile");
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Message msg = new Message(new String[][]{
                {"type", "create_file"},
                {"location", "/src/tmp"},
                {"filename", "test.java"},
                {"content", "Test"},
        });

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
            finalWse.joinSession(Connect.NOM_SESSION);
            finalWse.sendMessage(msg.getMessage(), Connect.NOM_SESSION);
        });

        // On effectue la connexion avec WSE
        finalWse.connect();
    }

}
