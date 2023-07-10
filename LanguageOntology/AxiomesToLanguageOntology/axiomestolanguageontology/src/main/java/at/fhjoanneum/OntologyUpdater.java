package at.fhjoanneum;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;

import java.io.*;

public class OntologyUpdater {
    // TODO
    private OWLOntology ontology;
    private OWLDataFactory dataFactory;
    private OWLOntologyManager manager;

    public OntologyUpdater(String ontologyFilePath) throws OWLOntologyCreationException {
        // TODO
        manager = OWLManager.createOWLOntologyManager();
        OWLOntologyLoaderConfiguration config = new OWLOntologyLoaderConfiguration();
        config = config.setMissingImportHandlingStrategy(MissingImportHandlingStrategy.SILENT);
        manager.setOntologyLoaderConfiguration(config);
        ontology = manager.loadOntologyFromOntologyDocument(new File(ontologyFilePath));
        dataFactory = manager.getOWLDataFactory();
    }

    public void addClassExpression(String inputNamespace, String sourceConcept, String type, String cardinality,
            String targetConcepts) throws OWLOntologyStorageException {
        // TODO
        IRI namespace = IRI.create(inputNamespace);
        OWLClass sourceClass = dataFactory.getOWLClass(namespace + sourceConcept);
        OWLObjectProperty property = dataFactory.getOWLObjectProperty(namespace + type);
        OWLObjectPropertyExpression propertyExpression = property;

        // TODO
        String[] concepts = targetConcepts.substring(1, targetConcepts.length() - 1).split(" or ");

        // TODO
        OWLClassExpression[] targetClasses = new OWLClassExpression[concepts.length];
        for (int i = 0; i < concepts.length; i++) {
            String concept = concepts[i];
            targetClasses[i] = dataFactory.getOWLClass(namespace + concept);
        }

        // TODO
        OWLClassExpression targetClassExpression = targetClasses.length > 1
                ? dataFactory.getOWLObjectUnionOf(targetClasses)
                : targetClasses[0];

        // TODO
        OWLClassExpression classExpression;
        if (cardinality.equals("only")) {
            classExpression = dataFactory.getOWLObjectAllValuesFrom(propertyExpression, targetClassExpression);
        } else {
            classExpression = dataFactory.getOWLObjectSomeValuesFrom(propertyExpression, targetClassExpression);
        }

        // TODO
        OWLSubClassOfAxiom axiom = dataFactory.getOWLSubClassOfAxiom(sourceClass, classExpression);
        manager.addAxiom(ontology, axiom);
    }

    // TODO
    public void saveOntology(String outputFilePath) throws OWLOntologyStorageException, FileNotFoundException {
        OutputStream output = new FileOutputStream(outputFilePath);
        manager.saveOntology(ontology, output);
        try {
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}