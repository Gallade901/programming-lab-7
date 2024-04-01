package org.example.server.utility;



import org.example.server.commands.*;
import org.example.utility.MarineAsker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Operates the commands.
 */
public class CommandManager {
    private final int COMMAND_HISTORY_SIZE = 15;
    private final int firstIndex = 0;
    private CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;
    private MarineAsker marineAsker;
    private DatabaseUserManager databaseUserManager;
    static Map<String, Command> commands = new HashMap<>();
    public static List<String> commandHistory = new ArrayList<String>();
    public CommandManager(CollectionManager collectionManager, MarineAsker marineAsker, DatabaseCollectionManager databaseCollectionManager, DatabaseUserManager databaseUserManager){
        this.marineAsker = marineAsker;
        this.collectionManager = collectionManager;
        this.databaseCollectionManager = databaseCollectionManager;
        this.databaseUserManager = databaseUserManager;
    }

    public void putCommand() {
        Command[] commandsList = {
                new AddCommand(collectionManager, databaseCollectionManager),
                new HelpCommand(),
                new ShowCommand(collectionManager),
                new UpdateCommand(collectionManager),
                new ClearCommand(collectionManager, databaseCollectionManager),
                new RemoveByIdCommand(collectionManager, databaseCollectionManager),
                new ExitCommand(collectionManager),
                new InfoCommand(collectionManager),
                new HistoryCommand(),
                new AddIfMaxCommand(collectionManager, databaseCollectionManager),
                new AddIfMinCommand(collectionManager, databaseCollectionManager),
                new CountGreaterThanMeleeWeaponCommand(collectionManager),
                new FilterByChapter(collectionManager, marineAsker),
                new PrintFieldDescendingMeleeWeaponCommand(collectionManager),
                new Update2Command(collectionManager, databaseCollectionManager),
                new LoginCommand(databaseUserManager),
                new RegisterCommand(databaseUserManager),
                new ExecuteScriptCommand()

        };

        for (Command command : commandsList) {
            commands.put(command.getName(), command);
        }
    }


    /**
     * @return List of manager's commands.
     */
    public static Map<String, Command> getCommands() {
        return commands;
    }

    /**
     * Adds command to command history.
     *
     * @param commandToStore Command to add.
     */
    public void addToHistory(String commandToStore) {

        for (Map.Entry<String, Command> command : commands.entrySet()) {
            if (command.getKey().equals(commandToStore)) {
                if (commandHistory.size() < COMMAND_HISTORY_SIZE) {
                    commandHistory.add(commandToStore);
                } else {
                    commandHistory.remove(firstIndex);
                    commandHistory.add(commandToStore);
                }
            }
        }
    }
}
