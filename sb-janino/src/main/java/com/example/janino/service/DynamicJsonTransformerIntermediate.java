package com.example.janino.service;
import java.lang.reflect.Method;
import java.util.logging.Logger;

import org.codehaus.janino.SimpleCompiler;
import org.json.JSONObject;

public class DynamicJsonTransformerIntermediate {
    // Using java.util.logging for output
    private static final Logger logger = Logger.getLogger(DynamicJsonTransformerIntermediate.class.getName());

    public static void main(String[] args) {
        try {
            // Example JSON input
            String jsonData = "{\"name\":\"john\", \"id\":2, \"age\":12}";
            JSONObject jsonObject = new JSONObject(jsonData);

            // Transformation logic defined as a String.
            // The logic creates a class "Transformer" with a static method "transform"
            // that takes a JSONObject, performs several transformations, and returns it.
            String transformationLogic = ""
                    + "public class Transformer {\n"
                    + "    public static org.json.JSONObject transform(org.json.JSONObject input) {\n"
                    + "        // Convert 'name' field to uppercase\n"
                    + "        String name = input.getString(\"name\").toUpperCase();\n"
                    + "        input.put(\"name\", name);\n"
                    + "\n"
                    + "        // Add 10 to 'age' field\n"
                    + "        int age = input.getInt(\"age\");\n"
                    + "        input.put(\"age\", age + 10);\n"
                    + "\n"
                    + "        // If 'id' is even, add a new field 'even' with value true\n"
                    + "        int id = input.getInt(\"id\");\n"
                    + "        if (id % 2 == 0) {\n"
                    + "            input.put(\"even\", true);\n"
                    + "        }\n"
                    + "        return input;\n"
                    + "    }\n"
                    + "}\n";


            // Compile the transformation logic using Janino's SimpleCompiler.
            SimpleCompiler compiler = new SimpleCompiler();
            compiler.cook(transformationLogic);

            // Load the compiled class and retrieve the 'transform' method.
            Class<?> transformerClass = compiler.getClassLoader().loadClass("Transformer");
            Method transformMethod = transformerClass.getMethod("transform", JSONObject.class);

            // Apply the transformation logic to the JSON data.
            JSONObject transformedJson = (JSONObject) transformMethod.invoke(null, jsonObject);

            // Log (or print) the transformed JSON with pretty-printing.
            logger.info("Transformed JSON:\n" + transformedJson.toString(2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
