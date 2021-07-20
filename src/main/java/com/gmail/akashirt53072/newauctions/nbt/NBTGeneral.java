package com.gmail.akashirt53072.newauctions.nbt;

import org.bukkit.entity.Player;

import com.gmail.akashirt53072.newauctions.Main;

public class NBTGeneral extends NBTLoader{

    public NBTGeneral(Main plugin,Player player) {
    	super(plugin,player);
    }
    public void init() {
    	super.writeInt("joinCount", 1);
    }
    
    public int getJoinCount() {
    	int result = super.readInt("joinCount");
    	if(result < 0) {
    		return 0;
    	}
    	return result;
    }
    
}
