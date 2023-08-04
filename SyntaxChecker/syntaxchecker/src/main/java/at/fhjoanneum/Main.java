package at.fhjoanneum;

import org.apache.jena.rdf.model.Model;
import org.w3c.dom.*;


/**
 * Evaluate Syntax of ArchiMate Model XML according to the ArchiMateIaCOntology
 * 
 * @param args [ArchiMateModel.xml] [ArchiMateIaCOntologyCombined.ttl] archi ||
 *             terra
 */
public class Main {

    
    public static void main(String[] args) throws Exception {
        // Check for correct number of Parameters
        if (args.length != 3) {
            Main.printUsage();
            System.exit(1);
        }

        // Load files
        Document archiMateModel = XMLLoader.getXMLDocument(args[0]);
        Model ArchiMateIaCOntologyCombined = OntologyApacheJena.getOntologyTTL(args[1]);

        if (args[2].equals("archi")) {
            ArchiMateValidations.validateElements(archiMateModel, ArchiMateIaCOntologyCombined);
            ArchiMateValidations.validateOpjectProperties(archiMateModel, ArchiMateIaCOntologyCombined);
            System.out.println("SUCCESS - The ArchiMate Model is syntactically VALID");


        } else if (args[2].equals("terra")) {
            ArchiMateValidations.validateElements(archiMateModel, ArchiMateIaCOntologyCombined);
            ArchiMateValidations.validateOpjectProperties(archiMateModel, ArchiMateIaCOntologyCombined);

            TerraformValidations.validateSyntax(archiMateModel, ArchiMateIaCOntologyCombined);
            System.out.println("SUCCESS - The ArchiMate Model is syntactically VALID and can be deployed with Terraform");

        } else {
            System.err.println("Please enter either \"archi\" or \"terra\" as third parameter.");
            System.exit(1);
        }

    }

    public static void printUsage() {
        System.err.println();
        System.err.println(
                "usage: java -jar syntaxChecker-X.Y-jar-with-dependencies [ArchiMateModel.xml] [ArchiMateIaCOntologyCombined.ttl] archi || terra");
        System.err.println("Enter archi to only validate the Syntax of the model for ArchiMate");
        System.err.println("Enter terra to validate the Syntax of the model for ArchiMate and also Terraform");
        System.err.println();
    }

    public static void abort(String reason) {
        System.err.println("ERROR");
        System.err.println("The ArchiMate Model is syntactically INVALID, due to the following reason:");
        System.err.println(reason);
        System.exit(1);
    }
}