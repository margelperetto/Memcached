package org.teste.memcached.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class PropertiesUtils {

	private static final String PROP_FILE = System.getProperty("user.home")+File.separator+"conf_memcached.properties";
	private static final Properties PROPERTIES = new Properties();
	
	public static void loadProperties() throws Exception{
		if(new File(PROP_FILE).exists()){
			PROPERTIES.load(new FileInputStream(PROP_FILE));
		}
	}
	
	public static void saveProperties() throws Exception{
		PROPERTIES.store(new FileOutputStream(PROP_FILE), "");
	}
	
	public static void setProp(Prop p, String value){
		PROPERTIES.setProperty(p.name(), value);
	}
	
	public static String getProp(Prop p){
		return PROPERTIES.getProperty(p.name());
	}
	
	public static enum Prop{
		HOST,PORT;
	}
}
