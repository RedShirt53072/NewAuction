package com.gmail.akashirt53072.newauctions.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import com.gmail.akashirt53072.newauctions.Main;
import com.gmail.akashirt53072.newauctions.gui.GuiID;
import com.gmail.akashirt53072.newauctions.gui.GuiTrade;
import com.gmail.akashirt53072.newauctions.nbt.NBTGui;
import com.gmail.akashirt53072.newauctions.nbt.NBTTrade;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;


public class TradeCommand implements TabExecutor{
	
	private Main plugin;
	
	public TradeCommand(Main plugin) {
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
            if(args.length == 0) {
            	player.sendMessage(ChatColor.RED + "トレードを送るプレイヤーのユーザー名を記入してください");
            	player.sendMessage("/trade <player name>");
            	return true;
            }
            Player target = player.getServer().getPlayer(args[0]);
			
            if(target == null) {
            	player.sendMessage(ChatColor.RED + "そのユーザー名のプレイヤーは見つかりませんでした");
            	player.sendMessage("/trade <player name>");
            	return true;
            }
            
            if(target.getLocation().distance(player.getLocation()) > 5) {
            	player.sendMessage(ChatColor.RED + "そのユーザー名のプレイヤー半径5m以内にいません");
            	player.sendMessage("/trade <player name>");
            	return true;
            }
            if(player.equals(target)) {
            	player.sendMessage(ChatColor.RED + "自分自身にトレードを送ることはできません");
            	player.sendMessage("/trade <player name>");
            	return true;
            }
            Player pre = new NBTTrade(plugin,player).getPrePlayer();
            if(pre != null) {
            	if(pre.equals(target)) {
            		//tradeGUI開く
                    new GuiTrade(plugin,player,target).create();
                    new GuiTrade(plugin,target,player).create();
                    return true;
            	}
            }
            //リクエスト送る
            player.sendMessage(target.getName() + "にトレードリクエストを送りました");
            target.sendMessage(ChatColor.GREEN + player.getName() + "からトレードリクエストが届きました");
            TextComponent message = new TextComponent();
    		message.addExtra(ChatColor.WHITE + "ここをクリックでトレードを開く");
    		message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/trade " + player.getName()));
    		target.spigot().sendMessage(message);
    		new NBTTrade(plugin,target).setPrePlayer(player);
            return true;
		}
        return false;
    }
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args){
		if (sender instanceof Player) {
			Player executor = (Player) sender;
			Location loc = executor.getLocation();
            List<String> tab = new ArrayList<String>();
            List<Player> players = executor.getWorld().getPlayers();
            for(Player p : players) {
            	double distance = p.getLocation().distance(loc);
            	if(distance < 5) {
            		if(p.equals(executor)) {
            			continue;
            		}
            		tab.add(p.getName());
            	}
            }
            if(tab.size() == 0) {
            	tab.add("半径5m以内にプレイヤーがいません！");
            }
            return tab;
		}
		return null;
	}
}
