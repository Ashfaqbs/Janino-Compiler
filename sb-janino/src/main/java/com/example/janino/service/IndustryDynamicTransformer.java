package com.example.janino.service;
import java.lang.reflect.Method;
import java.util.logging.Logger;

import org.codehaus.janino.SimpleCompiler;
import org.json.JSONObject;

public class IndustryDynamicTransformer {
    private static final Logger logger = Logger.getLogger(IndustryDynamicTransformer.class.getName());

    public static void main(String[] args) {
        try {
            // Sample JSON representing a financial transaction.
            String jsonData = "{\"amountCents\":10500, \"transactionType\":\"INTERNATIONAL\"}";
            JSONObject jsonObject = new JSONObject(jsonData);

            // Dynamic transformation logic provided as a String.
            // This logic:
            // 1. Converts amountCents to amountDollars.
            // 2. Sets a risk factor based on transactionType.
            // 3. Computes a riskScore.
            String transformationLogic = ""
                    + "public class Transformer {\n"
                    + "    public static org.json.JSONObject transform(org.json.JSONObject input) {\n"
                    + "        // Convert amount from cents to dollars\n"
                    + "        int amountCents = input.getInt(\"amountCents\");\n"
                    + "        double amountDollars = amountCents / 100.0;\n"
                    + "        input.put(\"amountDollars\", amountDollars);\n"
                    + "\n"
                    + "        // Determine risk factor based on transaction type\n"
                    + "        String type = input.getString(\"transactionType\");\n"
                    + "        double riskFactor = \"INTERNATIONAL\".equals(type) ? 1.5 : 1.0;\n"
                    + "        input.put(\"riskFactor\", riskFactor);\n"
                    + "\n"
                    + "        // Compute risk score\n"
                    + "        double riskScore = amountDollars * riskFactor;\n"
                    + "        input.put(\"riskScore\", riskScore);\n"
                    + "\n"
                    + "        return input;\n"
                    + "    }\n"
                    + "}\n";

            // Dynamically compile the transformation logic using Janino.
            SimpleCompiler compiler = new SimpleCompiler();
            compiler.cook(transformationLogic);

            // Load the compiled class and retrieve its transform method.
            Class<?> transformerClass = compiler.getClassLoader().loadClass("Transformer");
            Method transformMethod = transformerClass.getMethod("transform", JSONObject.class);

            // Apply the transformation logic.
            JSONObject transformedJson = (JSONObject) transformMethod.invoke(null, jsonObject);

            // Log the transformed JSON.
            logger.info("Transformed JSON:\n" + transformedJson.toString(2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
