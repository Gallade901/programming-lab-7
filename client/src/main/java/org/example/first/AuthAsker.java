package org.example.first;



import org.example.exceptions.MustBeNotEmptyException;
import org.example.exceptions.NotInDeclaredLimitsException;
import org.example.utility.Console;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Asks user a login and password.
 */
public class AuthAsker {
    private Scanner userScanner;

    public AuthAsker(Scanner userScanner) {
        this.userScanner = userScanner;
    }

    /**
     * Asks user a login.
     *
     * @return login.
     */
    public String askLogin() {
        String login;
        while (true) {
            try {
                Console.println("Введите логин:");
                Console.print("> ");
                login = userScanner.nextLine().trim();
                if (login.equals("")) throw new MustBeNotEmptyException();
                break;
            } catch (NoSuchElementException exception) {
                Console.printerror("Данного логина не существует!");
            } catch (MustBeNotEmptyException exception) {
                Console.printerror("Имя не может быть пустым!");
            } catch (IllegalStateException exception) {
                Console.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return login;
    }

    /**
     * Asks user a password.
     *
     * @return password.
     */
    public String askPassword() {
        String password;
        while (true) {
            try {
                Console.println("Введите пароль:");
                Console.print("> ");
                password = userScanner.nextLine().trim();
                if (password.equals("")) throw new MustBeNotEmptyException();
                break;
            } catch (NoSuchElementException exception) {
                Console.printerror("Неверный логин или пароль!");
            } catch (MustBeNotEmptyException exception) {
                Console.printerror("Пароль не может быть пустым!");
            } catch (IllegalStateException exception) {
                Console.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return password;
    }

    /**
     * Asks a user a question.
     *
     * @param question A question.
     * @return Answer (true/false).
     */
    public boolean askQuestion(String question) {
        String finalQuestion = question + " (+/-):";
        String answer;
        while (true) {
            try {
                Console.println(finalQuestion);
                Console.print("> ");
                answer = userScanner.nextLine().trim();
                if (!answer.equals("+") && !answer.equals("-")) throw new NotInDeclaredLimitsException();
                break;
            } catch (NoSuchElementException exception) {
                Console.printerror("Ответ не распознан!");
            } catch (NotInDeclaredLimitsException exception) {
                Console.printerror("Ответ должен быть представлен знаками '+' или '-'!");
            } catch (IllegalStateException exception) {
                Console.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return answer.equals("+");
    }
}
