package com.gmail.akashirt53072.newauctions;

import java.util.UUID;

import org.bukkit.inventory.ItemStack;

public class AuctionItemData {
	private int price;
	private int time;
	private int index;
	private ItemStack nbt;
	private UUID uuid;
	public AuctionItemData(int price ,int time, ItemStack nbt ,UUID uuid,int index) {
		this.price = price;
		this.nbt = nbt;
		this.time = time;
		this.uuid = uuid;
		this.index = index;
	}
	public int getPrice() {
		return price;
	}
	public int getTime() {
		return time;
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
