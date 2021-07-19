package com.gmail.akashirt53072.newauctions;

import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.akashirt53072.newauctions.command.AuctionCommand;
import com.gmail.akashirt53072.newauctions.config.ConfigLoader;


public class Main extends JavaPlugin {
	@Override
	public void onEnable() {
		
		new ConfigLoader(this,"allauctiondata.yml").saveDefaultConfig();
		new ConfigLoader(this,"preadddata.yml").saveDefaultConfig();
		new ConfigLoader(this,"storagedata.yml").saveDefaultConfig();
		new ConfigLoader(this,"general.yml").saveDefaultConfig();
		
		new PlayerLogin(this);
		new PlayerAction(this);
		
		this.getCommand("auction").setExecutor(new AuctionCommand(this));
		

	}
}
