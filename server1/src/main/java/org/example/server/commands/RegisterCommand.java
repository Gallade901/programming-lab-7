package org.example.server.commands;


import org.example.exceptions.DatabaseHandlingException;
import org.example.exceptions.UserAlreadyExists;
import org.example.exceptions.WrongAmountOfElementsException;
import org.example.server.utility.Command;
import org.example.server.utility.DatabaseUserManager;
import org.example.server.utility.ResponseOutputer;
import org.example.utility.User;

import java.io.Serializable;

/**
 * Command 'register'. Allows the user to register.
 */
public class RegisterCommand implements Command, Serializable {
    private DatabaseUserManager databaseUserManager;
    private String name = "register";
    public RegisterCommand(DatabaseUserManager databaseUserManager) {
        this.databaseUserManager = databaseUserManager;
    }

    /**
     * Executes the command.
     *
     * @return Command exit status.
     */
    public boolean execute(String stringArgument, Object objectArgument, User user) {
        try {
            if (!stringArgument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            if (databaseUserManager.insertUser(user)) ResponseOutputer.appendln("Пользователь " +
                    user.getUsername() + " зарегистрирован.");
            else throw new UserAlreadyExists();
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Использование: эммм...эээ.это внутренняя команда...");
        } catch (ClassCastException exception) {
            ResponseOutputer.appendln("Переданный клиентом объект неверен!");
        } catch (DatabaseHandlingException exception) {
            ResponseOutputer.appendln("Произошла ошибка при обращении к базе данных!");
        } catch (UserAlreadyExists exception) {
            ResponseOutputer.appendln("Пользователь " + user.getUsername() + " уже существует!");
        }
        return false;
    }


    public String description() {
        return name + " - регистрирует пользователя";
    }

    @Override
    public String getName() {
        return name;
    }
}
