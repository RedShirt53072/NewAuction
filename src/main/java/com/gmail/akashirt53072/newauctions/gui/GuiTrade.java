package com.gmail.akashirt53072.newauctions.gui;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import com.gmail.akashirt53072.newauctions.Main;
import com.gmail.akashirt53072.newauctions.StorageSystem;
import com.gmail.akashirt53072.newauctions.TradeLoop;
import com.gmail.akashirt53072.newauctions.config.TradeDatabase;
import com.gmail.akashirt53072.newauctions.datatype.ItemType;
import com.gmail.akashirt53072.newauctions.datatype.PlayerItemData;
import com.gmail.akashirt53072.newauctions.nbt.NBTGui;
import com.gmail.akashirt53072.newauctions.nbt.NBTItemType;
import com.gmail.akashirt53072.newauctions.nbt.NBTTrade;
import com.gmail.akashirt53072.newauctions.nbt.NBTTradeItem;

public class GuiTrade extends Gui{
	private Player target;
	public GuiTrade(Main plugin, Player player) {
		super(plugin, player);
	}
	public GuiTrade(Main plugin, Player player,Player target) {
		super(plugin, player);
		this.target = target;
	}
	public GuiTrade(Main plugin, Player player,Inventory inventory) {
		super(plugin, player,inventory);
	}
	
	@Override
	public void create() {
		new NBTGui(plugin,player).setID(GuiID.TRADE);
		new NBTTrade(plugin,player).setPlayer(target);
		inv = Bukkit.createInventory(null, 54, "トレード");
		for(int index = 0;index < 54;index ++) {
			switch(index){
			case 0:
			case 1:
			case 2:
			case 4:
			case 6:
			case 7:
			case 8:
			case 13:
			case 22:
			case 31:
			case 40:
			case 47:
			case 49:
			case 51:
			case 52:
				inv.setItem(index, createItem(Material.BLACK_STAINED_GLASS_PANE," ",null,1,null));
				break;
			case 3:
				ItemStack item1 = createItem(Material.PLAYER_HEAD,ChatColor.WHITE + player.getName(),null,1,null);
				SkullMeta meta1 = (SkullMeta)item1.getItemMeta();
				meta1.setOwningPlayer(player);
				
				item1.setItemMeta(meta1);
				inv.setItem(index, item1);
				break;
			case 5:
				ItemStack item2 = createItem(Material.PLAYER_HEAD,ChatColor.WHITE + target.getName(),null,1,null);
				SkullMeta meta2 = (SkullMeta)item2.getItemMeta();
				meta2.setOwningPlayer(target);
				
				item2.setItemMeta(meta2);
				inv.setItem(index, item2);
				break;
			case 45:
				//値段
				inv.setItem(index, createItem(Material.GOLD_NUGGET,ChatColor.GOLD + "0$",null,1,null));
				break;
			case 46:
				inv.setItem(index, createItem(Material.BIRCH_SIGN,ChatColor.WHITE + "金額を入力",null,1,null));
				break;
			case 48:
				inv.setItem(index, createItem(Material.LIGHT_GRAY_WOOL,ChatColor.WHITE + "クリックでトレード内容を確定",null,1,null));
				break;
			case 50:
				inv.setItem(index, createItem(Material.LIGHT_GRAY_WOOL,ChatColor.WHITE + "まだトレード内容を確定していません",null,1,null));
				break;
			case 53:
				inv.setItem(index, createItem(Material.GOLD_NUGGET,ChatColor.GOLD + "0$",null,1,null));
				break;
			default:
			}
		}
		player.openInventory(inv);
		
	}
	public void onReturn() {
		new NBTGui(plugin,player).setID(GuiID.TRADE);
		target = new NBTTrade(plugin,player).getPlayer();
		inv = Bukkit.createInventory(null, 54, "トレード");
		for(int index = 0;index < 54;index ++) {
			switch(index){
			case 0:
			case 1:
			case 2:
			case 4:
			case 6:
			case 7:
			case 8:
			case 13:
			case 22:
			case 31:
			case 40:
			case 47:
			case 49:
			case 51:
			case 52:
				inv.setItem(index, createItem(Material.BLACK_STAINED_GLASS_PANE," ",null,1,null));
				break;
			case 3:
				ItemStack item1 = createItem(Material.PLAYER_HEAD,ChatColor.WHITE + player.getName(),null,1,null);
				SkullMeta meta1 = (SkullMeta)item1.getItemMeta();
				meta1.setOwningPlayer(player);
				
				item1.setItemMeta(meta1);
				inv.setItem(index, item1);
				break;
			case 5:
				ItemStack item2 = createItem(Material.PLAYER_HEAD,ChatColor.WHITE + target.getName(),null,1,null);
				SkullMeta meta2 = (SkullMeta)item2.getItemMeta();
				meta2.setOwningPlayer(target);
				
				item2.setItemMeta(meta2);
				inv.setItem(index, item2);
				break;
			case 45:
				//値段
				int playerPrice = new NBTTrade(plugin,player).getPrice();
				Enchantment ench1 = null;
				int amount1 = 1;
				if(playerPrice > 1) {
					ench1 = Enchantment.MENDING;
					String priceText = String.valueOf(playerPrice);
					String c = priceText.substring(0, 1);
					if(priceText.length() > 1) {
						String c2 = priceText.substring(0,2);
						amount1 = Integer.valueOf(c2); 
					}else {
						amount1 = Integer.valueOf(c);
					}
				}
				inv.setItem(index, createItem(Material.GOLD_NUGGET,ChatColor.GOLD + String.valueOf(playerPrice) + "$",null,amount1,ench1));
				break;
			case 46:
				inv.setItem(index, createItem(Material.BIRCH_SIGN,ChatColor.WHITE + "金額を入力",null,1,null));
				break;
			case 48:
				int phase1 = new NBTTrade(plugin,player).getPhase();
				if(phase1 == 0) {
					inv.setItem(index, createItem(Material.LIGHT_GRAY_WOOL,ChatColor.WHITE + "クリックでトレード内容を確定",null,1,null));
				}
				if(phase1 == 1) {
					inv.setItem(index, createItem(Material.LIME_WOOL,ChatColor.WHITE + "クリックで確定をキャンセル",null,1,null));
				}
				if(phase1 > 9) {
					ArrayList<String> lore = new ArrayList<String>();
					lore.add(ChatColor.WHITE + "クリックでキャンセル");
					inv.setItem(index, createItem(Material.LIME_WOOL,ChatColor.WHITE + "あと" + (phase1 - 10) + "秒で取引が実行されます",lore,phase1 - 10,Enchantment.MENDING));
				}
				break;
			case 50:
				int phase2 = new NBTTrade(plugin,player).getPhase();
				if(phase2 == 0) {
					inv.setItem(index, createItem(Material.LIGHT_GRAY_WOOL,ChatColor.WHITE + "まだトレード内容を確定していません",null,1,null));
				}
				if(phase2 == 1) {
					inv.setItem(index, createItem(Material.LIME_WOOL,ChatColor.WHITE + "トレード内容を確定済み",null,1,null));
				}
				if(phase2 > 9) {
					inv.setItem(index, createItem(Material.LIME_WOOL,ChatColor.WHITE + "あと" + (phase2 - 10) + "秒で取引が実行されます",null,phase2 - 10,Enchantment.MENDING));
				}
				break;
			case 53:
				int targetPrice = new NBTTrade(plugin,target).getPrice();
				Enchantment ench = null;
				int amount = 1;
				if(targetPrice > 1) {
					ench = Enchantment.MENDING;
					String priceText = String.valueOf(targetPrice);
					String c = priceText.substring(0, 1);
					if(priceText.length() > 1) {
						String c2 = priceText.substring(0,2);
						amount = Integer.valueOf(c2); 
					}else {
						amount = Integer.valueOf(c);
					}
				}
				inv.setItem(index, createItem(Material.GOLD_NUGGET,ChatColor.GOLD + String.valueOf(targetPrice) + "$",null,amount,ench));
				break;
			default:
			}
		}
		player.openInventory(inv);
		loadItems(target);
    	new GuiTrade(plugin,target).update();
	}
	
	public void update() {
		inv = player.getOpenInventory().getTopInventory();
		if(!inv.getType().equals(InventoryType.CHEST)) {
			return;
		}
		target = new NBTTrade(plugin,player).getPlayer();
		for(int index = 0;index < 54;index ++) {
			switch(index){
			case 45:
				//値段
				int playerPrice = new NBTTrade(plugin,player).getPrice();
				Enchantment ench1 = null;
				int amount1 = 1;
				if(playerPrice > 1) {
					ench1 = Enchantment.MENDING;
					String priceText = String.valueOf(playerPrice);
					String c = priceText.substring(0, 1);
					if(priceText.length() > 1) {
						String c2 = priceText.substring(0,2);
						amount1 = Integer.valueOf(c2); 
					}else {
						amount1 = Integer.valueOf(c);
					}
				}
				if(amount1 > 64) {
					amount1 = 64;
				}
				inv.setItem(index, createItem(Material.GOLD_NUGGET,ChatColor.GOLD + String.valueOf(playerPrice) + "$",null,amount1,ench1));
				break;
			case 46:
				inv.setItem(index, createItem(Material.BIRCH_SIGN,ChatColor.WHITE + "金額を入力",null,1,null));
				break;
			case 48:
				int phase1 = new NBTTrade(plugin,player).getPhase();
				if(phase1 == 0) {
					inv.setItem(index, createItem(Material.LIGHT_GRAY_WOOL,ChatColor.WHITE + "クリックでトレード内容を確定",null,1,null));
				}
				if(phase1 == 1) {
					inv.setItem(index, createItem(Material.LIME_WOOL,ChatColor.WHITE + "クリックで確定をキャンセル",null,1,null));
				}
				if(phase1 > 9) {
					ArrayList<String> lore = new ArrayList<String>();
					lore.add(ChatColor.WHITE + "クリックでキャンセル");
					inv.setItem(index, createItem(Material.LIME_WOOL,ChatColor.WHITE + "あと" + (phase1 - 10) + "秒で取引が実行されます",lore,phase1 - 10,Enchantment.MENDING));
				}
				break;
			case 50:
				int phase2 = new NBTTrade(plugin,target).getPhase();
				if(phase2 == 0) {
					inv.setItem(index, createItem(Material.LIGHT_GRAY_WOOL,ChatColor.WHITE + "まだトレード内容を確定していません",null,1,null));
				}
				if(phase2 == 1) {
					inv.setItem(index, createItem(Material.LIME_WOOL,ChatColor.WHITE + "トレード内容を確定済み",null,1,null));
				}
				if(phase2 > 9) {
					inv.setItem(index, createItem(Material.LIME_WOOL,ChatColor.WHITE + "あと" + (phase2 - 10) + "秒で取引が実行されます",null,phase2 - 10,Enchantment.MENDING));
				}
				break;
			case 53:
				int targetPrice = new NBTTrade(plugin,target).getPrice();
				Enchantment ench = null;
				int amount = 1;
				if(targetPrice > 1) {
					ench = Enchantment.MENDING;
					String priceText = String.valueOf(targetPrice);
					String c = priceText.substring(0, 1);
					if(priceText.length() > 1) {
						String c2 = priceText.substring(0,2);
						amount = Integer.valueOf(c2); 
					}else {
						amount = Integer.valueOf(c);
					}
				}
				if(amount > 64) {
					amount = 64;
				}
				inv.setItem(index, createItem(Material.GOLD_NUGGET,ChatColor.GOLD + String.valueOf(targetPrice) + "$",null,amount,ench));
				break;
			default:
			}
		}
		loadItems(new NBTTrade(plugin,player).getPlayer());
	}
	
	public void loadItems(Player target) {
		UUID uuid1 = player.getUniqueId();
		UUID uuid2 = target.getUniqueId();
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
            	//out of minecraft
            	TradeDatabase database = new TradeDatabase(plugin);
            	ArrayList<PlayerItemData> dataList1 = database.getPlayerItem(uuid1);
            	ArrayList<PlayerItemData> dataList2 = database.getPlayerItem(uuid2);
            	
            	Bukkit.getScheduler().runTask(plugin, new Runnable() {
            		@Override
                    public void run() {
            			//in minecraft
                    	if(!new NBTGui(plugin,player).getID().equals(GuiID.TRADE)) {
                    		return;
                    	}
                    	for(int i = 0;i < 16;i ++) {
                    		int ind = i + 9 + (((i) / 4)* 5);
                    		inv.clear(ind);
                		}
                    	for(int i = 0;i < 16;i ++) {
                    		int ind = i + 14 + (((i) / 4)* 5);
                    		inv.clear(ind);
                		}
                    	inv = player.getOpenInventory().getTopInventory();
                    	for(int i = 0; i < dataList1.size();i ++) {
                    		PlayerItemData data = dataList1.get(i);
                    		ItemStack item = data.getItem();  
                        	new NBTItemType(plugin,item).init(ItemType.TRADEITEM);
                        	new NBTTradeItem(plugin,item).init(data.getIndex());
                    		int ind = i + 9 + (((i) / 4)* 5);
                        	inv.setItem(ind, item);
                    	}
                    	for(int i = 0; i < dataList2.size();i ++) {
                    		PlayerItemData data = dataList2.get(i);
                    		ItemStack item = data.getItem();  
                        	new NBTItemType(plugin,item).init(ItemType.GUI);
                        	int ind = i + 14 + (((i) / 4)* 5);
                        	inv.setItem(ind, item);
                    	}
                    	
                    }
            	});
            }
    	});
	}
	
	@Override
	public void onClick(int slot) {
		target = new NBTTrade(plugin,player).getPlayer();
		switch(slot) {
		case 46:
			player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
			new NBTGui(plugin,player).setID(GuiID.TRADEPRICE);
			player.closeInventory();
			
			player.sendMessage(ChatColor.YELLOW + "トレードする金額を数字でチャット欄に入力してください。");
			confirmCancel();
			break;
		case 48:
			player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
			NBTTrade nbt = new NBTTrade(plugin,player);
			int phase2 = nbt.getPhase();
			if(phase2 == 0) {
				NBTTrade targetnbt = new NBTTrade(plugin,target);
				if(targetnbt.getPhase() == 1) {
					targetnbt.setPhase(15);
					nbt.setPhase(15);
					//loop回す
					new TradeLoop(plugin,player,100).runTask(plugin);
					update();
					new GuiTrade(plugin,target).update();
					return;
				}
				nbt.setPhase(1);
				update();
				new GuiTrade(plugin,target).update();
				return;
			}
			if(phase2 == 1) {
				nbt.setPhase(0);
				update();
				new GuiTrade(plugin,target).update();
				return;
			}
			if(phase2 > 9) {
				confirmCancel();
				new GuiTrade(plugin,target).confirmCancel();
				return;
			}
			break;
		}
	}
	private void confirmCancel() {
		//トレードカウントダウンを止める
		target =  new NBTTrade(plugin,player).getPlayer();
		player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, (float)0.5);
		target.playSound(target.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, (float)0.5);
		
		new NBTTrade(plugin,player).setPhase(0);
		update();
		new GuiTrade(plugin,target).update();
	}
	
	public boolean onInvClick(ItemStack item) {
		//プレイヤーの普通のアイテムがクリックされた
		if(item == null) {
			return false;
		}
		NBTTrade nbt = new NBTTrade(plugin,player);
		if(nbt.getPreItem() == 16) {
			return false;
		}
		nbt.addPreItem(1);
		confirmCancel();
		//アイテムを保存する
		player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
		UUID uuid = player.getUniqueId();
		
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
            	//out of minecraft
            	TradeDatabase database = new TradeDatabase(plugin);
            	int id = database.addItem(uuid, item);
            	Bukkit.getScheduler().runTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                    	//in minecraft
                    	if(!new NBTGui(plugin,player).getID().equals(GuiID.TRADE)) {
                    		return;
                    	}
                    	inv = player.getOpenInventory().getTopInventory();  
                    	new NBTItemType(plugin,item).init(ItemType.TRADEITEM);
                    	new NBTTradeItem(plugin,item).init(id);
                    	int i = nbt.getPreItem() - 1;
                		int index = i + 9 + (((i) / 4)* 5);
                    	inv.setItem(index, item);
                    	confirmCancel();
                    }
            	});
            }
    	});
		return true;
	}
	public void onItemClick(ItemStack item) {
		int id = new NBTTradeItem(plugin,item).getID();
		NBTTrade nbt = new NBTTrade(plugin,player);
		nbt.addPreItem(-1);
		confirmCancel();
		
		UUID uuid = player.getUniqueId();
		player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
            	//out of minecraft
            	TradeDatabase database = new TradeDatabase(plugin);
            	PlayerItemData data = database.getIDItem(uuid,id);
            	database.removeItem(uuid, id);
            	Bukkit.getScheduler().runTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                    	//in minecraft
                    	if(data == null) {
                    		plugin.getLogger().warning("onItemClick");
                    		plugin.getLogger().warning("トレードのアイテムの返却にて発生した問題：プレイヤーのアイテムデータがnullです");
                    		return;
                    	}
                    	ItemStack item = data.getItem();
                    	ArrayList<ItemStack> items = new ArrayList<ItemStack>();
                    	items.add(item);
                    	new StorageSystem(plugin,player).giveItem(items);
                    	confirmCancel();
                    }
            	});
            }
        });
	}
	public void confirmClose() {
		target = new NBTTrade(plugin,player).getPlayer();
		int price = new NBTTrade(plugin,target).getPrice();
    	
		UUID uuid = target.getUniqueId();
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
            	//out of minecraft
            	TradeDatabase database = new TradeDatabase(plugin);
            	ArrayList<PlayerItemData> dataList = database.getPlayerItem(uuid);
            	database.removePlayerItem(uuid);
            	Bukkit.getScheduler().runTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                    	//in minecraft
                    	Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
            			Objective obj = board.getObjective("emerald");
            			Score score = obj.getScore(player.getName());
            			int now = score.getScore();
                    	now += price;
                    	score.setScore(now);
                    	Score score2 = obj.getScore(target.getName());
                    	int now2 = score2.getScore();
                    	now2 -= price;
                    	score2.setScore(now2);

                		new NBTTrade(plugin,player).init();
                    	if(dataList.size() == 0) {
                    		return;
                    	}
                    	ArrayList<ItemStack> items = new ArrayList<ItemStack>();
                    	for(PlayerItemData data : dataList) {
                    		ItemStack item = data.getItem();
                    		items.add(item);
                    	}                    	
                    	new StorageSystem(plugin,player).giveItem(items);
                    }
            	});
            }
        });
		

		new NBTGui(plugin,player).setID(GuiID.NONE);

		player.closeInventory();
	}
	
	
	public void cancelClose() {
		//アイテム返却
		UUID uuid = player.getUniqueId();
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
            	//out of minecraft
            	TradeDatabase database = new TradeDatabase(plugin);
            	ArrayList<PlayerItemData> dataList = database.getPlayerItem(uuid);
            	database.removePlayerItem(uuid);
            	Bukkit.getScheduler().runTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                    	//in minecraft
                    	if(dataList.size() == 0) {
                    		return;
                    	}
                    	ArrayList<ItemStack> items = new ArrayList<ItemStack>();
                    	for(PlayerItemData data : dataList) {
                    		ItemStack item = data.getItem();
                    		items.add(item);
                    	}
                    	
                    	new StorageSystem(plugin,player).giveItem(items);
                    }
            	});
            }
        });
		new NBTGui(plugin,player).setID(GuiID.NONE);
		new NBTTrade(plugin,player).init();
	}
	public void closedByTarget() {
		cancelClose();
		player.sendMessage("トレードがキャンセルされました");
		player.closeInventory();
	}
	public void onClose() {
		//相手も閉じる
		player.sendMessage("トレードをキャンセルしました");
		Player target = new NBTTrade(plugin,player).getPlayer();
		new GuiTrade(plugin,target).closedByTarget();
		cancelClose();
	}
}
