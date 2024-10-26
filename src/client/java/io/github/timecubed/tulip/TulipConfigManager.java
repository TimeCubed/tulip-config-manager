package io.github.timecubed.tulip;

import net.fabricmc.loader.api.FabricLoader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

@SuppressWarnings("unused")
public class TulipConfigManager {
	Properties properties = new Properties();
	Properties defaultProperties = new Properties();
	String configPath, modID;
	
	/**
	 * Creates a new instance of the Tulip Config Manager.
	 *
	 * @param modID Your mod's id for config. Saves configs to the standard fabric config
	 *              folder, with the mod ID as the name of the config file.
	 */
	
	public TulipConfigManager(String modID, Boolean absolutePath) {
		if (!absolutePath) 
			this.configPath = FabricLoader.getInstance()
				.getConfigDir().toString() + 
				"\\" + modID + ".properties";
		else
			this.configPath = modID;

		this.modID = modID;
	}
	
	/**
	 *
	 * @param absolutePath The absolute path to the config file, including the name and extension.
	 * @param modID Your mod's ID.
	 */
	public TulipConfigManager(String absolutePath, String modID) {
		this.configPath = modID;
		
		this.modID = modID;
	}

	/**
	 * Saves your config to a file. Make sure to set the default values for your config by using
	 * the {@code saveProperty()} method, otherwise your config file may be broken.
	 * <p>
	 * Safe version, this handles any exception automatically for you.
	 */
	public void save() {
		try {
			saveUnsafe();
		} catch (IOException ioException) {
			MainServer.LOGGER.error("Could not save Tulip config file for mod {}", modID, ioException);
		}
	}
	
	/**
	 * Saves your config to a file. Make sure to set the default values for your config by using 
	 * the {@code saveProperty()} method, otherwise your config file may be broken.
	 * <p>
	 * Unsafe version, use this if you want to run custom code when saving fails.
	 *
	 * @throws IOException Thrown when the manager fails to save the file
	 * (invalid path, file already exists, requires administrator permissions, etc.).
	 */
	public void saveUnsafe() throws IOException {
		MainServer.LOGGER.info("Saving Tulip config to path '{}'...", configPath);
		
		if (fileExists()) {
			MainServer.LOGGER.warn("Tulip config for mod {} does not exist. Creating a new config file.", modID);
		}
		
		createFileWithProperties();
	}

	/**
	 * Loads your config file and separates it into key-value pairs for getting values back.
	 * <p>
	 * Safe version, this handles any exception automatically for you. This method will also
	 * save your config after loading, to sync changes between mod versions.
	 */
	public void load() {
		try {
			loadUnsafe();
			save();
		} catch (IOException ioException) {
			MainServer.LOGGER.error("Could not load Tulip config file for mod {}", modID, ioException);
		}
	}
	
	/**
	 * Same as {@code load()}, but doesn't save after loading.
	 */
	public void loadWithoutSave() {
		try {
			loadUnsafe();
		} catch (IOException ioException) {
			MainServer.LOGGER.error("Could not load Tulip config file for mod {}", modID, ioException);
		}
	}
	
	/**
	 * Loads your config file and separates it into key-value pairs for getting values back.
	 * Unsafe version, use this if you want to run custom code when loading fails.
	 *
	 * @throws IOException Exception is uncommon to occur, but can still occur if 
	 * the file path is inaccessible (a.k.a. requires administrator access).
	 */
	public void loadUnsafe() throws IOException {
		MainServer.LOGGER.info("Loading Tulip config from path '{}'...", configPath);
		
		if (!fileExists()) {
			MainServer.LOGGER.error("Tulip config for mod {} does not exist. Tulip will create a new config file, but this error should be reported.", modID);
			createFileWithProperties();
		}
		File configFile = new File(configPath);
		
		properties.load(new FileInputStream(configFile));
	}
	
	private void createFileWithProperties() throws IOException {
		File newConfigFile = new File(configPath);
		
		if (!newConfigFile.createNewFile()) {
			throw new IOException("File already exists");
		}
		
		properties.store(new FileWriter(newConfigFile), null);
	}
	
	/**
	 * Saves a property entry to the config. Overwrites the values of properties
	 * if they already exist.
	 *
	 * @param key Your property's key
	 * @param value Your property's value
	 */
	public void saveProperty(String key, Object value) {
		properties.setProperty(key, String.valueOf(value));
	}
	
	/**
	 * Sets the initial value of a property. Does not overwrite the values of
	 * properties.
	 * @param key Your property's key
	 * @param defaultValue The default value for the property
	 */
	public void setDefault(String key, Object defaultValue) {
		if (!properties.containsKey(key)) {
			properties.setProperty(key, String.valueOf(defaultValue));
		}
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
	
	@SuppressWarnings("inverted")
	private boolean fileExists() {
		File file = new File(configPath);
		
		return file.exists();
	}
}