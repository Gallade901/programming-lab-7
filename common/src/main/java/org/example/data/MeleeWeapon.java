package org.example.data;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;

/**
 * Enumeration with marine melee weapon constants.
 */
@XmlRootElement
public enum MeleeWeapon implements Serializable {
    CHAIN_SWORD(1),
    POWER_SWORD(2),
    CHAIN_AXE(3),
    LIGHTING_CLAW(4),
    POWER_FIST(5);
    @XmlTransient
    private final Integer number;
    MeleeWeapon(Integer number) {
        this.number = number;
    }

    /**
     * Generates a beautiful list of enum string values.
     * @return String with all enum values splitted by comma.
     */


    public static String names() {
        StringBuilder nameList = new StringBuilder();
        for (var astaresCategory: values()) {
            nameList.append(astaresCategory.number).append("-").append(astaresCategory.name()).append(", ");
        }
        return nameList.substring(0, nameList.length()-2);
    }



    public int getNumber() {
        return number;
    }
    public static MeleeWeapon fromInteger(String value) throws IllegalAccessException {
        switch (value) {
            case "1":
                return CHAIN_SWORD;
            case "2":
                return POWER_SWORD;
            case "3":
                return CHAIN_AXE;
            case "4":
                return LIGHTING_CLAW;
            case "5":
                return POWER_FIST;
            default: throw new IllegalAccessException("такого аргумента нет");
        }
    }
}
