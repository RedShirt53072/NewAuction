package com.gmail.akashirt53072.newauctions;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


public class PlayerTick extends BukkitRunnable{
	private Player player;
	private Main plugin;
	private int phase;
	
	public PlayerTick(Main plugin,Player player,int phase) {
	    this.player = player;
	    this.plugin = plugin;
	    this.phase = phase;
	}

	@Override
	public void run() {
		phase ++;
		if(phase == 100) {
			//storage参照
			new StorageSystem(plugin,player).loop();
			phase = 0;
		}
		if(player.isOnline()) {
			new PlayerTick(plugin,player,phase).runTaskLater(plugin,1);
		}
	}
}
