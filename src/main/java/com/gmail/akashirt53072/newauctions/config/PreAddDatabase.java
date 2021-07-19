package com.gmail.akashirt53072.newauctions.config;

import java.util.UUID;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.gmail.akashirt53072.newauctions.PlayerItemData;


public class PreAddDatabase extends DataConfig{

	public PreAddDatabase(Plugin plugin) {
		super(plugin,"preadddata.yml");
	}

	public void addItem(UUID uuid,ItemStack nbt){	
		String path = "item";
		int index = getNextIndex(path);
		path = path + index;
		super.setData(path + ".UUID",uuid.toString());
		super.setData(path + ".NBT",nbt);
	}
	
	public PlayerItemData getPlayerItem(UUID uuid){	
		int index = getNextIndex("item");
		PlayerItemData data = null;
		for(int i = 1;i < index;i ++) {
			String path = "item" + i;
			UUID uuid2 = UUID.fromString(getStringData(path + ".UUID"));
			if(!uuid.equals(uuid2)) {
				continue;
			}
			ItemStack item = getItem(path + ".NBT");
			data = new PlayerItemData(item,uuid,i);
			break;
		}
		return data;
	}
	
	public void removeItem(UUID uuid){
		int maxindex = getNextIndex("item");
		for(int i = 1;i < maxindex;i ++) {
			String path = "item" + i;
			UUID uuid2 = UUID.fromString(getStringData(path + ".UUID"));
			if(!uuid.equals(uuid2)) {
				continue;
			}
			for(int j = (i + 1);j < maxindex;j ++) {
				String path1 = "item" + j;
				String path2 = "item" + (j - 1);
				ItemStack item = getItem(path1 + ".NBT");
				UUID uuid1 = UUID.fromString(getStringData(path1 + ".UUID"));
				super.setData(path2 + ".UUID",uuid1.toString());
				super.setData(path2 + ".NBT",item);
			}
			String pathend = "item" + (maxindex - 1);
			super.setData(pathend,null);
			break;
		}
		
	}
}
