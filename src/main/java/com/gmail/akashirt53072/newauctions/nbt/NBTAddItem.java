package com.gmail.akashirt53072.newauctions.nbt;

import org.bukkit.entity.Player;

import com.gmail.akashirt53072.newauctions.Main;

public class NBTAddItem extends NBTLoader{

    public NBTAddItem(Main plugin,Player player) {
    	super(plugin,player);
    }
    public void init() {
    	super.writeInt("itemPrice", 100);
    	super.writeInt("hasPreItem", 0);
    }
    public void setPreItem(boolean hasItem) {
    	if(hasItem) {
        	super.writeInt("hasPreItem", 1);	
    	}else {
    		super.writeInt("hasPreItem", 0);
    	}
    }
    public void setPrice(int price) {
    	super.writeInt("itemPrice", price);
    }
    
    public boolean hasPreItem() {
    	int result = super.readInt("hasPreItem");
    	if(result == 1) {
    		return true;
    	}
    	return false;
    }
    
    public int getPrice() {
    	int result = super.readInt("itemPrice");
    	if(result < 1) {
    		return 1;
    	}
    	if(result > 1000000000) {
    		return 999999999;
    	}
    	return result;
    }
}
