package org.example.utility;


import org.example.data.*;

import java.io.Serializable;

/**
 * Class for get Marines value.
 */
public class MarineRaw implements Serializable {
    private String name;
    private Coordinates coordinates;
    private long health;
    private AstartesCategory category;
    private long heartCount;
    private MeleeWeapon meleeWeapon;
    private Chapter chapter;

    public MarineRaw(String name, Coordinates coordinates, long health, AstartesCategory category,
                     long heartCount, MeleeWeapon meleeWeapon, Chapter chapter) {
        this.name = name;
        this.coordinates = coordinates;
        this.health = health;
        this.category = category;
        this.heartCount = heartCount;
        this.meleeWeapon = meleeWeapon;
        this.chapter = chapter;
    }

    /**
     * @return Name of the marine.
     */
    public String getName() {
        return name;
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
     * @return Category of the marine.
     */
    public AstartesCategory getCategory() {
        return category;
    }

    /**
     * @return Weapon type of the marine.
     */
    public long getHeartCount() {
        return heartCount;
    }

    /**
     * @return Melee weapon of the marine.
     */
    public MeleeWeapon getMeleeWeapon() {
        return meleeWeapon;
    }

    /**
     * @return Chapter of the marine.
     */
    public Chapter getChapter() {
        return chapter;
    }

    @Override
    public String toString() {
        String info = "";
        info += "'Сырой' солдат";
        info += "\n Имя: " + name;
        info += "\n Местоположение: " + coordinates;
        info += "\n Здоровье: " + health;
        info += "\n Категория: " + category;
        info += "\n Количество сердец: " + heartCount;
        info += "\n Ближнее оружие: " + meleeWeapon;
        info += "\n Орден: " + chapter;
        return info;
    }

}
