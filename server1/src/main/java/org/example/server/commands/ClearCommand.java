package org.example.server.commands;


import org.example.data.SpaceMarine;
import org.example.exceptions.DatabaseHandlingException;
import org.example.exceptions.ManualDatabaseEditException;
import org.example.exceptions.PermissionDeniedException;
import org.example.exceptions.WrongAmountOfElementsException;
import org.example.server.utility.Command;
import org.example.server.utility.CollectionManager;
import org.example.server.utility.DatabaseCollectionManager;
import org.example.server.utility.ResponseOutputer;
import org.example.utility.User;

import java.io.Serializable;

/**
 * The command clears the collection
 */

public class ClearCommand implements Command, Serializable {
    String name = "clear";
    private final CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;
    public ClearCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        this.collectionManager = collectionManager;
        this.databaseCollectionManager = databaseCollectionManager;
    }

    /**
     * Executes the command.
     */
    public boolean execute(String argument, Object objectArgument, User user) {
        try {
            if (!argument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            for (SpaceMarine marine : collectionManager.getCollection()) {
                if (!marine.getOwner().equals(user)) throw new PermissionDeniedException();
                if (!databaseCollectionManager.checkMarineUserId(marine.getId(), user)) throw new ManualDatabaseEditException();
            }
            databaseCollectionManager.clearCollection();
            collectionManager.clearCollection();
            ResponseOutputer.appendln("Коллекция очищена!");
            return true;
        } catch (WrongAmountOfElementsException e) {
            ResponseOutputer.appendln("Команда не принимает аргументы");
        } catch (DatabaseHandlingException exception) {
            ResponseOutputer.appendln("Произошла ошибка при обращении к базе данных!");
        } catch (PermissionDeniedException exception) {
            ResponseOutputer.appendln("Недостаточно прав для выполнения данной команды!");
            ResponseOutputer.appendln("Принадлежащие другим пользователям объекты доступны только для чтения.");
        } catch (ManualDatabaseEditException exception) {
            ResponseOutputer.appendln("Произошло прямое изменение базы данных!");
            ResponseOutputer.appendln("Перезапустите клиент для избежания возможных ошибок.");
        }
        return false;
    }

    public String description() {
        return name + " - очищает коллекцию";
    }
    public String getName() {return name;}
}
