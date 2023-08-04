package at.fhjoanneum;

import org.apache.jena.rdf.model.Model;
import org.javatuples.Pair;
import java.util.ArrayList;
import java.util.List;

public class ArchiMateIaCValidations {

    public static String validateterraformClassForArchiMateClass(Model ArchiMateIaCOntologyCombined,
            String queryTerraformClassForArchiMateClass, String element) {
        String returnString = "";
        String terraformClassForArchiMateClass = OntologyApacheJena.returnTerraformClassForArchiMateClass(
                ArchiMateIaCOntologyCombined, queryTerraformClassForArchiMateClass);

        if (terraformClassForArchiMateClass.equals("")) {
            Main.abort("The ArchiMate Class: " + element
                    + " does not have a semantically annotated Terraform Class");
        } else {
            returnString = terraformClassForArchiMateClass;
        }

        return returnString;

    }

    public static void validateRequiresOtherTerraformResource(Model ArchiMateIaCOntologyCombined,
            String terraformClassForArchiMateClass, String conceptName, List<String> sourceConcepts) {

        List<Pair<String, Integer>> classesAndCardinality = OntologyApacheJena.returnRequiredOtherTerraformClasses(
                ArchiMateIaCOntologyCombined,
                SPARQLArchiMateIaCQueries.queryRequiredOtherTerrafromResources(terraformClassForArchiMateClass));

        List<String> sourceTerraformConcepts = new ArrayList<>();
        for (String singlesSourceConcept : sourceConcepts) {
            sourceTerraformConcepts.add(validateterraformClassForArchiMateClass(ArchiMateIaCOntologyCombined,
                    SPARQLArchiMateIaCQueries.queryTerraformClassForArchimateClass(singlesSourceConcept),
                    singlesSourceConcept));
        }

        for (Pair<String, Integer> singleClassAndCardinality : classesAndCardinality) {
            int numberOfCoccurances = ListTransformer.getNumberOfOccurances(sourceTerraformConcepts,
                    singleClassAndCardinality.getValue0());
            if (singleClassAndCardinality.getValue1() != numberOfCoccurances) {
                Main.abort(conceptName + " requires exactly " + singleClassAndCardinality.getValue1()
                        + " incoming Terraform Concept(s): " + singleClassAndCardinality.getValue0() + " but received: "
                        + numberOfCoccurances + "\nAll the required incomming relationships are:"
                        + classesAndCardinality);
            }
        }
    }

}
