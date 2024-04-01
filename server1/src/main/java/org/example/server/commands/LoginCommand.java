package org.example.server.commands;

import org.example.exceptions.DatabaseHandlingException;
import org.example.exceptions.UserIsNotFoundException;
import org.example.exceptions.WrongAmountOfElementsException;
import org.example.server.utility.Command;
import org.example.server.utility.DatabaseUserManager;
import org.example.server.utility.ResponseOutputer;
import org.example.utility.User;


import java.io.Serializable;

/**
 * Command 'login'. Allows the user to login.
 */
public class LoginCommand implements Command, Serializable {
    private DatabaseUserManager databaseUserManager;
    String name = "login";


    public LoginCommand(DatabaseUserManager databaseUserManager) {
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
            if (databaseUserManager.checkUserByUsernameAndPassword(user)) ResponseOutputer.appendln("Пользователь " +
                    user.getUsername() + " авторизован.");
            else throw new UserIsNotFoundException();
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Использование: эммм...эээ.это внутренняя команда...");
        } catch (ClassCastException exception) {
            ResponseOutputer.appendln("Переданный клиентом объект неверен!");
        } catch (DatabaseHandlingException exception) {
            ResponseOutputer.appendln("Произошла ошибка при обращении к базе данных!");
        } catch (UserIsNotFoundException exception) {
            ResponseOutputer.appendln("Неправильные имя пользователя или пароль!");
        }
        return false;
    }
    public String description() {
        return name + " - логинит";
    }
    public String getName() {return name;}
}
