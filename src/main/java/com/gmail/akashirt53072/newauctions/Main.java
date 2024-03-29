package com.gmail.akashirt53072.newauctions;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.akashirt53072.newauctions.command.AuctionCommand;
import com.gmail.akashirt53072.newauctions.command.StorageCommand;
import com.gmail.akashirt53072.newauctions.command.TradeCommand;
import com.gmail.akashirt53072.newauctions.config.AuctionDatabase;
import com.gmail.akashirt53072.newauctions.config.ConfigLoader;
import com.gmail.akashirt53072.newauctions.config.ExpiredDatabase;
import com.gmail.akashirt53072.newauctions.datatype.AuctionItemData;


public class Main extends JavaPlugin {
	@Override
	public void onEnable() {
		
		new ConfigLoader(this,"allauctiondata.yml").saveDefaultConfig();
		new ConfigLoader(this,"preadddata.yml").saveDefaultConfig();
		new ConfigLoader(this,"storagedata.yml").saveDefaultConfig();
		new ConfigLoader(this,"expireditem.yml").saveDefaultConfig();
		new ConfigLoader(this,"solditem.yml").saveDefaultConfig();
		new ConfigLoader(this,"tradedata.yml").saveDefaultConfig();
		
		new PlayerLogin(this);
		new PlayerAction(this);
		
		this.getCommand("auction").setExecutor(new AuctionCommand(this));
		this.getCommand("storage").setExecutor(new StorageCommand(this));
		this.getCommand("trade").setExecutor(new TradeCommand(this));
		
		Main plugin = this;
		LocalDateTime now = LocalDateTime.now();
		int day = now.getDayOfMonth();
		int before = new AuctionDatabase(this).getDay();
		if(day == before) {
			return;
		}
		Bukkit.getScheduler().runTaskAsynchronously(this, new Runnable() {
            @Override
            public void run() {
            	//out of minecraft
            	AuctionDatabase database = new AuctionDatabase(plugin);
            	ArrayList<AuctionItemData> dataList = database.addTime();
            	//コピー
            	for(AuctionItemData data : dataList) {
                	new ExpiredDatabase(plugin).addItem(data.getPlayer(), data.getItem(), data.getPrice(), data.getid());
            	}
            	database.setDay(day);
            }
        });
	}
}
