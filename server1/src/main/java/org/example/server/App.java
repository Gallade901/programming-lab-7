package org.example.server;


import org.example.data.AstartesCategory;
import org.example.exceptions.NotInDeclaredLimitsException;
import org.example.exceptions.WrongAmountOfElementsException;
import org.example.server.utility.*;
import org.example.utility.Console;
import org.example.utility.MarineAsker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBException;
import java.util.Scanner;
import java.util.logging.LogManager;


/**
 * Main server class. Creates all server instances.
 *
 * @author Sviridov Dmitry and Orlov Egor.
 */
public class App {
    private static final int MAX_CLIENTS = 1000;
    public static int port;
    public static String file;
    public static final int CONNECTION_TIMEOUT = 60 * 1000;
    public static final Logger logger = LoggerFactory.getLogger(App.class);
    private static String databaseUsername = "s367617";
    private static String databaseHost;
    private static String databasePassword;
    private static String databaseAddress;


    private static boolean initializeConnectionAddress(String[] args) {
        try {
            if (args.length != 3) throw new WrongAmountOfElementsException();
            port = Integer.parseInt(args[0]);
            if (port < 0) throw new NotInDeclaredLimitsException();
            databaseHost = args[1];
            databasePassword = args[2];
            databaseAddress = "jdbc:postgresql://" + databaseHost + ":5432/studs";
            return true;
        } catch (WrongAmountOfElementsException exception) {
            Console.println("Должно быть передано три аргумента");
        } catch (NumberFormatException exception) {
            Console.printerror("Порт должен быть представлен числом!");
        } catch (NotInDeclaredLimitsException exception) {
            Console.printerror("Порт не может быть отрицательным!");
        }
        return false;
    }

    public static void main(String[] args) {

        if (!initializeConnectionAddress(args)) return;
        DatabaseHandler databaseHandler = new DatabaseHandler(databaseAddress, databaseUsername, databasePassword);
        DatabaseUserManager databaseUserManager = new DatabaseUserManager(databaseHandler);
        DatabaseCollectionManager databaseCollectionManager = new DatabaseCollectionManager(databaseHandler, databaseUserManager);
        Scanner scanner = new Scanner(System.in);
        MarineAsker marineAsker = new MarineAsker(scanner);
        CollectionManager collectionManager = new CollectionManager(marineAsker, databaseCollectionManager);
        CommandManager commandManager = new CommandManager(collectionManager, marineAsker, databaseCollectionManager, databaseUserManager);
        commandManager.putCommand();
        Server server = new Server(port, MAX_CLIENTS, commandManager);
        server.run();

    }

}
