package com.cefriel.chimera.camel.routesbuilder;

import org.apache.camel.builder.RouteBuilder;

import com.cefriel.chimera.camel.processors.WebsocketProcessor;


public class WebsocketRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		from("timer://testWebsocket?repeatCount=1&delay=3000")
			.routeId("websocketTimer")
			.process(new WebsocketProcessor());
		
		
		from("direct:processWebsocketData")
			.routeId("webSocketDataProcessor")
			.to("direct:harmonize")			
			.to("file:{{app.data.folder}}/samples?fileName=sample_${date:now:yyyy-MM-dd_HH-mm-ss_SSS}.ttl&charset=utf-8")
			.to("direct:logProcessingTime");
        	
		
		from("direct:logProcessingTime")
			.routeId("processingTimeLog")
			.choice()
				.when(header("isFirstSample").isEqualTo(false))   
		    		.setBody(simple("${exchange.getProperty('fileRowProcessStart')}"))
					.convertBodyTo(String.class)			
					.to("file:{{app.data.folder}}/records?fileName=records.txt&charset=utf-8&fileExist=append&appendChars=\n")
					.setBody(simple("${exchange.getProperty('fileRowProcessEnd')}"))
					.convertBodyTo(String.class)			
					.to("file:{{app.data.folder}}/records?fileName=records.txt&charset=utf-8&fileExist=append&appendChars=\n")    			
				.otherwise()
					.log("Skipped saving harmonization time of first sample")
				.end();
	}
}
