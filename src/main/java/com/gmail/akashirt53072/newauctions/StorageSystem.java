package com.gmail.akashirt53072.newauctions;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.gmail.akashirt53072.newauctions.config.StorageDatabase;
import com.gmail.akashirt53072.newauctions.datatype.PlayerItemData;
import com.gmail.akashirt53072.newauctions.nbt.NBTStorage;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class StorageSystem {
	private Main plugin;
	private Player player;
	public StorageSystem(Main plugin,Player player) {
		this.plugin = plugin;
		this.player = player;
	}
	public void giveItem(ArrayList<ItemStack> items) {
		Inventory inv = player.getInventory();
		int length = items.size();
		ArrayList<ItemStack> items2 = new ArrayList<ItemStack>();
		for(ItemStack i : items) {
			items2.add(i);
		}
		
		for(int i = length - 1;i > -1;i--) {
			ItemStack item = items2.get(i);
			if(inv.firstEmpty() == -1) {
				break;
			}
			inv.addItem(item);
			items.remove(i);
		}
		if(items.isEmpty()) {
			return;
		}
		saveItemData(items);
	}
	
	private void saveItemData(ArrayList<ItemStack> items) {
		UUID uuid = player.getUniqueId();
		new NBTStorage(plugin,player).addCount(items.size());
		player.sendTitle(ChatColor.RED + "インベントリがいっぱいです！", " ", 0, 30, 20);
		player.sendMessage(ChatColor.RED + "インベントリがいっぱいです！");
		player.sendMessage(ChatColor.YELLOW + String.valueOf(items.size()) + "個のアイテムがストレージに送られました");
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
            	//out of minecraft
            	StorageDatabase database = new StorageDatabase(plugin);
            	for(ItemStack item : items) {
            		database.addItem(uuid,item);
            	}
            }
		});
	}
	
	public void loop() {
		//与えてないのあればチャット出す
		int count = new NBTStorage(plugin,player).getCount();
		if(count == 0) {
			return;
		}
		TextComponent message = new TextComponent();
		message.addExtra(ChatColor.WHITE + "ここをクリックでアイテムを取り出す");
		message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/storage"));
		player.sendMessage(ChatColor.YELLOW + "ストレージに" + count + "個の未回収のアイテムが残っています");
		player.spigot().sendMessage(message);
		
	}
	
	public void giveFromData() {
		UUID uuid = player.getUniqueId();
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
            	//out of minecraft
            	StorageDatabase database = new StorageDatabase(plugin);
            	ArrayList<PlayerItemData> data = database.getPlayerItem(uuid);
            	Bukkit.getScheduler().runTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                    	//in minecraft
                    	Inventory inv = player.getInventory();
                    	if(data.isEmpty()) {
                    		new NBTStorage(plugin,player).addCount(-100);
                    		player.sendMessage(ChatColor.YELLOW + "ストレージは空です");
                    		return;
                    	}
                    	ArrayList<PlayerItemData> newData = new ArrayList<PlayerItemData>();
                    	int length = data.size();
                    	int length2 = length;
                    	for(int i = 0;i < length - 1;i++) {
                			PlayerItemData itemData = data.get(i);
                			ItemStack item = itemData.getItem();
                			if(inv.firstEmpty() == -1) {
                				break;
                			}
                			inv.addItem(item);
                			newData.add(itemData);
                			length2 --;
                		}
                    	if(newData.isEmpty()) {
                			player.sendTitle(ChatColor.RED + "インベントリがいっぱいです！", " ", 0, 30, 20);
                			player.sendMessage(ChatColor.RED + "インベントリがいっぱいです！");
            				return;
            			}else {
            				player.sendMessage(ChatColor.GREEN + String.valueOf(newData.size()) + "個のアイテムをストレージから取り出しました");
            			}
                    	if(length2 != 0) {
                			player.sendTitle(ChatColor.RED + "インベントリがいっぱいです！", " ", 0, 30, 20);
                			player.sendMessage(ChatColor.RED + "インベントリがいっぱいです！");
                			player.sendMessage(ChatColor.YELLOW + String.valueOf(length2) + "個のアイテムがストレージに残っています");
                			new NBTStorage(plugin,player).addCount(0 - newData.size());
                    	}else {
                			new NBTStorage(plugin,player).addCount(-100);
                		}
                		//giveできた分データ削除
                		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
                            @Override
                            public void run() {
                            	//out of minecraft
                            	StorageDatabase database = new StorageDatabase(plugin);
                            	for(PlayerItemData itemData : newData) {
                            		int id = itemData.getIndex();
                                	database.removeItem(uuid, id);
                            	}
                            }
                		});
                    }
            	});
            }
		});
	}
}
