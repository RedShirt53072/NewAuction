package com.gmail.akashirt53072.newauctions.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.akashirt53072.newauctions.Main;


public class AuctionCommand implements CommandExecutor{
	
	private Main plugin;
	
	public AuctionCommand(Main plugin) {
	    this.plugin = plugin;
	}
	
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
            Player player = (Player) sender;
            //opengui
            
            
            return true;
		}
        return false;
    }
}
