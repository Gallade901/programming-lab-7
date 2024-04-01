package org.example.data;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;

/**
 * Enumeration with marine category constants.
 */
@XmlRootElement
public enum AstartesCategory implements Serializable {
    ASSAULT(1),
    TERMINATOR(2),
    CHAPLAIN(3);
    @XmlTransient
    private final int number;

    AstartesCategory(int number) {
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
    public static AstartesCategory fromInteger(String value) throws IllegalAccessException {
        return switch (value) {
            case "1" -> ASSAULT;
            case "2" -> TERMINATOR;
            case "3" -> CHAPLAIN;
            default -> throw new IllegalAccessException("такого аргумента нет");
        };
    }
}
