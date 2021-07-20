package com.gmail.akashirt53072.newauctions.datatype;

import java.util.UUID;

import org.bukkit.inventory.ItemStack;

public class AuctionItemData {
	private int price;
	private int time;
	private int id;
	private ItemStack nbt;
	private UUID uuid;
	public AuctionItemData(int price ,int time, ItemStack nbt ,UUID uuid,int id) {
		this.price = price;
		this.nbt = nbt;
		this.time = time;
		this.uuid = uuid;
		this.id = id;
	}
	public int getPrice() {
		return price;
	}
	public int getTime() {
		return time;
	}
	public int getid() {
		return id;
	}
	public UUID getPlayer() {
		return uuid;
	}
	public ItemStack getItem() {
		return nbt;
	}
}
