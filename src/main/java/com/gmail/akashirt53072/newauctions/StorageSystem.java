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
		for(int i = 0;i < items.size();i++) {
			ItemStack item = items.get(i);
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
		message.addExtra(ChatColor.YELLOW + "ストレージに" + count + "個の未回収のアイテムが残っています");
		message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/storage"));
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
                    	ArrayList<PlayerItemData> newData = new ArrayList<PlayerItemData>();
                    	for(int i = 0;i < data.size();i++) {
                			PlayerItemData itemData = data.get(i);
                			ItemStack item = itemData.getItem();
                			if(inv.firstEmpty() == -1) {
                				break;
                			}
                			inv.addItem(item);
                			newData.add(itemData);
                			data.remove(i);
                		}
                    	if(newData.isEmpty()) {
                			player.sendTitle(ChatColor.RED + "インベントリがいっぱいです！", " ", 0, 30, 20);
                			player.sendMessage(ChatColor.RED + "インベントリがいっぱいです！");
            				return;
            			}else {
            				player.sendMessage(ChatColor.GREEN + String.valueOf(newData.size()) + "個のアイテムをストレージから取り出しました");
            			}
                    	if(!data.isEmpty()) {
                			player.sendTitle(ChatColor.RED + "インベントリがいっぱいです！", " ", 0, 30, 20);
                			player.sendMessage(ChatColor.RED + "インベントリがいっぱいです！");
                			player.sendMessage(ChatColor.YELLOW + String.valueOf(data.size()) + "個のアイテムがストレージに残っています");
                		}
                		//giveできた分データ削除
                		new NBTStorage(plugin,player).addCount(0 - newData.size());
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
