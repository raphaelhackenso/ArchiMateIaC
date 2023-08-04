package at.fhjoanneum;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class XMLParser {

    public static void main(String[] args) {
        try {
            // Load the XML file
            File xmlFile = new File("no.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);

            // Select elements with the tag name "ProductName"
            NodeList nodeList = doc.getElementsByTagName("attributes");

            // Create a Set to store unique values
            Set<String> uniqueValues = new HashSet<>();

            // Iterate over the selected elements and add unique values to the Set
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                String value = element.getTextContent();
                uniqueValues.add(value);
            }

            // Print the unique values
            for (String value : uniqueValues) {
                System.out.println(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}