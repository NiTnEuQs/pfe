package api;

public enum MessageType {

    CREATE_PACKAGES("Créer des packages"),
    CREATE_FILE("Créer un fichier"),
    CREATE_CONCEPT("Créer un concept"),
    OPEN_FILE("Ouvrir le fichier ..."),
    GO_TO_LINE("Aller à la ligne ...");

    final String text;

    MessageType(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return this.text;
    }

}
