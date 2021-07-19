package com.gmail.akashirt53072.newauctions;
//playerの行動をトリガーに様々な処理クラスへと取り次ぐクラス
import org.bukkit.event.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import com.gmail.akashirt53072.newauctions.gui.GuiBuyList;
import com.gmail.akashirt53072.newauctions.gui.GuiID;
import com.gmail.akashirt53072.newauctions.gui.GuiSellList;
import com.gmail.akashirt53072.newauctions.nbt.NBTGui;

import org.bukkit.entity.Player;

public final class PlayerAction implements Listener {
	Main plugin;
    public PlayerAction(Main plugin) {
    	this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    //インベントリ内クリック
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();
        GuiID id = new NBTGui(plugin,player).getID();
        switch(id) {
        case BUYLIST:
    		GuiBuyList menu1 = new GuiBuyList(plugin,player);
    		menu1.onClick(slot);
            event.setCancelled(true);
            break;
        case BUYITEM:
        case BUYCONFIRM:
        case SELLLIST:
    		GuiSellList menu4 = new GuiSellList(plugin,player);
    		menu4.onClick(slot);
            event.setCancelled(true);
            break;
        case ITEMSOLD:
        case CANCELORDER:
        case CANCELCONFIRM:
        case ITEMEXPIRED:
        case ADDITEM:
        case SELLCONFIRM:
        case EDITPRICE:
        }
    }
    
    @EventHandler
    public void onInventoryClose(final InventoryCloseEvent event) {
        final Player player = (Player) event.getPlayer();
        GuiID id = new NBTGui(plugin,player).getID();
        switch(id) {
        case BUYLIST:
        	GuiBuyList menu1 = new GuiBuyList(plugin,player);
    		menu1.onClose();
    	    break;
        case BUYITEM:
        case BUYCONFIRM:
        case SELLLIST:
        	GuiSellList menu4 = new GuiSellList(plugin,player);
    		menu4.onClose();
    	    break;
        case ITEMSOLD:
        case CANCELORDER:
        case CANCELCONFIRM:
        case ITEMEXPIRED:
        case ADDITEM:
        case SELLCONFIRM:
        case EDITPRICE:
        }
    }
}