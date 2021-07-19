package com.gmail.akashirt53072.newauctions;

import org.bukkit.event.Listener;

import com.gmail.akashirt53072.newauctions.nbt.NBTGui;

import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.entity.Player;


public final class PlayerLogin implements Listener {
    Main plugin;
	public PlayerLogin(Main plugin) {
    	this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void getLoginPlayer(PlayerLoginEvent event) {
    	Player player = event.getPlayer();
    	new NBTGui(plugin,player).init();
    	
    	//tickLoop
		//new PlayerTick(plugin,player).runTaskLater(plugin,1);
    	
    	
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void getLogoutPlayer(PlayerQuitEvent event) {
    	onPlayerLogout(event);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void getLeaveWorldPlayer(PlayerChangedWorldEvent event) {
    	onPlayerLogout(event);
    }
    public void onPlayerLogout(PlayerEvent event) {
    	Player player = event.getPlayer();
    	new NBTGui(plugin,player).init();
    	
    }
}
