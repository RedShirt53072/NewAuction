package com.gmail.akashirt53072.newauctions.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.akashirt53072.newauctions.Main;
import com.gmail.akashirt53072.newauctions.StorageSystem;


public class StorageCommand implements CommandExecutor{
	
	private Main plugin;
	
	public StorageCommand(Main plugin) {
	    this.plugin = plugin;
	}
	
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
            Player player = (Player) sender;
            //giveStorageItem
            new StorageSystem(plugin,player).giveFromData();
            return true;
		}
        return false;
    }
}
