package at.fhjoanneum;

public class SPARQLTerraformSelectQueries {

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

    public static String returnAllIndividualsAndClasses() {
        return String.join(nl,
                prefixes,
                "SELECT ?individual ?class",
                "WHERE {",
                "?individual a owl:NamedIndividual .",
                "?individual a ?class",
                "FILTER(?class != owl:NamedIndividual)",
                "FILTER(REGEX(STR(?individual), STR(archimateiac:)))",
                "}");
    }

    public static String askIfIndividualClassIsAResourceSchema(String individualClass) {
        return String.join(nl,
                prefixes,
                "SELECT ?individual",
                "WHERE {",
                "?individual terra:hasBlockType archimateiac:" + individualClass,
                "}");
    }

    public static String returnDataPropertiesAndValuesForIndividual(String individualName, String className) {
        return String.join(nl,
                prefixes,
                "SELECT ?dataproperty ?value",
                "WHERE {",
                "archimateiac:" + individualName + " a terra:" + className + " .",
                "archimateiac:" + individualName + " ?dataproperty ?value .",
                "FILTER(?dataproperty != terra:hasBlockType)",
                "FILTER(?dataproperty != rdf:type)",
                "}");
    }

    public static String returnAllConnectedBlockTypeIndividuals(String individualName, String className){
        return String.join(nl, 
        prefixes,
        "SELECT ?blocktype",
        "WHERE {",
        "archimateiac:" + individualName + " a terra:" + className + " .",
        "archimateiac:" + individualName + " terra:hasBlockType ?blocktype .",
        "}");
    }

}