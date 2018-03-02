package carbonx;

import api.Concept;
import api.Message;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.squareup.javapoet.*;
import fr.lifl.carbon.wse.WSEListener;
import org.json.JSONObject;
import utilities.Log;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CarbonXListener extends WSEListener {

    @Override
    public void onMessage(JSONObject jsonObject) {
        Message msg = new Message(jsonObject);
        Message data = msg.getJSONToMessage("data");

        handleMessage(data);
    }

    private void handleMessage(Message msg) {
        Project project = ProjectManager.getInstance().getOpenProjects()[0];

        String stringTmp;

        switch (msg.getType()) {
            case CREATE_FILE:
                // {"type": "create_file","location": "/src/tmp","filename": "xmlToBitmap.java","content": "Test"}
                stringTmp = msg.getAsString("location") + "/" + msg.getAsString("filename");

                createFile(project, stringTmp);
                break;
            case CREATE_PACKAGES:
                // {"type": "create_packages","packages": "/src/com/pfe/model"}
                stringTmp = msg.getAsString("packages");

                createPackages(project, stringTmp);
                break;
            case CREATE_CONCEPT:
                // {"type": "create_concept","concept": "Personne: age(int), nom(String), prenom(String)\nFete: personnes(Personne[]), adresse(String), heure(Date)"}
                stringTmp = msg.getAsString("concept");

                createConcept(project, stringTmp);
                break;
            case OPEN_FILE:
                stringTmp = msg.getAsString("location") + "/" + msg.getAsString("filename");

                openFile(project, stringTmp);
                break;
            case GO_TO_LINE:

                break;
            default:

                break;
        }
    }

    private void createPackages(Project project, String relativePath) {
        File file = new File(project.getBasePath() + relativePath);

        if (!file.exists() && file.mkdirs()) {
            Log.i("Packages \"" + file.getPath() + "\" created !");

            refreshProject(project);
        } else {
            Log.i("Packages \"" + file.getPath() + "\" already existing");
        }
    }

    private void createFile(Project project, String relativePath) {
        File file = new File(project.getBasePath() + relativePath);

        if (!file.exists()) {
            try {
                if (!file.getParentFile().exists() && file.getParentFile().mkdirs()) {
                    Log.i("Folders \"" + file.getParent() + "\" created !");
                }

                if (file.createNewFile()) {
                    Log.i("File \"" + file.getPath() + "\" created !");
                }

                refreshProject(project);
            } catch (IOException e) {
                Log.e("I/O Exception on \"" + file.getPath() + "\"");
            }
        } else {
            Log.i("File \"" + file.getPath() + "\" already existing");
        }
    }

    private void createConcept(Project project, String string_concept) {
        Concept concept = new Concept(string_concept);

        for (String entity : concept.keySet()) {
            //String relativePath = "/src/main/java/model/" + entity + ".java";
            String relativePath = "/src/main/java/model/";

            //createFile(project, relativePath + ".java");

            List<FieldSpec> fieldSpecs = new ArrayList<>();
            for (String attribute : concept.getAttributes(entity)) {
                fieldSpecs.add(
                        FieldSpec.builder(int.class, attribute)
                                .addModifiers(Modifier.PRIVATE)
                                .build()
                );
            }

            CodeBlock.Builder builder = CodeBlock.builder();
            List<ParameterSpec> constructorParameters = new ArrayList<>();
            for (FieldSpec fieldSpec : fieldSpecs) {
                constructorParameters.add(
                        ParameterSpec.builder(int.class, fieldSpec.name)
                                .build()
                );

                builder.add("this.$N = $N;\n", fieldSpec.name, fieldSpec.name);
            }

            CodeBlock codeBlock = builder.build();
            MethodSpec constructor = MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addParameters(constructorParameters)
                    .addStatement(codeBlock)
                    .build();

            TypeSpec javaClass = TypeSpec.classBuilder(entity)
                    .addModifiers(Modifier.PUBLIC)
                    .addFields(fieldSpecs)
                    .addMethod(constructor)
                    .build();

            JavaFile javaFile = JavaFile.builder("main.java.model", javaClass)
                    .build();

            File directory = new File(project.getBasePath() + "/src");

            try {
                if (directory.exists())
                    javaFile.writeTo(directory);
            } catch (IOException e) {
                e.printStackTrace();
            }

            /*
            String filePath = project.getBasePath() + relativePath;

            PrintWriter writer;

            try {
                writer = new PrintWriter(filePath, "UTF-8");
                writer.print(concept.getClassContent(entity));
                writer.close();
            } catch (FileNotFoundException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            */
        }
    }

    private void openFile(Project project, String relativePath) {
        VirtualFile vf = project.getBaseDir().findFileByRelativePath(relativePath);

        if (vf == null) {
            Log.e("File \"" + relativePath + "\" not found");
        } else {
            OpenFileDescriptor ofd = new OpenFileDescriptor(project, vf);
            ApplicationManager.getApplication().invokeLater(() ->
                    FileEditorManager.getInstance(project).openTextEditor(ofd, true));

            Log.i("File \"" + relativePath + "\" now opened !");
        }
    }

    private void refreshProject(Project project) {
        project.getBaseDir().refresh(false, true);
        Log.i("Project refreshed !");
    }

}
