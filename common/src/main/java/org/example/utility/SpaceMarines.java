package org.example.utility;


import org.example.data.SpaceMarine;

import javax.xml.bind.annotation.*;
import java.util.LinkedHashSet;

/**
 * Helper class for marshaling a collection
 */
@XmlRootElement
@XmlType(name = "squad", propOrder = {"spaceMarineLinkedHashSet"})
@XmlAccessorType(XmlAccessType.FIELD)
public class SpaceMarines {

    @XmlElement(name = "warrior")
    public LinkedHashSet<SpaceMarine> spaceMarineLinkedHashSet;

    public SpaceMarines() {
    }

    public SpaceMarines(LinkedHashSet<SpaceMarine> spaceMarineLinkedHashSet) {
        this.spaceMarineLinkedHashSet = spaceMarineLinkedHashSet;
    }

    /**
     * Returns a collection of elements
     * @return spaceMarineLinkedHashSet
     */
    public LinkedHashSet<SpaceMarine> getSpaceMarineLinkedHashSet() {
        return spaceMarineLinkedHashSet;
    }
}
