package connection;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import fr.lifl.carbon.wse.WSE;
import fr.lifl.carbon.wse.WSEListener;
import message.Message;
import org.json.JSONObject;
import utilities.Log;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * La classe qui contient l'action de "CarbonX > Connexion"
 * La connexion est effectué avec le serveur distant, une session est rejointe et les messages sont traités
 */
public class Connect extends AnAction {

    public final static String NOM_SESSION = "pfe";
    public static WSE wse;

    public Connect() {
        super("Connexion");
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        wse = null;

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
                    Message data = msg.getJSONToMessage("data");

                    handleMessage(data);
                }
            }, NOM_SESSION);

            // On envoie ensuite le message de lancement de l'application Android Studio
            /*
            Message msg = new Message(new String[][]{
                    {"application", "AndroidStudio"},
                    {"action", "started"},
            });

            finalWse.sendMessage(msg.getMessage(), NOM_SESSION);
            */
        });

        // On effectue la connexion avec WSE
        finalWse.connect();
    }

    public void handleMessage(Message msg) {
        Project project = ProjectManager.getInstance().getOpenProjects()[0];

        switch (msg.getType()) {
            case CREATE_FILE:
                String location = msg.getAsString("location");
                String filename = msg.getAsString("filename");
                String relativePath = location + "/" + filename;

                File file = new File(project.getBasePath() + relativePath);

                if (!file.exists()) {
                    try {
                        if (!file.getParentFile().exists() && file.getParentFile().mkdirs()) {
                            Log.v("Folders created ! (" + file.getParent() + ")");
                        }

                        if (file.createNewFile()) {
                            Log.v("File created ! (" + file.getPath() + ")");
                        }
                    } catch (IOException e) {
                        Log.e(file.getPath());
                        e.printStackTrace();
                    }
                }

                //VirtualFile vf = project.getBaseDir().findFileByRelativePath(relativePath);
                //FileEditorManager.getInstance(project).openFile(vf, true, true);

                //OpenFileDescriptor ofd = new OpenFileDescriptor(project, vf);
                //if (ofd.canNavigateToSource()) ofd.navigate(true);

                break;
            case GO_TO_FILE:

                break;
            case GO_TO_LINE:

                break;
            default:

                break;
        }
    }

}
