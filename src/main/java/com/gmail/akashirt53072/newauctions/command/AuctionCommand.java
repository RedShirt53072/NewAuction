package com.gmail.akashirt53072.newauctions.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.akashirt53072.newauctions.Main;
import com.gmail.akashirt53072.newauctions.gui.GuiBuyList;
import com.gmail.akashirt53072.newauctions.gui.GuiID;
import com.gmail.akashirt53072.newauctions.nbt.NBTGui;


public class AuctionCommand implements CommandExecutor{
	
	private Main plugin;
	
	public AuctionCommand(Main plugin) {
	    this.plugin = plugin;
	}
	
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
            Player player = (Player) sender;
            GuiID id = new NBTGui(plugin,player).getID();
        	
            if(id.equals(GuiID.TRADEPRICE)) {
            	player.sendMessage(ChatColor.RED + "現在トレード中です");
            	return true;
            }
            //opengui
            new GuiBuyList(plugin,player).create();
            
            return true;
		}
        return false;
    }
}
