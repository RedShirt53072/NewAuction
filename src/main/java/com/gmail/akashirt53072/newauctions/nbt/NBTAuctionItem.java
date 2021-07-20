package com.gmail.akashirt53072.newauctions.nbt;

import org.bukkit.inventory.ItemStack;

import com.gmail.akashirt53072.newauctions.Main;
import com.gmail.akashirt53072.newauctions.datatype.ItemStatus;



public class NBTAuctionItem extends ItemNBTLoader{

    public NBTAuctionItem(Main plugin,ItemStack item) {
    	super(plugin,item);
    }
    public void init(int id,ItemStatus status) {
    	super.writeInt("id", id);
    	super.writeString("auctionStatus", status.toString());
    }
    
    public ItemStatus getStatus() {
    	String result = super.readString("auctionStatus");
    	if(result == null) {
    		return ItemStatus.All;
    	}
    	return ItemStatus.valueOf(result);
    }

    public int getID() {
    	return super.readInt("id");
    }
}
