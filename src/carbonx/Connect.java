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

    public Connect() {
        super("Connexion à Carbon X");
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Log.i("IDE is trying to link to CarbonX ...");

        try {
            // On initialise la connexion vers le serveur distant
            WSEHandler.wse = new WSE("http://vps131654.ovh.net", 80);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        // On vérifie que wse est bien initialisé
        assert WSEHandler.wse != null;

        WSE finalWse = WSEHandler.wse;
        finalWse.onConnection(objects -> {
            Log.i("IDE now linked with CarbonX !");

            // Dès que la carbonx est effectuée, on rejoint la session NOM_SESSION
            finalWse.joinSession(WSEHandler.NOM_SESSION);

            // On écoute les messages envoyés sur la session NOM_SESSION
            finalWse.addListener(new CarbonXListener(), WSEHandler.NOM_SESSION);
        });

        finalWse.onConnectionError(objects -> Log.e("Connection error"));
        finalWse.onConnectionTimeout(objects -> Log.e("Connection timeout"));
        finalWse.onReconnectFailed(objects -> Log.e("Reconnection failed"));
        finalWse.onReconnectError(objects -> Log.e("Reconnection timeout"));
        finalWse.onError(objects -> Log.e("Error"));
        finalWse.onDisconnect(objects -> Log.e("Disconnected !"));

        // On effectue la connexion avec WSE
        finalWse.connect();

        // Test de gestion de la capture d'écran grâce aux Listeners
        /*
          Apparemment, CaptureService ne peut pas être cast en CaptureService ... Voici le message d'erreur:

          com.android.tools.idea.profiling.capture.CaptureTypeService cannot be cast to com.android.tools.idea.profiling.capture.CaptureTypeService
          java.lang.ClassCastException: com.android.tools.idea.profiling.capture.CaptureTypeService cannot be cast to com.android.tools.idea.profiling.capture.CaptureTypeService
	      at com.android.tools.idea.profiling.capture.CaptureTypeService.getInstance(CaptureTypeService.java:30)
	      at carbonx.Connect.actionPerformed(Connect.java:73)
        */
//        Project project = ProjectManager.getInstance().getOpenProjects()[0];
//        CaptureService cs = CaptureService.getInstance(project);
//        cs.addListener(capture -> {
//            VirtualFile screenshot = capture.getFile();
//            Log.i(screenshot.getPath());
//        });

        /*
          Détecte bien l'action de Save Screenshot mais impossible de récupérer la capture d'écran. Il est nécessaire
          d'avoir un IDevice
        */
//        ActionManager am = ActionManager.getInstance();
//        am.addAnActionListener(new AnActionListener.Adapter() {
//            @Override
//            public void beforeActionPerformed(AnAction action, DataContext dataContext, AnActionEvent event) {
//                super.beforeActionPerformed(action, dataContext, event);
//            }
//
//            @Override
//            public void afterActionPerformed(AnAction action, DataContext dataContext, AnActionEvent event) {
//                super.afterActionPerformed(action, dataContext, event);
//
//                String actionClass = action.getClass().toString();
//
//                //Log.v(String.format("%s == %s", actionClass, SaveScreenshotAction.class.toString()));
//                if (actionClass.equals(SaveScreenshotAction.class.toString())) {
//                    Project project = event.getProject();
//                    IDevice device = null;
//
//                    if (project != null) {
//                        ScreenshotTask st = new ScreenshotTask(project, device);
//                        BufferedImage bi = st.getScreenshot();
//                        Log.i(String.format("On vient de screenshot: %s", bi.toString()));
//                    }
//                }
//            }
//
//            @Override
//            public void beforeEditorTyping(char c, DataContext dataContext) {
//                super.beforeEditorTyping(c, dataContext);
//            }
//        });
    }

}
