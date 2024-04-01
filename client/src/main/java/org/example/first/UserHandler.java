package org.example.first;

import org.example.exceptions.*;
import org.example.utility.*;
import org.example.data.AstartesCategory;
import org.example.data.Chapter;
import org.example.data.Coordinates;
import org.example.data.MeleeWeapon;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class UserHandler {
    private final int maxRewriteAttempts = 1;

    private Scanner userScanner;
    private Stack<File> scriptStack = new Stack<>();
    private Stack<Scanner> scannerStack = new Stack<>();
    final HashSet<String> processingOk = new HashSet<>(Arrays.asList("update", "clear", "exit", "help", "info", "show", "remove_by_id", "exit", "history", "print_field_descending_melee_weapon"));
    final HashSet<String> processingCodeObject = new HashSet<>(Arrays.asList("add", "add_if_min", "add_if_max"));
    final static Map<String, ProcessingCode> processingCodeMap = new HashMap<>();

    public void addProcessing() {
        for (String object : processingCodeObject) {
            processingCodeMap.put(object, ProcessingCode.OBJECT);
        }
        for (String ok : processingOk) {
            processingCodeMap.put(ok, ProcessingCode.OK);
        }
        processingCodeMap.put("execute_script", ProcessingCode.SCRIPT);
//        processingCodeMap.put("update", ProcessingCode.UPDATE_OBJECT);
        processingCodeMap.put("filter_by_chapter", ProcessingCode.CHAPTER);
    }

    public UserHandler(Scanner userScanner) {
        this.userScanner = userScanner;
    }

    /**
     * Receives user input.
     *
     * @param serverResponseCode Last server's response code.
     * @return New request to server.
     */
    public Request handle(ResponseCode serverResponseCode, String responseBody, User user) {
        String userInput;
        String[] userCommand = new String[0];
        ProcessingCode processingCode;
        int rewriteAttempts = 0;
        try {
            do {
                try {
                    if (serverResponseCode == ResponseCode.UPDATE) {
                        MarineRaw marineUpdateRaw = generateMarineUpdate();
                        return new Request("update2", responseBody, marineUpdateRaw, user);
                    }
                    if (fileMode() && (serverResponseCode == ResponseCode.ERROR))
                        throw new IncorrectInputInScriptException();
                    while (fileMode() && !userScanner.hasNextLine()) {
                        userScanner.close();
                        userScanner = scannerStack.pop();
                        Console.println("Возвращаюсь к скрипту '" + scriptStack.pop().getName() + "'...");
                    }
                    if (fileMode()) {
                        userInput = userScanner.nextLine();
                        if (!userInput.isEmpty()) {
                            Console.print("> ");
                            Console.println(userInput);
                        }
                    } else {
                        Console.print("> ");
                        userInput = userScanner.nextLine();
                    }
                    userCommand = (userInput.trim() + " ").split(" ", 2);
                    userCommand[1] = userCommand[1].trim();
                } catch (NoSuchElementException | IllegalStateException exception) {
                    Console.println("");
                    Console.printerror("Произошла ошибка при вводе команды!");
                    userCommand = new String[]{"", ""};
                    rewriteAttempts++;
                    if (rewriteAttempts >= maxRewriteAttempts) {
                        Console.printerror("Превышено количество попыток ввода!");
                        System.exit(0);
                    }
                }
                processingCode = processCommand(userCommand[0], userCommand[1]);
            } while (processingCode == ProcessingCode.ERROR && !fileMode() || userCommand[0].isEmpty());
            try {
                if (fileMode() && (serverResponseCode == ResponseCode.ERROR || processingCode == ProcessingCode.ERROR))
                    throw new IncorrectInputInScriptException();
                switch (processingCode) {
                    case OBJECT:
//                        askUpdate();
                        MarineRaw marineAddRaw = generateMarineAdd();
                        return new Request(userCommand[0], userCommand[1], marineAddRaw, user);
                    case CHAPTER:
                        Chapter chapter = generateChapter();
                        return new Request(userCommand[0], userCommand[1], chapter, user);
                    case SCRIPT:
                        File scriptFile = new File(userCommand[1]);
                        if (!scriptFile.exists()) throw new FileNotFoundException();
                        if (!scriptStack.isEmpty() && scriptStack.search(scriptFile) != -1)
                            throw new ScriptRecursionException();
                        scannerStack.push(userScanner);
                        scriptStack.push(scriptFile);
                        userScanner = new Scanner(scriptFile);
                        Console.println("Выполняю скрипт '" + scriptFile.getName() + "'...");
                        break;
                }
            } catch (FileNotFoundException exception) {
                Console.printerror("Файл со скриптом не найден!");
            } catch (ScriptRecursionException exception) {
                Console.printerror("Скрипты не могут вызываться рекурсивно!");
                throw new IncorrectInputInScriptException();
            } catch (InappropriateFormatException e) {
                Console.println("Невалидные поля");
            }
        } catch (IncorrectInputInScriptException exception) {
            Console.printerror("Выполнение скрипта прервано!");
            while (!scannerStack.isEmpty()) {
                userScanner.close();
                userScanner = scannerStack.pop();
            }
            scriptStack.clear();
            return new Request(user);
        }catch (InappropriateFormatException e) {
            Console.println("Невалидные поля");
        }
        return new Request(userCommand[0], userCommand[1], null, user);
    }

    /**
     * Processes the entered command.
     *
     * @return Status of code.
     */
    private ProcessingCode processCommand(String command, String commandArgument) {
        addProcessing();
        for (Map.Entry<String, ProcessingCode> element : processingCodeMap.entrySet()) {
            if (command.equals(element.getKey())) {
                return element.getValue();
            }

        }
        Console.println("Команда '" + command + "' не найдена. Наберите 'help' для справки.");
        return ProcessingCode.ERROR;
    }

    /**
     * Generates marine to add.
     *
     * @return Marine to add.
     * @throws IncorrectInputInScriptException When something went wrong in script.
     */
    private MarineRaw generateMarineAdd() throws IncorrectInputInScriptException, InappropriateFormatException {
        MarineAsker marineAsker = new MarineAsker(userScanner);
        if (fileMode()) marineAsker.setFileMode();
        return new MarineRaw(
                marineAsker.askName(),
                marineAsker.askCoordinates(),
                marineAsker.askHealth(),
                marineAsker.askCategory(),
                marineAsker.askHeartCount(),
                marineAsker.askMeleeWeapon(),
                marineAsker.askChapter()
        );
    }

    /**
     * Generates marine to update.
     *
     * @return Marine to update.
     * @throws IncorrectInputInScriptException When something went wrong in script.
     */
    private MarineRaw generateMarineUpdate() throws IncorrectInputInScriptException, InappropriateFormatException {
        MarineAsker marineAsker = new MarineAsker(userScanner);
        if (fileMode()) marineAsker.setFileMode();
        String name = marineAsker.askQuestion("Хотите изменить имя солдата?") ?
                marineAsker.askName() : null;
        Coordinates coordinates = marineAsker.askQuestion("Хотите изменить координаты солдата?") ?
                marineAsker.askCoordinates() : null;
        long health = marineAsker.askQuestion("Хотите изменить здоровье солдата?") ?
                marineAsker.askHealth() : -1;
        AstartesCategory category = marineAsker.askQuestion("Хотите изменить категорию солдата?") ?
                marineAsker.askCategory() : null;
        long heartCount = marineAsker.askQuestion("Хотите изменить количество сердец солдата?") ?
                marineAsker.askHeartCount() : -1;
        MeleeWeapon meleeWeapon = marineAsker.askQuestion("Хотите изменить оружие ближнего боя солдата?") ?
                marineAsker.askMeleeWeapon() : null;
        Chapter chapter = marineAsker.askQuestion("Хотите изменить орден солдата?") ?
                marineAsker.askChapter() : null;
        return new MarineRaw(
                name,
                coordinates,
                health,
                category,
                heartCount,
                meleeWeapon,
                chapter
        );
    }

    /**
     * Checks if UserHandler is in file mode now.
     *
     * @return Is UserHandler in file mode now boolean.
     */
    private boolean fileMode() {
        return !scannerStack.isEmpty();
    }

    private Chapter generateChapter() throws IncorrectInputInScriptException {
        MarineAsker marineAsker = new MarineAsker(userScanner);
        return marineAsker.askChapter();
    }
    private void askUpdate() {

    }
}
