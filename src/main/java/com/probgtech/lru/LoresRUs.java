package com.probgtech.lru;

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

import com.gimmecraft.keepitclean.KeepItClean;



public class LoresRUs extends JavaPlugin {
	
	static PluginDescriptionFile pluginyml;
	Logger logger;
	static LoresRUs instance;
	static FileConfiguration cfg;
	public static Economy economy = null;
	public KeepItClean kic = null;
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
		
		//get prefix

		//global prefix
		String s = cfg.getString("Primary_Color") + cfg.getString("Prefix") + cfg.getString("Secondary_Color")+ " ";
		Globals.Prefix = ChatColor.translateAlternateColorCodes('&', s);
		
		//log authors
		logger.info(Globals.Prefix + "Plugin by " + pluginyml.getAuthors());
		
		//register command
		getCommand("plore").setExecutor(new LoreCommand());
		
		//register command listener
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new CommandListener(), this);
		
		//setup vault
		if (setupEconomy()){
			logger.info(Globals.Prefix + "Hooked into Vault!");
		}
		
		if (hookKIC()){
			logger.info(Globals.Prefix + "Hooked into KeepItClean!");
		}
		
		//register perLetter cost
		Globals.Price_Per_Letter = cfg.getDouble("Price_Per_Letter");
		
		//economy name
		Globals.Economy_Name = cfg.getString("Economy_Name");
	}

	private boolean hookKIC() {
		kic = (KeepItClean) getServer().getPluginManager().getPlugin("KeepItClean");
		return (kic != null);
	}

	public void onDisable() {
		Bukkit.getScheduler().cancelTasks(this);
		logger.info("Plugin disabled!");
	}
	
	private boolean setupEconomy(){
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
        return (economy != null);
    }
	
	public static LoresRUs getInstance(){
		return instance;
	}
	
}

class Globals {
	public static String Prefix = ChatColor.RED +  "[Lores 'R' Us] "+ ChatColor.GRAY;
	public static double Price_Per_Letter = 1.0;
	public static String Economy_Name = "Dollar(s)";
}
