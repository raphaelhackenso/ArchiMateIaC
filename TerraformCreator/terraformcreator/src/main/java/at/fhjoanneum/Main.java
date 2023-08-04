package at.fhjoanneum;

import org.apache.jena.rdf.model.Model;
import java.util.ArrayList;
import java.util.List;
import org.javatuples.Pair;

import java.io.*;
import java.io.IOException;

/**
 * Create Terraform HCL from Ontology
 * 
 * @param args [OntologyWithIndividuals.ttl]
 * 
 */
public class Main {
    public static void main(String[] args) throws Exception {
        // Check for correct number of Parameters
        if (args.length != 1) {
            Main.printUsage();
            System.exit(1);
        }

        Model IndividualOntology = OntologyApacheJena.getOntologyTTL(args[0]);
        String terraformFile = args[0].replace(".ttl", ".tf");

        List<Pair<String, String>> allIndividualsAndClasses = OntologyApacheJena.returnIndividualsAndClasses(
                IndividualOntology, SPARQLTerraformSelectQueries.returnAllIndividualsAndClasses());

        createTerraformAndAddHeader(terraformFile, TerraformHCL.createHeader());

        for (Pair<String, String> singleIndvidualAndClass : allIndividualsAndClasses) {
            if (OntologyApacheJena.askCountSPARQLQuery(IndividualOntology, SPARQLTerraformSelectQueries
                    .askIfIndividualClassIsAResourceSchema(singleIndvidualAndClass.getValue0())) == 0) {

                String individual = singleIndvidualAndClass.getValue0();
                String className = singleIndvidualAndClass.getValue1();

                // all direct attributes
                List<Pair<String, String>> allAttributesAndValues = OntologyApacheJena
                        .returnDataPropertiesAndTheirValues(IndividualOntology,
                                SPARQLTerraformSelectQueries.returnDataPropertiesAndValuesForIndividual(
                                        individual, className));

                // all BlockTypes
                List<String> allBlockTypes = OntologyApacheJena.returnConnectedBlockTypeIndividuals(IndividualOntology,
                        SPARQLTerraformSelectQueries.returnAllConnectedBlockTypeIndividuals(individual, className));

                List<Pair<String, List<Pair<String, String>>>> blockTypesAndAttributes = new ArrayList();

                for (String singleBlockType : allBlockTypes) {
                    String classForBlocktype = findTerraformClass(singleBlockType, allIndividualsAndClasses);
                    List<Pair<String, String>> blockTypeAttributesAndValues = OntologyApacheJena
                            .returnDataPropertiesAndTheirValues(IndividualOntology,
                                    SPARQLTerraformSelectQueries.returnDataPropertiesAndValuesForIndividual(
                                            singleBlockType, classForBlocktype));

                    blockTypesAndAttributes.add(new Pair(classForBlocktype, blockTypeAttributesAndValues));
                }

                appendTerraform(terraformFile,
                        TerraformHCL.createResource(className, individual, allAttributesAndValues, blockTypesAndAttributes));
            }
        }

        System.out.println("SUCCESS - Created a Terraform File from the Ontology");

    }

    public static void createTerraformAndAddHeader(String terraformFile, String header) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(terraformFile)))) {
            writer.write(header);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void appendTerraform(String terraformFile, String resource) {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(terraformFile, true)))) {
            writer.write(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String findTerraformClass(String indivudal, List<Pair<String, String>> allIndividualsAndClasses) {
        String result = "";
        for (Pair<String, String> individualAndClass : allIndividualsAndClasses) {
            if (individualAndClass.getValue0().equals(indivudal)) {
                result = individualAndClass.getValue1();
            }
        }
        return result;
    }

    public static void printUsage() {
        System.err.println();
        System.err.println(
                "usage: java -jar terraformcreator-X.Y-jar-with-dependencies [OntologyWithIndividuals.ttl]");
        System.err.println();
    }

    public static void abort(String reason) {
        System.err.println("ERROR");
        System.err.println(reason);
        System.exit(1);
    }
}
