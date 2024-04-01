package org.example.server.commands;


import org.example.data.SpaceMarine;
import org.example.exceptions.*;
import org.example.server.utility.Command;
import org.example.server.utility.CollectionManager;
import org.example.server.utility.DatabaseCollectionManager;
import org.example.server.utility.ResponseOutputer;
import org.example.utility.User;

import java.io.Serializable;
import java.util.Scanner;

/**
 * The command removes an element from the collection by its id
 */
public class RemoveByIdCommand implements Command, Serializable {
    private CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;
    String name = "remove_by_id";

    public RemoveByIdCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        this.collectionManager = collectionManager;
        this.databaseCollectionManager = databaseCollectionManager;
    }

    /**
     * Executes the command.
     */
    public boolean execute(String argument, Object objectArgument, User user) {
        try {
            if (argument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            Integer id = Integer.parseInt(argument);
            SpaceMarine marineToRemove = collectionManager.getById(id);
            if (marineToRemove == null) throw new MarineNotFoundException();
            if (marineToRemove == null) throw new MarineNotFoundException();
            if (!marineToRemove.getOwner().equals(user)) throw new PermissionDeniedException();
            if (!databaseCollectionManager.checkMarineUserId(marineToRemove.getId(), user)) throw new ManualDatabaseEditException();
            databaseCollectionManager.deleteMarineById(id);
            collectionManager.removeFromCollection(marineToRemove);
            ResponseOutputer.appendln("Солдат успешно удален!");
            return true;
        } catch (WrongAmountOfElementsException e) {
            ResponseOutputer.appendln("Команда должна принимать один аргумент");
        } catch (MarineNotFoundException e) {
            ResponseOutputer.appendln("Солдата с таким Id нет");
        } catch (NumberFormatException e) {
            ResponseOutputer.appendln("Неверно введено поле для Id");
        } catch (PermissionDeniedException exception) {
            ResponseOutputer.appendln("Недостаточно прав для выполнения данной команды!");
            ResponseOutputer.appendln("Принадлежащие другим пользователям объекты доступны только для чтения.");
        } catch (ManualDatabaseEditException exception) {
            ResponseOutputer.appendln("Произошло прямое изменение базы данных!");
            ResponseOutputer.appendln("Перезапустите клиент для избежания возможных ошибок.");
        } catch (DatabaseHandlingException exception) {
            ResponseOutputer.appendln("Произошла ошибка при обращении к базе данных!");
        }
        return false;
    }

    public String description() {
        return name + " - удаляет элемент из коллекции по его id";
    }
    public String getName() {return name;}
}

