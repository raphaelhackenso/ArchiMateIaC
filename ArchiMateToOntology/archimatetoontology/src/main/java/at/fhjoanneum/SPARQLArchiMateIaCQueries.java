package at.fhjoanneum;

public class SPARQLArchiMateIaCQueries {

    public static String nl = System.getProperty("line.separator");
    public static String prefixes = String.join(nl,
            "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>",
            "PREFIX owl:      <http://www.w3.org/2002/07/owl#>",
            "PREFIX rdf:      <http://www.w3.org/1999/02/22-rdf-syntax-ns#>",
            "PREFIX rdfs:     <http://www.w3.org/2000/01/rdf-schema#>",
            "PREFIX rdfsplus: <http://topbraid.org/spin/rdfsplus#>",
            "PREFIX archi: <http://ikm-group.ch/archiMEO/archimate#>",
            "PREFIX terra: <http://archimateiac.raphaelhackenso.at/TerraformOntology#>",
            "PREFIX archimateiac: <http://archimateiac.raphaelhackenso.at/ArchiMateIaCOntology#>");


            public static String queryTerraformClassForArchimateClass(String className){
                return String.join(nl,
                prefixes,
                "SELECT ?terraformClass",
                "WHERE {",
                "archi:" + className + " rdfs:subClassOf [",
                "rdf:type owl:Restriction ;",
                "owl:onProperty archimateiac:semanticallyAnnotatedAs ;",
                "owl:someValuesFrom ?terraformClass",
                " ] .",
                "}");
            }
  
}
