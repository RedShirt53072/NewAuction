package com.gmail.akashirt53072.newauctions.gui;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.gmail.akashirt53072.newauctions.Main;
import com.gmail.akashirt53072.newauctions.StorageSystem;
import com.gmail.akashirt53072.newauctions.config.AuctionDatabase;
import com.gmail.akashirt53072.newauctions.datatype.AuctionItemData;
import com.gmail.akashirt53072.newauctions.nbt.NBTAddItem;
import com.gmail.akashirt53072.newauctions.nbt.NBTBuyList;
import com.gmail.akashirt53072.newauctions.nbt.NBTGui;

public class GuiCancelConfirm extends Gui{
	public GuiCancelConfirm(Main plugin, Player player) {
		super(plugin, player);
	}
	public GuiCancelConfirm(Main plugin, Player player,Inventory inventory) {
		super(plugin, player,inventory);
	}
	
	@Override
	public void create() {
		new NBTGui(plugin,player).setID(GuiID.CANCELCONFIRM);
		inv = Bukkit.createInventory(null, 27, "確認：出品中のアイテムを回収しますか？");
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
			new NBTAddItem(plugin,player).addSellingItem(-1);
			//非同期でアイテムデータ読み込んで削除と与える、売却リスト開く処理をする
			int id = new NBTBuyList(plugin,player).getSelectItem();
			Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
	            @Override
	            public void run() {
	            	//out of minecraft
	            	AuctionDatabase database = new AuctionDatabase(plugin);
	            	AuctionItemData data = database.getIDItem(id);
	            	if(data == null) {
	            		Bukkit.getScheduler().runTask(plugin, new Runnable() {
		                    @Override
		                    public void run() {
		                    	player.sendMessage(ChatColor.RED + "このアイテムは既に売却が完了しています");
		                    	new GuiSellList(plugin,player).create();
		                    }
		                });
	            		return;
	            	}
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
			GuiCancelOrder gui = new GuiCancelOrder(plugin,player);
			gui.create();
			gui.asyncLoad(new NBTBuyList(plugin,player).getSelectItem());
			break;
		}
	}
	
}
