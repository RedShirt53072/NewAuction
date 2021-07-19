package com.gmail.akashirt53072.newauctions.config;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;



public class DataConfig {
	private FileConfiguration config;
	private ConfigLoader configLoader;
	private final Plugin plugin;

	protected DataConfig(Plugin plugin,String filePath) {
		this.plugin = plugin;
		configLoader = new ConfigLoader(plugin,filePath);
		config = configLoader.getConfig();
	}
	protected int getNextIndex(String path){
		int i = 1;
		while(config.contains(path + i)) {
			i ++;
		}
        return i;
    }
	
	protected String getStringData(String path){
		if (config.contains(path)) {
            return config.getString(path);
        }else {
        	plugin.getLogger().warning(path + " is null(String)");
        }
        return null;
    }
	protected int getIntData(String path){
		if (config.contains(path)) {
            return config.getInt(path);
        }else {
        	plugin.getLogger().warning(path + " is null(int)");
        }
        return -1;
    }
	protected Inventory getInventory(String path){
		if (config.contains(path)) {
            return (Inventory)config.get(path);
        }else {
        	plugin.getLogger().warning(path + " is null(int)");
        }
        return null;
    }
	protected ItemStack getItem(String path){
		if (config.contains(path)) {
            return config.getItemStack(path);
        }else {
        	plugin.getLogger().warning(path + " is null(int)");
        }
        return null;
	}
	protected Location getLocation(String path){
		if (config.contains(path)) {
            return config.getLocation(path);
        }else {
        	plugin.getLogger().warning(path + " is null(int)");
        }
        return null;
	}
	
	protected void setData(String path,Object value){
		config.set(path,value);
        configLoader.saveConfig();
    }
}
