package message;

import org.json.JSONException;
import org.json.JSONObject;

public class Message {

    // Attributes

    private JSONObject message;

    // Constructors

    public Message() {
        this.message = new JSONObject();
    }

    public Message(JSONObject message) {
        this.message = message;
    }

    public Message(Message message) {
        this.message = message.getMessage();
    }

    public Message(String[][] data) {
        this();
        for (String[] d : data) {
            put(d[0], d[1]);
        }
    }

    public Message(JSONObject message, String key) {
        this.message = getJSON(key);
    }

    // Getters/Setters

    public JSONObject getMessage() {
        return message;
    }

    public void setMessage(JSONObject message) {
        this.message = message;
    }

    public MessageType getType() {
        return MessageType.valueOf(getAsString("type").toUpperCase());
    }

    // Functions

    public Object get(String key) {
        try {
            return message.get(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getAsString(String key) {
        return get(key).toString();
    }

    public JSONObject getJSON(String key) {
        try {
            return message.getJSONObject(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Message getJSONToMessage(String key) {
        return new Message(getJSON(key));
    }

    public Object getData() {
        return get("data");
    }

    public String getDataAsString() {
        return getData().toString();
    }

    public Object getID() {
        return get("id");
    }

    public String getIDAsString() {
        return getID().toString();
    }

    public Object getSource() {
        return get("source");
    }

    public String getSourceAsString() {
        return getSource().toString();
    }

    public Object getTimestamp() {
        return get("timestamp");
    }

    public String getTimestampAsString() {
        return getTimestamp().toString();
    }

    public Object getSession() {
        return get("session");
    }

    public String getSessionAsString() {
        return getSession().toString();
    }

    public void put(String key, String value) {
        try {
            message.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return message.toString();
        /*
        String str = "";

        str += ("-*-*-(" + getID() + ")-*-*-");
        str += ("\nSource: " + getSource());
        str += ("\nTimestamp: " + getTimestamp());
        str += ("\nSession: " + getSession());
        str += ("\nData: " + getDataAsString());

        return str;
        */
    }
}
