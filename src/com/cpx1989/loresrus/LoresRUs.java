package com.cpx1989.loresrus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.scriptjunkie.KeepItClean.KeepItClean;



public class LoresRUs extends JavaPlugin {
	
	static PluginDescriptionFile pluginyml;
	Logger logger;
	static LoresRUs instance;
	static FileConfiguration cfg;
	public static Economy economy = null;
	public KeepItClean kic = null;
	static int perLetter = 1;
	static List<Player> plist = new ArrayList<Player>();
	static Map<Player, LoreBuilder> lbarray = new HashMap<Player, LoreBuilder>();
	
	public void onEnable() {
		//Get logger before anything else
		logger = Logger.getLogger("Minecraft");
		
		//save instance
		instance = this;
		
		//get description
		pluginyml = getDescription();

		//get config
		cfg = getConfig();
		cfg.options().copyDefaults(true);
		saveConfig();
		
		//log authors
		logger.info(getPrefix() + "Plugin by " + pluginyml.getAuthors());
		
		//register command
		getCommand("plore").setExecutor(new LoreCommand());
		
		//register command listener
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new CommandListener(), this);
		
		//setup vault
		if (setupEconomy()){
			logger.info(getPrefix() + "Hooked into Vault!");
		}
		
		if (hookKIC()){
			logger.info(getPrefix() + "Hooked into KeepItClean!");
		}
		
		//register perLetter cost
		perLetter = cfg.getInt("perletter");

	}

	private boolean hookKIC() {
		kic = (KeepItClean) getServer().getPluginManager().getPlugin("KeepItClean");
		return (kic != null);
	}

	public void onDisable() {
		Bukkit.getScheduler().cancelTasks(this);
		logger.info("Plugin disabled!");
	}
	
	public static String getPrefix(){
		String s = cfg.getString("pricolor") + cfg.getString("prefix") + cfg.getString("seccolor")+ " ";
		return ChatColor.translateAlternateColorCodes('&', s);
	}
	
	private boolean setupEconomy(){
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
        return (economy != null);
    }
	
	public static LoresRUs getInstance(){
		return instance;
	}
	
}
