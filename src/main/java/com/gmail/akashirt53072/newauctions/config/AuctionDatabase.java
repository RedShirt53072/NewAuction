package com.gmail.akashirt53072.newauctions.config;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.gmail.akashirt53072.newauctions.AuctionItemData;


public class AuctionDatabase extends DataConfig{

	public AuctionDatabase(Plugin plugin) {
		super(plugin,"allauctiondata.yml");
	}

	public void addItem(UUID uuid,ItemStack nbt,int price){	
		String path = "item";
		int index = getNextIndex(path);
		path = path + index;
		super.setData(path + ".UUID",uuid.toString());
		super.setData(path + ".NBT",nbt);
		super.setData(path + ".time",0);
		super.setData(path + ".price",price);
	}
	
	public ArrayList<AuctionItemData> getItem(int start, int end){	
		int index = getNextIndex("item");
		if(index > end) {
			index = end;
		}
		ArrayList<AuctionItemData> data = new ArrayList<AuctionItemData>();
		for(int i = start;i < index;i ++) {
			String path = "item" + i;
			int price = getIntData(path + ".price");
			int time = getIntData(path + ".time");
			ItemStack item = getItem(path + ".NBT");
			UUID uuid = UUID.fromString(getStringData(path + ".UUID"));
			data.add(new AuctionItemData(price,time,item,uuid,i));
		}
		return data;
	}
	
	public ArrayList<AuctionItemData> getPlayerItem(UUID uuid){	
		int index = getNextIndex("item");
		ArrayList<AuctionItemData> data = new ArrayList<AuctionItemData>();
		for(int i = 1;i < index;i ++) {
			String path = "item" + i;
			UUID uuid2 = UUID.fromString(getStringData(path + ".UUID"));
			if(!uuid.equals(uuid2)) {
				continue;
			}
			int price = getIntData(path + ".price");
			int time = getIntData(path + ".time");
			ItemStack item = getItem(path + ".NBT");
			data.add(new AuctionItemData(price,time,item,uuid,i));
		}
		return data;
	}
	
	public void removeItem(int index){	
		int maxindex = getNextIndex("item");
		for(int i = (index + 1);i < maxindex;i ++) {
			String path = "item" + i;
			String path2 = "item" + (i - 1);
			int price = getIntData(path + ".price");
			int time = getIntData(path + ".time");
			ItemStack item = getItem(path + ".NBT");
			UUID uuid = UUID.fromString(getStringData(path + ".UUID"));
			super.setData(path2 + ".UUID",uuid.toString());
			super.setData(path2 + ".NBT",item);
			super.setData(path2 + ".time",time);
			super.setData(path2 + ".price",price);
		}
		String path = "item" + (maxindex - 1);
		super.setData(path,null);
	}
}
