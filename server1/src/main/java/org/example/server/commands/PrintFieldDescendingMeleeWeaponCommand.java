package org.example.server.commands;

import org.example.exceptions.CollectionIsEmptyException;
import org.example.exceptions.WrongAmountOfElementsException;
import org.example.server.utility.Command;
import org.example.server.utility.CollectionManager;
import org.example.server.utility.ResponseOutputer;
import org.example.utility.User;

import java.io.Serializable;

/**
 * The command displays the values of the meleeWeapon field of all elements in descending order
 */
public class PrintFieldDescendingMeleeWeaponCommand implements Command, Serializable {
    String name = "print_field_descending_melee_weapon";
    private final CollectionManager collectionManager;
    public PrintFieldDescendingMeleeWeaponCommand (CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Executes the command.
     */
    public boolean execute(String argument, Object objectArgument, User user) {
        try {
            if (!argument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            if (collectionManager.collectionSize() == 0) throw new CollectionIsEmptyException();
            collectionManager.fieldDescending();
            return true;
        } catch (WrongAmountOfElementsException e) {
            ResponseOutputer.appendln("Команда не принимает аргументы");
        } catch (CollectionIsEmptyException e) {
            ResponseOutputer.appendln("Коллекция пустая");
        }
        return false;
    }


    public String description() {
        return name + " - выводит значения поля meleeWeapon всех элементов в порядке убывания";
    }
    public String getName() {return name;}
}
