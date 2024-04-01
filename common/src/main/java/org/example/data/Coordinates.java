package org.example.data;

import org.example.exceptions.InappropriateFormatException;
import org.example.utility.Console;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * X-Y coordinates.
 */
@XmlRootElement
@XmlType(name = "Coordinates", propOrder = {
        "x",
        "y"
})
public class Coordinates implements Serializable {
    @XmlElement
    private float x; //Максимальное значение поля: 634
    @XmlElement
    private int y;
    @XmlTransient
    public static int MAX_X = 634;

    public Coordinates(float x, int y) {
        setX(x);
        setY(y);
    }
    public Coordinates(){}

    public void setX(float x) {
        try {
            if (x > MAX_X) throw new InappropriateFormatException("Координата X должна быть меньше " + MAX_X);
            this.x = x;
        } catch (InappropriateFormatException e) {
            Console.println(e.toString());
        }
    }

    public void setY(int y) {
        this.y = y;
    }
    public float getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
