package com.maha.apitest.util;

import java.util.EnumSet;
import java.util.Set;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.GsonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.GsonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;

public class ConfigUtil {

	private static PropertiesConfiguration config = null;

	private static PropertiesConfiguration getConfig() {
		if (config == null) {
			try {
				config = new PropertiesConfiguration("config.properties");
			} catch (ConfigurationException e) {
				e.printStackTrace();
			}
		}

		return config;
	}
	
	public static String getProperty(String key){
		return getConfig().getProperty(key).toString();
	}
	
	static {
		Configuration.setDefaults(new Configuration.Defaults() {

		    private final JsonProvider jsonProvider = new GsonJsonProvider();
		    private final MappingProvider mappingProvider = new GsonMappingProvider();

		    public JsonProvider jsonProvider() {
		        return jsonProvider;
		    }

		    public MappingProvider mappingProvider() {
		        return mappingProvider;
		    }

		    public Set<Option> options() {
		        return EnumSet.noneOf(Option.class);
		    }
		});
	}
	
}
