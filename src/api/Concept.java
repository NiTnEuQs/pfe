package api;

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

}
