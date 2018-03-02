package carbonx;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import api.Message;
import com.android.tools.idea.editors.layoutInspector.LayoutInspectorEditor;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import utilities.Log;

import java.awt.*;
import java.io.ByteArrayOutputStream;

/**
 * La classe qui contient l'action de "CarbonX > Envoyer l'image du layout"
 */
public class SendScreenLayout extends AnAction {

    public SendScreenLayout() {
        super("Envoyer l'image du layout");
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        // On vérifie que wse est bien initialisé
        assert WSEHandler.wse != null;

        Project project = ProjectManager.getInstance().getOpenProjects()[0];

        String pathname = "/app/src/main/res/layout/activity_main.xml";
        VirtualFile vf = project.getBaseDir().findFileByRelativePath(pathname);
        Graphics g = null;

        if (vf != null) {
            LayoutInspectorEditor lie = new LayoutInspectorEditor(project, vf);
            g = lie.getComponent().getGraphics();
        } else {
            Log.e(String.format("VirtualFile %s is null", pathname));
        }

        Message message = null;

        if (g != null) {
            message = new Message(new String[][]{
                    {"layout_image", g.toString()}//bitmapToString(xmlToBitmap(pathname))}
            });
        } else {
            Log.e(String.format("Graphics of VirtualFile %s is null", pathname));
        }

        /*
        ScreenshotAction sa = new ScreenshotAction(project, new DeviceContext());
        com.android.tools.idea.actions.SaveScreenshotAction s = new com.android.tools.idea.actions.SaveScreenshotAction();
        ActionManager am = ActionManager.getInstance();
        ActionManagerEx amx;
        amx.fireBeforeActionPerformed(s, );
        am.tryToExecute(s, );
        com.android.layoutinspector.LayoutInspectorCaptureOptions c;
        c.
        com.android.ide.common.rendering.api.Bridge b;
        */

        /*
          Essai en cherchant l'Editor correspondant
        */
//        FileEditor[] editors = FileEditorManager.getInstance(project).getAllEditors();
//        for (FileEditor fe : editors) {
//            if (fe.getClass().toString().equals(NlEditor.class.toString())) {
//                Log.i("Trouvé: " + fe.getName());
//            }
//        }

        if (message != null) {
            WSEHandler.wse.sendMessage(message.getMessage(), WSEHandler.NOM_SESSION);
        } else {
            Log.e("Message is null");
        }
    }

    public Bitmap xmlToBitmap(String pathname) {
        Drawable d = Drawable.createFromPath(pathname);
        assert d != null;
        return Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
    }

    public String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public Bitmap stringToBitmap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

}
