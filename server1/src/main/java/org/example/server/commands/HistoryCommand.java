package org.example.server.commands;

import org.example.exceptions.HistoryIsEmptyException;
import org.example.exceptions.WrongAmountOfElementsException;
import org.example.server.utility.Command;
import org.example.server.utility.ResponseOutputer;
import org.example.utility.User;

import java.io.Serializable;

import static org.example.server.utility.CommandManager.commandHistory;


/**
 * Command prints the last 15 commands
 */
public class HistoryCommand implements Command, Serializable {
    String name = "history";

    /**
     * Executes the command.
     */
    public boolean execute(String argument, Object objectArgument, User user) {
        try {
            if (!argument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            if (commandHistory.size() == 0) throw new HistoryIsEmptyException();
            ResponseOutputer.appendln("Последние использованные команды:");
            for (String command : commandHistory) {
                if (command != null) ResponseOutputer.appendln(" " + command);
            }
            return true;
        } catch (WrongAmountOfElementsException e) {
            ResponseOutputer.appendln("Команда не принимает аргументы");
        } catch (HistoryIsEmptyException exception) {
        ResponseOutputer.appendln("Ни одной команды еще не было использовано!");
    }
        return false;
    }


    public String description() {
        return name + " - выводит последние 15 команд";
    }
    public String getName() {return name;}
}
