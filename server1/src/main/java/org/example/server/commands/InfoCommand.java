package org.example.server.commands;

import org.example.exceptions.WrongAmountOfElementsException;
import org.example.server.utility.Command;
import org.example.server.utility.CollectionManager;
import org.example.server.utility.ResponseOutputer;
import org.example.utility.User;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * The command displays information about the collection
 */
public class InfoCommand implements Command, Serializable {
    private final CollectionManager collectionManager;
    String name = "info";
    public InfoCommand (CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Executes the command.
     */
    public boolean execute(String argument, Object objectArgument, User user) {
        try {
            if (!argument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            LocalDateTime lastInitTime = collectionManager.getLastInitTime();
            String lastInitTimeString = (lastInitTime == null) ? "в данной сессии инициализации еще не происходило" :
                    lastInitTime.toLocalDate().toString() + " " + lastInitTime.toLocalTime().toString();

            LocalDateTime lastSaveTime = collectionManager.getLastSaveTime();
            String lastSaveTimeString = (lastSaveTime == null) ? "в данной сессии сохранения еще не происходило" :
                    lastSaveTime.toLocalDate().toString() + " " + lastSaveTime.toLocalTime().toString();
            ResponseOutputer.appendln("Сведения о коллекции:");
            ResponseOutputer.appendln(" Тип: " + collectionManager.collectionType());
            ResponseOutputer.appendln(" Количество элементов: " + collectionManager.collectionSize());
            ResponseOutputer.appendln(" Дата последнего сохранения: " + lastSaveTimeString);
            ResponseOutputer.appendln(" Дата последней инициализации: " + lastInitTimeString);
            return true;
        } catch (WrongAmountOfElementsException e) {
            ResponseOutputer.appendln("Команда не принимает аргументы");

        }
        return false;
    }

    public String description() {
        return name + " - выводит информацию о коллекции";
    }
    public String getName() {return name;}
}
