package com.gmail.akashirt53072.newauctions.gui;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.gmail.akashirt53072.newauctions.Main;
import com.gmail.akashirt53072.newauctions.config.AuctionDatabase;
import com.gmail.akashirt53072.newauctions.config.PreAddDatabase;
import com.gmail.akashirt53072.newauctions.datatype.PlayerItemData;
import com.gmail.akashirt53072.newauctions.nbt.NBTAddItem;
import com.gmail.akashirt53072.newauctions.nbt.NBTGui;

public class GuiSellConfirm extends Gui{
	public GuiSellConfirm(Main plugin, Player player) {
		super(plugin, player);
	}
	public GuiSellConfirm(Main plugin, Player player,Inventory inventory) {
		super(plugin, player,inventory);
	}
	
	@Override
	public void create() {
		new NBTGui(plugin,player).setID(GuiID.SELLCONFIRM);
		inv = Bukkit.createInventory(null, 27, "確認：アイテムを出品しますか？");
		for(int index = 0;index < 27;index ++) {
			switch(index){
			case 11:
				inv.setItem(index, createItem(Material.LIME_WOOL,ChatColor.WHITE + "決定",null,1,null));
				break;
			case 15:
				inv.setItem(index, createItem(Material.RED_WOOL,ChatColor.WHITE + "キャンセル",null,1,null));
				break;
			default:
				inv.setItem(index, createItem(Material.BLACK_STAINED_GLASS_PANE," ",null,1,null));
			}
		}
		player.openInventory(inv);
	}
	@Override
	public void onClick(int slot) {
		switch(slot) {
		case 11:
			player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
			close();
			//非同期でキャッシュ削除、購入リスト登録、新規出品開く処理をする
			UUID uuid = player.getUniqueId();
			NBTAddItem nbt = new NBTAddItem(plugin,player);
			int price = nbt.getPrice();
			nbt.addSellingItem(1);
			nbt.setPreItem(false);
			Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
	            @Override
	            public void run() {
	            	//out of minecraft
	            	PreAddDatabase preDatabase = new PreAddDatabase(plugin);
	            	PlayerItemData itemData = preDatabase.getPlayerItem(uuid);
	            	preDatabase.removeItem(uuid);
	            	new AuctionDatabase(plugin).addItem(uuid, itemData.getItem(), price);
	            	Bukkit.getScheduler().runTask(plugin, new Runnable() {
	                    @Override
	                    public void run() {
	                    	//in minecraft
	                    	new GuiSellList(plugin,player).create();
	                    }
	            	});
	            }
	        });
			break;
		case 15:
			player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
			close();
			new GuiAddItem(plugin,player).create();
			break;
		}
	}
	
}
