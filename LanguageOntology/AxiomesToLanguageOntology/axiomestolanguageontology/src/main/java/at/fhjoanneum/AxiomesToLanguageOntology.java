package at.fhjoanneum;

import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;

import java.io.*;
import java.io.IOException;

import org.semanticweb.owlapi.model.*;

/**
 * Adds all direct relationship axiomes to the ArchiMate Language Ontology
 * 
 * @param args [ArchiMate.ttl] [allDirectAxiomes.xml] [ArchiMateSyntax.ttl]
 */
public class AxiomesToLanguageOntology {

    // Main function
    public static void main(String[] args) throws Exception {
        // Check for correct number of Parameters
        if (args.length != 3) {
            AxiomesToLanguageOntology.printUsage();
            System.exit(1);
        }

        // Set the variables according to the parameters
        String inputLanguageOntologyString = args[0];
        String inputAxiomesString = args[1];
        String outputLanguageOntologyString = args[2];

        try {

            // TODO what this does
            OntologyUpdater updater = new OntologyUpdater(inputLanguageOntologyString);

            // Load the XML file
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(inputAxiomesString);
            doc.getDocumentElement().normalize();

            // Retrieve the <axiome> elements
            NodeList axiomeList = doc.getElementsByTagName("axiome");
            for (int i = 0; i < axiomeList.getLength(); i++) {
                Node axiomeNode = axiomeList.item(i);
                if (axiomeNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element axiomeElement = (Element) axiomeNode;
                    String sourceConcept = getElementValue(axiomeElement, "sourceConcept");
                    String type = getElementValue(axiomeElement, "type");
                    String cardinality = getElementValue(axiomeElement, "cardinality");
                    String targetConcepts = getElementValue(axiomeElement, "targetConcepts");

                    // TODO
                    //System.out.println("sourceConcept: " + sourceConcept + "; type: " + type + "; cardinality: "
                            //+ cardinality + "; targetConcepts: " + targetConcepts);

                    // TODO
                    //updater.addClassExpression("http://ikm-group.ch/archiMEO/archimate#", sourceConcept, type,
                    //        cardinality, targetConcepts);
                    updater.addClassExpression("http://github.com/raphaelhackenso/ArchiMateIaC/TerraformOntology#", sourceConcept, type,
                            cardinality, targetConcepts);
                }
            }

            updater.saveOntology(outputLanguageOntologyString);
            System.out.println("Class expressions added and ontology saved successfully!");

        } catch (OWLOntologyCreationException | OWLOntologyStorageException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * TODO what this does
     */
    private static String getElementValue(Element parentElement, String tagName) {
        Node node = parentElement.getElementsByTagName(tagName).item(0);
        return node.getTextContent().trim();
    }

    /**
     * Print the usage for adding axiomes to the ArchiMate Language Ontology
     */
    public static void printUsage() {
        System.err.println();
        System.err.println(
                "usage: java -jar axiomestolanguageOntology-X.Y-jar-with-dependencies [ArchiMate3_1_update.ttl] [allDirectAxiomes.xml] [ArchiMateSyntax.ttl]");
        System.err.println();
    }

}
