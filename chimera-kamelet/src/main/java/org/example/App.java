package org.example;

import org.apache.camel.main.Main;

public class App {
    public static void main(String[] args) throws Exception {
        // use Camels Main class
        Main main = new Main();
        main.configure().addRoutesBuilder(MyRouteBuilder.class);
        // now keep the application running until the JVM is terminated (ctrl + c or sigterm)
        main.run(args);
    }
}