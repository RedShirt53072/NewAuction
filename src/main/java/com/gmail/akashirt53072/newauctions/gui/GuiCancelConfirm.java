package com.gmail.akashirt53072.newauctions.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.gmail.akashirt53072.newauctions.Main;
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
		new NBTGui(plugin,player).setID(GuiID.BUYCONFIRM);
		inv = Bukkit.createInventory(null, 27, "確認：出品中のアイテムを回収しますか？");
		for(int index = 0;index < 27;index ++) {
			switch(index){
			case 11:
				inv.setItem(index, createItem(Material.LIME_WOOL,ChatColor.WHITE + "決定",null,1,Enchantment.MENDING));
				break;
			case 15:
				inv.setItem(index, createItem(Material.RED_WOOL,ChatColor.WHITE + "キャンセル",null,1,null));
				break;
			default:
				inv.setItem(index, createItem(Material.BLACK_STAINED_GLASS_PANE,"",null,1,null));
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
			//非同期でアイテムデータ読み込んで削除と与える、売却リスト開く処理をする
			
			break;
		case 15:
			player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
			close();
			new GuiCancelOrder(plugin,player).create();
			break;
		}
	}
	
}
