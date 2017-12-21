package message;

public enum MessageType {

    CREATE_FILE("Créer un fichier"),
    GO_TO_FILE("Aller au fichier ..."),
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
