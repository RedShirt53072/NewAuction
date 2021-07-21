package com.gmail.akashirt53072.newauctions.gui;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import com.gmail.akashirt53072.newauctions.Main;
import com.gmail.akashirt53072.newauctions.StorageSystem;
import com.gmail.akashirt53072.newauctions.config.AuctionDatabase;
import com.gmail.akashirt53072.newauctions.config.SoldDatabase;
import com.gmail.akashirt53072.newauctions.datatype.AuctionItemData;
import com.gmail.akashirt53072.newauctions.nbt.NBTBuyList;
import com.gmail.akashirt53072.newauctions.nbt.NBTGui;

public class GuiBuyConfirm extends Gui{
	public GuiBuyConfirm(Main plugin, Player player) {
		super(plugin, player);
	}
	public GuiBuyConfirm(Main plugin, Player player,Inventory inventory) {
		super(plugin, player,inventory);
	}
	
	@Override
	public void create() {
		new NBTGui(plugin,player).setID(GuiID.BUYCONFIRM);
		inv = Bukkit.createInventory(null, 27, "確認：アイテムを購入しますか？");
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
			//非同期でアイテムデータ読み込んで削除と与える、送金、購入リスト開く処理をする
			int id = new NBTBuyList(plugin,player).getSelectItem();
			Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
	            @Override
	            public void run() {
	            	//out of minecraft
	            	//データベース移動
	            	AuctionDatabase database = new AuctionDatabase(plugin);
	            	AuctionItemData data = database.getIDItem(id);
	            	if(data == null) {
	            		Bukkit.getScheduler().runTask(plugin, new Runnable() {
		                    @Override
		                    public void run() {
		                    	player.sendMessage(ChatColor.RED + "このアイテムは出品が終了しました");
		                    	new GuiBuyList(plugin,player).create();
		                    }
		                });
	            		return;
	            	}
	            	database.removeItem(id);
	            	new SoldDatabase(plugin).addItem(data.getPlayer(), data.getItem(), data.getPrice(), id);
	            	
	            	Bukkit.getScheduler().runTask(plugin, new Runnable() {
	                    @Override
	                    public void run() {
	                    	//in minecraft
	                    	ItemStack item = data.getItem();
	                    	ArrayList<ItemStack> items = new ArrayList<ItemStack>();
	                    	items.add(item);
	                    	new StorageSystem(plugin,player).giveItem(items);
	                    	//送金
	            			Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
	            			Objective obj = board.getObjective("emerald");
	            			Score score = obj.getScore(player.getName());
	            			int now = score.getScore();
	            			now -= data.getPrice();
	            			score.setScore(now);
	                    	new GuiBuyList(plugin,player).create();
	                    }
	            	});
	            }
	        });
			
			break;
		case 15:
			player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
			close();
			GuiBuyItem gui = new GuiBuyItem(plugin,player);
			gui.create();
			gui.asyncLoad(new NBTBuyList(plugin,player).getSelectItem());
			break;
		}
	}
	
}
