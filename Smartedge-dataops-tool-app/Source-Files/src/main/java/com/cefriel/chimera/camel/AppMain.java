package com.cefriel.chimera.camel;

import org.apache.camel.main.Main;

import com.cefriel.chimera.camel.routesbuilder.ChimeraRouteBuilder;
import com.cefriel.chimera.camel.routesbuilder.WebsocketRouteBuilder;

public class AppMain {
	public static void main(String[] args) throws Exception {
		Main main = new Main();
	
		//Registers the UtilityBean to the camel context
		main.bind("UtilityBean", UtilityBean.class);
		
        //Contains common endpoint used by all the other routes
        main.configure().addRoutesBuilder(new ChimeraRouteBuilder());
        
		 //Contains endpoint for managing the websocket connection
        main.configure().addRoutesBuilder(new WebsocketRouteBuilder());
		
		//Run the application
        main.run(args);
	}
}
