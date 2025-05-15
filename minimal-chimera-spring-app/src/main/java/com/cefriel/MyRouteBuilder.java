package com.cefriel;

import com.cefriel.util.ChimeraResourceBean;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
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
			.to("micrometer:counter:num_executions?increment=1&tags=routeid=lifting")						
			.to("micrometer:timer:processing_time?action=start&tags=routeid=lifting")
			.to("mapt://csv?template=#bean:liftingTemplate&format=turtle")
			.to("micrometer:timer:processing_time?action=stop&tags=routeid=lifting")
			.log("After lifting: ${body}")			
			.log("Lowering...")
			.to("micrometer:timer:processing_time?action=start&tags=routeid=lowering")
			.to("mapt://rdf?template=#bean:loweringTemplate")
			.to("micrometer:timer:processing_time?action=stop&tags=routeid=lowering")
			.log("After lowering: ${body}");	
    }
}
