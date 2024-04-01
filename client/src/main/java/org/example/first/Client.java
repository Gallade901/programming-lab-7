package org.example.first;

import org.example.utility.*;
import org.example.exceptions.ConnectionErrorException;
import org.example.exceptions.NotInDeclaredLimitsException;
import org.example.utility.Console;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class Client {
    private String host;
    private int port;
    private int reconnectionTimeout;
    private int reconnectionAttempts;
    private int maxReconnectionAttempts;
    private UserHandler userHandler;
    private SocketChannel socketChannel;
    private ObjectOutputStream serverWriter;
    private ObjectInputStream serverReader;
    boolean flag = false;
    Request requestToServerFlag = null;
    Response serverResponseFlag = null;
    private AuthHandler authHandler;
    private User user;

    public Client(String host, int port, int reconnectionTimeout, int maxReconnectionAttempts, UserHandler userHandler, AuthHandler authHandler) {
        this.host = host;
        this.port = port;
        this.reconnectionTimeout = reconnectionTimeout;
        this.maxReconnectionAttempts = maxReconnectionAttempts;
        this.userHandler = userHandler;
        this.authHandler = authHandler;
    }

    /**
     * Begins client operation.
     */
    public void run() {
        try {
            while (true) {
                try {
                    connectToServer();
                    processAuthentication();
                    processRequestToServer();
                    break;
                } catch (ConnectionErrorException exception) {
                    if (reconnectionAttempts >= maxReconnectionAttempts) {
                        Console.printerror("Превышено количество попыток подключения!");
                        break;
                    }
                    try {
                        Thread.sleep(reconnectionTimeout);
                    } catch (IllegalArgumentException timeoutException) {
                        Console.printerror("Время ожидания подключения '" + reconnectionTimeout +
                                "' находится за пределами возможных значений!");
                        Console.println("Повторное подключение будет произведено немедленно.");
                    } catch (Exception timeoutException) {
                        Console.printerror("Произошла ошибка при попытке ожидания подключения!");
                        Console.println("Повторное подключение будет произведено немедленно.");
                    }
                }
                reconnectionAttempts++;
            }
            if (socketChannel != null) socketChannel.close();
            Console.println("Работа клиента успешно завершена.");
        } catch (NotInDeclaredLimitsException exception) {
            Console.printerror("Клиент не может быть запущен!");
        } catch (IOException exception) {
            Console.printerror("Произошла ошибка при попытке завершить соединение с сервером!");
        }
    }

    /**
     * Connecting to server.
     */
    private void connectToServer() throws ConnectionErrorException, NotInDeclaredLimitsException {
        try {
            if (reconnectionAttempts >= 1) Console.println("Повторное соединение с сервером...");
            socketChannel = SocketChannel.open(new InetSocketAddress(host, port));
            Console.println("Соединение с сервером успешно установлено.");
            Console.println("Ожидание разрешения на обмен данными...");
            serverWriter = new ObjectOutputStream(socketChannel.socket().getOutputStream());
            serverReader = new ObjectInputStream(socketChannel.socket().getInputStream());
            Console.println("Разрешение на обмен данными получено.");
        } catch (IllegalArgumentException exception) {
            Console.printerror("Адрес сервера введен некорректно!");
            throw new NotInDeclaredLimitsException();
        } catch (IOException exception) {
            Console.printerror("Произошла ошибка при соединении с сервером!");
            throw new ConnectionErrorException();
        }
    }

    /**
     * Server request process.
     */
    private void processRequestToServer() throws IOException {
        Request requestToServer = null;
        Response serverResponse = null;
        do {
            try {
                if (flag) {
                    serverWriter.writeObject(requestToServerFlag);
                    serverResponseFlag = (Response) serverReader.readObject();
                    if (!(serverResponseFlag.getResponseCode() == ResponseCode.UPDATE)) {
                        Console.print(serverResponseFlag.getResponseBody());
                    }
                    flag = false;
                }
                requestToServer = serverResponse != null ? userHandler.handle(serverResponse.getResponseCode(), serverResponse.getResponseBody().trim(), user) :
                        userHandler.handle(null, "", user);
                if (requestToServer.isEmpty()) continue;
                serverWriter.writeObject(requestToServer);
                serverResponse = (Response) serverReader.readObject();
                if (!(serverResponse.getResponseCode() == ResponseCode.UPDATE)) {
                    Console.print(serverResponse.getResponseBody());
                }
            } catch (InvalidClassException | NotSerializableException exception) {
                Console.printerror("Произошла ошибка при отправке данных на сервер!");
            } catch (ClassNotFoundException exception) {
                Console.printerror("Произошла ошибка при чтении полученных данных!");
            } catch (IOException exception) {
                Console.printerror("Соединение с сервером разорвано!");
                flag = true;
                serverResponseFlag = serverResponse;
                requestToServerFlag = requestToServer;
                try {
                    reconnectionAttempts++;
                    connectToServer();
                } catch (ConnectionErrorException | NotInDeclaredLimitsException reconnectionException) {
                    if (requestToServer.getCommandName().equals("exit"))
                        Console.println("Команда не будет зарегистрирована на сервере.");
                    else Console.println("Попробуйте повторить команду позднее.");
                }
            }
        } while (!requestToServer.getCommandName().equals("exit"));

    }
    private void processAuthentication() {
        Request requestToServer = null;
        Response serverResponse = null;
        do {
            try {
                requestToServer = authHandler.handle();
                if (requestToServer.isEmpty()) continue;
                serverWriter.writeObject(requestToServer);
                serverResponse = (Response) serverReader.readObject();
                Console.print(serverResponse.getResponseBody());
            } catch (InvalidClassException | NotSerializableException exception) {
                Console.printerror("Произошла ошибка при отправке данных на сервер!");
            } catch (ClassNotFoundException exception) {
                Console.printerror("Произошла ошибка при чтении полученных данных!");
            } catch (IOException exception) {
                Console.printerror("Соединение с сервером разорвано!");
                try {
                    connectToServer();
                } catch (ConnectionErrorException | NotInDeclaredLimitsException reconnectionException) {
                    Console.println("Попробуйте повторить авторизацию позднее.");
                }
            }
        } while (serverResponse == null || !serverResponse.getResponseCode().equals(ResponseCode.OK));
        user = requestToServer.getUser();
    }

}
