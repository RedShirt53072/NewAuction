package com.gmail.akashirt53072.newauctions.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
import com.gmail.akashirt53072.newauctions.config.ExpiredDatabase;
import com.gmail.akashirt53072.newauctions.config.SoldDatabase;
import com.gmail.akashirt53072.newauctions.datatype.AuctionItemData;
import com.gmail.akashirt53072.newauctions.datatype.ItemStatus;
import com.gmail.akashirt53072.newauctions.datatype.ItemType;
import com.gmail.akashirt53072.newauctions.nbt.NBTAuctionItem;
import com.gmail.akashirt53072.newauctions.nbt.NBTGui;
import com.gmail.akashirt53072.newauctions.nbt.NBTItemType;

public class GuiSellList extends Gui{
	public GuiSellList(Main plugin, Player player) {
		super(plugin, player);
	}
	public GuiSellList(Main plugin, Player player,Inventory inventory) {
		super(plugin, player,inventory);
	}
	
	@Override
	public void create() {
		new NBTGui(plugin,player).setID(GuiID.SELLLIST);
		inv = Bukkit.createInventory(null, 54, "売却中アイテム一覧");
		for(int index = 0;index < 54;index ++) {
			switch(index){
			case 20:
			case 21:
			case 22:
			case 23:
			case 24:
			case 29:
			case 30:
			case 31:
			case 32:
			case 33:
			case 38:
			case 39:
			case 40:
			case 41:
			case 42:
				break;
			case 2:
				inv.setItem(index, createItem(Material.GLOW_ITEM_FRAME,ChatColor.WHITE + "購入メニューへ",null,1,null));
				break;
			case 4:
				inv.setItem(index, createItem(Material.CHEST_MINECART,ChatColor.WHITE + "売却中アイテム一覧",null,1,Enchantment.MENDING));
				break;
			case 6:
				inv.setItem(index, createItem(Material.HOPPER,ChatColor.WHITE + "新規出品メニューへ",null,1,null));
				break;
			default:
				inv.setItem(index, createItem(Material.BLACK_STAINED_GLASS_PANE," ",null,1,null));
			}
		}
		player.openInventory(inv);
		//非同期でアイテムデータ読み込んで表示させる
		UUID uuid = player.getUniqueId();
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
            	//out of minecraft
            	ExpiredDatabase database1 = new ExpiredDatabase(plugin);
            	ArrayList<AuctionItemData> dataList1 = database1.getPlayerItem(uuid);
            	SoldDatabase database2 = new SoldDatabase(plugin);
            	ArrayList<AuctionItemData> dataList2 = database2.getPlayerItem(uuid);
            	AuctionDatabase database3 = new AuctionDatabase(plugin);
            	ArrayList<AuctionItemData> dataList3 = database3.getPlayerItem(uuid);
            	Bukkit.getScheduler().runTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                    	//in minecraft
                    	if(!new NBTGui(plugin,player).getID().equals(GuiID.SELLLIST)) {
                    		return;
                    	}
                    	inv = player.getOpenInventory().getTopInventory();
                    	int loop = 0;
                    	for(int i = 0; i < dataList1.size();i ++) {
                    		AuctionItemData data = dataList1.get(i);
                    		ItemStack item = data.getItem();
                    		new NBTAuctionItem(plugin,item).init(data.getid(),ItemStatus.EXPIRED);
                    		new NBTItemType(plugin,item).init(ItemType.AUCTIONITEM);
                    		ItemMeta meta = item.getItemMeta();
                    		List<String> loreData = meta.getLore();
                    		if(loreData == null) {
                    			loreData = new ArrayList<String>();
                    		}
                    		loreData.add(ChatColor.WHITE + "----------------");
                    		loreData.add(ChatColor.WHITE + "出品者：" + Bukkit.getOfflinePlayer(data.getPlayer()).getName());
                    		loreData.add(ChatColor.WHITE + "価格：" + ChatColor.GOLD + data.getPrice() + "$");
                    		loreData.add(ChatColor.RED + "出品期限切れ");
                    		loreData.add(ChatColor.WHITE + "----------------");
                    		meta.setLore(loreData);
                    		item.setItemMeta(meta);
                    		int ind = loop + 20 + (((loop) / 5)* 4);
                    		inv.setItem(ind, item);
                    		loop ++;
                    	}
                    	for(int i = 0; i < dataList2.size();i ++) {
                    		AuctionItemData data = dataList2.get(i);
                    		ItemStack item = data.getItem();
                    		new NBTAuctionItem(plugin,item).init(data.getid(),ItemStatus.SOLD);
                    		new NBTItemType(plugin,item).init(ItemType.AUCTIONITEM);
                    		ItemMeta meta = item.getItemMeta();
                    		List<String> loreData = meta.getLore();
                    		if(loreData == null) {
                    			loreData = new ArrayList<String>();
                    		}
                    		loreData.add(ChatColor.WHITE + "----------------");
                    		loreData.add(ChatColor.WHITE + "出品者：" + Bukkit.getOfflinePlayer(data.getPlayer()).getName());
                    		loreData.add(ChatColor.WHITE + "価格：" + ChatColor.GOLD + data.getPrice() + "$");
                    		loreData.add(ChatColor.YELLOW + "売却完了");
                    		loreData.add(ChatColor.WHITE + "----------------");
                    		meta.setLore(loreData);
                    		item.setItemMeta(meta);
                    		int ind = loop + 20 + (((loop) / 5)* 4);
                    		inv.setItem(ind, item);
                    		loop ++;
                    	}
                    	for(int i = 0; i < dataList3.size();i ++) {
                    		AuctionItemData data = dataList3.get(i);
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
                    		int ind = loop + 20 + (((loop) / 5)* 4);
                    		inv.setItem(ind, item);
                    		loop ++;
                    	}
                    	for(int i = loop; i < 15;i ++) {
                    		int ind = i + 20 + (((i) / 5)* 4);
                    		inv.setItem(ind, createItem(Material.GRAY_DYE,ChatColor.GRAY + "空きスロット",null,1,null));
                    	}
                    }
            	});
            }
        });
	}
	@Override
	public void onClick(int slot) {
		switch(slot) {
		case 2:
			player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
			close();
			new GuiBuyList(plugin,player).create();
			break;
		case 6:
			player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
			close();
			new GuiAddItem(plugin,player).create();
			break;
		}
	}
	
	public void onBuyClick(ItemStack item) {
       	player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
        int id = new NBTAuctionItem(plugin,item).getID();
		close();
		ItemStatus status = new NBTAuctionItem(plugin,item).getStatus();
		if(status.equals(ItemStatus.EXPIRED)) {
			GuiItemExpired gui = new GuiItemExpired(plugin,player);
    		gui.create();
    		gui.asyncLoad(id);
    		return;
		}
		if(status.equals(ItemStatus.SOLD)) {
			GuiItemSold gui = new GuiItemSold(plugin,player);
    		gui.create();
    		gui.asyncLoad(id);
    		return;
		}
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
                    		player.sendMessage(ChatColor.RED + "このアイテムは既に売却が完了しています");
                    		new GuiSellList(plugin,player).create();
                    		return;
                    	}
                		GuiCancelOrder gui = new GuiCancelOrder(plugin,player);
                		gui.create();
                		gui.asyncLoad(id);
                    }
            	});
            }
        });
	}
}
