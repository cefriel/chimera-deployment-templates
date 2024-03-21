package com.cefriel.chimera.camel.processors;

import java.io.IOException;
import java.net.URI;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class WebsocketProcessor implements Processor {
	private static Logger log = LoggerFactory.getLogger(WebsocketProcessor.class);
	
	private CamelContext cmlContext;
	private WebSocketClient client;
	private WebSocketHandler handler;
	private String websocketUri;
	
	@Override
	public void process(Exchange exchange) throws Exception {
		cmlContext = exchange.getContext();
		String token = exchange.getContext().resolvePropertyPlaceholders("{{websocket.token}}");
		this.websocketUri = exchange.getContext().resolvePropertyPlaceholders("{{websocket.uri}}")+"channel=lidar.otaniemi.2.json&token="+token;
		WebSocketClientConnect wsc = new WebSocketClientConnect();
		wsc.connect(websocketUri);		
	}

	
	private class WebSocketClientConnect {
		
	    public void connect(String uri) {
	        client = new WebSocketClient();
	        client.setConnectTimeout(5000);
	        //client.setMaxIdleTimeout(5000);
	        
	        try {
	            client.start();
	            handler = new WebSocketHandler();	           
	            client.connect(handler, new URI(uri), new ClientUpgradeRequest());

	        } catch (Exception e) {
	            log.error("Unable to connect to the websocke uri ["+uri+"]",e);
	        } 
	    }
	}
	
	private class WebSocketHandler extends WebSocketAdapter{

	    private Session session;

		
	    @Override
	    public void onWebSocketConnect(Session session) {
	    	this.session = session;
	        super.onWebSocketConnect(session);
	        log.info("WebSocket Connected: " + session.getRemoteAddress());	       
	    }

	    @Override
	    public void onWebSocketText(String message) {	       	        
	        cmlContext.createProducerTemplate().send("direct:processWebsocketData", exchange -> exchange.getIn().setBody(message));
	    }

	    @Override
	    public void onWebSocketClose(int statusCode, String reason) {	      
	        log.info("WebSocket Closed: " + statusCode + " - " + reason);
	        
	        try {
	        	log.info("Reconnecting the Websocket client...");
	        	Thread.sleep(2000);
				client.connect(handler, new URI(websocketUri), new ClientUpgradeRequest());
			} 
	        catch (Exception e) {
				  log.error("Unable to reconnect to the websocket uri ["+websocketUri+"]",e);
			}
	    }

	    @Override
	    public void onWebSocketError(Throwable cause) {	        
	        log.error("Received a websocket error: "+cause.getMessage(),(Object)cause.getStackTrace());	       
	        try {
				session.disconnect();
			} catch (IOException e) {
				log.error("Unable to close the websocket connection after receiving an error",e);
			}
	    }
	}
}
