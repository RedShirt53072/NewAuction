package com.gmail.akashirt53072.newauctions.nbt;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.gmail.akashirt53072.newauctions.Main;

public class NBTTrade extends NBTLoader{

    public NBTTrade(Main plugin,Player player) {
    	super(plugin,player);
    }
    public void init() {
    	super.writeInt("TradePrice", 0);
    	super.writeString("tradeplayer", "null");
    	super.writeString("pretradeplayer", "null");
    	super.writeInt("TradePreItems", 0);
    	super.writeInt("TradePhase", 0);
    }
    
    public void setPlayer(Player p) {
    	super.writeString("tradeplayer", p.getUniqueId().toString());
    }
    public void setPrePlayer(Player p) {
    	super.writeString("pretradeplayer", p.getUniqueId().toString());
    }
    
    public void setPhase(int phase) {
    	/*
    	0:無
    	1:OK
    	10~15:カウントダウン
    	*/
    	super.writeInt("TradePhase", phase);
    }
    
    public void setPrice(int price) {
    	super.writeInt("TradePrice", price);
    }

    public void addPreItem(int value) {
    	int result = super.readInt("TradePreItems");
    	result += value;
    	if(result < 0) {
    		result = 0;
    	}
    	if(result > 16) {
    		result = 16;
    	}
    	super.writeInt("TradePreItems", result);
    }
    
    public int getPreItem() {
    	int result = super.readInt("TradePreItems");
    	if(result < 0) {
    		return 0;
    	}
    	if(result > 16) {
    		return 16;
    	}
    	return result;
    }
    
    public int getPhase() {
    	int result = super.readInt("TradePhase");
    	if(result < 0) {
    		return 0;
    	}
    	return result;
    }
    public Player getPlayer() {
    	String result = super.readString("tradeplayer");
    	if(result == null) {
    		return null;
    	}
    	if(result.equals("null")) {
    		return null;
    	}
    	return Bukkit.getPlayer(UUID.fromString(result));
    }
    public Player getPrePlayer() {
    	String result = super.readString("pretradeplayer");
    	if(result == null) {
    		return null;
    	}
    	if(result.equals("null")) {
    		return null;
    	}
    	return Bukkit.getPlayer(UUID.fromString(result));
    }

    public int getPrice() {
    	int result = super.readInt("TradePrice");
    	if(result < 0) {
    		return 0;
    	}
    	if(result > 1000000000) {
    		return 999999999;
    	}
    	return result;
    }
    
}
