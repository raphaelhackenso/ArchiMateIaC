package at.fhjoanneum;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Reduce the content of a given XML File
 * 
 * @param args [input.txt] [input.xml] [output.xml]
 */
import java.io.*;
import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;
import org.xml.sax.SAXException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Reduce the content of a given XML File
 * 
 * @param args [input.txt] [input.xml] [output.xml]
 */
public class ReduceXMLMulti {
    private static final int THREAD_COUNT = 4; // Number of threads to use

    public static void main(String[] args) {
        String firstFile = args[0];
        String secondFile = args[1];
        String outputFile = args[2];

        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new File(secondFile));

            //NodeList lines = readLinesFromFile(firstFile);
            List<String> lines = readTagsFromFile(firstFile);

            ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
            for (int i = 0; i < lines.getLength(); i++) {
                Node lineNode = lines.item(i);
                if (lineNode.getNodeType() == Node.ELEMENT_NODE) {
                    String tag = lineNode.getTextContent().replace("\"", "");
                    executor.submit(() -> removeTag(doc, tag));
                }
            }

            executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

            saveModifiedXML(doc, outputFile);
            System.out.println("Processing completed successfully!");
        } catch (ParserConfigurationException | SAXException | IOException | InterruptedException | TransformerException e) {
            ((Throwable) e).printStackTrace();
        }
    }

    private static NodeList readLinesFromFile(String filename) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(new File(filename));

        doc.getDocumentElement().normalize();
        return doc.getDocumentElement().getChildNodes();
    }

    private static List<String> readTagsFromFile(String filename) throws IOException {
        List<String> tags = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                tags.add(line.trim()); // Trim any leading/trailing spaces from each line
            }
        }

        return tags;
    }

    private static void removeTag(Document doc, String tag) {
        NodeList nodeList = doc.getElementsByTagName(tag);
        for (int i = nodeList.getLength() - 1; i >= 0; i--) {
            Node node = nodeList.item(i);
            node.getParentNode().removeChild(node);
        }
    }

    private static void saveModifiedXML(Document doc, String filename) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(filename));
        transformer.transform(source, result);
    }
}