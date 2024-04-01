package org.example.server.commands;

import org.example.exceptions.CollectionIsEmptyException;
import org.example.exceptions.WrongAmountOfElementsException;
import org.example.server.utility.Command;
import org.example.server.utility.CollectionManager;
import org.example.server.utility.ResponseOutputer;
import org.example.utility.User;

import java.io.Serializable;

/**
 * The command displays the number of elements whose
 * meleeWeapon field value is greater than the specified value
 */
public class CountGreaterThanMeleeWeaponCommand  implements Command, Serializable {
    String name = "count_greater_than_melee_weapon";
    private final CollectionManager collectionManager;
    public CountGreaterThanMeleeWeaponCommand (CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Executes the command.
     */
    public boolean execute(String argument, Object objectArgument, User user) {
        try {
            if (argument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            if (collectionManager.collectionSize() == 0) throw new CollectionIsEmptyException();
            collectionManager.countGreater(argument);
            return true;
        } catch (WrongAmountOfElementsException e) {
            ResponseOutputer.appendln("Команда должна принимать один аргумент");
        } catch (CollectionIsEmptyException e) {
            ResponseOutputer.appendln("Коллекция пустая");
        } catch (IllegalArgumentException e) {
            ResponseOutputer.appendln("Введено оружие не из списка");
        }
        return  false;
    }


    public String description() {
        return name + " - выводит количество элементов, значение поля meleeWeapon которых больше заданного";
    }
    public String getName() {return name;}
}
