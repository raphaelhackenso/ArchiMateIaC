#!/bin/bash

csv_file="ressourceschema_att_required.csv"

generate_sparql_script() {
    script=""

    while IFS=";" read -r resource_schema blocktype attribute types cardinality; do
        axiom=":${blocktype} rdf:type owl:Class ;
             rdfs:subClassOf [ rdf:type owl:Class ;
                               owl:intersectionOf ( :${resource_schema} [ rdf:type owl:Restriction ;
                                                                  owl:onProperty :${attribute}_dp ;
                                                                  owl:qualifiedCardinality "\""1"\""^^xsd:nonNegativeInteger ;
                                                                  owl:onDataRange ${types} 
                                                                ]
                                                            )  
                             ] .
"
        script+=$axiom
    done < "$csv_file"

    echo "$script"
}

sparql_script=$(generate_sparql_script)

echo "$sparql_script"