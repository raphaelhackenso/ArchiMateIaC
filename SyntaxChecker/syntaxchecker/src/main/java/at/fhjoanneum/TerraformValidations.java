package at.fhjoanneum;

import org.apache.commons.lang3.math.NumberUtils;
import org.javatuples.Triplet;
import org.apache.jena.rdf.model.Model;
import org.javatuples.Pair;
import org.w3c.dom.*;
import java.util.ArrayList;
import java.util.List;

public class TerraformValidations {

    public static void validateSyntax(Document archiMateModel, Model ArchiMateIaCOntologyCombined) {
        NodeList elemList = archiMateModel.getElementsByTagName("element");

        for (int i = 0; i < elemList.getLength(); i++) {
            Node elementNode = elemList.item(i);
            if (elementNode.getNodeType() == Node.ELEMENT_NODE) {
                Element elementElement = (Element) elementNode;
                String elementName = elementElement.getElementsByTagName("name").item(0).getTextContent();
                String queryTerraformClassForArchiMateClass = SPARQLArchiMateIaCQueries
                        .queryTerraformClassForArchimateClass(elementElement.getAttribute("xsi:type"));

                String terraformClassForArchiMateClass = ArchiMateIaCValidations
                        .validateterraformClassForArchiMateClass(ArchiMateIaCOntologyCombined,
                                queryTerraformClassForArchiMateClass, elementElement.getAttribute("xsi:type"));

                ArchiMateIaCValidations.validateRequiresOtherTerraformResource(ArchiMateIaCOntologyCombined,
                        terraformClassForArchiMateClass,
                        elementName,
                        XMLLoader.returnAllIncomingRelationships(archiMateModel, elementElement));

                NodeList properties = elementElement.getElementsByTagName("property");

                List<Pair<String, String>> propertiesPair = new ArrayList<>();
                List<Pair<String, String>> blockTypePair = new ArrayList<>();

                for (int k = 0; k < properties.getLength(); k++) {
                    Node propertyNode = properties.item(k);
                    if (propertyNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element propertyElement = (Element) propertyNode;

                        String propertyName = XMLLoader.getPropertyName(archiMateModel,
                                propertyElement.getAttribute("propertyDefinitionRef"));
                        String propertyValue = propertyElement.getElementsByTagName("value").item(0).getTextContent();

                        String propertyType = "string";

                        if (propertyValue.equals("true") || propertyValue.equals("false")) {
                            propertyType = "boolean";
                        }

                        if (propertyValue.contains("[") && propertyValue.contains("]")) {
                            propertyType = "list";
                        }

                        if (NumberUtils.isCreatable(propertyValue) == true) {
                            propertyType = "float";
                        }

                        if (propertyName.substring(0, 1).equals("/") == true) {
                            blockTypePair.add(new Pair<String, String>(propertyName, propertyType));
                        } else {
                            propertiesPair.add(new Pair(propertyName, propertyType));
                        }

                    }
                }

                validateAttributes(ArchiMateIaCOntologyCombined, terraformClassForArchiMateClass, propertiesPair, elementName);
                validateBlockTypes(ArchiMateIaCOntologyCombined, terraformClassForArchiMateClass, blockTypePair, elementName);
                validateAttibutesForBlockTypes(ArchiMateIaCOntologyCombined, terraformClassForArchiMateClass,
                        blockTypePair, elementName);

            }
        }

    }

    public static void validateAttributes(Model ArchiMateIaCOntologyCombined, String terraformClassForArchiMateClass,
            List<Pair<String, String>> propertiesPair, String elementName) {
        String sparqlQueryRequiredAttributes = SPARQLTerraformQueries
                .queryRequiredAttributes(terraformClassForArchiMateClass);
        List<Pair<String, String>> requiredAttributesPair = OntologyApacheJena
                .returnAttributes(ArchiMateIaCOntologyCombined, sparqlQueryRequiredAttributes);

        for (Pair<String, String> singleRequiredAttribute : requiredAttributesPair) {

            if (propertiesPair.contains(singleRequiredAttribute) == true) {
                propertiesPair.remove(singleRequiredAttribute);
            } else {
                Main.abort(elementName + " as a Class: " + terraformClassForArchiMateClass + " requires an Attribute: "
                        + singleRequiredAttribute.getValue0() + " of Type: "
                        + singleRequiredAttribute.getValue1() + "\nThe required attributes are: "
                        + requiredAttributesPair);
            }
        }

        String sparqlQueryOptionalAttributes = SPARQLTerraformQueries
                .queryOptionalAttributes(terraformClassForArchiMateClass);
        List<Pair<String, String>> optionalAttributesPair = OntologyApacheJena
                .returnAttributes(ArchiMateIaCOntologyCombined, sparqlQueryOptionalAttributes);

        for (Pair<String, String> singleOptionalProperty : propertiesPair) {
            if (optionalAttributesPair.contains(singleOptionalProperty) == false) {
                Main.abort(elementName + " as a Class: " + terraformClassForArchiMateClass
                        + " does not have an optional Attribute: " + singleOptionalProperty.getValue0()
                        + " of Type: "
                        + singleOptionalProperty.getValue1() + "\nThe optional attributes are: "
                        + optionalAttributesPair);
            }
        }
    }

    public static void validateBlockTypes(Model ArchiMateIaCOntologyCombined, String terraformClassForArchiMateClass,
            List<Pair<String, String>> blockTypePair, String elementName) {

        List<Triplet<String, Integer, List<Pair<String, String>>>> blockTypeTriplets = ListTransformer
                .transformBlockTypePair(blockTypePair);

        String sparqlQueryMinimalBlockTypes = SPARQLTerraformQueries
                .queryMinimalBlockTypes(terraformClassForArchiMateClass);
        List<Pair<String, Integer>> minimalBlockTypesPair = OntologyApacheJena
                .returnMinOrMaxBlockTypes(ArchiMateIaCOntologyCombined, sparqlQueryMinimalBlockTypes, "min");

        for (Pair<String, Integer> singleminimalBlockTypePair : minimalBlockTypesPair) {
            Integer occurancesOfBlockType = ListTransformer.findHighestBlockTypeNumber(blockTypeTriplets,
                    singleminimalBlockTypePair.getValue0());
            if (occurancesOfBlockType < singleminimalBlockTypePair.getValue1()) {
                Main.abort(elementName + " as a Class:" + terraformClassForArchiMateClass + " requires a least "
                        + singleminimalBlockTypePair.getValue1() + " Blocktype: " +
                        singleminimalBlockTypePair.getValue0() + "\nHowever only " + occurancesOfBlockType
                        + " of Blocktype: " + singleminimalBlockTypePair.getValue0() + " were provided" +
                        "\nThe minimal blocktypes are " + minimalBlockTypesPair);
            }
        }

        String sparqlQueryMaximalBlockTypes = SPARQLTerraformQueries
                .queryMaximalBlockTypes(terraformClassForArchiMateClass);
        List<Pair<String, Integer>> maximalBlockTypesPair = OntologyApacheJena
                .returnMinOrMaxBlockTypes(ArchiMateIaCOntologyCombined, sparqlQueryMaximalBlockTypes, "max");

        for (Pair<String, Integer> singlemaximalBlockTypePair : maximalBlockTypesPair) {
            Integer occurancesOfBlockType = ListTransformer.findHighestBlockTypeNumber(blockTypeTriplets,
                    singlemaximalBlockTypePair.getValue0());
            if (occurancesOfBlockType > singlemaximalBlockTypePair.getValue1()) {
                Main.abort(elementName + " as a Class:" + terraformClassForArchiMateClass + " requires a most "
                        + singlemaximalBlockTypePair.getValue1() + " Blocktype: " +
                        singlemaximalBlockTypePair.getValue0() + "\nHowever " + occurancesOfBlockType
                        + " of Blocktype: " + singlemaximalBlockTypePair.getValue0() + " were provided" +
                        "\nThe maximal blocktypes are " + maximalBlockTypesPair);
            }
        }

        String sparqlQueryOptionalBlockTypes = SPARQLTerraformQueries
                .queryOptionalBlockTypes(terraformClassForArchiMateClass);
        List<String> optionalBlockTypesList = OntologyApacheJena.returnOptionalBlockTypes(ArchiMateIaCOntologyCombined,
                sparqlQueryOptionalBlockTypes);

        List<String> providedBlockTypes = ListTransformer.findUniqueElementTriple(blockTypeTriplets);
        List<String> uniqueMinimalBlockTypes = ListTransformer.findUniqueElementPair(minimalBlockTypesPair);
        List<String> uniqueMaximalBlockTypes = ListTransformer.findUniqueElementPair(maximalBlockTypesPair);

        for (String singleuniqueMinimalBlockType : uniqueMinimalBlockTypes) {
            providedBlockTypes.remove(singleuniqueMinimalBlockType);
        }

        for (String singleuniqueMaximalBlockType : uniqueMaximalBlockTypes) {
            providedBlockTypes.remove(singleuniqueMaximalBlockType);
        }

        for (String singleProvidedBlockType : providedBlockTypes) {
            if (optionalBlockTypesList.contains(singleProvidedBlockType) == false) {
                Main.abort(elementName + " as a Class: " + terraformClassForArchiMateClass + " does not have an optional BlockType: "
                        + singleProvidedBlockType +
                        "\nThe optional BlockTypes are: " + optionalBlockTypesList);
            }
        }
    }

    public static void validateAttibutesForBlockTypes(Model ArchiMateIaCOntologyCombined,
            String terraformClassForArchiMateClass,
            List<Pair<String, String>> blockTypePair, String elementName) {

        List<Triplet<String, Integer, List<Pair<String, String>>>> blockTypeTriplets = ListTransformer
                .transformBlockTypePair(blockTypePair);

        for (Triplet<String, Integer, List<Pair<String, String>>> singleBlockTypeTriplet : blockTypeTriplets) {

            String sparqlQueryRequiredAttributesForBlockType = SPARQLTerraformQueries
                    .queryRequiredAttributesForBlockType(singleBlockTypeTriplet.getValue0(),
                            terraformClassForArchiMateClass);

            List<Pair<String, String>> requiredAttributesPairForBlockType = OntologyApacheJena
                    .returnAttributes(ArchiMateIaCOntologyCombined, sparqlQueryRequiredAttributesForBlockType);

            for (Pair<String, String> singleRequiredAttribute : requiredAttributesPairForBlockType) {

                if (singleBlockTypeTriplet.getValue2().contains(singleRequiredAttribute) == true) {
                    singleBlockTypeTriplet.getValue2().remove(singleRequiredAttribute);
                } else {
                    Main.abort("The Blocktype: " + singleBlockTypeTriplet.getValue0() + "for the " + elementName + " as a Class: "
                            + terraformClassForArchiMateClass + " requires an Attribute: "
                            + singleRequiredAttribute.getValue0() + " of Type: "
                            + singleRequiredAttribute.getValue1()
                            + "\nThe required attributes for this given Class-Blocktype are: "
                            + requiredAttributesPairForBlockType);
                }
            }

            String sparqlQueryOptionalAttributesForBlockType = SPARQLTerraformQueries
                    .queryOptionalAttributesForBlockType(singleBlockTypeTriplet.getValue0(),
                            terraformClassForArchiMateClass);

            List<Pair<String, String>> optionalAttributesPairForBlockType = OntologyApacheJena
                    .returnAttributes(ArchiMateIaCOntologyCombined, sparqlQueryOptionalAttributesForBlockType);

            for (Pair<String, String> singleOptionalAttribute : singleBlockTypeTriplet.getValue2()) {
                if (optionalAttributesPairForBlockType.contains(singleOptionalAttribute) == false) {
                    Main.abort("The Blocktype: " + singleBlockTypeTriplet.getValue0() + " for the " + elementName + " as a Class: "
                            + terraformClassForArchiMateClass + " does not have an optional Attribute: "
                            + singleOptionalAttribute.getValue0()
                            + " of Type: "
                            + singleOptionalAttribute.getValue1()
                            + "\nThe optional attributes for this given Class-Blocktype are: "
                            + optionalAttributesPairForBlockType);
                }
            }

        }

    }

}
