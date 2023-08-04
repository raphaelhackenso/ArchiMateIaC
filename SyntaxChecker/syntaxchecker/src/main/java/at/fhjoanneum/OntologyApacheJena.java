package at.fhjoanneum;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.javatuples.Pair;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import java.util.ArrayList;
import java.util.List;



public class OntologyApacheJena {
    public static Model getOntologyTTL(String pathToTTL) throws Exception {
        try {
            // Load the ontology from the TTL file
            Model model = ModelFactory.createDefaultModel();
            RDFDataMgr.read(model, pathToTTL, Lang.TURTLE);
            return model;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean askBooleanSPARQLQuery(Model model, String sparqlQueryString) {
        try (QueryExecution queryExecution = QueryExecutionFactory.create(sparqlQueryString, model)) {
            return queryExecution.execAsk();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String returnRelationshipOPSPARQLQuery(Model model, String sparqlQueryString) {
        try (QueryExecution queryExecution = QueryExecutionFactory.create(sparqlQueryString, model)) {
            ResultSet resultSet = queryExecution.execSelect();

            while (resultSet.hasNext()) {
                QuerySolution soln = resultSet.nextSolution();
                RDFNode subject = soln.get("relationshipOP");

                if (subject.isResource()) {
                    Resource subjectResource = subject.asResource();
                    return subjectResource.toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static List<Pair<String, String>> returnAttributes(Model model, String sparqlQueryString) {
        List<Pair<String, String>> requiredAttributes = new ArrayList<>();

        try (QueryExecution queryExecution = QueryExecutionFactory.create(sparqlQueryString, model)) {
            ResultSet resultSet = queryExecution.execSelect();

            while (resultSet.hasNext()) {
                QuerySolution soln = resultSet.nextSolution();
                RDFNode objectPropertyNode = soln.get("objectProperty");
                RDFNode typeNode = soln.get("type");

                if (objectPropertyNode.isURIResource() && typeNode.isURIResource()) {
                    String objectProperty = objectPropertyNode.asResource().getLocalName();
                    String type = typeNode.asResource().getLocalName();
                    requiredAttributes.add(new Pair(objectProperty.replace("_dp", ""), type));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requiredAttributes;
    }

    public static String returnTerraformClassForArchiMateClass(Model model, String sparqlQueryString) {
        String result = "";
        try (QueryExecution queryExecution = QueryExecutionFactory.create(sparqlQueryString, model)) {
            ResultSet resultSet = queryExecution.execSelect();

            while (resultSet.hasNext()) {
                QuerySolution soln = resultSet.nextSolution();
                RDFNode terraformNode = soln.get("terraformClass");

                if (terraformNode.isURIResource()) {
                    result = terraformNode.asResource().getLocalName();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List<Pair<String, Integer>> returnMinOrMaxBlockTypes(Model model, String sparqlQueryString,
            String minMax) {
        List<Pair<String, Integer>> minMaxBlockTypes = new ArrayList<>();

        try (QueryExecution queryExecution = QueryExecutionFactory.create(sparqlQueryString, model)) {
            ResultSet resultSet = queryExecution.execSelect();

            while (resultSet.hasNext()) {
                QuerySolution soln = resultSet.nextSolution();
                RDFNode blockTypeNode = soln.get("blockType");
                RDFNode cardinalityNode;
                if (minMax.equals("min")) {
                    cardinalityNode = soln.get("minCardinality");
                } else {
                    cardinalityNode = soln.get("maxCardinality");
                }

                if (blockTypeNode.isURIResource()) {
                    String blockType = blockTypeNode.asResource().getLocalName();
                    String cardinality = cardinalityNode.toString();
                    minMaxBlockTypes.add(
                            new Pair(blockType, Integer.parseInt(cardinality.substring(0, cardinality.indexOf("^")))));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return minMaxBlockTypes;
    }

    public static List<String> returnOptionalBlockTypes(Model model, String sparqlQueryString) {
        List<String> optionalBlockTypes = new ArrayList<>();
        try (QueryExecution queryExecution = QueryExecutionFactory.create(sparqlQueryString, model)) {
            ResultSet resultSet = queryExecution.execSelect();

            while (resultSet.hasNext()) {
                QuerySolution soln = resultSet.nextSolution();
                RDFNode blockTypeNode = soln.get("blockType");

                if (blockTypeNode.isURIResource()) {
                    String blockType = blockTypeNode.asResource().getLocalName();
                    optionalBlockTypes.add(blockType);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return optionalBlockTypes;
    }

    public static List<Pair<String, Integer>> returnRequiredOtherTerraformClasses(Model model, String sparqlQueryString) {
        List<Pair<String, Integer>> classesAndCardinality = new ArrayList<>();

        try (QueryExecution queryExecution = QueryExecutionFactory.create(sparqlQueryString, model)) {
            ResultSet resultSet = queryExecution.execSelect();

            while (resultSet.hasNext()) {
                QuerySolution soln = resultSet.nextSolution();
                RDFNode terraformClassNode = soln.get("terraformClass");
                RDFNode cardinalityNode = soln.get("cardinality");

                if (terraformClassNode.isURIResource()) {
                    String terraformClass = terraformClassNode.asResource().getLocalName();
                    String cardinality = cardinalityNode.toString();
                    classesAndCardinality.add(
                            new Pair(terraformClass, Integer.parseInt(cardinality.substring(0, cardinality.indexOf("^")))));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return classesAndCardinality;
    }
}
