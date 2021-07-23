package com.gmail.akashirt53072.newauctions.nbt;

import org.bukkit.inventory.ItemStack;

import com.gmail.akashirt53072.newauctions.Main;



public class NBTTradeItem extends ItemNBTLoader{

    public NBTTradeItem(Main plugin,ItemStack item) {
    	super(plugin,item);
    }
    public void init(int id) {
    	super.writeInt("tradeid", id);
    }
    

    public int getID() {
    	return super.readInt("tradeid");
    }
}
