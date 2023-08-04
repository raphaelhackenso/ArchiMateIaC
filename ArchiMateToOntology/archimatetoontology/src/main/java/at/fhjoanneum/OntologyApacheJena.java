package at.fhjoanneum;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.update.UpdateAction;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.RDFNode;
import java.io.*;


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


    public static void executeInsertData(Model model, String sparqlQueryString) {
        try {
            UpdateAction.parseExecute(sparqlQueryString, model);
        } catch (Exception e) {
            e.printStackTrace();
        }
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


    public static void saveOntology(Model model, String pathToTTL) {
        try (OutputStream out = new FileOutputStream(pathToTTL)) {
            RDFDataMgr.write(out, model, Lang.TTL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


   
}
