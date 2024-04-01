package org.example.server.commands;



import org.example.exceptions.WrongAmountOfElementsException;
import org.example.server.utility.CollectionManager;
import org.example.server.utility.Command;
import org.example.server.utility.ResponseOutputer;
import org.example.utility.User;

import java.io.Serializable;

/**
 * The command displays all elements of the collection
 */
public class ShowCommand implements Command, Serializable {
    String name = "show";
    private CollectionManager collectionManager;
    public ShowCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Executes the command.
     */
    public boolean execute(String argument, Object objectArgument, User user) {
        try {
            if (!argument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            ResponseOutputer.appendln(collectionManager.showCollection());
            return true;
        } catch (WrongAmountOfElementsException e) {
            ResponseOutputer.appendln("Команда не принимает аргументы");
        }
        return false;
    }

    public String description() {
        return name + " - выводит все элементы коллекции";
    }
    public String getName() {return name;}
}
