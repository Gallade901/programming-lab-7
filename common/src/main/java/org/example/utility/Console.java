package org.example.utility;

import java.util.Scanner;

/**
 * Controls the output of messages.
 */
public class Console {
    private MarineAsker marineAscer;
    private Scanner userScanner;

    public Console(MarineAsker marineAsker, Scanner userScanner) {
        this.marineAscer = marineAsker;
        this.userScanner = userScanner;
    }

    /**
     * Prints a message with a line break
     * @param toOut
     */
    public static void println(Object toOut) {
        System.out.println(toOut);
    }

    /**
     * prints a message
     * @param toOut
     */
    public static void print(Object toOut) {
        System.out.print(toOut);
    }

    /**
     * displays an error message
     * @param toOut
     */
    public static void printerror(Object toOut) {
        System.out.println("error: " + toOut);
    }


}
