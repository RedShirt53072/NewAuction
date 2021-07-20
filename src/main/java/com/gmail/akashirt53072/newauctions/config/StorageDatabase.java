package com.gmail.akashirt53072.newauctions.config;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.gmail.akashirt53072.newauctions.datatype.PlayerItemData;


public class StorageDatabase extends DataConfig{

	public StorageDatabase(Plugin plugin) {
		super(plugin,"storagedata.yml");
	}
	private int getPlayerIndex(UUID uuid) {
		int index = getNextIndex("item");
		for(int i = 0;i < index;i++) {
			String path = "player" + i + ".UUID";
			UUID uuid2 = UUID.fromString(getStringData(path));
			if(uuid.equals(uuid2)) {
				return i;
			}
		}
		//new player
		setData("player" + index + ".id",1);
		return index;
	}
	
	private int getNextID(int playerIndex) {
		int id = getIntData("player" + playerIndex + ".id");
		id ++;
		setData("player" + playerIndex + "id",id);
		return id;
	}
	
	public void addItem(UUID uuid,ItemStack nbt){	
		int playerIndex = getPlayerIndex(uuid);
		String path = "player" + playerIndex + ".item";
		int index = getNextIndex(path);
		path = path + index;
		int id = getNextID(playerIndex);
		setData(path + ".NBT",nbt);
		setData(path + ".id",id);
	}
	
	public ArrayList<PlayerItemData> getPlayerItem(UUID uuid){	
		int playerIndex = getPlayerIndex(uuid);
		String path = "player" + playerIndex + ".item";
		int index = getNextIndex(path);
		ArrayList<PlayerItemData> data = new ArrayList<PlayerItemData>();
		for(int i = 1;i < index;i ++) {
			ItemStack item = getItem(path + i + ".NBT");
			int id = getIntData(path + i + ".id");
			data.add(new PlayerItemData(item,uuid,id));
		}
		return data;
	}
	
	public void removeItem(UUID uuid,int id){
		int playerIndex = getPlayerIndex(uuid);
		String path = "player" + playerIndex + ".item";
		int maxindex = getNextIndex(path);
		for(int i = 1;i < maxindex;i ++) {
			int id2 = getIntData(path + i + ".id");
			if(id != id2) {
				continue;
			}
			for(int j = (i + 1);j < maxindex;j ++) {
				String path1 = "item" + j;
				String path2 = "item" + (j - 1);
				ItemStack item = getItem(path1 + ".NBT");
				int id1 = getIntData(path1 + ".id");
				setData(path2 + ".id",id1);
				setData(path2 + ".NBT",item);
			}
			String pathend = path + (maxindex - 1);
			setData(pathend,null);
			break;
		}
	}
}
