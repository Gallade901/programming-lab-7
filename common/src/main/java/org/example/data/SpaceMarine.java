package org.example.data;

import org.example.exceptions.InappropriateFormatException;
import org.example.utility.Console;
import org.example.utility.LocalDateTimeAdapter;
import org.example.utility.User;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Main character. Is stored in the collection.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SpaceMarine", propOrder = {"id", "name", "coordinates", "creationDate", "health", "heartCount", "category", "meleeWeapon", "chapter"})
public class SpaceMarine implements Comparable<SpaceMarine>, Serializable {

    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически

    private String name; //Поле не может быть null, Строка не может быть пустой

    private Coordinates coordinates; //Поле не может быть null
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически

    private long health; //Значение поля должно быть больше 0

    private long heartCount; //Значение поля должно быть больше 0, Максимальное значение поля: 3

    private AstartesCategory category; //Поле может быть null

    private MeleeWeapon meleeWeapon; //Поле может быть null

    private Chapter chapter; //Поле может быть null
    private User owner;
    @XmlTransient
    private final int minId = 0;
    @XmlTransient
    private final int minHealth = 0;
    @XmlTransient
    private final int minHeartCount = 0;
    @XmlTransient
    private final int maxHeartCount = 3;



    public SpaceMarine(Integer id, String name, Coordinates coordinates, LocalDateTime creationDate, long health, long heartCount, AstartesCategory category, MeleeWeapon meleeWeapon, Chapter chapter, User owner) {
        setId(id);
        setName(name);
        setCoordinates(coordinates);
        setCreationDate(creationDate);
        setHealth(health);
        setHeartCount(heartCount);
        setCategory(category);
        setMeleeWeapon(meleeWeapon);
        setChapter(chapter);
        this.owner = owner;
    }
    public SpaceMarine() {}

    /**
     * @return ID of the marine.
     */
    public Integer getId() {
        return id;
    }

    /**
     * @return Name of the marine.
     */
    public String getName() {
        return name;
    }

    /**
     * @return Chapter of the marine.
     */
    public Chapter getChapter() {
        return chapter;
    }

    /**
     * @return Coordinates of the marine.
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * @return Health of the marine.
     */
    public long getHealth() {
        return health;
    }

    /**
     * @return HeartCount of the marine.
     */
    public long getHeartCount() {
        return heartCount;
    }

    /**
     * @return Category of the marine.
     */
    public AstartesCategory getCategory() {
        return category;
    }

    /**
     * @return MeleeWeapon of the marine.
     */
    public MeleeWeapon getMeleeWeapon() {
        return meleeWeapon;
    }

    /**
     * @return CreationDate of the marine.
     */
    public LocalDateTime getCreationDate() {
        return creationDate;
    }
    public User getOwner() {
        return owner;
    }

    public void setId(Integer id) {
        try {
            if (id == null || id <= minId) throw new InappropriateFormatException("Id должно быть больше " + minId + " и не равно null");
            this.id = id;
        } catch (InappropriateFormatException e) {
            Console.println(e.toString());
        }

    }

    public void setName(String name) {
        try {
            if (name == null || name.isEmpty()) throw new InappropriateFormatException("Mariane name не может быть null, и не может быть пустым");
            this.name = name;
        } catch (InappropriateFormatException e) {
            Console.println(e.toString());
        }

    }

    public void setCoordinates(Coordinates coordinates) {
        try {
            if (coordinates == null) throw new InappropriateFormatException("coordinates не может быть null");
            this.coordinates = coordinates;
        } catch (InappropriateFormatException e) {
            Console.println(e.toString());
        }

    }

    public void setCreationDate(LocalDateTime creationDate) {
        try {
            if (creationDate == null) throw new InappropriateFormatException("creationDate не может быть null");
            this.creationDate = creationDate;
        } catch (InappropriateFormatException e) {
            Console.println(e.toString());
        }

    }

    public void setHealth(long health) {
        try {
            if (health <= minHealth) throw new InappropriateFormatException("health должно быть больше " + minHealth);
            this.health = health;
        } catch (InappropriateFormatException e) {
            Console.println(e.toString());
        }

    }

    public void setHeartCount(long heartCount) {
        try {
            if (heartCount <= minHeartCount || heartCount > maxHeartCount) throw new InappropriateFormatException("heartCount должно бать больше " + minHeartCount + " и меньше " + maxHeartCount);
            this.heartCount = heartCount;
        } catch (InappropriateFormatException e) {
            Console.println(e.toString());
        }

    }

    public void setCategory(AstartesCategory category) {
        this.category = category;
    }

    public void setMeleeWeapon(MeleeWeapon meleeWeapon) {
        this.meleeWeapon = meleeWeapon;
    }

    public void setChapter(Chapter chapter) {
        this.chapter = chapter;
    }

    @Override
    public String toString() {
        String info = "";
        info += "Солдат №" + id;
        info += " (добавлен " + creationDate.toLocalDate() + " " + creationDate.toLocalTime() + ")";
        info += "\n имя: " + name;
        info += "\n Местоположение: " + coordinates;
        info += "\n Здоровье: " + health;
        info += "\n Категория: " + category;
        info += "\n Количество сердец: " + heartCount;
        info += "\n Ближнее оружие: " + meleeWeapon;
        info += "\n Орден: " + chapter;
        return info;
    }


    @Override
    public int compareTo(SpaceMarine marineObj) {
        return id.compareTo(marineObj.getId());
    }
}
