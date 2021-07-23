package com.gmail.akashirt53072.newauctions;
//playerの行動をトリガーに様々な処理クラスへと取り次ぐクラス
import org.bukkit.event.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import com.gmail.akashirt53072.newauctions.datatype.ItemType;
import com.gmail.akashirt53072.newauctions.gui.GuiAddItem;
import com.gmail.akashirt53072.newauctions.gui.GuiBuyConfirm;
import com.gmail.akashirt53072.newauctions.gui.GuiBuyItem;
import com.gmail.akashirt53072.newauctions.gui.GuiBuyList;
import com.gmail.akashirt53072.newauctions.gui.GuiCancelConfirm;
import com.gmail.akashirt53072.newauctions.gui.GuiCancelOrder;
import com.gmail.akashirt53072.newauctions.gui.GuiID;
import com.gmail.akashirt53072.newauctions.gui.GuiItemExpired;
import com.gmail.akashirt53072.newauctions.gui.GuiItemSold;
import com.gmail.akashirt53072.newauctions.gui.GuiSellConfirm;
import com.gmail.akashirt53072.newauctions.gui.GuiSellList;
import com.gmail.akashirt53072.newauctions.gui.GuiTrade;
import com.gmail.akashirt53072.newauctions.nbt.NBTAddItem;
import com.gmail.akashirt53072.newauctions.nbt.NBTGui;
import com.gmail.akashirt53072.newauctions.nbt.NBTItemType;
import com.gmail.akashirt53072.newauctions.nbt.NBTTrade;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public final class PlayerAction implements Listener {
	Main plugin;
    public PlayerAction(Main plugin) {
    	this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    
    @EventHandler
    public void onPlayerChat(final AsyncPlayerChatEvent event) {
    	Player player = event.getPlayer();
    	GuiID id = new NBTGui(plugin,player).getID();
    	if(id.equals(GuiID.TRADEPRICE)) {
    		event.setCancelled(true);
            Bukkit.getScheduler().runTask(plugin, new Runnable() {
                @Override
                public void run() {
                	//in minecraft
                    String price = event.getMessage();
                    //チャット処理
                    int length = price.length();
                    if(length == 0 || length > 10) {
                    	player.sendMessage(ChatColor.RED + price + "は長すぎます");
                    	new GuiTrade(plugin,player).onReturn();
                    	return;
                    }
                    ArrayList<String> numberData = new ArrayList<String>();
                    for(int i = 0;i < price.length();i ++) {
                     	char c = price.charAt(i);
                     	String ca = String.valueOf(c);
                     	if(ca.matches("[0-9]")) {
                     		numberData.add(ca);
                     	}
                    }
                    if(numberData.isEmpty()) {
                    	player.sendMessage(ChatColor.RED + price + "には数字がありません");
                    	new GuiTrade(plugin,player).onReturn();
                    	return;
                    }
                    int result = 0;
                    int size = numberData.size();
                    for(int i = 0;i < size;i ++) {
                    	int m = (int)Math.pow(10, size - 1 - i);
                    	 result += Integer.valueOf(numberData.get(i)) * m;
                    }
                    if(result < 1) {
                    	player.sendMessage(ChatColor.RED + price + "は0以下です");
                    	new GuiTrade(plugin,player).onReturn();
                    	return;
                    }
                    Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        			Objective obj = board.getObjective("emerald");
        			Score score = obj.getScore(player.getName());
        			int now = score.getScore();
        			if(result > now) {
        				player.sendMessage(ChatColor.RED + "あなたは" + price + "$のお金を持っていません");
        				return;
        			}
        			new NBTTrade(plugin,player).setPrice(result);
                    new GuiTrade(plugin,player).onReturn();
                }
        	});
        
    		return;
    	}
        if(!id.equals(GuiID.EDITPRICE)) {
        	return;
        }
        event.setCancelled(true);
        Bukkit.getScheduler().runTask(plugin, new Runnable() {
            @Override
            public void run() {
            	//in minecraft
                String price = event.getMessage();
                //チャット処理
                int length = price.length();
                if(length == 0 || length > 10) {
                	player.sendMessage(ChatColor.RED + price + "は長すぎます");
                	new GuiAddItem(plugin,player).create();
                	return;
                }
                ArrayList<String> numberData = new ArrayList<String>();
                for(int i = 0;i < price.length();i ++) {
                 	char c = price.charAt(i);
                 	String ca = String.valueOf(c);
                 	if(ca.matches("[0-9]")) {
                 		numberData.add(ca);
                 	}
                }
                if(numberData.isEmpty()) {
                	player.sendMessage(ChatColor.RED + price + "には数字がありません");
                	new GuiAddItem(plugin,player).create();
                	return;
                }
                int result = 0;
                int size = numberData.size();
                for(int i = 0;i < size;i ++) {
                	int m = (int)Math.pow(10, size - 1 - i);
                	 result += Integer.valueOf(numberData.get(i)) * m;
                }
                if(result < 1) {
                	player.sendMessage(ChatColor.RED + price + "は0以下です");
                	new GuiAddItem(plugin,player).create();
                	return;
                }
                new NBTAddItem(plugin,player).setPrice(result);
                new GuiAddItem(plugin,player).create();
            }
    	});
    }
    
    //インベントリ内クリック
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();
        GuiID id = new NBTGui(plugin,player).getID();
        switch(id) {
        case BUYLIST:
    		event.setCancelled(true);
    		GuiBuyList menu1 = new GuiBuyList(plugin,player);
    		ItemStack item1 = event.getCurrentItem();
    		if(item1 == null) {
    			break;
    		}
    		if(new NBTItemType(plugin,item1).getType().equals(ItemType.AUCTIONITEM)) {
    			menu1.onBuyClick(item1);
    		}else {
    			menu1.onClick(slot);
    		}
            break;
        case BUYITEM:
    		GuiBuyItem menu2 = new GuiBuyItem(plugin,player);
    		menu2.onClick(slot);
    		event.setCancelled(true);
            break;
        case BUYCONFIRM:
    		GuiBuyConfirm menu3 = new GuiBuyConfirm(plugin,player);
    		menu3.onClick(slot);
            event.setCancelled(true);
            break;
        case SELLLIST:
            event.setCancelled(true);
    		GuiSellList menu4 = new GuiSellList(plugin,player);
    		ItemStack item4 = event.getCurrentItem();
    		if(item4 == null) {
    			break;
    		}
    		if(new NBTItemType(plugin,item4).getType().equals(ItemType.AUCTIONITEM)) {
    			menu4.onBuyClick(item4);
    		}else {
    			menu4.onClick(slot);
    		}
            break;
        case ITEMSOLD:
    		GuiItemSold menu5 = new GuiItemSold(plugin,player);
    		menu5.onClick(slot);
            event.setCancelled(true);
            break;
        case CANCELORDER:
    		GuiCancelOrder menu6 = new GuiCancelOrder(plugin,player);
    		menu6.onClick(slot);
            event.setCancelled(true);
            break;
        case CANCELCONFIRM:
    		GuiCancelConfirm menu7 = new GuiCancelConfirm(plugin,player);
    		menu7.onClick(slot);
            event.setCancelled(true);
            break;
        case ITEMEXPIRED:
    		GuiItemExpired menu8 = new GuiItemExpired(plugin,player);
    		menu8.onClick(slot);
            event.setCancelled(true);
            break;
        case ADDITEM:
    		GuiAddItem menu9 = new GuiAddItem(plugin,player);
    		menu9.onClick(slot);
    		if(slot > 53) {
    			ItemStack item = event.getCurrentItem();
    			if(menu9.onInvClick(item)) {
    				Bukkit.getScheduler().runTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                        	//in minecraft
                        	Inventory inv = player.getOpenInventory().getBottomInventory();
                        	inv.clear(inv.first(item));
                        }
                	});
    			}
    		}
    		event.setCancelled(true);
            break;
        case SELLCONFIRM:
    		GuiSellConfirm menu10 = new GuiSellConfirm(plugin,player);
    		menu10.onClick(slot);
            event.setCancelled(true);
            break;
        case TRADE:
            event.setCancelled(true);
    		GuiTrade menu11 = new GuiTrade(plugin,player);
    		ItemStack item2 = event.getCurrentItem();
    		if(slot > 53) {
    			if(menu11.onInvClick(item2)) {
    				Bukkit.getScheduler().runTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                        	//in minecraft
                        	Inventory inv = player.getOpenInventory().getBottomInventory();
                        	inv.clear(inv.first(item2));
                        }
                	});
    			}
    			break;
    		}
    		if(item2 == null) {
    			break;
    		}
    		if(new NBTItemType(plugin,item2).getType().equals(ItemType.TRADEITEM)) {
    			menu11.onItemClick(item2);
    		}else {
    			menu11.onClick(slot);
    		}
    		break;
        default:
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
    		GuiBuyItem menu2 = new GuiBuyItem(plugin,player);
    		menu2.onClose();
            break;
        case BUYCONFIRM:
    		GuiBuyConfirm menu3 = new GuiBuyConfirm(plugin,player);
    		menu3.onClose();
            break;
        case SELLLIST:
    		GuiSellList menu4 = new GuiSellList(plugin,player);
    		menu4.onClose();
            break;
        case ITEMSOLD:
    		GuiItemSold menu5 = new GuiItemSold(plugin,player);
    		menu5.onClose();
            break;
        case CANCELORDER:
    		GuiCancelOrder menu6 = new GuiCancelOrder(plugin,player);
    		menu6.onClose();
            break;
        case CANCELCONFIRM:
    		GuiCancelConfirm menu7 = new GuiCancelConfirm(plugin,player);
    		menu7.onClose();
            break;
        case ITEMEXPIRED:
    		GuiItemExpired menu8 = new GuiItemExpired(plugin,player);
    		menu8.onClose();
            break;
        case ADDITEM:
    		GuiAddItem menu9 = new GuiAddItem(plugin,player);
    		menu9.onClose();
            break;
        case SELLCONFIRM:
    		GuiSellConfirm menu10 = new GuiSellConfirm(plugin,player);
    		menu10.onClose();
            break;
        case TRADE:
        	GuiTrade menu11 = new GuiTrade(plugin,player);
    		menu11.onClose();
            break;
        default:
        }
    }
}