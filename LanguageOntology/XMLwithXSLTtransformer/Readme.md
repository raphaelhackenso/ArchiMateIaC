TODO

Licence - https://github.com/archimatetool/archi/blob/master/License.txt 

Original XML from https://github.com/archimatetool/archi/blob/9ba0559c2bd6e1ff15a7019c9986afadb29e2e71/com.archimatetool.model/model/relationships.xml
Version 3.1

relationships3_1.xml - the original xml File with all inputs but as characters

relationships3_1.xml -> must be updated because the Relationship - o is not valid, this does not make sense
manually updated relatinships3_1.xml to -> relationships3_1_update.xml

createAllDirectRelations.xslt - the Transformator to get all possible direct relations as XML


First: 
java -jar .\xmlwithxslttransformer-1.0-jar-with-dependencies.jar .\relationships3_1_update.xml .\createAllDirectRelations.xslt allDirectRelations.xml

creates all direct relations, but those a not used as axiomes


Second: 
java -jar .\xmlwithxslttransformer-1.0-jar-with-dependencies.jar .\allDirectRelations.xml .\createAllDirectAxiomes.xslt allDirectAxioms.xml

creates all Axiomes as only and with or split