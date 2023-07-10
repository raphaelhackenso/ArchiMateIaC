TODO

Licence - https://github.com/BPaaSModelling/Ontology4ModelingEnvironment/blob/master/LICENSE

https://github.com/BPaaSModelling/Ontology4ModelingEnvironment/blob/master/ArchiMate.ttl

-> to ArchiMate_original.ttl

The language ontology is missing some concepts

First: 
java -jar .\axiomestolanguageontology-1.0-jar-with-dependencies.jar .\ArchiMate3_1_update.ttl .\allDirectAxioms.xml ArchiMateSyntax.ttl



-----------------------------------

###  http://ikm-group.ch/archiMEO/archimate#Assessment
archi:Assessment rdf:type owl:Class ;
                 rdfs:subClassOf archi:MotivationElements ,
                                 [ rdf:type owl:Restriction ;
                                   owl:onProperty archi:usedBy ;
                                   owl:allValuesFrom [ rdf:type owl:Class ;
                                                       owl:unionOf ( archi:Driver
                                                                     archi:Yoda
                                                                   )
                                                     ]
                                 ] ;
                 rdfs:comment """Archimate V3.1 Motivation Layer Concept.
\"An assessment represents the result of an analysis of the state of affairs of the enterprise with respect to some driver.\" (OpenGroup 2019)"""^^xsd:string .

