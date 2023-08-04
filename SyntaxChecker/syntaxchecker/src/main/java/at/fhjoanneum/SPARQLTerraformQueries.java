package at.fhjoanneum;

public class SPARQLTerraformQueries {

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
                "ASK { terra:" + className + " a owl:Class }");
    }

    public static String queryOptionalBlockTypes(String className) {
        return String.join(nl,
                prefixes,
                "SELECT DISTINCT ?blockType",
                "WHERE {",
                "terra:" + className + " a owl:Class ;",
                "rdfs:subClassOf ?restriction .",
                "?restriction a owl:Restriction ;",
                "owl:onProperty terra:hasBlockType ;",
                "owl:someValuesFrom ?blockType .",
                "}");
    }

    public static String queryMinimalBlockTypes(String className) {
        return String.join(nl,
                prefixes,
                "SELECT DISTINCT ?blockType ?minCardinality",
                "WHERE {",
                "terra:" + className + " a owl:Class .",
                "terra:" + className + " rdfs:subClassOf ?restriction .",
                "?restriction owl:onProperty terra:hasBlockType ;",
                "owl:minQualifiedCardinality ?minCardinality ;",
                "owl:onClass ?blockType",
                "}");
    }

    public static String queryMaximalBlockTypes(String className) {
        return String.join(nl,
                prefixes,
                "SELECT DISTINCT ?blockType ?maxCardinality",
                "WHERE {",
                "terra:" + className + " a owl:Class .",
                "terra:" + className + " rdfs:subClassOf ?restriction .",
                "?restriction owl:onProperty terra:hasBlockType ;",
                "owl:maxQualifiedCardinality ?maxCardinality ;",
                "owl:onClass ?blockType",
                "}");
    }

    public static String queryOptionalAttributes(String className) {
        return String.join(nl,
                prefixes,
                "SELECT DISTINCT ?objectProperty ?type",
                "WHERE {",
                "terra:" + className + " a owl:Class ;",
                "rdfs:subClassOf ?restriction .",
                "?restriction a owl:Restriction ;",
                "owl:onProperty ?objectProperty ;",
                "owl:someValuesFrom ?type .",
                "FILTER(?objectProperty != terra:hasBlockType)",
                "FILTER(?objectProperty != archimateiac:semanticallyAnnotatedAs)",
                "FILTER(?objectProperty != archimateiac:requiresOtherTerraformResource)",
                "}");
    }

    public static String queryRequiredAttributes(String className) {
        return String.join(nl,
                prefixes,
                "SELECT DISTINCT ?objectProperty ?type",
                "WHERE {",
                "terra:" + className + " a owl:Class .",
                "terra:" + className + " rdfs:subClassOf ?restriction .",
                "?restriction owl:onProperty ?objectProperty ;",
                "owl:qualifiedCardinality \"1\"^^xsd:nonNegativeInteger ;",
                "owl:onDataRange ?type",
                "FILTER(?objectProperty != terra:hasBlockType)",
                "FILTER(?objectProperty != archimateiac:semanticallyAnnotatedAs)",
                "FILTER(?objectProperty != archimateiac:requiresOtherTerraformResource)",
                "}");
    }

    public static String queryRequiredAttributesForBlockType(String blockType, String resourceSchema) {
        return String.join(nl,
                prefixes,
                "SELECT DISTINCT ?objectProperty ?type",
                "WHERE {",
                "terra:" + blockType + " a owl:Class ;",
                "rdfs:subClassOf ?restriction .",
                "?restriction owl:intersectionOf( ?resourceSchema  [",
                "a owl:Restriction ;",
                "owl:onProperty ?objectProperty ;",
                "owl:qualifiedCardinality \"1\"^^xsd:nonNegativeInteger ;",
                "owl:onDataRange ?type",
                "] )",
                " FILTER(?resourceSchema = terra:" + resourceSchema + ")",
                "}");
    }

    public static String queryOptionalAttributesForBlockType(String blockType, String resourceSchema) {
        return String.join(nl,
                prefixes,
                "SELECT DISTINCT ?objectProperty ?type",
                "WHERE {",
                "terra:" + blockType + " a owl:Class ;",
                "rdfs:subClassOf ?restriction .",
                " ?restriction owl:intersectionOf( ?resourceSchema  [",
                " a owl:Restriction ;",
                " owl:onProperty ?objectProperty ;",
                " owl:someValuesFrom ?type ;",
                "  ] )",
                "FILTER(?resourceSchema = terra:" + resourceSchema + ")",
                "}");
    }

}
