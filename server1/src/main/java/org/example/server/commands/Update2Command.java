package org.example.server.commands;

import org.example.data.*;
import org.example.exceptions.*;
import org.example.server.utility.CollectionManager;
import org.example.server.utility.Command;
import org.example.server.utility.DatabaseCollectionManager;
import org.example.server.utility.ResponseOutputer;
import org.example.utility.Console;
import org.example.utility.MarineRaw;
import org.example.utility.User;


import javax.xml.crypto.Data;
import java.io.Serializable;
import java.time.LocalDateTime;


public class Update2Command implements Command, Serializable {
    private CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;
    String name = "update2";

    public Update2Command(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        this.collectionManager = collectionManager;
        this.databaseCollectionManager = databaseCollectionManager;
    }



    /**
     * Executes the command.
     */
    public boolean execute(String argument, Object objectArgument, User user) {
        try {
            if (argument.isEmpty() || objectArgument == null) throw new WrongAmountOfElementsException();
            if (collectionManager.collectionSize() == 0) throw new CollectionIsEmptyException();
            Integer id = Integer.parseInt(argument);
            if (id <= 0) throw new NumberFormatException();
            SpaceMarine oldMarine = collectionManager.getById(id);
            if (oldMarine == null) throw new MarineNotFoundException();
            if (!oldMarine.getOwner().equals(user)) throw new PermissionDeniedException();
            if (!databaseCollectionManager.checkMarineUserId(oldMarine.getId(), user)) throw new ManualDatabaseEditException();
            MarineRaw marineRaw = (MarineRaw) objectArgument;

            databaseCollectionManager.updateMarineById(id, marineRaw);

            String name = marineRaw.getName() == null ? oldMarine.getName() : marineRaw.getName();
            Coordinates coordinates = marineRaw.getCoordinates() == null ? oldMarine.getCoordinates() : marineRaw.getCoordinates();
            LocalDateTime creationDate = oldMarine.getCreationDate();
            long health = marineRaw.getHealth() == -1 ? oldMarine.getHealth() : marineRaw.getHealth();
            AstartesCategory category = marineRaw.getCategory() == null ? oldMarine.getCategory() : marineRaw.getCategory();
            long heartCount = marineRaw.getHeartCount() == -1 ? oldMarine.getHeartCount() : marineRaw.getHeartCount();
            MeleeWeapon meleeWeapon = marineRaw.getMeleeWeapon() == null ? oldMarine.getMeleeWeapon() : marineRaw.getMeleeWeapon();
            Chapter chapter = marineRaw.getChapter() == null ? oldMarine.getChapter() : marineRaw.getChapter();

            collectionManager.removeFromCollection(oldMarine);
            collectionManager.addToCollection(new SpaceMarine(
                    id,
                    name,
                    coordinates,
                    creationDate,
                    health,
                    heartCount,
                    category,
                    meleeWeapon,
                    chapter,
                    user
            ));
            ResponseOutputer.appendln("Солдат успешно изменен!");
            return true;
        } catch (WrongAmountOfElementsException e) {
            ResponseOutputer.appendln("Команда должна принимать один аргумент");
        } catch (MarineNotFoundException e) {
            ResponseOutputer.appendln("Солдата с таким Id нет");
        } catch (NumberFormatException e) {
            ResponseOutputer.appendln("Неверно введено поле для Id");
        } catch (CollectionIsEmptyException e) {
            ResponseOutputer.appendln("Коллекция пуста");
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
        return name + " - обновляет значение элемента коллекции, по заданному id";
    }
    public String getName() {return name;}
}
