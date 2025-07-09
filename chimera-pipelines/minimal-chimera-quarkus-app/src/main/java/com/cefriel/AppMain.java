package com.cefriel;

import org.apache.camel.quarkus.main.CamelMainApplication;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class AppMain {
    public static void main(String... args) {
        Quarkus.run(CamelMainApplication.class, args);
        Quarkus.waitForExit();
    }
}
 
