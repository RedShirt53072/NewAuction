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
		//非同期でアイテムデータ読み込んで表示させる
	}
	@Override
	public void onClick(int slot) {
		switch(slot) {
		case 11:
			player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
			//アイテムを回収
			
			close();
			new GuiSellList(plugin,player).create();
			break;
		case 15:
			player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
			close();
			new GuiSellList(plugin,player).create();
			break;
		}
	}
	
}
