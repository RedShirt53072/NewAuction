package com.gmail.akashirt53072.newauctions.nbt;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.gmail.akashirt53072.newauctions.Main;


public class NBTLoader {
	private Main plugin;
	private PersistentDataContainer data;
	
    protected NBTLoader(Main plugin,Entity entity) {
    	this.plugin = plugin;
    	data = entity.getPersistentDataContainer();
    }
    protected void writeInt(String keyword,int value) {
    	NamespacedKey key = new NamespacedKey(plugin,keyword);
    	data.set(key, PersistentDataType.INTEGER, value);
    }
    protected void writeString(String keyword,String value) {
    	NamespacedKey key = new NamespacedKey(plugin,keyword);
    	data.set(key, PersistentDataType.STRING, value);
    }
    protected int readInt(String keyword) {
    	NamespacedKey key = new NamespacedKey(plugin,keyword);
    	Integer score = data.get(key, PersistentDataType.INTEGER);
    	if(score == null) {
    		return 0;
    	}
    	return score.intValue();
    }
    protected String readString(String keyword) {
    	NamespacedKey key = new NamespacedKey(plugin,keyword);
    	return data.get(key, PersistentDataType.STRING);
    }
}
