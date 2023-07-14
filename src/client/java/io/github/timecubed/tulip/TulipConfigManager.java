package io.github.timecubed.tulip;

import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class TulipConfigManager {
	Properties properties = new Properties();
	String configPath, modid;
	
	/**
	 * Creates a new instance of the Tulip Config Manager.
	 *
	 * @param modid Your mod's id for config. Optionally, you can enable absolute path if you want to save your file to a custom path, in which case provide the full path in this parameter (including file name and extension).
	 * @param absolutePath Enable this to be able to provide a custom path instead of the default one. Make sure your file extension ends with .properties
	 */
	
	public TulipConfigManager(String modid, Boolean absolutePath) {
		if (!absolutePath) this.configPath = FabricLoader.getInstance().getConfigDir().toString() + "\\" + modid + ".properties";
		else this.configPath = modid;
		this.modid = modid;
	}
	
	/**
	 * Saves your config to a file. Make sure to set the default values for your config by using the <pre>saveProperty()</pre> method, otherwise your config file may be broken.
	 *
	 * @throws IOException Exception is uncommon to occur, but can still occur if the file path is inaccessible (a.k.a. requires administrator access).
	 */
	public void save() throws IOException {
		MainServer.LOGGER.info("Saving Tulip config to path '" + configPath + "'...");
		
		if (fileExists()) {
			MainServer.LOGGER.warn("Tulip config for mod " + modid + " does not exist. Creating a new config file, but the values may be wrong");
		}
		
		createFileWithProperties();
	}
	
	/**
	 * Loads your config file and separates it into key-value pairs for getting values back.
	 *
	 * @throws IOException Exception is uncommon to occur, but can still occur if the file path is inaccessible (a.k.a. requires administrator access).
	 */
	public void load() throws IOException {
		MainServer.LOGGER.info("Loading Tulip config from path '" + configPath + "'...");
		
		if (!fileExists()) {
			MainServer.LOGGER.error("Tulip config for mod " + modid + " does not exist. Tulip will create a new config file, but this error should be reported.");
			createFileWithProperties();
		}
		File configFile = new File(configPath);
		
		properties.load(new FileInputStream(configFile));
	}
	
	private void createFileWithProperties() throws IOException {
		File newConfigFile = new File(configPath);
		
		if (!newConfigFile.exists()) {
			newConfigFile.createNewFile();
		}
		
		properties.store(new FileWriter(newConfigFile), null);
	}
	
	/**
	 * Saves a property entry to the config. This is used to set default values only, but should be overridden when you use the {@code load()} method.
	 * Optionally, you can set a property's values if you want to overwrite it.
	 *
	 * @param key Your property's key, used as an identifier to get back your property's value
	 * @param value Your property's value
	 */
	public void saveProperty(String key, Object value) {
		properties.setProperty(key, String.valueOf(value));
	}
	
	/**
	 * Returns the integer value for the key specified.
	 *
	 * @param key The key for the value you want.
	 * @return Integer value for the key provided.
	 */
	public int getInt(String key) {
		return Integer.parseInt(properties.getProperty(key));
	}
	
	/**
	 * Returns the long value for the key specified.
	 *
	 * @param key The key for the value you want.
	 * @return Long value for the key provided.
	 */
	public long getLong(String key) {
		return Long.parseLong(properties.getProperty(key));
	}
	
	/**
	 * Returns the float value for the key specified.
	 *
	 * @param key The key for the value you want.
	 * @return Float value for the key provided.
	 */
	public float getFloat(String key) {
		return Float.parseFloat(properties.getProperty(key));
	}
	
	/**
	 * Returns the double value for the key specified.
	 *
	 * @param key The key for the value you want.
	 * @return Double value for the key provided.
	 */
	public double getDouble(String key) {
		return Double.parseDouble(properties.getProperty(key));
	}
	
	/**
	 * Returns the boolean for the key specified.
	 *
	 * @param key The key for the value you want.
	 * @return Boolean value for the key provided
	 */
	public Boolean getBoolean(String key) {
		return Boolean.parseBoolean(properties.getProperty(key));
	}
	
	/**
	 * Returns the string value for the key specified.
	 * @param key The key for the value you want.
	 * @return String value for the key provided.
	 */
	public String getString(String key) {
		return properties.getProperty(key);
	}
	
	private boolean fileExists() {
		File file = new File(configPath);
		
		return file.exists();
	}
}