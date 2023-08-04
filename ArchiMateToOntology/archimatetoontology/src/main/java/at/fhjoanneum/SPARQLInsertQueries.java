package at.fhjoanneum;

public class SPARQLInsertQueries {

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

    public static String insertQueryCreateIndividual(String individualName, String terraformClass) {
        return String.join(nl,
                prefixes,
                "INSERT DATA {",
                "archimateiac:" + individualName + " rdf:type owl:NamedIndividual ,",
                "terra:" + terraformClass + " ;",
                "}");
    }

    public static String insertQueryCreateDirectAttribute(String individualName, String dataProperty, String value,
            String type) {
        return String.join(nl,
                prefixes,
                "INSERT DATA {",
                "archimateiac:" + individualName + " terra:" + dataProperty + "_dp \"" + value + "\"^^" + type + " .",
                "}");
    }

    public static String insertQueryCreateConncetedBlockType(String individualName, String blockTypeIndividual) {
        return String.join(nl,
                prefixes,
                "INSERT DATA {",
                "archimateiac:" + individualName + " terra:hasBlockType archimateiac:" + blockTypeIndividual + " .",
                "}");
    }

}
