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

public class GuiBuyList extends Gui{
	public GuiBuyList(Main plugin, Player player) {
		super(plugin, player);
	}
	public GuiBuyList(Main plugin, Player player,Inventory inventory) {
		super(plugin, player,inventory);
	}
	
	@Override
	public void create() {
		new NBTGui(plugin,player).setID(GuiID.BUYLIST);
		inv = Bukkit.createInventory(null, 54, "購入メニュー");
		for(int index = 0;index < 54;index ++) {
			switch(index){
			case 0:
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 9:
			case 17:
			case 18:
			case 26:
			case 27:
			case 35:
			case 36:
			case 44:
			case 46:
			case 47:
			case 48:
			case 49:
			case 50:
			case 51:
			case 52:
				inv.setItem(index, createItem(Material.BLACK_STAINED_GLASS_PANE,"",null,1,null));
				break;
			case 2:
				inv.setItem(index, createItem(Material.GLOW_ITEM_FRAME,ChatColor.WHITE + "購入メニュー",null,1,Enchantment.MENDING));
				break;
			case 4:
				inv.setItem(index, createItem(Material.CHEST_MINECART,ChatColor.WHITE + "売却中アイテム一覧へ",null,1,null));
				break;
			case 6:
				inv.setItem(index, createItem(Material.HOPPER,ChatColor.WHITE + "新規出品メニューへ",null,1,null));
				break;
			case 45:
				inv.setItem(index, createItem(Material.ARROW,ChatColor.WHITE + "前のページへ",null,1,null));
				break;
			case 53:
				inv.setItem(index, createItem(Material.ARROW,ChatColor.WHITE + "次のページへ",null,1,null));
				break;
			}
		}
		player.openInventory(inv);
		//非同期でアイテムデータ読み込んで表示させる
	}
	@Override
	public void onClick(int slot) {
		switch(slot) {
		case 4:
			player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
			close();
			new GuiSellList(plugin,player).create();
			break;
		case 6:
			player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
			close();
			new GuiAddItem(plugin,player).create();
			break;
		case 45:
			player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
			//非同期でアイテムデータ読み込んで表示させる
			break;
		case 53:
			player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
			//非同期でアイテムデータ読み込んで表示させる
			break;
		}
	}
	
}
