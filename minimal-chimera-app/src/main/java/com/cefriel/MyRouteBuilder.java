package com.cefriel;

import com.cefriel.util.ChimeraResourceBean;
import org.apache.camel.builder.RouteBuilder;

public class MyRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // 1. read data from local file
        // 2. mapt lift to rdf
        // 3. mapt lower to json

		getContext().getRegistry().bind("liftingTemplate",
				new ChimeraResourceBean("file://./data/lift.vm", "vtl"));

		getContext().getRegistry().bind("loweringTemplate",
				new ChimeraResourceBean("file://./data/lower.vm", "vtl"));

	from("file:./data?fileName=input.csv&noop=true")
            .log("Reading file: ${file:name}")
            .convertBodyTo(String.class)
			.log("File content: ${body}")
			.log("Lifting...")
			.to("mapt://csv?template=#bean:liftingTemplate&format=turtle")
			.log("After lifting: ${body}")
			.log("Lowering...")
			.to("mapt://rdf?template=#bean:loweringTemplate")
			.log("After lowering: ${body}");
	
    }
}
