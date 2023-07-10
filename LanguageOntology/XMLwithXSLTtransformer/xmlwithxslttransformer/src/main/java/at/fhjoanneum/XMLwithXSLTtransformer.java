package at.fhjoanneum;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;

/**
 * Starts XML with XSLT transformator to new XML
 * 
 * @param args [input.xml] [input.xslt] [output.xml]
 */
public class XMLwithXSLTtransformer {
    public static void main(String[] args) {

        // Check for correct number of Parameters
        if (args.length != 3) {
            XMLwithXSLTtransformer.printUsage();
            System.exit(1);
        }

        // Set the variables according to the parameters
        String inputXMLString = args[0];
        String inputXSLTString = args[1];
        String outputXMLString = args[2];

        // Try the transformation
        try {

            // Create Files
            File inputXMLFile = new File(inputXMLString);
            File inputXSLTFile = new File(inputXSLTString);

            // Create Transformer Factory and the transformer
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer t = tf.newTransformer(new StreamSource(inputXSLTFile));

            // Apply the transformation
            t.transform(new StreamSource(inputXMLFile), new StreamResult(outputXMLString));

            // Notify user
            System.out.println("New XML file: " + outputXMLString);

            // Catch error
        } catch (TransformerException e) {
            e.printStackTrace();
        }

    }

    /**
     * Print the usage of the XML with XSLT transformator
     */
    public static void printUsage() {
        System.err.println();
        System.err.println(
                "usage: java -jar xmlwithxslttransformer-X.Y-jar-with-dependencies [input.xml] [input.xslt] [output.xml]");
        System.err.println();
    }
}
