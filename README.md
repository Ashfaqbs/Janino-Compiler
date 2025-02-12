Janino is a lightweight, high-performance Java compiler library designed for embedding into applications that require on-the-fly Java code compilation. Below is an in-depth look into Janino, including an introduction, its purpose, and practical examples of its usage.

---

### Introduction to Janino

Janino implements the "commons-compiler" API and offers a super-small, super-fast alternative to invoking the JDK’s traditional compiler. Because it is independent of the JDK’s `tools.jar`, Janino can be easily embedded in environments where a full Java compiler might be too heavyweight or unavailable. It is particularly useful in scenarios where dynamic code evaluation is necessary, such as in scripting engines, rule engines, or dynamic expression evaluators.

**Key Features:**
- **Lightweight and Fast:** Janino compiles Java snippets quickly without the overhead of a full-blown compiler.
- **Embedded Use:** Its small footprint makes it ideal for applications that need runtime code compilation.
- **Independence:** It does not rely on the JDK’s `tools.jar`, which enhances portability and ease of integration.

Janino is a runtime Java compiler—it compiles Java code from a string (or other input) dynamically when our program is running, not when our application is built with javac. In other words, if we supply a Java code snippet as a string, Janino’s API (for example, via its SimpleCompiler) will compile that code on the fly during execution. This allows we to load and execute new classes without having to precompile them as part of our overall application.

For instance, when we call the SimpleCompiler’s cook() method with our source code string, Janino compiles the code at that moment (i.e. at runtime), then loads the resulting class into the JVM so we can instantiate and invoke its methods immediately.

This dynamic (runtime) compilation is useful in scenarios where we need to evaluate or modify code on the fly. However, it’s important to note that if we’re using GraalVM’s Polyglot API to embed code from guest languages, Java isn’t treated as a guest language because Java is already the host language. That’s why for dynamically evaluating Java code, we would either use Janino (which compiles at runtime) or a tool like JShell.


### Why Use Janino?

There are several reasons to consider Janino for our application:

1. **Runtime Code Evaluation:**  
   Janino is particularly useful for scenarios where we want to compile and run code on the fly. For example, we might need to evaluate user-defined expressions or rules at runtime.

2. **Embedded Scripting:**  
   Instead of integrating a full scripting engine, Janino allows developers to embed a Java-based scripting capability within their applications.

3. **Simplified Deployment:**  
   Since Janino is self-contained and does not depend on external compiler tools, it simplifies deployment in environments where we might not have access to the full JDK tools.

4. **Performance Considerations:**  
   For small code snippets, Janino offers much faster compilation times compared to using the standard Java compiler, which can be crucial in performance-sensitive applications.

---

### Example Usage of Janino

Here are some examples that demonstrate how Janino can be used in different scenarios.

#### 1. Evaluating an Expression

Suppose we need to evaluate a simple arithmetic expression at runtime. Janino can compile a small snippet of Java code to accomplish this:

```java
import org.codehaus.janino.ExpressionEvaluator;

public class ExpressionExample {
    public static void main(String[] args) throws Exception {
        // Create an ExpressionEvaluator instance for evaluating a mathematical expression.
        ExpressionEvaluator ee = new ExpressionEvaluator();
        ee.setExpressionType(int.class);
        ee.setParameters(new String[] { "a", "b" }, new Class[] { int.class, int.class });
        ee.cook("a + b");

        // Evaluate the expression with parameters a=5 and b=10
        int result = (Integer) ee.evaluate(new Object[] { 5, 10 });
        System.out.println("Result: " + result); // Output: Result: 15
    }
}
```

In this example:
- **ExpressionEvaluator**: A utility provided by Janino that compiles a Java expression.
- **Dynamic Parameters**: The expression `a + b` is compiled, and parameters are passed at runtime to compute the result.

#### 2. Compiling a Class at Runtime

Janino also allows we to compile entire classes dynamically. This can be useful when we need to generate classes on the fly based on user input or configuration:

```java
import org.codehaus.janino.SimpleCompiler;

public class RuntimeCompilationExample {
    public static void main(String[] args) throws Exception {
        // Define a simple class source code as a String
        String className = "HelloWorld";
        String sourceCode =
            "public class HelloWorld {" +
            "  public void sayHello() {" +
            "    System.out.println(\"Hello, Janino!\");" +
            "  }" +
            "}";

        // Create a SimpleCompiler instance
        SimpleCompiler compiler = new SimpleCompiler();
        compiler.cook(sourceCode);

        // Load the compiled class and invoke the sayHello() method
        Class<?> helloWorldClass = compiler.getClassLoader().loadClass(className);
        Object instance = helloWorldClass.getDeclaredConstructor().newInstance();
        helloWorldClass.getMethod("sayHello").invoke(instance);
    }
}
```

In this case:
- **SimpleCompiler**: A utility for compiling Java source code provided as a String.
- **Dynamic Class Loading**: The compiled class is loaded into the JVM, and its method is invoked reflectively.

*(References: cite40†Janino  » cite41†3.1.12)*

---

### Maven Integration

To integrate Janino into our Maven project, include the following dependency in our `pom.xml`:

```xml
<dependency>
    <groupId>org.codehaus.janino</groupId>
    <artifactId>janino</artifactId>
    <version>3.1.12</version>
</dependency>
```

This dependency makes Janino available for compiling code dynamically at runtime.


---

### Conclusion

Janino is a valuable tool for applications that require runtime code evaluation or dynamic compilation of Java code. Its lightweight nature, ease of integration, and performance advantages make it an ideal choice for embedding a compiler within our application. Whether we are evaluating expressions or compiling classes on the fly, Janino provides a robust solution that fits within a wide array of use cases.

---

