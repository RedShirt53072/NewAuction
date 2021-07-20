package com.gmail.akashirt53072.newauctions.gui;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.akashirt53072.newauctions.Main;
import com.gmail.akashirt53072.newauctions.datatype.ItemType;
import com.gmail.akashirt53072.newauctions.nbt.NBTGui;
import com.gmail.akashirt53072.newauctions.nbt.NBTItemType;

public abstract class Gui {
	protected Player player;
	protected  Main plugin;
	protected Inventory inv;
	public Gui(Main plugin,Player player) {
	    this.player = player;
	    this.plugin = plugin;
	}
	
	protected Gui(Main plugin,Player player,Inventory inventory) {
	    this.player = player;
	    this.plugin = plugin;
	    this.inv = inventory;
	}
	protected ItemStack createItem(Material material,String name,ArrayList<String> lore,int amount,Enchantment ench){
		final ItemStack item = new ItemStack(material, amount);
	    final ItemMeta meta = item.getItemMeta();
	    meta.setDisplayName(name);
	    if(lore != null) {
	    	meta.setLore(lore);
	    }
	    if(ench != null) {
	    	meta.addEnchant(ench, 1, true);
	    }
	    meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
	    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
	    item.setItemMeta(meta);
	    new NBTItemType(plugin,item).init(ItemType.GUI);
		return item;
	}
	protected abstract void create();

	protected abstract void onClick(int slot);

	protected void close() {
		onClose();
		player.closeInventory();
	}
	public void onClose() {
		new NBTGui(plugin,player).setID(GuiID.NONE);
	}
}
