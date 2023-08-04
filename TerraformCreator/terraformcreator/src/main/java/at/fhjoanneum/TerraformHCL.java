package at.fhjoanneum;

import org.javatuples.Pair;
import java.util.List;

public class TerraformHCL {

    public static String nl = System.getProperty("line.separator");

    public static String createResource(String className, String individual,
            List<Pair<String, String>> attributesAndValues,
            List<Pair<String, List<Pair<String, String>>>> blockTypesAndAttributes) {
        String returnResource = createResourceHeader(className, individual) + createAttributes(attributesAndValues, "\t");

        for (Pair<String, List<Pair<String, String>>> singleBlockTypeWithAttributes : blockTypesAndAttributes) {
            returnResource = returnResource + createBlockTypeHeader(singleBlockTypeWithAttributes.getValue0())
                    + createAttributes(singleBlockTypeWithAttributes.getValue1(), "\t\t") + "\n\t}";
        }

        return (returnResource + "\n}\n\n");
    }

    public static String createAttributes(List<Pair<String, String>> attributesAndValues, String tabs) {
        String returnAttributes = "";

        for (Pair<String, String> singleAttribute : attributesAndValues) {
            returnAttributes = returnAttributes + tabs + singleAttribute.getValue0() + " = "
                    + singleAttribute.getValue1() + "\n";
        }

        return returnAttributes;
    }

    public static String createResourceHeader(String className, String individualName) {
        return ("resource \"" + className + "\" \"" + individualName + "\" {\n");
    }

    public static String createBlockTypeHeader(String blockTypeName) {
        return ("\n\t" + blockTypeName + " {\n");
    }

    public static String createHeader() {
        return String.join(nl,
                "terraform {",
                "  required_providers {",
                "      azurerm = {",
                "          source  = \"hashicorp/azurerm\"",
                "          version = \"~> 3.58.0\"",
                "      }",
                "  }\n",
                "  required_version = \">= 1.1.0\"",
                "}\n",
                "provider \"azurerm\" {",
                "  features {}",
                "}\n\n");
    }
}
