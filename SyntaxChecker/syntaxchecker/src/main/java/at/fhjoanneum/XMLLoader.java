package at.fhjoanneum;

import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

public class XMLLoader {
    public static Document getXMLDocument(String pathToXML) throws Exception {
        try {
            // Load the XML file
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(pathToXML);
            doc.getDocumentElement().normalize();
            return doc;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Element findElementById(Document archiMateModel, String id) {
        NodeList elements = archiMateModel.getElementsByTagName("element");
        for (int i = 0; i < elements.getLength(); i++) {
            Element element = (Element) elements.item(i);
            String identifier = element.getAttribute("identifier");
            if (identifier.equals(id)) {
                return element;
            }
        }
        return null;
    }

    public static String getPropertyName(Document archiMateModel, String propertyDefinitionRef) {
        NodeList propertyDefinitions = archiMateModel.getElementsByTagName("propertyDefinition");
        for (int i = 0; i < propertyDefinitions.getLength(); i++) {
            Element propertyDefinition = (Element) propertyDefinitions.item(i);
            String identifier = propertyDefinition.getAttribute("identifier");
            if (identifier.equals(propertyDefinitionRef)) {
                return propertyDefinition.getElementsByTagName("name").item(0).getTextContent();
            }
        }
        return null;
    }

    public static List<String> returnAllIncomingRelationships(Document archiMateModel, Element concept) {
        List<String> returnList = new ArrayList<>();

        NodeList realtionList = archiMateModel.getElementsByTagName("relationship");
        for (int i = 0; i < realtionList.getLength(); i++) {
            Element relationship = (Element) realtionList.item(i);
            if (relationship.getAttribute("target").equals(concept.getAttribute("identifier"))) {
                returnList.add(
                        findElementById(archiMateModel, relationship.getAttribute("source")).getAttribute("xsi:type"));
            }
        }

        return returnList;
    }

}
