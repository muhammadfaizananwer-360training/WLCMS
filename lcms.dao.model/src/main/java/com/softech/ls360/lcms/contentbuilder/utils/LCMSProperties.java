package  com.softech.ls360.lcms.contentbuilder.utils;
 

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author jason
 *
 */
public class LCMSProperties {
	
	private static Properties properties;
 	private static String roleBasedUrl = null;

	static {
		try {
			properties = new Properties();
			ClassLoader classLoader = LCMSProperties.class
					.getClassLoader();
			InputStream resourceAsStream = classLoader.getResourceAsStream("vu360-lcms.properties");
			if (resourceAsStream != null) {
				properties.load(resourceAsStream);
				roleBasedUrl = properties.getProperty("lcms.cas.BaseUrl") + "/login?auto=true";
				roleBasedUrl += "&service=";
				roleBasedUrl += properties.getProperty("lms.BaseUrl") + "/login.do?to=/";
				
			}
		 
	        
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getLCMSProperty(String key) {
		return properties.getProperty(key);
	}
	
	public static Properties getLCMSProperties() {
		return properties;
	}
	
	public static String getHost(){

		String context = properties.getProperty("lcms.running.environment.context");
		String host = properties.getProperty("lcms.environment.Host");
		
		return  host +"/"+context;
		
	}
	
	public static String getRoleBasedURL(){
		
		return roleBasedUrl;
		
	}
}