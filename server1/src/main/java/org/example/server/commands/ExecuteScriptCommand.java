package org.example.server.commands;

import org.example.exceptions.WrongAmountOfElementsException;
import org.example.server.utility.Command;
import org.example.server.utility.ResponseOutputer;
import org.example.utility.User;


import java.io.Serializable;

/**
 * The command executes a script from a file
 */
public class ExecuteScriptCommand implements Command, Serializable {
    String name = "execute_script";


    /**
     * Executes the command.
     */
    public boolean execute(String argument, Object objectArgument, User user) {
        try {
            if (argument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            return true;
        } catch (WrongAmountOfElementsException e) {
            ResponseOutputer.appendln("Команда должна принимать аргумент");
        }
        return false;
    }

    public String description() {
        return name + " - исполняет скрипт из файла";
    }
    public String getName() {return name;}
}
