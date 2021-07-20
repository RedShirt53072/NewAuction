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

import com.gmail.akashirt53072.newauctions.Main;
import com.gmail.akashirt53072.newauctions.StorageSystem;
import com.gmail.akashirt53072.newauctions.config.ExpiredDatabase;
import com.gmail.akashirt53072.newauctions.datatype.AuctionItemData;
import com.gmail.akashirt53072.newauctions.datatype.ItemStatus;
import com.gmail.akashirt53072.newauctions.datatype.ItemType;
import com.gmail.akashirt53072.newauctions.nbt.NBTAddItem;
import com.gmail.akashirt53072.newauctions.nbt.NBTAuctionItem;
import com.gmail.akashirt53072.newauctions.nbt.NBTBuyList;
import com.gmail.akashirt53072.newauctions.nbt.NBTGui;
import com.gmail.akashirt53072.newauctions.nbt.NBTItemType;

public class GuiItemExpired extends Gui{
	public GuiItemExpired(Main plugin, Player player) {
		super(plugin, player);
	}
	public GuiItemExpired(Main plugin, Player player,Inventory inventory) {
		super(plugin, player,inventory);
	}
	
	@Override
	public void create() {
		new NBTGui(plugin,player).setID(GuiID.ITEMEXPIRED);
		inv = Bukkit.createInventory(null, 27, "アイテムの出品期限が切れました");
		for(int index = 0;index < 27;index ++) {
			switch(index){
			case 13:
				break;
			case 4:
				inv.setItem(index, createItem(Material.CLOCK,ChatColor.WHITE + "出品期限切れ",null,1,Enchantment.MENDING));
				break;
			case 11:
				inv.setItem(index, createItem(Material.GOLD_BLOCK,ChatColor.WHITE + "アイテムを回収",null,1,null));
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
            	ExpiredDatabase database = new ExpiredDatabase(plugin);
            	AuctionItemData data = database.getIDItem(id);
            	Bukkit.getScheduler().runTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                    	//in minecraft
                    	if(!new NBTGui(plugin,player).getID().equals(GuiID.ITEMEXPIRED)) {
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
                		loreData.add(ChatColor.WHITE + "出品期限切れ");
                		loreData.add(ChatColor.WHITE + "----------------");
                		meta.setLore(loreData);
                		item.setItemMeta(meta);
                		inv.setItem(13, item);
                		
                    }
            	});
            }
        });
	}
	
	@Override
	public void onClick(int slot) {
		switch(slot) {
		case 11:
			player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
			close();
			new NBTAddItem(plugin,player).addSellingItem(-1);
			//アイテムを回収
			int id = new NBTBuyList(plugin,player).getSelectItem();
			Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
	            @Override
	            public void run() {
	            	//out of minecraft
	            	ExpiredDatabase database = new ExpiredDatabase(plugin);
	            	AuctionItemData data = database.getIDItem(id);
	            	database.removeItem(id);
	            	Bukkit.getScheduler().runTask(plugin, new Runnable() {
	                    @Override
	                    public void run() {
	                    	//in minecraft
	                    	ArrayList<ItemStack> items = new ArrayList<ItemStack>();
	                    	items.add(data.getItem());
	                    	new StorageSystem(plugin,player).giveItem(items);
	                    	
	            			new GuiSellList(plugin,player).create();
	                    }
	            	});
	            }
	        });
			break;
		case 15:
			player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
			close();
			new GuiSellList(plugin,player).create();
			break;
		}
	}
	
}
