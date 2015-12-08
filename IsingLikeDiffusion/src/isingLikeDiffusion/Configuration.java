package isingLikeDiffusion;

import java.io.IOException;
import java.util.Properties;

public class Configuration {
	private static Configuration instance;
	private static Properties p;
	
	private Configuration(){
		p = new Properties();
		try {
			p.load(Configuration.class.getResourceAsStream("conf.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Configuration getInstance(){
		if (instance == null){
			instance = new Configuration();
		}
		return instance;
	}
	
	public String getString(String name){
		return p.getProperty(name);
	}

	public Double getDouble(String name) {
		return Double.valueOf(getString(name));
	}

	public Boolean getBoolean(String name) {
		return Boolean.valueOf(getString(name));
	}

	public Integer getInteger(String name) {
		return Integer.valueOf(getString(name));
	}

}
