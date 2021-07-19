package com.gmail.akashirt53072.newauctions.gui;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.gmail.akashirt53072.newauctions.Main;
import com.gmail.akashirt53072.newauctions.PlayerItemData;
import com.gmail.akashirt53072.newauctions.config.PreAddDatabase;
import com.gmail.akashirt53072.newauctions.nbt.NBTAddItem;
import com.gmail.akashirt53072.newauctions.nbt.NBTGui;

public class GuiAddItem extends Gui{
	public GuiAddItem(Main plugin, Player player) {
		super(plugin, player);
	}
	public GuiAddItem(Main plugin, Player player,Inventory inventory) {
		super(plugin, player,inventory);
	}
	
	@Override
	public void create() {
		new NBTGui(plugin,player).setID(GuiID.ADDITEM);
		inv = Bukkit.createInventory(null, 54, "新規出品メニュー");
		boolean hasPreItem = new NBTAddItem(plugin,player).hasPreItem();
		for(int index = 0;index < 54;index ++) {
			switch(index){
			case 22:
				if(hasPreItem) {
					break;
				}
				inv.setItem(index, createItem(Material.STONE_BUTTON,ChatColor.WHITE + "手持ちアイテムをクリックで追加",null,1,null));
				break;
			case 31:
				int price = new NBTAddItem(plugin,player).getPrice();
				inv.setItem(index, createItem(Material.GOLD_NUGGET,ChatColor.WHITE + "売却価格：" + price +"$",null,1,null));
				break;
			case 2:
				inv.setItem(index, createItem(Material.GLOW_ITEM_FRAME,ChatColor.WHITE + "購入メニューへ",null,1,null));
				break;
			case 4:
				inv.setItem(index, createItem(Material.CHEST_MINECART,ChatColor.WHITE + "売却中アイテム一覧へ",null,1,null));
				break;
			case 6:
				inv.setItem(index, createItem(Material.HOPPER,ChatColor.WHITE + "新規出品メニュー",null,1,Enchantment.MENDING));
				break;
			case 38:
				if(hasPreItem) {
					inv.setItem(index, createItem(Material.LIME_WOOL,ChatColor.WHITE + "決定",null,1,null));
				}else {
					inv.setItem(index, createItem(Material.BLACK_STAINED_GLASS_PANE," ",null,1,null));
				}
				break;
			case 40:
				inv.setItem(index, createItem(Material.BIRCH_SIGN,ChatColor.WHITE + "値段を入力",null,1,null));
				break;
			case 42:
				if(hasPreItem) {
					inv.setItem(index, createItem(Material.ARROW,ChatColor.WHITE + "キャンセル",null,1,null));
				}else {
					inv.setItem(index, createItem(Material.BLACK_STAINED_GLASS_PANE," ",null,1,null));					
				}
				break;
			default:
				inv.setItem(index, createItem(Material.BLACK_STAINED_GLASS_PANE," ",null,1,null));
			}
		}
		player.openInventory(inv);
		if(hasPreItem) {
			//非同期でアイテムデータ読み込んで表示させる
			UUID uuid = player.getUniqueId();
			Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
	            @Override
	            public void run() {
	            	//out of minecraft
	            	PreAddDatabase database = new PreAddDatabase(plugin);
	            	PlayerItemData itemData = database.getPlayerItem(uuid);
	            	Bukkit.getScheduler().runTask(plugin, new Runnable() {
	                    @Override
	                    public void run() {
	                    	//in minecraft
	                    	if(itemData == null) {
	                    		plugin.getLogger().warning("出品予定アイテムの表示にて発生した問題：プレイヤー"+ Bukkit.getPlayer(uuid).getName() + "の出品予定アイテムのデータがnullです");
	                    		return;
	                    	}
	                    	ItemStack item = itemData.getItem();
	                    	if(new NBTGui(plugin,player).getID().equals(GuiID.NONE)) {
	                    		return;
	                    	}
	                    	inv = player.getOpenInventory().getTopInventory();
	                    	inv.setItem(22, item);
	                    }
	            	});
	            }
	    	});
		}
	}
	@Override
	public void onClick(int slot) {
		switch(slot) {
		case 2:
			player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
			close();
			new GuiBuyList(plugin,player).create();
			break;
		case 4:
			player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
			close();
			new GuiSellList(plugin,player).create();
			break;
		case 38:
			if(new NBTAddItem(plugin,player).hasPreItem()) {
				player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
				close();
				new GuiSellConfirm(plugin,player).create();
			}
			break;
		case 40:
			player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
			close();
			new NBTGui(plugin,player).setID(GuiID.EDITPRICE);
			player.sendMessage(ChatColor.YELLOW + "値段を数字でチャット欄に入力してください。");
			break;
		case 42:
			if(new NBTAddItem(plugin,player).hasPreItem()) {
				player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
				//アイテム戻す
				new NBTAddItem(plugin,player).setPreItem(false);
				inv = player.getOpenInventory().getTopInventory();
				inv.setItem(22, createItem(Material.STONE_BUTTON,ChatColor.WHITE + "手持ちアイテムをクリックで追加",null,1,null));
				inv.setItem(38, createItem(Material.BLACK_STAINED_GLASS_PANE," ",null,1,null));
				inv.setItem(42, createItem(Material.BLACK_STAINED_GLASS_PANE," ",null,1,null));		
				UUID uuid = player.getUniqueId();
				Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
		            @Override
		            public void run() {
		            	//out of minecraft
		            	PreAddDatabase database = new PreAddDatabase(plugin);
		            	PlayerItemData itemData = database.getPlayerItem(uuid);
		            	database.removeItem(uuid);
		            	Bukkit.getScheduler().runTask(plugin, new Runnable() {
		                    @Override
		                    public void run() {
		                    	//in minecraft
		                    	if(itemData == null) {
		                    		plugin.getLogger().warning("出品予定アイテムの返却にて発生した問題：プレイヤー"+ Bukkit.getPlayer(uuid).getName() + "の出品予定アイテムのデータがnullです");
		                    		return;
		                    	}
		                    	ItemStack item = itemData.getItem();
		                    	//storageシステムに移行すべし
		                    	player.getInventory().addItem(item);
		                    }
		            	});
		            }
		    	});
			}
			break;
		}
	}
	public boolean onInvClick(ItemStack item) {
		//プレイヤーの普通のアイテムがクリックされた
		if(new NBTAddItem(plugin,player).hasPreItem()) {
			return false;
		}
		if(item == null) {
			return false;
		}
		//アイテムを保存する
		//決定を表示する
		player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
		UUID uuid = player.getUniqueId();
		
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
            	//out of minecraft
            	PreAddDatabase database = new PreAddDatabase(plugin);
            	database.addItem(uuid, item);
            	Bukkit.getScheduler().runTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                    	//in minecraft
                    	new NBTAddItem(plugin,player).setPreItem(true);
                    	if(new NBTGui(plugin,player).getID().equals(GuiID.NONE)) {
                    		return;
                    	}
                    	inv = player.getOpenInventory().getTopInventory();
                    	inv.setItem(38, createItem(Material.LIME_WOOL,ChatColor.WHITE + "決定",null,1,null));
                    	inv.setItem(42, createItem(Material.ARROW,ChatColor.WHITE + "キャンセル",null,1,null));
                    	inv.setItem(22, item);
                    }
            	});
            }
    	});
		return true;
	}
}
