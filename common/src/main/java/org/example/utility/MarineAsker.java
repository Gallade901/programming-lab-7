package org.example.utility;


import org.example.data.AstartesCategory;
import org.example.data.Chapter;
import org.example.data.Coordinates;
import org.example.data.MeleeWeapon;
import org.example.exceptions.InappropriateFormatException;
import org.example.exceptions.IncorrectInputInScriptException;
import org.example.exceptions.MustBeNotEmptyException;
import org.example.exceptions.NotInDeclaredLimitsException;


import java.util.Scanner;

/**
 * Asks a user a marine's value.
 */
public class MarineAsker {
    private final Scanner userScanner;
    private boolean fileMode;

    public MarineAsker(Scanner userScanner) {
        this.userScanner = userScanner;
        fileMode = false;
    }

    public void setFileMode() {
        fileMode = true;

    }

    /**
     * Asks a user the marine's name.
     * @return Marine's name.
     */
    public String askName() throws IncorrectInputInScriptException {
        String name;
        while (true) {
            try {
                Console.println("Введите имя:");
                name = userScanner.nextLine().trim();
                if (fileMode) Console.println(name);
                if (name.isEmpty()) throw new MustBeNotEmptyException();
                break;
            } catch (MustBeNotEmptyException e) {
                Console.printerror("имя не может быть пустым");
                if (fileMode) throw new IncorrectInputInScriptException();
            }
        }
        return name;
    }

    /**
     *
     * Returns the x and y coordinates
     * @return x, y
     */
    public Coordinates askCoordinates() throws IncorrectInputInScriptException {
        Float x;
        Integer y;
        x = askX();
        y = askY();
        return new Coordinates(x, y);
    }

    /**
     * Asks a user the marine's Y coordinate.
     * @return Marine's Y coordinate.
     */
    public Integer askY() throws IncorrectInputInScriptException {
        while (true) {
            try {
                String strY;
                int y;
                Console.println("Введите координату Y");
                strY = userScanner.nextLine().trim();
                if (fileMode) Console.println(strY);
                y = Integer.parseInt(strY);
                return y;
            } catch (NumberFormatException e) {
                Console.printerror("Некорректный тип введённых данных");
                if (fileMode) throw new IncorrectInputInScriptException();
            }

        }
    }

    /**
     * Asks a user the marine's X coordinate.
     * @return Marine's X coordinate.
     */
    public Float askX() throws IncorrectInputInScriptException {
        int maxX = 634;
        while (true) {
            try {
                String strX;
                float x;
                Console.println("Введите координату X <= " + maxX);
                strX = userScanner.nextLine().trim();
                if (fileMode) Console.println(strX);
                x = Float.parseFloat(strX);
                if (x > maxX) {
                    Console.printerror("Некорректно введена переменная X");
                    if (fileMode) throw new IncorrectInputInScriptException();
                }
                else {
                    return x;
                }
            }
            catch (NumberFormatException e) {
                Console.printerror("Некорректный тип введённых данных");
                if (fileMode) throw new IncorrectInputInScriptException();
            }
        }
    }

    /**
     * Asks a user the marine's health.
     * @return Marine's health.
     */
    public long askHealth() throws IncorrectInputInScriptException {
        while (true) {
            try {
                String strHealth;
                int minHealth = 0;
                long health;
                Console.println("Введите здорье > " + minHealth);
                strHealth = userScanner.nextLine().trim();
                if (fileMode) Console.println(strHealth);
                health = Long.parseLong(strHealth);
                if (health <= minHealth) {
                    Console.printerror("Введено некорректное здоровье");
                    if (fileMode) throw new IncorrectInputInScriptException();
                } else {
                    return health;
                }
            } catch (NumberFormatException e) {
                Console.printerror("Некорректный тип введённых данных");
                if (fileMode) throw new IncorrectInputInScriptException();
            }
        }
    }

    /**
     * Asks a user the marine's count heart.
     * @return Marine's heart.
     */
    public Long askHeartCount() throws IncorrectInputInScriptException{
        String strHeart;
        Long heart;
        int minHeart = 1;
        int maxHeart = 3;
        while (true) {
            try {
                Console.println("Введите количество сердец (" + minHeart + "-" + maxHeart + ")");
                strHeart = userScanner.nextLine().trim();
                if (fileMode) Console.println(strHeart);
                heart = Long.parseLong(strHeart);
                if (heart > maxHeart || heart < minHeart) {
                    Console.printerror("Введено некорректное количество сердец");
                    if (fileMode) throw new IncorrectInputInScriptException();
                } else {
                    break;
                }

            } catch (NumberFormatException e) {
                Console.printerror("Некорректный тип введённых данных");
                if (fileMode) throw new IncorrectInputInScriptException();
            }
        }
        return heart;
    }

    /**
     * Asks a user the marine's category.
     * @return Marine's category.
     */
    public AstartesCategory askCategory() throws IncorrectInputInScriptException {
        AstartesCategory category;
        while (true) {
            try {
                String strCategory;
                Console.println("Список категорий - " + AstartesCategory.names());
                Console.println("Введите категорию:");
                strCategory = userScanner.nextLine().trim();
                if (fileMode) Console.println(strCategory);
                if (strCategory.isEmpty()) {
                    return null;
                } else {
                    category = AstartesCategory.fromInteger(strCategory);
                    return category;
                }
            } catch (IllegalArgumentException e) {
                Console.printerror("Категории нет в списке!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (IllegalAccessException e) {
                Console.printerror("доступа");
                if (fileMode) throw new IncorrectInputInScriptException();
            }
        }
    }

    /**
     * Asks a user the marine's melee weapon.
     * @return Marine's melee weapon.
     */
    public MeleeWeapon askMeleeWeapon() throws IncorrectInputInScriptException {
        while (true) {
            try {
                String strMeleeWeapon;
                MeleeWeapon meleeWeapon;
                Console.println("Список оружия ближнего боя - " + MeleeWeapon.names());
                Console.println("Введите оружие ближнего боя:");
                strMeleeWeapon = userScanner.nextLine().trim();
                if (fileMode) Console.println(strMeleeWeapon);
                if (strMeleeWeapon.isEmpty()) {
                    return null;
                } else {
                    meleeWeapon = MeleeWeapon.fromInteger(strMeleeWeapon);
                    return meleeWeapon;
                }
            } catch (IllegalArgumentException e) {
                Console.printerror("Категории нет в списке!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (IllegalAccessException e) {
                Console.printerror("доступа");
                if (fileMode) throw new IncorrectInputInScriptException();
            }
        }
    }

    /**
     * Asks a user the marine chapter's name.
     * @return Chapter's name.
     */
    public String askChapterName() throws  IncorrectInputInScriptException {
        String name;
        while (true) {
            try {
                Console.println("Введите имя");
                name = userScanner.nextLine().trim();
                if (fileMode) Console.println(name);
                if (name.isEmpty()) throw new MustBeNotEmptyException();
                break;
            } catch (MustBeNotEmptyException e) {
                Console.printerror("имя не может быть пустым");
                if (fileMode) throw new IncorrectInputInScriptException();
            }
        }
        return name;
    }

    /**
     * Asks a user the marine chapter's parent legion.
     * @return Chapter's parent legion.
     */
    public String askChapterParentLegion() {
        Console.println("Введите название легиона");
        String parentLegion = userScanner.nextLine().trim();
        if (fileMode) Console.println(parentLegion);
        return parentLegion;
    }

    /**
     * Asks a user the marine's chapter.
     * @return Marine's chapter.
     */
    public Chapter askChapter() throws IncorrectInputInScriptException {
        String chapter;
        String name;
        String parentLegion;
        Console.println("Если хотите оставить Chapter пустым нажмите Enter, иначе введите что угодно");
        chapter = userScanner.nextLine().trim();
        if (fileMode) Console.println(chapter);
        if (chapter.isEmpty()) {
            return null;
        } else {
            name = askChapterName();
            parentLegion = askChapterParentLegion();
            return new Chapter(name, parentLegion);
        }
    }

    /**
     * Asks a user a question.
     * @return Answer (true/false).
     * @param question A question.
     */
    public boolean askQuestion(String question) throws IncorrectInputInScriptException {
        String finalQuestion = question + " (+/-):";
        String answer;
        while (true) {
            try {
                Console.println(finalQuestion);
                answer = userScanner.nextLine().trim();
                if (fileMode) Console.println(answer);
                if (!answer.equals("+") && !answer.equals("-")) throw new NotInDeclaredLimitsException();
                break;
            } catch (NotInDeclaredLimitsException e) {
                Console.printerror("Ответ должен быть представлен знаками '+' или '-'");
                if (fileMode) throw new IncorrectInputInScriptException();
            }

        }
        return answer.equals("+");
    }
}

