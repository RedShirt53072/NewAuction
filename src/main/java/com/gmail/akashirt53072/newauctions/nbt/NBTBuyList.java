package com.gmail.akashirt53072.newauctions.nbt;

import org.bukkit.entity.Player;

import com.gmail.akashirt53072.newauctions.Main;

public class NBTBuyList extends NBTLoader{

    public NBTBuyList(Main plugin,Player player) {
    	super(plugin,player);
    }
    public void init() {
    	super.writeInt("buyListPage", 1);
    	super.writeInt("toBuyItemID", 0);
    }
    public void setPage(int page) {
    	super.writeInt("buyListPage", page);
    }
    public void setSelectItem(int id) {
    	super.writeInt("selectItemID", id);
    }
   
    
    public int getSelectItem() {
    	int result = super.readInt("selectItemID");
    	if(result < 0) {
    		return 0;
    	}
    	return result;
    }
    public int getPage() {
    	int result = super.readInt("buyListPage");
    	if(result < 0) {
    		return 0;
    	}
    	return result;
    }
}
