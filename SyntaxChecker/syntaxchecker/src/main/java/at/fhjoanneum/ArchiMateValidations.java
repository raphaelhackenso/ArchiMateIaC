package at.fhjoanneum;

import org.apache.commons.lang3.StringUtils;
import org.apache.jena.rdf.model.Model;
import org.w3c.dom.*;

public class ArchiMateValidations {

    public static void validateElements(Document archiMateModel, Model ArchiMateIaCOntologyCombined) {
        NodeList elemList = archiMateModel.getElementsByTagName("element");

        for (int i = 0; i < elemList.getLength(); i++) {
            Node elementNode = elemList.item(i);
            if (elementNode.getNodeType() == Node.ELEMENT_NODE) {
                Element elementElement = (Element) elementNode;
                String elementClass = elementElement.getAttribute("xsi:type");

                String sparqlQuery = SPARQLArchiMateQueries.queryDoesClassExist(elementClass);
                if (OntologyApacheJena.askBooleanSPARQLQuery(ArchiMateIaCOntologyCombined, sparqlQuery) == false) {
                    Main.abort("The Concept " + elementClass + " cannot be found in the ontology");
                }
            }
        }
    }

    public static void validateOpjectProperties(Document archiMateModel, Model ArchiMateIaCOntologyCombined) {
        NodeList realtionList = archiMateModel.getElementsByTagName("relationship");

        for (int i = 0; i < realtionList.getLength(); i++) {
            Node relationNode = realtionList.item(i);
            if (relationNode.getNodeType() == Node.ELEMENT_NODE) {
                Element relationElement = (Element) relationNode;
                String relationClass = relationElement.getAttribute("xsi:type");
                String sourceID = relationElement.getAttribute("source");
                String targetID = relationElement.getAttribute("target");
                if (relationClass.equals("Serving")) {
                    relationClass = "UsedBy";
                }

                System.out.println(relationClass);

                String sparqlQuery = SPARQLArchiMateQueries.queryDoesClassExist(relationClass);
                if (OntologyApacheJena.askBooleanSPARQLQuery(ArchiMateIaCOntologyCombined, sparqlQuery) == false) {
                    Main.abort("The Concept " + relationClass + " cannot be found in the ontology");
                }

                if (relationClass.equals("UsedBy")) {
                    relationClass = "used By";
                }

                if (relationClass.equals("Realization")) {
                    relationClass = "realisation";
                }

                String sparqlQueryOP = SPARQLArchiMateQueries.queryOPforRelationship(relationClass);
                if (OntologyApacheJena.returnRelationshipOPSPARQLQuery(ArchiMateIaCOntologyCombined,
                        sparqlQueryOP) == ""
                        || OntologyApacheJena.returnRelationshipOPSPARQLQuery(ArchiMateIaCOntologyCombined,
                                sparqlQueryOP) == null) {
                    Main.abort("The ObjectProperty " + relationClass + " cannot be found in the ontology");

                } else {
                    String relationConnection = StringUtils.substringAfterLast(OntologyApacheJena
                            .returnRelationshipOPSPARQLQuery(ArchiMateIaCOntologyCombined, sparqlQueryOP), "#");
                    String sourceClass = XMLLoader.findElementById(archiMateModel, sourceID).getAttribute("xsi:type");
                    String targetClass = XMLLoader.findElementById(archiMateModel, targetID).getAttribute("xsi:type");

                    String sparqlQueryIsDirectRelationAllowed = SPARQLArchiMateQueries
                            .queryIsDirectRelationAllowed(sourceClass, relationConnection, targetClass);
                    if (OntologyApacheJena.askBooleanSPARQLQuery(ArchiMateIaCOntologyCombined,
                            sparqlQueryIsDirectRelationAllowed) == false) {
                        Main.abort("The direct Relation " + relationConnection + " between " + sourceClass + " and "
                                + targetClass + " is not allowed.");
                    }

                }
            }
        }
    }

}
