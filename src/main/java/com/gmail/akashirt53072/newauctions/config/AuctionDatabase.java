package com.gmail.akashirt53072.newauctions.config;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.gmail.akashirt53072.newauctions.datatype.AuctionItemData;


public class AuctionDatabase extends DataConfig{

	public AuctionDatabase(Plugin plugin) {
		super(plugin,"allauctiondata.yml");
	}
	private int getNextID() {
		int id = getIntData("id");
		id ++;
		setData("id",id);
		return id;
	}
	public int getIndex() {
		return getNextIndex("item") - 1;
	}
	public int getDay() {
		return getIntData("day");
	}
	
	public void setDay(int day) {
		setData("day",day);
	}
	public void addItem(UUID uuid,ItemStack nbt,int price){	
		String path = "item";
		int index = getNextIndex(path);
		int id = getNextID();
		path = path + index;
		setData(path + ".UUID",uuid.toString());
		setData(path + ".NBT",nbt);
		setData(path + ".time",0);
		setData(path + ".price",price);
		setData(path + ".id",id);
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
			int id = getIntData(path + ".id");
			ItemStack item = getItem(path + ".NBT");
			UUID uuid = UUID.fromString(getStringData(path + ".UUID"));
			data.add(new AuctionItemData(price,time,item,uuid,id));
		}
		return data;
	}
	
	public AuctionItemData getIDItem(int id){	
		int index = getNextIndex("item");
		AuctionItemData data = null;
		for(int i = 1;i < index;i ++) {
			String path = "item" + i;
			int id2 = getIntData(path + ".id");
			if(id != id2) {
				continue;
			}
			UUID uuid = UUID.fromString(getStringData(path + ".UUID"));
			int price = getIntData(path + ".price");
			int time = getIntData(path + ".time");
			ItemStack item = getItem(path + ".NBT");
			data = new AuctionItemData(price,time,item,uuid,id);
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
			int id = getIntData(path + ".id");
			ItemStack item = getItem(path + ".NBT");
			data.add(new AuctionItemData(price,time,item,uuid,id));
		}
		return data;
	}
	
	public ArrayList<AuctionItemData> addTime(){	
		int index = getNextIndex("item");
		ArrayList<AuctionItemData> data = new ArrayList<AuctionItemData>();
		for(int i = 1;i < index;i ++) {
			String path = "item" + i;
			int time = getIntData(path + ".time");
			time ++;
			if(time != 7){
				setData(path + ".time",time);
				continue;
			}
			UUID uuid = UUID.fromString(getStringData(path + ".UUID"));
			int price = getIntData(path + ".price");
			int id = getIntData(path + ".id");
			ItemStack item = getItem(path + ".NBT");
			data.add(new AuctionItemData(price,time,item,uuid,id));
			removeItem(id);
		}
		return data;
	}
	
	public void removeItem(int id){	
		int maxindex = getNextIndex("item");
		int start = 0;
		for(int i = 1;i < maxindex;i ++) {
			String path = "item" + i;
			if(getIntData(path + ".id") != id) {
				continue;
			}
			start = i;
			break;
		}
		if(start == 0) {
			return;
		}
		for(int i = (start + 1);i < maxindex;i ++) {
			String path = "item" + i;
			String path2 = "item" + (i - 1);
			int price = getIntData(path + ".price");
			int time = getIntData(path + ".time");
			int id1 = getIntData(path + ".id");
			ItemStack item = getItem(path + ".NBT");
			UUID uuid = UUID.fromString(getStringData(path + ".UUID"));
			setData(path2 + ".UUID",uuid.toString());
			setData(path2 + ".NBT",item);
			setData(path2 + ".id",id1);
			setData(path2 + ".time",time);
			setData(path2 + ".price",price);
		}
		String path = "item" + (maxindex - 1);
		setData(path,null);
	}
}
