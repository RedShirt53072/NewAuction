package com.gmail.akashirt53072.newauctions.config;

import org.bukkit.plugin.Plugin;

public class GeneralConfig extends DataConfig{
	
	public GeneralConfig(Plugin plugin) {
		super(plugin,"general.yml");
	}
	
	public int getLeaveDays(){
		String path = "leavedays";
		return super.getIntData(path);
    }
	
}