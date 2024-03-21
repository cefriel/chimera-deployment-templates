package com.cefriel.chimera.camel.routesbuilder;

import org.apache.camel.builder.RouteBuilder;


import com.cefriel.chimera.camel.UtilityBean;

import java.text.DecimalFormat;
import java.util.Date;

public class ChimeraRouteBuilder extends RouteBuilder {

	private static int count = 1;
	private static boolean isFirstSample = true;
	private static long countProcessingTime = 0L;
	private static int maxProcessingTime = 0;
	private static int minProcessingTime = 0;
	
	@Override
	public void configure() throws Exception {
		
		UtilityBean ub = new UtilityBean();
		ub.loadChimeraBeans(getContext());
         
        from("direct:harmonize")
        	.routeId("dataHarmonization")
        	.process(exchange ->{
        		String jsonBody = exchange.getIn().getBody(String.class);
        		byte[] bodyBytes = jsonBody.getBytes("UTF-8");
        		int bodySize = bodyBytes.length;
        		exchange.setProperty("bodySize", bodySize);
        		
        		long startTime = new Date().getTime();
        		exchange.setProperty("startTime",startTime);        	
        		String fileRowProcessStart = exchange.getExchangeId()+",lifting_started,"+startTime;   
        		exchange.setProperty("fileRowProcessStart", fileRowProcessStart);
        	})     
        	.toD("mapt://json?template=#bean:"+UtilityBean.TEMPLATE_RADAR) 
        	.process(exchange ->{       		
        		long endTime = new Date().getTime();
        		int processingTime = (int) (endTime - (long)exchange.getProperty("startTime"));
        		exchange.setProperty("processingTime", processingTime);  
        		
        		String fileRowProcessEnd = exchange.getExchangeId()+",lifting_completed,"+endTime;   
        		exchange.setProperty("fileRowProcessEnd", fileRowProcessEnd);
        		
        		if(!isFirstSample) {       	
	        		countProcessingTime += processingTime;
	        		exchange.setProperty("count", count);
	        		double averageProcessingTime = (double) ((double)countProcessingTime/count++);
	        		DecimalFormat numberFormat = new DecimalFormat("#.00");
	    		
	        		maxProcessingTime = (processingTime > maxProcessingTime) ? processingTime : maxProcessingTime;
	        		minProcessingTime = (processingTime < minProcessingTime) ? processingTime : minProcessingTime;	        		
	        		
	        		exchange.setProperty("averageProcessingTime", numberFormat.format(averageProcessingTime));
	        		exchange.setProperty("maxProcessingTime", maxProcessingTime);
	        		exchange.setProperty("minProcessingTime", minProcessingTime);
	        		
	        		exchange.getIn().setHeader("isFirstSample", false);
        		}else {
        			isFirstSample = false;
        			minProcessingTime = processingTime;
        			exchange.getIn().setHeader("isFirstSample", true);
        		}
 
        	})   
        	.choice()
        		.when(header("isFirstSample").isEqualTo(false))   
        			.log("CM=[${exchange.getProperty('count')}] - "
					   + "CPT=[${exchange.getProperty('processingTime')}ms] - "
					   + "SZ=[${exchange.getProperty('bodySize')}byte] - "
					   + "APT=[${exchange.getProperty('averageProcessingTime')}ms] - "
					   + "MXPT=${exchange.getProperty('maxProcessingTime')}ms] - "
					   + "MNPT=${exchange.getProperty('minProcessingTime')}ms]")
        			
        		.otherwise()
        			.log("Skipped harmonization time of first sample [${exchange.getProperty('processingTime')}]")
			.end();
        
	}
}
