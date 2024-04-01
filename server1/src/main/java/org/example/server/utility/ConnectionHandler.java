package org.example.server.utility;


import org.example.server.App;
import org.example.server.Server;
import org.example.utility.Console;
import org.example.utility.Request;
import org.example.utility.Response;
import org.example.utility.ResponseCode;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.*;

import static org.example.server.ScannerMonitor.check;

/**
 * Handles user connection.
 */
public class ConnectionHandler implements Runnable {
    private Server server;
    private Socket clientSocket;
    private CommandManager commandManager;
    private ExecutorService fixedThreadTreatment = Executors.newFixedThreadPool(10);
    private ExecutorService fixedThreadSending = Executors.newFixedThreadPool(10);


    public ConnectionHandler(Server server, Socket clientSocket, CommandManager commandManager) {
        this.server = server;
        this.clientSocket = clientSocket;
        this.commandManager = commandManager;
    }

    /**
     * Main handling cycle.
     */
    @Override
    public void run() {
        Request userRequest = null;
        Response responseToUser = null;
        boolean stopFlag = false;
        try (ObjectInputStream clientReader = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream clientWriter = new ObjectOutputStream(clientSocket.getOutputStream())) {
            do {
                userRequest = (Request) clientReader.readObject();
                responseToUser = fixedThreadTreatment.submit(new RequestHandler(userRequest, commandManager)).get();
                App.logger.info("Запрос '" + userRequest.getCommandName() + "' обработан.");
                Response finalResponseToUser = responseToUser;
                if (!fixedThreadSending.submit(() -> {
                    try {
                        clientWriter.writeObject(finalResponseToUser);
                        clientWriter.flush();
                        return true;
                    } catch (IOException exception) {
                        Console.printerror("Произошла ошибка при отправке данных на клиент!");
                        App.logger.error("Произошла ошибка при отправке данных на клиент!");
                    }
                    return false;
                }).get()) break;
            } while (responseToUser.getResponseCode() != ResponseCode.SERVER_EXIT &&
                    responseToUser.getResponseCode() != ResponseCode.CLIENT_EXIT && check());
            if (responseToUser.getResponseCode() == ResponseCode.SERVER_EXIT)
                stopFlag = true;
        } catch (ClassNotFoundException exception) {
            Console.printerror("Произошла ошибка при чтении полученных данных!");
            App.logger.error("Произошла ошибка при чтении полученных данных!");
        } catch (CancellationException | ExecutionException | InterruptedException exception) {
            Console.println("При обработке запроса произошла ошибка многопоточности!");
            App.logger.warn("При обработке запроса произошла ошибка многопоточности!");
        } catch (IOException exception) {
            Console.printerror("Непредвиденный разрыв соединения с клиентом!");
            App.logger.warn("Непредвиденный разрыв соединения с клиентом!");
        } finally {
            try {
                fixedThreadTreatment.shutdown();
                clientSocket.close();
                Console.println("Клиент отключен от сервера.");
                App.logger.info("Клиент отключен от сервера.");
            } catch (IOException exception) {
                Console.printerror("Произошла ошибка при попытке завершить соединение с клиентом!");
                App.logger.error("Произошла ошибка при попытке завершить соединение с клиентом!");
            }
            if (stopFlag) server.stop();
            server.releaseConnection();
        }
    }
}
