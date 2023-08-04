#!/bin/bash

csv_file="Semantic_Mapping_ArchiMate_Terraform_all_restrictions.CSV"

generate_sparql_script() {
    script=""

    while IFS=";" read -r archimateConcept sourceTerra amount targetTerra; do
        axiom="terra:${sourceTerra} rdf:type owl:Class ;
             rdfs:subClassOf [ rdf:type owl:Restriction ;
                               owl:onProperty archimateiac:requiresOtherTerraformResource ;
                               owl:qualifiedCardinality \"1\"^^xsd:nonNegativeInteger ; 
                               owl:onClass terra:${targetTerra}
                             ] .
"
        script+=$axiom
    done < "$csv_file"

    echo "$script"
}

sparql_script=$(generate_sparql_script)

echo "$sparql_script"