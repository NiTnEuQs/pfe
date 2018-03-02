package api;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;

public class Concept extends HashMap<String, String[]> {

    public Concept(String string_concept) {
        super();

        String[] lines = string_concept.split("\n");

        for (String line : lines) {
            String entity = line.split(":")[0].trim();
            String[] attributes = line.split(":")[1].replace(" ", "").split(",");

            put(entity, attributes);
        }
    }

    public String getClassContent(String entity) {
        return "package main.java.model;\n\n" +
                //getImportsAsString(entity) + "\n" +
                "public class " + entity + " {\n\n" +
                "\t// Attributes\n\n" +
                getAttributesAsString(entity) + "\n" +
                "\t// Constructors\n\n" +
                getConstructorAsString(entity) + "\n" +
                "\t// Getters / Setters\n\n" +
                getGettersSettersAsString(entity) +
                "}";
    }

    public String getImportsAsString(String entity) {
        StringBuilder imports = new StringBuilder();

        for (String attribute : get(entity)) {
            String attribute_type = attribute.split("\\(")[1].replace(")", "").trim();

            imports.append("import ").append(attribute_type).append(";\n");
        }


        return imports.toString();
    }

    public String[] getAttributes(String entity) {
        String[] attributes = new String[get(entity).length];

        for (int i = 0; i < get(entity).length; i++) {
            String attribute = get(entity)[i];
            String attribute_name = attribute.split("\\(")[0].trim();
            String attribute_type = attribute.split("\\(")[1].replace(")", "").trim();

            attributes[i] = attribute_name;
        }

        return attributes;
    }

    private String getAttributesAsString(String entity) {
        StringBuilder attributes = new StringBuilder();

        for (String attribute : get(entity)) {
            String attribute_name = attribute.split("\\(")[0].trim();
            String attribute_type = attribute.split("\\(")[1].replace(")", "").trim();

            attributes.append("\tprivate ").append(attribute_type).append(" ").append(attribute_name).append(";\n");
        }

        return attributes.toString();
    }

    private String getConstructorAsString(String entity) {
        StringBuilder constructor = new StringBuilder("\tpublic " + entity + " (");

        for (int i = 0; i < get(entity).length; i++) {
            String attribute = get(entity)[i];
            String attribute_name = attribute.split("\\(")[0].trim();
            String attribute_type = attribute.split("\\(")[1].replace(")", "").trim();

            constructor.append(attribute_type).append(" ").append(attribute_name).append(i < get(entity).length - 1 ? ", " : "");
        }

        constructor.append(") {\n");

        for (String attribute : get(entity)) {
            String attribute_name = attribute.split("\\(")[0].trim();

            constructor.append("\t\tthis.").append(attribute_name).append(" = ").append(attribute_name).append(";\n");
        }

        constructor.append("\t}\n");

        return constructor.toString();
    }

    private String getGettersSettersAsString(String entity) {
        StringBuilder gettersSetters = new StringBuilder();

        for (String attribute : get(entity)) {
            String attribute_name = attribute.split("\\(")[0].trim();
            String attribute_type = attribute.split("\\(")[1].replace(")", "").trim();

            gettersSetters
                    .append("\tpublic ")
                    .append(attribute_type)
                    .append(" get")
                    .append(StringUtils.capitalize(attribute_name))
                    .append("() {\n")
                    .append("\t\treturn this.")
                    .append(attribute_name)
                    .append(";\n")
                    .append("\t}\n");
            gettersSetters
                    .append("\tpublic void set")
                    .append(StringUtils.capitalize(attribute_name))
                    .append("(").append(attribute_type)
                    .append(" ")
                    .append(attribute_name).append(") {\n")
                    .append("\t\tthis.")
                    .append(attribute_name)
                    .append(" = ")
                    .append(attribute_name)
                    .append(";\n")
                    .append("\t}\n\n");
        }

        return gettersSetters.toString();
    }

}
