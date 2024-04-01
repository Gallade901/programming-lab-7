package org.example.server.commands;


import org.example.data.SpaceMarine;
import org.example.exceptions.DatabaseHandlingException;
import org.example.exceptions.InappropriateFormatException;
import org.example.exceptions.WrongAmountOfElementsException;
import org.example.server.utility.Command;
import org.example.server.utility.CollectionManager;
import org.example.server.utility.DatabaseCollectionManager;
import org.example.server.utility.ResponseOutputer;
import org.example.utility.MarineRaw;
import org.example.utility.User;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
/**
 * The team adds a new fighter with atrubos to the collection
 */
public class AddCommand implements Command, Serializable {
    String name = "add";
    private final CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;

    public AddCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        this.collectionManager = collectionManager;
        this.databaseCollectionManager = databaseCollectionManager;
    }

    /**
     * Executes the command.
     */
    public boolean execute(String argument, Object objectArgument, User user) throws NoSuchElementException {
        try {
            if (!argument.isEmpty() || objectArgument == null) throw new WrongAmountOfElementsException();
            MarineRaw marineRaw = (MarineRaw) objectArgument;
            collectionManager.addToCollection(databaseCollectionManager.insertMarine(marineRaw, user));
            ResponseOutputer.appendln("Солдат успешно добавлен!");
            return true;
        } catch (WrongAmountOfElementsException e) {
            ResponseOutputer.appendln("Команда не принимает аргументы");
        } catch (DatabaseHandlingException exception) {
            ResponseOutputer.appendln("Произошла ошибка при обращении к базе данных!");
        }
        return false;
    }
    public String description () {
        return name + " - добавляет новый элемент в коллекцию";
    }
    public String getName () {
        return name;
    }
}

