package org.example.server.commands;


import org.example.data.*;
import org.example.exceptions.CollectionIsEmptyException;
import org.example.exceptions.InappropriateFormatException;
import org.example.exceptions.MarineNotFoundException;
import org.example.exceptions.WrongAmountOfElementsException;
import org.example.server.utility.Command;
import org.example.server.utility.CollectionManager;
import org.example.server.utility.ResponseOutputer;
import org.example.utility.MarineRaw;
import org.example.utility.User;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * The command updates the value of the collection element, by the given id
 */
public class UpdateCommand implements Command, Serializable {
    private final CollectionManager collectionManager;
    String name = "update";

    public UpdateCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }


    /**
     * Executes the command.
     */
    public boolean execute(String argument, Object objectArgument, User user) {
        try {
            if (argument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            if (collectionManager.collectionSize() == 0) throw new CollectionIsEmptyException();
            Integer id = Integer.parseInt(argument);
            if (id <= 0) throw new NumberFormatException();
            SpaceMarine oldMarine = collectionManager.getById(id);
            if (oldMarine == null) throw new MarineNotFoundException();
            ResponseOutputer.appendln(argument);
            return true;
        } catch (WrongAmountOfElementsException e) {
            ResponseOutputer.appendln("Команда должна принимать один аргумент");
        } catch (MarineNotFoundException e) {
            ResponseOutputer.appendln("Солдата с таким Id нет");
        } catch (NumberFormatException e) {
            ResponseOutputer.appendln("Неверно введено поле для Id");
        } catch (CollectionIsEmptyException e) {
            ResponseOutputer.appendln("Коллекция пуста");
        }
        return false;
    }
    public String description() {
        return name + " - обновляет значение элемента коллекции, по заданному id";
    }
    public String getName() {return name;}
}
