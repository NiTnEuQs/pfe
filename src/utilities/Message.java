package utilities;

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

    public Message(String[][] data) {
        this();
        for (String[] d : data) {
            put(d[0], d[1]);
        }
    }

    // Getters/Setters

    public JSONObject getMessage() {
        return message;
    }

    public void setMessage(JSONObject message) {
        this.message = message;
    }

    // Functions

    public String[][] getData() {
        try {
            return (String[][]) message.get("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getDataAsString() {
        try {
            return (String) message.get("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Object getID() {
        try {
            return message.get("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Object getSource() {
        try {
            return message.get("source");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Object getTimestamp() {
        try {
            return message.get("timestamp");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Object getSession() {
        try {
            return message.get("session");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
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
        String str = "";

        str += ("-*-*-(" + getID() + ")-*-*-");
        str += ("\nSource: " + getSource());
        str += ("\nTimestamp: " + getTimestamp());
        str += ("\nSession: " + getSession());
        str += ("\nData: " + getDataAsString());

        return str;
    }
}
