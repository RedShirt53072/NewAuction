package com.gmail.akashirt53072.newauctions.nbt;

import org.bukkit.entity.Player;

import com.gmail.akashirt53072.newauctions.Main;

public class NBTStorage extends NBTLoader{

    public NBTStorage(Main plugin,Player player) {
    	super(plugin,player);
    }
    public void init() {
    	super.writeInt("storageCount", 0);
    	
    }
    public void addCount(int items) {
    	int now = super.readInt("storageCount");
    	now += items;
    	if(now < 0){
    		now = 0;
    	}
    	super.writeInt("storageCount",now);
    }
    
    public int getCount() {
    	int result = super.readInt("storageCount");
    	if(result < 0) {
    		return 0;
    	}
    	return result;
    }
}
