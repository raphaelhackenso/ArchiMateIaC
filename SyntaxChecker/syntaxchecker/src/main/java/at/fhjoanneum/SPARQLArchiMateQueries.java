package at.fhjoanneum;

public class SPARQLArchiMateQueries {

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

    public static String queryDoesClassExist(String className) {
        return String.join(nl,
                prefixes,
                "ASK { archi:" + className + " a owl:Class }");
    }

    public static String queryOPforRelationship(String relation){
        return String.join(nl,
        prefixes,
         "SELECT ?relationshipOP",
         "WHERE {",
         "?relationshipOP rdfs:label "+ "\"" + relation.toLowerCase() + "\"" + "@en",
         "}");
    }

    public static String queryIsDirectRelationAllowed(String source, String relation, String target){
        return String.join(nl,
        prefixes,
         "ASK {",
         "archi:"+ source  + " rdfs:subClassOf ?restriction .",
         "?restriction rdf:type owl:Restriction ;",
            "owl:onProperty archi:" + relation + ";",
            "owl:allValuesFrom ?value .",
         "?value rdf:type owl:Class ;",
         "owl:unionOf ?list .",
         "?list rdf:rest*/rdf:first archi:" + target + ".",
         "}"
         );
    }
}
