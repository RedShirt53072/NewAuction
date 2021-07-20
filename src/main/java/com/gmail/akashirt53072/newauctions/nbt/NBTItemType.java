package com.gmail.akashirt53072.newauctions.nbt;

import org.bukkit.inventory.ItemStack;

import com.gmail.akashirt53072.newauctions.Main;
import com.gmail.akashirt53072.newauctions.datatype.ItemType;



public class NBTItemType extends ItemNBTLoader{

    public NBTItemType(Main plugin,ItemStack item) {
    	super(plugin,item);
    }
    public void init(ItemType type) {
    	super.writeString("itemType", type.toString());
    }
    

    public ItemType getType() {
    	String setting = super.readString("itemType");
    	if(setting == null) {
    		return ItemType.NULLITEM;
    	}
    	return ItemType.valueOf(setting);
    }
}
