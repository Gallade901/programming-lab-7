package org.example.server.utility;


import org.example.utility.*;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Handles requests.
 */
public class RequestHandler implements Callable<Response> {
    private CommandManager commandManager;
    private Request request;
    private Map<String, Command> commandMap = commandManager.getCommands();
    private ReadWriteLock collectionLocker = new ReentrantReadWriteLock();

    public RequestHandler(Request request, CommandManager commandManager) {
        this.request = request;
        this.commandManager = commandManager;
    }
    /**
     * Handles requests.
     */
    public Response call() {
        User hashedUser = new User(
                request.getUser().getUsername(),
                PasswordHasher.hashPassword(request.getUser().getPassword())
        );
        commandManager.addToHistory(request.getCommandName());
        collectionLocker.writeLock().lock();
        ResponseCode responseCode = executeCommand(request.getCommandName(), request.getCommandStringArgument(),
                request.getCommandObjectArgument(), hashedUser);
        collectionLocker.writeLock().unlock();

        return new Response(responseCode, ResponseOutputer.getAndClear());
    }

    /**
     * Executes a command from a request.
     *
     * @param command               Name of command.
     * @param commandStringArgument String argument for command.
     * @param commandObjectArgument Object argument for command.
     * @return Command execute status.
     */
    private ResponseCode executeCommand(String command, String commandStringArgument,
                                        Object commandObjectArgument, User user) {
        collectionLocker.readLock().lock();
        for (Map.Entry<String, Command> commandElement : commandMap.entrySet()) {
            if (commandElement.getKey().equals(command)) {
                if (!commandElement.getValue().execute(commandStringArgument, commandObjectArgument, user)) {
                    return ResponseCode.ERROR;
                }
                if (command.equals("update")) {
                    return ResponseCode.UPDATE;
                }
            }
        }
        collectionLocker.readLock().unlock();
        return ResponseCode.OK;
    }
}
