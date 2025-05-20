package org.example;

import org.apache.camel.builder.RouteBuilder;

public class MyRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("file:./data?fileName=input.csv&noop=true")
                .log("Reading file: ${file:name}")
                .convertBodyTo(String.class)
                .log("${body}")
                .to("kamelet:lifting?outputFormat=turtle");
    }

}
