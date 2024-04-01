package org.example.server.commands;

import org.example.data.Chapter;
import org.example.exceptions.CollectionIsEmptyException;
import org.example.exceptions.WrongAmountOfElementsException;
import org.example.server.utility.Command;
import org.example.server.utility.CollectionManager;
import org.example.server.utility.ResponseOutputer;
import org.example.utility.MarineAsker;
import org.example.utility.User;

import java.io.Serializable;

/**
 * The command outputs elements whose chapter field value is equal to the specified
 */
public class FilterByChapter implements Command, Serializable {
    String name = "filter_by_chapter";
    private final CollectionManager collectionManager;
    private final MarineAsker marineAsker;
    public FilterByChapter (CollectionManager collectionManager, MarineAsker marineAsker) {
        this.collectionManager = collectionManager;
        this.marineAsker = marineAsker;
    }

    /**
     * Executes the command.
     */
    public boolean execute(String argument, Object objectArgument, User user) {
        try {
            if (!argument.isEmpty() || objectArgument == null) throw new WrongAmountOfElementsException();
            if (collectionManager.collectionSize() == 0) throw new CollectionIsEmptyException();
            collectionManager.filterChapter((Chapter) objectArgument);
            return true;
        } catch (WrongAmountOfElementsException e) {
            ResponseOutputer.appendln("Команда не принимает аргументы");
        } catch (CollectionIsEmptyException e) {
            ResponseOutputer.appendln("Коллекция пустая");
        }
        return false;
    }


    public String description() {
        return name + " - выводит элементы, значение поля chapter которые равны заданному";
    }
    public String getName() {return name;}
}
