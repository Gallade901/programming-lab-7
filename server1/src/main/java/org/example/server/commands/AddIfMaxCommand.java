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

/**
 * The command adds a new element to the collection
 * if its value is greater than the value of the largest element in this collection
 */
public class AddIfMaxCommand implements Command, Serializable {
    String name = "add_if_max";
    private final CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;
    public AddIfMaxCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        this.collectionManager = collectionManager;
        this.databaseCollectionManager = databaseCollectionManager;

    }

    /**
     * Executes the command.
     */
    public boolean execute(String argument, Object objectArgument, User user) {
        try {
            if (!argument.isEmpty() || objectArgument == null) throw new WrongAmountOfElementsException();
            MarineRaw marineRaw = (MarineRaw) objectArgument;
            SpaceMarine marineToAdd = databaseCollectionManager.insertMarine(marineRaw, user);
            if (collectionManager.collectionSize() == 0 || marineToAdd.compareTo(collectionManager.getLast()) > 0) {
                if (CollectionManager.addToCollection(marineToAdd)) {
                    ResponseOutputer.appendln("Боец успешно добавлен");
                    return true;
                } else {
                    ResponseOutputer.appendln("Не удалось добавить бойца");
                }
            } else ResponseOutputer.appendln("Значение солдата меньше, чем значение наибольшего из солдат!");
        } catch (WrongAmountOfElementsException e) {
            ResponseOutputer.appendln("Команда не принимает аргументы");
        } catch (DatabaseHandlingException exception) {
            ResponseOutputer.appendln("Произошла ошибка при обращении к базе данных!");
        }
        return false;
    }


    public String description() {
        return name + " - добавляет новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции";
    }
    public String getName() {return name;}
}
