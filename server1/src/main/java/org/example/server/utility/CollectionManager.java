package org.example.server.utility;


import org.example.data.*;
import org.example.exceptions.*;
import org.example.server.App;
import org.example.utility.Console;
import org.example.utility.MarineAsker;
import org.example.utility.SpaceMarines;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Operates the collection itself.
 */
public class CollectionManager {
    private static LocalDateTime lastSaveTime;
    private LocalDateTime lastInitTime;

    Scanner sc = new Scanner(System.in);
    public static LinkedHashSet<SpaceMarine> marinesCollection = new LinkedHashSet<>();
    
    public SpaceMarine lastSpace = (marinesCollection.stream().reduce((first, second) -> second).orElse(null));
    public Integer lastId = (lastSpace == null) ? 0 : lastSpace.getId();

    private static String xmlFile;
    private final MarineAsker marineAsker;
    private final DatabaseCollectionManager databaseCollectionManager;

    public CollectionManager(MarineAsker marineAsker, DatabaseCollectionManager databaseCollectionManager) {
        lastInitTime = LocalDateTime.now();
        this.marineAsker = marineAsker;
        this.databaseCollectionManager = databaseCollectionManager;
        loadCollection();
    }

    /**
     * Adds a new marine to collection.
     * @param marine A marine to add.
     */
    public static boolean addToCollection(SpaceMarine marine) {
        try {
            if (!marinesCollection.add(marine)) throw new NotUniqueItemException();
            return true;
        } catch (NotUniqueItemException e) {
            ResponseOutputer.appendln("нельзя добавить не уникальный элемент");
        }
        return false;
    }

    public String showCollection() {
        if (marinesCollection.isEmpty()) return "Коллекция пуста!";
        return marinesCollection.stream().reduce("", (sum, m) -> sum += m + "\n\n", (sum1, sum2) -> sum1 + sum2).trim();
    }


    /**
     * Generates next ID. It will be (the bigger one + 1).
     * @return Next ID.
     */
    public Integer generateNextId() {
        lastId++;
        return lastId;
    }

    /**
     * @param id ID of the marine.
     * @return A marine by his ID or null if marine isn't found.
     */
    public SpaceMarine getById(Integer id) {
        for (SpaceMarine marine : marinesCollection) {
            if (marine.getId().equals(id)) return marine;
        }
        return null;
    }

    /**
     * @return Last initialization time or null if there wasn't initialization.
     */
    public LocalDateTime getLastInitTime() {
        return lastInitTime;
    }

    /**
     * @return Last save time or null if there wasn't saving.
     */
    public LocalDateTime getLastSaveTime() {
        return lastSaveTime;
    }

    /**
     * @return Name of the collection's type.
     */
    public String collectionType() {
        return marinesCollection.getClass().getName();
    }

    /**
     * @return Size of the collection.
     */
    public int collectionSize() {
        return marinesCollection.size();
    }

    /**
     * @return The first element of the collection or null if collection is empty.
     */
    public SpaceMarine getFirst() {
        return marinesCollection.stream().findFirst().orElse(null);
    }

    /**
     * @return The last element of the collection or null if collection is empty.
     */
    public SpaceMarine getLast() {
        return (marinesCollection.stream().reduce((first, second) -> second).orElse(null));
    }

    /**
     * Removes a new marine to collection.
     * @param marine A marine to remove.
     */
    public boolean removeFromCollection(SpaceMarine marine) {
        try {
            if (!marinesCollection.remove(marine)) throw new DeleteException();
            return true;
        } catch (DeleteException e) {
            ResponseOutputer.appendln("Ошибка удаления элемента из коллекции");
        }
        return false;
    }

    /**
     * Terminates the program
     */
    public void systemExit() throws NullPointerException{
        Console.println("Программа завершила свою работу");
        System.exit(0);
    }
    /**
     * Counts the number of elements whose meleeWeapon field value is greater than the given one
     * @param argument for comparison
     */
    public void countGreater (String argument) {
        MeleeWeapon meleeWeapon = MeleeWeapon.valueOf(argument.toUpperCase());
        int count = 0;
        for (SpaceMarine marine : marinesCollection) {
            if (marine.getMeleeWeapon() != null) {
                if (marine.getMeleeWeapon().compareTo(meleeWeapon) > 0) {
                    count++;
                }
            }
        }
        ResponseOutputer.appendln(count);
    }

    /**
     * Clears the collection.
     */
    public void clearCollection() {
        marinesCollection.clear();
    }

    /**
     * Outputs elements whose chapter field value is equal to the given value
     * @param argument for comparison
     */
    public void filterChapter(Chapter argument) {
        if (argument == null) {
            for (SpaceMarine marine : marinesCollection) {
                if (marine.getChapter() == null) {
                    ResponseOutputer.appendln(marine.toString());
                }
            }
        } else {
            for (SpaceMarine marine : marinesCollection) {
                if (marine.getChapter() != null && marine.getChapter().toString().equals(argument.toString())) {
                    ResponseOutputer.appendln(marine.toString());
                }
            }
        }
    }

    /**
     * Prints the values of the meleeWeapon field of all elements in descending order
     */
    public void fieldDescending() {
        List<SpaceMarine> sorted = new ArrayList<>(marinesCollection);
        sorted.sort(Collections.reverseOrder());
        for (SpaceMarine marine: sorted) {
            ResponseOutputer.appendln(marine.getMeleeWeapon());
        }
    }

    /**
     * Saves the collection to file.
     */
    public static void saveCollection() {
        try {
            SpaceMarines sp = new SpaceMarines(marinesCollection);
            FileOutputStream out = new FileOutputStream(xmlFile);
            BufferedOutputStream bf = new BufferedOutputStream(out);
            JAXBContext context = JAXBContext.newInstance(SpaceMarines.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(sp, out);
            lastSaveTime = LocalDateTime.now();
        } catch(IOException | JAXBException e){
            Console.printerror("не удалось сохранить файл");
        }
    }


    public LinkedHashSet<SpaceMarine> getCollection() {
        return marinesCollection;
    }
    private void loadCollection() {
        try {
            marinesCollection = databaseCollectionManager.getCollection();
            lastInitTime = LocalDateTime.now();
            Console.println("Коллекция загружена.");
            App.logger.info("Коллекция загружена.");
        } catch (DatabaseHandlingException exception) {
            marinesCollection = new LinkedHashSet<>();
            Console.printerror("Коллекция не может быть загружена!");
            App.logger.error("Коллекция не может быть загружена!");
        }
    }
}
