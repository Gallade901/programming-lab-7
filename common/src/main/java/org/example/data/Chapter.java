package org.example.data;

import org.example.exceptions.InappropriateFormatException;
import org.example.utility.Console;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;


/**
 * Chapter with marines.
 */
@XmlRootElement
@XmlType(name = "Chapter", propOrder = {
        "name",
        "parentLegion"
})
public class Chapter implements Serializable {
    @XmlElement
    private String name; //Поле не может быть null, Строка не может быть пустой
    @XmlElement
    private String parentLegion;

    public Chapter(String name, String parentLegion) {
        setName(name);
        setParentLegion(parentLegion);
    }
    public Chapter() {}

    public void setName(String name) {
        try {
            if (name == null || name.isEmpty()) throw new InappropriateFormatException("Chapter name не может быть пустым или равным null");
            this.name = name;
        } catch (InappropriateFormatException e) {
            Console.println(e.toString());
        }
    }
    public String getName() {
        return name;
    }
    public String getParentLegion() {
        return parentLegion;
    }

    public void setParentLegion(String parentLegion) {
        this.parentLegion = parentLegion;
    }


    @Override
    public String toString() {
        return "имя: " + name + ", легион: " + parentLegion;
    }

}
