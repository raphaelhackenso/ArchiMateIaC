#!/bin/bash

csv_file="block_types_max.csv"

generate_sparql_script() {
    script=""

    while IFS=";" read -r resource_schema block_type amount cardinality; do
        axiom=":${resource_schema} rdf:type owl:Class ;
             rdfs:subClassOf [ rdf:type owl:Restriction ;
                               owl:onProperty :hasBlockType ;
                               owl:maxCardinality ${amount} ; 
                               owl:onClass :${block_type}
                             ] .
"
        script+=$axiom
    done < "$csv_file"

    echo "$script"
}

sparql_script=$(generate_sparql_script)

echo "$sparql_script"