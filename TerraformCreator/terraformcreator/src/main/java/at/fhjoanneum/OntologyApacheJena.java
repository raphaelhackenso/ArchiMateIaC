package at.fhjoanneum;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.RDFNode;

import java.util.ArrayList;
import java.util.List;
import org.javatuples.Pair;

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

    public static int askCountSPARQLQuery(Model model, String sparqlQueryString) {
        int returnNumber = 0;
        try (QueryExecution queryExecution = QueryExecutionFactory.create(sparqlQueryString, model)) {
            ResultSet resultSet = queryExecution.execSelect();
            if(resultSet.hasNext()){
                returnNumber++;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnNumber;
    }

    public static List<Pair<String, String>> returnIndividualsAndClasses(Model model, String sparqlQueryString) {
        List<Pair<String, String>> individualsAndClasses = new ArrayList<>();

        try (QueryExecution queryExecution = QueryExecutionFactory.create(sparqlQueryString, model)) {
            ResultSet resultSet = queryExecution.execSelect();

            while (resultSet.hasNext()) {
                QuerySolution soln = resultSet.nextSolution();
                RDFNode individualNode = soln.get("individual");
                RDFNode classNode = soln.get("class");

                if (individualNode.isURIResource() && classNode.isURIResource()) {
                    String individual = individualNode.asResource().getLocalName();
                    String classForIndividual = classNode.asResource().getLocalName();
                    individualsAndClasses.add(new Pair(individual, classForIndividual));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return individualsAndClasses;
    }

    public static List<Pair<String, String>> returnDataPropertiesAndTheirValues(Model model, String sparqlQueryString) {
        List<Pair<String, String>> dataPropertiesAndValues = new ArrayList<>();

        try (QueryExecution queryExecution = QueryExecutionFactory.create(sparqlQueryString, model)) {
            ResultSet resultSet = queryExecution.execSelect();

            while (resultSet.hasNext()) {
                QuerySolution soln = resultSet.nextSolution();
                RDFNode dataPropertyNode = soln.get("dataproperty");
                RDFNode valuesNode = soln.get("value");

                if (dataPropertyNode.isURIResource()) {
                    String dataProperty = dataPropertyNode.asResource().getLocalName();
                    String value = valuesNode.toString();

                    if(value.indexOf("^") != -1){
                        value = value.substring(0, value.indexOf("^"));
                    }

                    dataPropertiesAndValues.add(
                            new Pair(dataProperty.replace("_dp", ""), value.replace("\\", "")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataPropertiesAndValues;
    }

    public static List<String> returnConnectedBlockTypeIndividuals(Model model, String sparqlQueryString) {
        List<String> connectedBlockTypes = new ArrayList<>();
        try (QueryExecution queryExecution = QueryExecutionFactory.create(sparqlQueryString, model)) {
            ResultSet resultSet = queryExecution.execSelect();

            while (resultSet.hasNext()) {
                QuerySolution soln = resultSet.nextSolution();
                RDFNode blockTypeNode = soln.get("blocktype");

                if (blockTypeNode.isURIResource()) {
                    String blockType = blockTypeNode.asResource().getLocalName();
                    connectedBlockTypes.add(blockType);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connectedBlockTypes;
    }
}
