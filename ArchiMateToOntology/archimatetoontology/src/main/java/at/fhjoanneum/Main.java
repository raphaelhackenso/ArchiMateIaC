package at.fhjoanneum;

import org.apache.jena.rdf.model.Model;
import org.w3c.dom.*;
import java.io.*;
import java.io.IOException;
import org.apache.commons.lang3.math.NumberUtils;
import org.javatuples.Triplet;
import org.javatuples.Pair;
import java.util.ArrayList;
import java.util.List;

/**
 * Convert a valid ArchiMate.xml Model into an Ontology
 * 
 * @param args [ArchiMateModel.xml] [ArchiMateIaCOntology.ttl]
 */
public class Main {

    public static void main(String[] args) throws Exception {
        // Check for correct number of Parameters
        if (args.length != 2) {
            Main.printUsage();
            System.exit(1);
        }

        // Load files
        Document archiMateModel = XMLLoader.getXMLDocument(args[0]);
        String modelOntologyPath = (args[0].replace(".xml", "") + ".ttl");
        copyOntology(args[1], modelOntologyPath);
        Model modelOntology = OntologyApacheJena.getOntologyTTL(modelOntologyPath);

        NodeList elemList = archiMateModel.getElementsByTagName("element");

        for (int i = 0; i < elemList.getLength(); i++) {
            Node elementNode = elemList.item(i);
            if (elementNode.getNodeType() == Node.ELEMENT_NODE) {
                Element elementElement = (Element) elementNode;

                String individualName = elementElement.getElementsByTagName("name").item(0).getTextContent();

                String queryTerraformClassForArchiMateClass = SPARQLArchiMateIaCQueries
                        .queryTerraformClassForArchimateClass(elementElement.getAttribute("xsi:type"));
                String terraformClassForArchiMateClass = OntologyApacheJena.returnTerraformClassForArchiMateClass(
                        modelOntology, queryTerraformClassForArchiMateClass);

                // Insert direct Attributes
                OntologyApacheJena.executeInsertData(modelOntology, SPARQLInsertQueries
                        .insertQueryCreateIndividual(individualName, terraformClassForArchiMateClass));

                NodeList properties = elementElement.getElementsByTagName("property");

                List<Triplet<String, String, String>> propertiesTriplet = new ArrayList<>();
                List<Triplet<String, String, String>> blockTypeTriplet = new ArrayList<>();

                for (int k = 0; k < properties.getLength(); k++) {
                    Node propertyNode = properties.item(k);
                    if (propertyNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element propertyElement = (Element) propertyNode;

                        String propertyName = XMLLoader.getPropertyName(archiMateModel,
                                propertyElement.getAttribute("propertyDefinitionRef"));
                        String propertyValue = propertyElement.getElementsByTagName("value").item(0).getTextContent();

                        String propertyType = "xsd:string";

                        if (propertyValue.equals("true") || propertyValue.equals("false")) {
                            propertyType = "xsd:boolean";
                        }

                        if (propertyValue.contains("[") && propertyValue.contains("]")) {
                            propertyType = "rdf:list";
                        }

                        if (NumberUtils.isCreatable(propertyValue) == true) {
                            propertyType = "xsd:float";
                        }

                        if (propertyName.substring(0, 1).equals("/") == true) {
                            blockTypeTriplet.add(
                                    new Triplet<String, String, String>(propertyName, propertyType, propertyValue.replace("\"", "\\\"")));
                        } else {
                            propertiesTriplet.add(
                                    new Triplet<String, String, String>(propertyName, propertyValue.replace("\"", "\\\""), propertyType));
                        }

                    }
                }

                createDirectAttributes(modelOntology, individualName, propertiesTriplet);
                createBlockTypes(modelOntology, individualName, blockTypeTriplet);
                createAttributesForBlockTypes(modelOntology, individualName, blockTypeTriplet);

            }
        }

        OntologyApacheJena.saveOntology(modelOntology, modelOntologyPath);
        System.out.println("SUCCESS - ArchiMate XML was converted into an ontology");

    }

    public static void createDirectAttributes(Model modelOntology, String individualName,
            List<Triplet<String, String, String>> propertiesTriplet) {
        for (Triplet<String, String, String> singlepropertiesTriplet : propertiesTriplet) {
            OntologyApacheJena.executeInsertData(modelOntology,
                    SPARQLInsertQueries.insertQueryCreateDirectAttribute(individualName,
                            singlepropertiesTriplet.getValue0(), singlepropertiesTriplet.getValue1(),
                            singlepropertiesTriplet.getValue2()));
        }

    }

    public static void createBlockTypes(Model modelOntology, String individualName,
            List<Triplet<String, String, String>> blockTypePair) {
        List<Triplet<String, Integer, List<Triplet<String, String, String>>>> blockTypeTriplets = ListTransformer
                .transformBlockTypePair(blockTypePair);

        List<Pair<String, Integer>> blockTypeAndNumber = new ArrayList<Pair<String, Integer>>();
        for (Triplet<String, Integer, List<Triplet<String, String, String>>> singleblockTypeTriplet : blockTypeTriplets) {
            blockTypeAndNumber.add(
                    new Pair<String, Integer>(singleblockTypeTriplet.getValue0(), singleblockTypeTriplet.getValue1()));
        }

        List<Pair<String, Integer>> blockTypeIndividualsList = ListTransformer
                .findUniqueElementPair(blockTypeAndNumber);
        for (Pair<String, Integer> singleblockTypeIndividuals : blockTypeIndividualsList) {
            String blockTypeIndividualName = (individualName + "_" + singleblockTypeIndividuals.getValue0() + "_"
                    + singleblockTypeIndividuals.getValue1());
            OntologyApacheJena
                    .executeInsertData(modelOntology,
                            SPARQLInsertQueries.insertQueryCreateIndividual(blockTypeIndividualName,
                                    singleblockTypeIndividuals.getValue0()));

            OntologyApacheJena.executeInsertData(modelOntology,
                    SPARQLInsertQueries.insertQueryCreateConncetedBlockType(individualName, blockTypeIndividualName));
        }

    }

    public static void createAttributesForBlockTypes(Model modelOntology, String individualName,
            List<Triplet<String, String, String>> blockTypePair) {

        List<Triplet<String, Integer, List<Triplet<String, String, String>>>> blockTypeTriplets = ListTransformer
                .transformBlockTypePair(blockTypePair);

        for (Triplet<String, Integer, List<Triplet<String, String, String>>> singleblockTypeTriplets : blockTypeTriplets) {
            String blockTypeIndividualName = (individualName + "_" + singleblockTypeTriplets.getValue0() + "_"
                    + singleblockTypeTriplets.getValue1());
            for (Triplet<String, String, String> attributesForBlockType : singleblockTypeTriplets.getValue2()) {
                OntologyApacheJena.executeInsertData(modelOntology,
                        SPARQLInsertQueries.insertQueryCreateDirectAttribute(blockTypeIndividualName,
                                attributesForBlockType.getValue0(), attributesForBlockType.getValue2(),
                                attributesForBlockType.getValue1()));
            }
        }
    }

    public static void copyOntology(String origin, String copy) throws IOException {
        File originFile = new File(origin);
        File copyFile = new File(copy);

        try (FileInputStream inputStream = new FileInputStream(originFile);
                FileOutputStream outputStream = new FileOutputStream(copyFile)) {

            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
    }

    public static void printUsage() {
        System.err.println();
        System.err.println(
                "usage: java -jar archimatetoontology-X.Y-jar-with-dependencies.jar [ArchiMateModel.xml] [ArchiMateIaCOntology.ttl]");
        System.err.println();
    }

}
