package com.gmail.akashirt53072.newauctions.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import com.gmail.akashirt53072.newauctions.Main;
import com.gmail.akashirt53072.newauctions.config.AuctionDatabase;
import com.gmail.akashirt53072.newauctions.datatype.AuctionItemData;
import com.gmail.akashirt53072.newauctions.datatype.ItemStatus;
import com.gmail.akashirt53072.newauctions.datatype.ItemType;
import com.gmail.akashirt53072.newauctions.nbt.NBTAuctionItem;
import com.gmail.akashirt53072.newauctions.nbt.NBTBuyList;
import com.gmail.akashirt53072.newauctions.nbt.NBTGui;
import com.gmail.akashirt53072.newauctions.nbt.NBTItemType;

public class GuiBuyItem extends Gui{
	public GuiBuyItem(Main plugin, Player player) {
		super(plugin, player);
	}
	public GuiBuyItem(Main plugin, Player player,Inventory inventory) {
		super(plugin, player,inventory);
	}
	
	@Override
	public void create() {
		new NBTGui(plugin,player).setID(GuiID.BUYITEM);
		inv = Bukkit.createInventory(null, 27, "アイテムを購入しますか？");
		for(int index = 0;index < 27;index ++) {
			switch(index){
			case 4:
			case 13:
				break;
			case 11:
				inv.setItem(index, createItem(Material.LIGHT_GRAY_WOOL,ChatColor.WHITE + "読み込み中",null,1,null));
				break;
			case 15:
				inv.setItem(index, createItem(Material.ARROW,ChatColor.WHITE + "戻る",null,1,null));
				break;
			default:
				inv.setItem(index, createItem(Material.BLACK_STAINED_GLASS_PANE," ",null,1,null));
			}
		}
		player.openInventory(inv);
	}
	public void asyncLoad(int id) {
		//非同期でアイテムデータ読み込んで表示させる
		new NBTBuyList(plugin,player).setSelectItem(id);
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
            	//out of minecraft
            	AuctionDatabase database = new AuctionDatabase(plugin);
            	AuctionItemData data = database.getIDItem(id);
            	Bukkit.getScheduler().runTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                    	//in minecraft
                    	if(!new NBTGui(plugin,player).getID().equals(GuiID.BUYITEM)) {
                    		return;
                    	}
                    	inv = player.getOpenInventory().getTopInventory();
                    	ItemStack item = data.getItem();
                		new NBTAuctionItem(plugin,item).init(data.getid(),ItemStatus.All);
                		new NBTItemType(plugin,item).init(ItemType.AUCTIONITEM);
                		ItemMeta meta = item.getItemMeta();
                		List<String> loreData = meta.getLore();
                		if(loreData == null) {
                			loreData = new ArrayList<String>();
                		}
                		String playerName = Bukkit.getOfflinePlayer(data.getPlayer()).getName();
                		loreData.add(ChatColor.WHITE + "----------------");
                		loreData.add(ChatColor.WHITE + "出品者：" + playerName);
                		loreData.add(ChatColor.WHITE + "価格：" + ChatColor.GOLD + data.getPrice() + "$");
                		loreData.add(ChatColor.WHITE + "終了まであと" + ChatColor.GOLD + (7 - data.getTime()) + "日");
                		loreData.add(ChatColor.WHITE + "----------------");
                		meta.setLore(loreData);
                		item.setItemMeta(meta);
                		inv.setItem(13, item);
                		Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
            			Objective obj = board.getObjective("emerald");
            			Score score = obj.getScore(player.getName());
            			int now = score.getScore();
            			if(data.getPrice() > now) {
            				inv.setItem(11, createItem(Material.BARRIER,ChatColor.RED + "お金が足りません",null,1,null));
            			}else {
            				inv.setItem(11, createItem(Material.LIME_WOOL,ChatColor.WHITE + "決定",null,1,null));    	
                    	}
                		
                		inv.setItem(4,createItem(Material.GOLD_NUGGET,ChatColor.GREEN + playerName + ChatColor.WHITE + "から" + ChatColor.GOLD + data.getPrice() + "$" + ChatColor.WHITE + "で購入しますか？",null,1,Enchantment.MENDING));
                    	
                    }
            	});
            }
        });
	}
	@Override
	public void onClick(int slot) {
		switch(slot) {
		case 11:
			close();
			int id = new NBTBuyList(plugin,player).getSelectItem();
			Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
	            @Override
	            public void run() {
	            	//out of minecraft
	            	AuctionDatabase database = new AuctionDatabase(plugin);
	            	AuctionItemData data = database.getIDItem(id);
	            	Bukkit.getScheduler().runTask(plugin, new Runnable() {
	                    @Override
	                    public void run() {
	                    	//in minecraft
	                    	if(data == null) {
	                    		player.sendMessage(ChatColor.RED + "このアイテムは出品が終了しました");
	                    		new GuiBuyList(plugin,player).create();
	                    		return;
	                    	}
	                    	Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
	            			Objective obj = board.getObjective("emerald");
	            			Score score = obj.getScore(player.getName());
	            			int now = score.getScore();
	            			if(data.getPrice() > now) {
	            				player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, (float)0.5);
	            				return;
	            			}
	            			player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
	            			new GuiBuyConfirm(plugin,player).create();
	            			
	                    }
	            	});
	            }
	        });
			break;
		case 15:
			player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
			close();
			new GuiBuyList(plugin,player).create();
			break;
		}
	}
	
}
