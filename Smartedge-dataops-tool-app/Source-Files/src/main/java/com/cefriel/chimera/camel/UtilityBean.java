package com.cefriel.chimera.camel;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.camel.CamelContext;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cefriel.util.ChimeraResourceBean;

public class UtilityBean {
	private static Logger log = LoggerFactory.getLogger(UtilityBean.class);
	private static final String TEMPLATE_FOLDER_PATH = "templates/";	
	public static final String TEMPLATE_RADAR = "template";
	public static final String TEMPLATE_RADAR_FILE = "template.vm";
	
	public void loadChimeraBeans(CamelContext context) {
		try {
			log.info("LOADING CHIMERA BEANS....");			
			setChimeraResourceBean(context,TEMPLATE_FOLDER_PATH+TEMPLATE_RADAR_FILE, TEMPLATE_RADAR_FILE, TEMPLATE_RADAR);
			log.info("CHIMERA BEAN LOADING FINISHED.");
		} catch (IOException e) {
			log.error("Failed loading chimera beans",e);
		}
	}
	
	private void setChimeraResourceBean(CamelContext context, String templatePath, String fileName, String beanName) throws IOException {			
		ClassLoader classLoader;		
		InputStream is;
		
		String templateFolderRuntime = context.resolvePropertyPlaceholders("{{app.template.folder}}");
		classLoader = ClassLoader.getSystemClassLoader();
		is = classLoader.getResourceAsStream(templatePath);
		File templateFile;
		
		if(is != null) {
			templateFile = setTemplateFile(is, templateFolderRuntime, fileName);
			context.getRegistry().bind(beanName, new ChimeraResourceBean("file://"+templateFile,null));
			log.info("Loading beans "+templatePath+": SUCCESS");
		}
		else {
			log.warn("Loading beans "+templatePath+": Template not found!");
		}
	}
	
	private File setTemplateFile(InputStream is, String templateFolder, String fileName) throws IOException {
		try {
			File file = new File(templateFolder+"/"+fileName);
			FileUtils.copyInputStreamToFile(is, file);
			return file;
		}
		catch(Exception e) {
			log.error("Failed set template file "+fileName,e);
			return null;
		}
	}
}
