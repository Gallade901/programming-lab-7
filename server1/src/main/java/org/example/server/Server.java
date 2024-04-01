package org.example.server;



import org.example.exceptions.ClosingSocketException;
import org.example.exceptions.ConnectionErrorException;
import org.example.exceptions.OpeningServerSocketException;
import org.example.server.utility.CommandManager;
import org.example.server.utility.ConnectionHandler;
import org.example.server.utility.RequestHandler;
import org.example.utility.Console;
import org.example.utility.Request;
import org.example.utility.Response;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static org.example.server.utility.CollectionManager.saveCollection;

/**
 * Runs the server.
 */
public class Server {
    private int port;
    private ServerSocket serverSocket;
    private boolean isStopped;
    private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
    private Semaphore semaphore;
    private CommandManager commandManager;

    public Server(int port, int maxClients, CommandManager commandManager) {
        this.port = port;
        this.semaphore = new Semaphore(maxClients);
        this.commandManager = commandManager;
    }

    /**
     * Begins server operation.
     */
    public void run() {
        try {
            openServerSocket();
            while (!isStopped()) {
                try {
                    acquireConnection();
                    if (isStopped()) throw new ConnectionErrorException();
                    Socket clientSocket = connectToClient();
                    fixedThreadPool.submit(new ConnectionHandler(this, clientSocket, commandManager));
                } catch (ConnectionErrorException exception) {
                    if (!isStopped()) {
                        Console.printerror("Произошла ошибка при соединении с клиентом!");
                        App.logger.error("Произошла ошибка при соединении с клиентом!");
                    } else break;
                }
            }
            fixedThreadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            Console.println("Работа сервера завершена.");
        } catch (OpeningServerSocketException exception) {
            Console.printerror("Сервер не может быть запущен!");
            App.logger.error("Сервер не может быть запущен!");
        } catch (InterruptedException e) {
            Console.printerror("Произошла ошибка при завершении работы с уже подключенными клиентами!");
        }
    }

    /**
     * Finishes server operation.
     */
    public synchronized void stop() {
        try {
            App.logger.info("Завершение работы сервера...");
            saveCollection();
            if (serverSocket == null) throw new ClosingSocketException();
            isStopped = true;
            fixedThreadPool.shutdown();
            serverSocket.close();
            Console.println("Работа сервера успешно завершена.");
            App.logger.info("Работа сервера успешно завершена.");
            System.exit(0);
        } catch (ClosingSocketException exception) {
            Console.printerror("Невозможно завершить работу еще не запущенного сервера!");
            App.logger.error("Невозможно завершить работу еще не запущенного сервера!");
        } catch (IOException exception) {
            Console.printerror("Произошла ошибка при завершении работы сервера!");
            App.logger.error("Произошла ошибка при завершении работы сервера!");
        }
    }

    /**
     * Open server socket.
     */
    private void openServerSocket() throws OpeningServerSocketException {
        try {
            App.logger.info("Запуск сервера...");
            serverSocket = new ServerSocket(port);
//            serverSocket.setSoTimeout(60000);
            App.logger.info("Сервер успешно запущен.");
        } catch (IllegalArgumentException exception) {
            Console.printerror("Порт '" + port + "' находится за пределами возможных значений!");
            App.logger.error("Порт '" + port + "' находится за пределами возможных значений!");
            throw new OpeningServerSocketException();
        } catch (IOException exception) {
            Console.printerror("Произошла ошибка при попытке использовать порт '" + port + "'!");
            App.logger.error("Произошла ошибка при попытке использовать порт '" + port + "'!");
            throw new OpeningServerSocketException();
        }
    }

    /**
     * Connecting to client.
     */
    private Socket connectToClient() throws ConnectionErrorException{
        try {
            Console.println("Прослушивание порта '" + port + "'...");
            App.logger.info("Прослушивание порта '" + port + "'...");
            Socket clientSocket = serverSocket.accept();
            App.logger.info("Соединение с клиентом успешно установлено.");
            Console.println("Соединение с клиентом успешно установлено.");
            return clientSocket;
        } catch (IOException exception) {
            Console.printerror("Произошла ошибка при соединении с клиентом!");
            App.logger.error("Произошла ошибка при соединении с клиентом!");
            throw new ConnectionErrorException();
        }
    }

//    /**
//     * The process of receiving a request from a client.
//     */
//    private boolean processClientRequest(Socket clientSocket) {
//        Request userRequest = null;
//        Response responseToUser = null;
//        try (ObjectInputStream clientReader = new ObjectInputStream(clientSocket.getInputStream());
//             ObjectOutputStream clientWriter = new ObjectOutputStream(clientSocket.getOutputStream())) {
//            do {
//                userRequest = (Request) clientReader.readObject();
//                responseToUser = requestHandler.handle(userRequest);
//                App.logger.info("Запрос '" + userRequest.getCommandName() + "' успешно обработан.");
//                clientWriter.writeObject(responseToUser);
//                clientWriter.flush();
//            } while (check());
//            return false;
//        } catch (ClassNotFoundException exception) {
//            Console.printerror("Произошла ошибка при чтении полученных данных!");
//            App.logger.error("Произошла ошибка при чтении полученных данных!");
//        } catch (InvalidClassException | NotSerializableException exception) {
//            Console.printerror("Произошла ошибка при отправке данных на клиент!");
//            App.logger.error("Произошла ошибка при отправке данных на клиент!");
//        } catch (IOException exception) {
//            if (userRequest == null) {
//                Console.printerror("Непредвиденный разрыв соединения с клиентом!");
//                App.logger.warn("Непредвиденный разрыв соединения с клиентом!");
//            } else {
//                Console.println("Клиент успешно отключен от сервера!");
//                App.logger.info("Клиент успешно отключен от сервера!");
//            }
//        }
//        return true;
//    }
    private synchronized boolean isStopped() {
        return isStopped;
    }
    public void acquireConnection() {
        try {
            semaphore.acquire();
            App.logger.info("Разрешение на новое соединение получено.");
        } catch (InterruptedException exception) {
            Console.printerror("Произошла ошибка при получении разрешения на новое соединение!");
            App.logger.error("Произошла ошибка при получении разрешения на новое соединение!");
        }
    }
    public void releaseConnection() {
        semaphore.release();
        App.logger.info("Разрыв соединения зарегистрирован.");
    }
}
