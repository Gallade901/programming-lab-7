package org.example.server.utility;

import org.example.data.SpaceMarine;
import org.example.exceptions.InappropriateFormatException;
import org.example.utility.Console;
import org.example.utility.SpaceMarines;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationException;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * unmarshall the file
 */
public class JaxbReader {
    private final int minId = 0;

    private final int minHealth = 0;

    private final int minHeartCount = 0;

    private final int maxHeartCount = 3;

    private static String xmlFile;
    public JaxbReader(String xmlFile){
        JaxbReader.xmlFile = xmlFile;
    }

    /**
     * Populating the collection
     */
    public void autocomplete() throws JAXBException {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(SpaceMarines.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            FileReader reader = new FileReader(xmlFile);
            SpaceMarines spaceMarines = (SpaceMarines) unmarshaller.unmarshal(reader);
            if (!spaceMarines.getSpaceMarineLinkedHashSet().isEmpty()) {
                for (SpaceMarine s : spaceMarines.getSpaceMarineLinkedHashSet()) {
                    if (s.getId() == null || s.getId() <= minId) throw new InappropriateFormatException("Id должно быть больше " + minId + " и не равно null");
                    if (s.getName() == null || s.getName().isEmpty()) throw new InappropriateFormatException("Mariane name не может быть null, и не может быть пустым");
                    if (s.getCoordinates() == null) throw new InappropriateFormatException("coordinates не может быть null");
                    if (s.getCreationDate() == null) throw new InappropriateFormatException("creationDate не может быть null");
                    if (s.getHealth() <= minHealth) throw new InappropriateFormatException("health должно быть больше " + minHealth);
                    if (s.getHeartCount() <= minHeartCount || s.getHeartCount() > maxHeartCount) throw new InappropriateFormatException("heartCount должно бать больше " + minHeartCount + " и меньше " + maxHeartCount);

                   CollectionManager.addToCollection(s);
                }
            }
        } catch (ValidationException e) {
            Console.printerror("валидации");
        } catch (JAXBException e) {
            Console.println("Не удалось загрузить данные из файла");
        } catch (FileNotFoundException e) {
              Console.println("Файл не найден");
        } catch (NullPointerException e) {
            Console.println("Файл пуст");
        } catch (InappropriateFormatException e) {
            Console.println(e + "проверьте строку с соответствующим параметром");
        }
    }
}
