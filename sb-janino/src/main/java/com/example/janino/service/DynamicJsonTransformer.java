package com.example.janino.service;

import java.lang.reflect.Method;
import java.util.logging.Logger;

import org.codehaus.janino.SimpleCompiler;
import org.json.JSONObject;

public class DynamicJsonTransformer {
    // Using java.util.logging for logging the output
    private static final Logger logger = Logger.getLogger(DynamicJsonTransformer.class.getName());

    public static void main(String[] args) {
        try {
            // Example JSON input
            String jsonData = "{\"name\":\"john\", \"id\":2, \"age\":12}";
            JSONObject jsonObject = new JSONObject(jsonData);

            // The transformation logic is provided as a String.
            // This example logic converts the "name" field to uppercase.
            // You can change this string at runtime as needed.
            String functionLogic = ""
                    + "public class NameTransformer {"
                    + "   public static String transform(String name) {"
                    + "       return name.toUpperCase();"
                    + "   }"
                    + "}";

            // Compile the provided logic using Janino's SimpleCompiler
            SimpleCompiler compiler = new SimpleCompiler();
            compiler.cook(functionLogic);

            // Load the dynamically compiled class and get the transform method
            Class<?> transformerClass = compiler.getClassLoader().loadClass("NameTransformer");
            Method transformMethod = transformerClass.getMethod("transform", String.class);

            // Retrieve the original value from JSON
            String originalName = jsonObject.getString("name");

            // Invoke the dynamic transform method on the original value
            String transformedName = (String) transformMethod.invoke(null, originalName);

            // Update the JSON object with the transformed value
            jsonObject.put("name", transformedName);

            // Log the transformed JSON (pretty printed with an indent factor of 2)
            logger.info("Transformed JSON: " + jsonObject.toString(2));
        } catch (Exception e) {
            logger.severe("An error occurred: " + e.getMessage());
        }
    }
}
