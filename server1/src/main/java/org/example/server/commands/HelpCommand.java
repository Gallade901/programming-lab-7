package org.example.server.commands;




import org.example.exceptions.WrongAmountOfElementsException;
import org.example.server.utility.CommandManager;
import org.example.server.utility.ResponseOutputer;

import org.example.server.utility.Command;
import org.example.utility.User;

import java.io.Serializable;
import java.util.Map;

/**
 * Command displays help on available commands
 */
public class HelpCommand  implements Command, Serializable {
    String name = "help";
    Map<String, Command> commands = CommandManager.getCommands();

    /**
     * Executes the command.
     */
    public boolean execute(String argument, Object objectArgument, User user) {
        try {
            if (!argument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            for (Command command : commands.values()) {
                ResponseOutputer.appendln(command.description());
            }
            return true;
        } catch (WrongAmountOfElementsException e) {
            ResponseOutputer.appendln("Команда " + name + " не принимает аргументы");
        }
        return false;
    }
    public String description() {
        return name + " - выводит справку по доступным командам";
    }
    public String getName() {return name;}
}

