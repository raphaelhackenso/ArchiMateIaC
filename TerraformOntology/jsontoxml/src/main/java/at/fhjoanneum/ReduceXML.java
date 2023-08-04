package at.fhjoanneum;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;
import org.xml.sax.SAXException;
import java.util.ArrayList;
import java.util.List;



/**
 * Reduce the content of a given XML File
 * 
 * @param args [input.txt] [input.xml] [output.xml]
 */
public class ReduceXML {
    public static void main(String[] args) {
        
        String firstFile = args[0];
        String secondFile = args[1];
        String outputFile = args[2];

        try {
            List<String> tags = readTagsFromFile(firstFile);

            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(secondFile);

            for (String tag : tags) {
                NodeList nodeList = doc.getElementsByTagName(tag);
                for (int i = nodeList.getLength() - 1; i >= 0; i--) {
                    Node node = nodeList.item(i);
                    node.getParentNode().removeChild(node);
                }
            }

            saveModifiedXML(doc, outputFile);
            System.out.println("Processing completed successfully!");
        } catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
            e.printStackTrace();
        }
    }

    private static List<String> readTagsFromFile(String filename) throws IOException {
        List<String> tags = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                tags.add(line.trim().substring(1, line.length() - 1)); // Trim any leading/trailing spaces from each line
            }
        }

        return tags;
    }

    private static void saveModifiedXML(Document doc, String filename) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(filename);
        transformer.transform(source, result);
    }
}