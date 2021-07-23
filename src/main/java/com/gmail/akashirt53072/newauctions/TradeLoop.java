package com.gmail.akashirt53072.newauctions;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.akashirt53072.newauctions.gui.GuiTrade;
import com.gmail.akashirt53072.newauctions.nbt.NBTTrade;

public class TradeLoop extends BukkitRunnable {
	private Main plugin;
	private int phase;
	private Player player;
	
	public TradeLoop(Main plugin,Player player,int phase) {
		this.plugin = plugin;
		this.phase = phase;
		this.player = player;
	}
	@Override
	public void run() {
		phase --;
		NBTTrade nbt = new NBTTrade(plugin,player);
		int phase2 = nbt.getPhase();
		Player target = nbt.getPlayer();
		NBTTrade targetnbt = new NBTTrade(plugin,target);
		
		if(phase2 < 2) {
			return;
		}
		if(phase != 0) {
			new TradeLoop(plugin,player,phase).runTaskLater(plugin, 1);
		}
		if(phase % 20 != 0) {
			return;
		}
		phase2 --;
		if(phase2 == 10) {
			//確定
			new GuiTrade(plugin,player).confirmClose();
			new GuiTrade(plugin,target).confirmClose();
			return;
		}
		nbt.setPhase(phase2);
		targetnbt.setPhase(phase2);
		new GuiTrade(plugin,player).update();
		new GuiTrade(plugin,target).update();
		
	}
}
