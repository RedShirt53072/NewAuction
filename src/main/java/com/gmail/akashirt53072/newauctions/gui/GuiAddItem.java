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

public class GuiAddItem extends Gui{
	public GuiAddItem(Main plugin, Player player) {
		super(plugin, player);
	}
	public GuiAddItem(Main plugin, Player player,Inventory inventory) {
		super(plugin, player,inventory);
	}
	
	@Override
	public void create() {
		new NBTGui(plugin,player).setID(GuiID.BUYLIST);
		inv = Bukkit.createInventory(null, 54, "新規出品メニュー");
		for(int index = 0;index < 54;index ++) {
			switch(index){
			case 22:
				break;
			case 31:
				//キャッシュ次第で分岐入る
				inv.setItem(index, createItem(Material.GOLD_NUGGET,ChatColor.WHITE + "売却価格：100$",null,1,null));
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
				//キャッシュ次第で分岐入る
				inv.setItem(index, createItem(Material.LIME_WOOL,ChatColor.WHITE + "決定",null,1,null));
				break;
			case 40:
				inv.setItem(index, createItem(Material.BIRCH_SIGN,ChatColor.WHITE + "値段を入力",null,1,null));
				break;
			case 42:
				inv.setItem(index, createItem(Material.ARROW,ChatColor.WHITE + "キャンセル",null,1,null));
				break;
			default:
				inv.setItem(index, createItem(Material.BLACK_STAINED_GLASS_PANE,"",null,1,null));
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
		case 4:
			player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
			close();
			new GuiSellList(plugin,player).create();
			break;
		case 38:
			//キャッシュ次第で分岐入る
			player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
			close();
			new GuiSellConfirm(plugin,player).create();
			break;
		case 40:
			player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
			close();
			new NBTGui(plugin,player).setID(GuiID.EDITPRICE);
			break;
		case 42:
			player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
			//リセット
			//あればアイテム戻す			
			break;
		}
	}
	
}
