package com.gmail.akashirt53072.newauctions.datatype;

import java.util.UUID;

import org.bukkit.inventory.ItemStack;

public class PlayerItemData {
	private int index;
	private ItemStack nbt;
	private UUID uuid;
	public PlayerItemData(ItemStack nbt ,UUID uuid,int index) {
		this.nbt = nbt;
		this.uuid = uuid;
		this.index = index;
	}
	public int getIndex() {
		return index;
	}
	public UUID getPlayer() {
		return uuid;
	}
	public ItemStack getItem() {
		return nbt;
	}
}
