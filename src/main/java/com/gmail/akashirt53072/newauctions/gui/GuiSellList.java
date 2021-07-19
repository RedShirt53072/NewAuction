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

public class GuiSellList extends Gui{
	public GuiSellList(Main plugin, Player player) {
		super(plugin, player);
	}
	public GuiSellList(Main plugin, Player player,Inventory inventory) {
		super(plugin, player,inventory);
	}
	
	@Override
	public void create() {
		new NBTGui(plugin,player).setID(GuiID.SELLLIST);
		inv = Bukkit.createInventory(null, 54, "売却中アイテム一覧");
		for(int index = 0;index < 54;index ++) {
			switch(index){
			case 20:
			case 21:
			case 22:
			case 23:
			case 24:
			case 29:
			case 30:
			case 31:
			case 32:
			case 33:
			case 38:
			case 39:
			case 40:
			case 41:
			case 42:
				break;
			case 2:
				inv.setItem(index, createItem(Material.GLOW_ITEM_FRAME,ChatColor.WHITE + "購入メニューへ",null,1,null));
				break;
			case 4:
				inv.setItem(index, createItem(Material.CHEST_MINECART,ChatColor.WHITE + "売却中アイテム一覧",null,1,Enchantment.MENDING));
				break;
			case 6:
				inv.setItem(index, createItem(Material.HOPPER,ChatColor.WHITE + "新規出品メニューへ",null,1,null));
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
		case 2:
			player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
			close();
			new GuiBuyList(plugin,player).create();
			break;
		case 6:
			player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
			close();
			new GuiAddItem(plugin,player).create();
			break;
		}
	}
	
}
