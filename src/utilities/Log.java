package utilities;

public class Log {

    /**
     * The parameter to enable or not the log informations
     * (default: true)
     */
    public static boolean enabled = true;

    /**
     * VERBOSE state of log
     *
     * @param text
     */
    public static void v(Object text) {
        System.out.println(text.toString());
    }

    /**
     * INFO state of log
     *
     * @param text
     */
    public static void i(Object text) {
        System.out.println("INFO: " + text.toString());
    }

    /**
     * ERROR state of log
     *
     * @param text
     */
    public static void e(Object text) {
        System.out.println("ERROR: " + text.toString());
    }

    /**
     * WARNING state of log
     *
     * @param text
     */
    public static void w(Object text) {
        System.out.println("WARNING: " + text.toString());
    }

    /**
     * Enable the log informations
     */
    public static void enable() {
        enabled = true;
    }

    /**
     * Disable the log informations
     */
    public static void disable() {
        enabled = false;
    }

    /**
     * Toggle the log informations
     */
    public static void toggle() {
        enabled = !enabled;
    }

}
