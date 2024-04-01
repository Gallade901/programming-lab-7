package org.example.first;

import org.example.exceptions.NotInDeclaredLimitsException;
import org.example.exceptions.WrongAmountOfElementsException;
import org.example.utility.Console;

import java.util.Scanner;

/**
 * Main client class. Creates all client instances.
 *
 * @author Sviridov Dmitry and Orlov Egor.
 */
public class App {

    private static final int RECONNECTION_TIMEOUT = 5 * 1000;
    private static final int MAX_RECONNECTION_ATTEMPTS = 5;

    private static String host;
    private static int port;

    private static boolean initializeConnectionAddress(String[] hostAndPortArgs) {
        try {
            if (hostAndPortArgs.length != 2) throw new WrongAmountOfElementsException();
            host = hostAndPortArgs[0];
            port = Integer.parseInt(hostAndPortArgs[1]);
            if (port < 0) throw new NotInDeclaredLimitsException();
            return true;
        } catch (WrongAmountOfElementsException exception) {
            Console.println("Должно быть передано два аргумента");
        } catch (NumberFormatException exception) {
            Console.printerror("Порт должен быть представлен числом!");
        } catch (NotInDeclaredLimitsException exception) {
            Console.printerror("Порт не может быть отрицательным!");
        }
        return false;
    }

    public static void main(String[] args) {
        if (!initializeConnectionAddress(args)) return;
        Scanner userScanner = new Scanner(System.in);
        UserHandler userHandler = new UserHandler(userScanner);
        AuthHandler authHandler = new AuthHandler(userScanner);
        Client client = new Client(host, port, RECONNECTION_TIMEOUT, MAX_RECONNECTION_ATTEMPTS, userHandler, authHandler);
        client.run();
        userScanner.close();
    }
}
