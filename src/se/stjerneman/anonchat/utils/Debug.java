package se.stjerneman.anonchat.utils;

/**
 * Handles debug output in the console.
 * 
 * @author Emil Stjerneman
 * 
 */
public class Debug {

    public static boolean debug;

    public static void setDebug (boolean debug) {
        Debug.debug = debug;
    }

    public static void debug (String text) {
        if (Debug.debug) {
            System.out.println(text);
        }
    }
}
