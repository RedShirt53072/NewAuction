package com.gmail.akashirt53072.newauctions.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.akashirt53072.newauctions.Main;
import com.gmail.akashirt53072.newauctions.config.AuctionDatabase;
import com.gmail.akashirt53072.newauctions.datatype.AuctionItemData;
import com.gmail.akashirt53072.newauctions.datatype.ItemStatus;
import com.gmail.akashirt53072.newauctions.datatype.ItemType;
import com.gmail.akashirt53072.newauctions.nbt.NBTAuctionItem;
import com.gmail.akashirt53072.newauctions.nbt.NBTBuyList;
import com.gmail.akashirt53072.newauctions.nbt.NBTGui;
import com.gmail.akashirt53072.newauctions.nbt.NBTItemType;

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
			case 50:
			case 51:
			case 52:
				inv.setItem(index, createItem(Material.BLACK_STAINED_GLASS_PANE," ",null,1,null));
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
			case 49:
				inv.setItem(index, createItem(Material.ENDER_EYE,ChatColor.WHITE + "更新",null,1,Enchantment.MENDING));
				break;
			case 53:
				inv.setItem(index, createItem(Material.ARROW,ChatColor.WHITE + "次のページへ",null,1,null));
				break;
			}
		}
		player.openInventory(inv);
		//非同期でアイテムデータ読み込んで表示させる
		asyncLoad();
	}
	private void asyncLoad() {
		int page = new NBTBuyList(plugin,player).getPage();
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
            	//out of minecraft
            	AuctionDatabase database = new AuctionDatabase(plugin);
            	int index = database.getIndex();
            	if(index == 0) {
            		return;
            	}
            	int max = ((index - 1) / 28) + 1;
            	int page2 = page;
            	if(page == 0) {
            		page2 = max;
            	}
            	if(max < page) {
            		page2 = 1;
            	}
            	int end = page2 * 28;
            	int start = end - 27;
            	int page3 = page2;
            	ArrayList<AuctionItemData> dataList = database.getItem(start, end);
            	Bukkit.getScheduler().runTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                    	//in minecraft
                    	if(!new NBTGui(plugin,player).getID().equals(GuiID.BUYLIST)) {
                    		return;
                    	}
                    	inv = player.getOpenInventory().getTopInventory();
                    	int nextPage = page3 + 1;
                    	if(nextPage > max) {
                    		nextPage = 1;
                    	}
                    	int backPage = page3 - 1;
                    	if(backPage == 0) {
                    		nextPage = max;
                    	}
                    	inv.setItem(45, createItem(Material.ARROW,ChatColor.WHITE + "前のページへ(" + backPage + "/" + max + ")",null,1,null));
                    	inv.setItem(53, createItem(Material.ARROW,ChatColor.WHITE + "次のページへ(" + nextPage + "/" + max + ")",null,1,null));
                    	for(int i = 0; i < dataList.size();i ++) {
                    		AuctionItemData data = dataList.get(i);
                    		ItemStack item = data.getItem();
                    		new NBTAuctionItem(plugin,item).init(data.getid(),ItemStatus.All);
                    		new NBTItemType(plugin,item).init(ItemType.AUCTIONITEM);
                    		ItemMeta meta = item.getItemMeta();
                    		List<String> loreData = meta.getLore();
                    		if(loreData == null) {
                    			loreData = new ArrayList<String>();
                    		}
                    		loreData.add(ChatColor.WHITE + "----------------");
                    		loreData.add(ChatColor.WHITE + "出品者：" + Bukkit.getOfflinePlayer(data.getPlayer()).getName());
                    		loreData.add(ChatColor.WHITE + "価格：" + ChatColor.GOLD + data.getPrice() + "$");
                    		loreData.add(ChatColor.WHITE + "終了まであと" + ChatColor.GOLD + (7 - data.getTime()) + "日");
                    		loreData.add(ChatColor.WHITE + "----------------");
                    		meta.setLore(loreData);
                    		item.setItemMeta(meta);
                    		int ind = i + 10 + (((i) / 7)* 2);
                    		inv.setItem(ind, item);
                    	}
                    }
            	});
            }
        });
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
			NBTBuyList nbt = new NBTBuyList(plugin,player);
			int page = nbt.getPage();
			nbt.setPage(page - 1);
			asyncLoad();
			break;
		case 49:
			player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
			//非同期でアイテムデータ読み込んで表示させる
			inv = player.getOpenInventory().getTopInventory();
			for(int index = 0;index < 54;index ++) {
				switch(index){
				case 10:
				case 11:
				case 12:
				case 13:
				case 14:
				case 15:
				case 16:
				case 19:
				case 20:
				case 21:
				case 22:
				case 23:
				case 24:
				case 25:
				case 28:
				case 29:
				case 30:
				case 31:
				case 32:
				case 33:
				case 34:
				case 37:
				case 38:
				case 39:
				case 40:
				case 41:
				case 42:
				case 43:
					inv.clear(index);
					break;
				}
			}
			asyncLoad();
			break;
		case 53:
			player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
			//非同期でアイテムデータ読み込んで表示させる
			NBTBuyList nbt1 = new NBTBuyList(plugin,player);
			int page1 = nbt1.getPage();
			nbt1.setPage(page1 - 1);
			asyncLoad();
			break;
		}
	}
	public void onBuyClick(ItemStack item) {
		int id = new NBTAuctionItem(plugin,item).getID();
		player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
		close();
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
            	//out of minecraft
            	AuctionDatabase database = new AuctionDatabase(plugin);
            	AuctionItemData data = database.getIDItem(id);
            	Bukkit.getScheduler().runTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                    	//in minecraft
                    	if(data == null) {
                    		player.sendMessage(ChatColor.RED + "このアイテムは出品が終了しました");
                    		new GuiBuyList(plugin,player).create();
                    		return;
                    	}
                    	if(data.getPlayer().equals(player.getUniqueId())){
                    		GuiCancelOrder gui = new GuiCancelOrder(plugin,player);
                    		gui.create();
                    		gui.asyncLoad(id);
                    		return;
                    	}
                		GuiBuyItem gui = new GuiBuyItem(plugin,player);
                		gui.create();
                		gui.asyncLoad(id);
                    }
            	});
            }
        });
	}
}
