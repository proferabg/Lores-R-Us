package com.cpx1989.loresrus;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LoreCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(args.length == 0){
			sender.sendMessage(Globals.Prefix + "Invalid Arguments! Try " + ChatColor.RED + "/plore create");
		} else if(args.length == 1){
			if (sender instanceof Player){
				Player p = (Player) sender;
				if (args[0].equalsIgnoreCase("create")){
					if (p.hasPermission("loresrus.creator")){
						p.sendMessage(ChatColor.YELLOW + "Welcome to Lores 'R' Us creation studio!");
						p.sendMessage(ChatColor.GRAY + "Type the folowing to get started:");
						p.sendMessage(ChatColor.GRAY + "   /name <string> - " + ChatColor.YELLOW + "Sets the items name");
						p.sendMessage(ChatColor.GRAY + "   /meta <string> - " + ChatColor.YELLOW + "Adds a line of Metadata");
						p.sendMessage(ChatColor.GRAY + "   /done - " + ChatColor.YELLOW + "Applies the lore to the item");
						p.sendMessage(ChatColor.GRAY + "   /cancel - " + ChatColor.YELLOW + "Exits the lore builder");
						p.sendMessage(ChatColor.GRAY + "   /help - " + ChatColor.YELLOW + "Displays lore builder commands");
						LoresRUs.plist.add(p);
						LoreBuilder lb = new LoreBuilder(p);
						LoresRUs.lbarray.put(p, lb);
					} else {
						p.sendMessage(Globals.Prefix + ChatColor.RED + "You do not have permission to use this command!");
					}
				} else if (args[0].equalsIgnoreCase("reload")){
					if (p.hasPermission("loresrus.reload")){
						LoresRUs.getInstance().reloadConfig();
						p.sendMessage(Globals.Prefix + "Config reloaded!");
					} else {
						p.sendMessage(Globals.Prefix + ChatColor.RED + "You do not have permission to use this command!");
					}
				} else if (args[0].equalsIgnoreCase("about")){
					p.sendMessage("");
					p.sendMessage(ChatColor.GRAY + "---- " + ChatColor.YELLOW + "Lores 'R' Us" + ChatColor.GRAY + " ----");
					p.sendMessage(ChatColor.GRAY + "Version: "+ ChatColor.RED + LoresRUs.pluginyml.getVersion());
					p.sendMessage("");
					p.sendMessage(ChatColor.GRAY + "Developer: "+ ChatColor.AQUA + LoresRUs.pluginyml.getAuthors());
					p.sendMessage("");
				} else if (args[0].equalsIgnoreCase("help")){
					p.sendMessage("");
					p.sendMessage(ChatColor.GRAY + "---- " + ChatColor.RED + "Lores 'R' Us" + ChatColor.GRAY + " ----");
					p.sendMessage(ChatColor.DARK_AQUA + "   /plore create - " + ChatColor.GRAY + "Starts the interactive builder.");
					p.sendMessage(ChatColor.DARK_AQUA + "   /plore name <string> - " + ChatColor.GRAY + "Sets the name of the item.");
					p.sendMessage(ChatColor.DARK_AQUA + "   /plore meta <string> - " + ChatColor.GRAY + "Sets the metas for the item");
					p.sendMessage(ChatColor.DARK_AQUA + "   /plore help - " + ChatColor.GRAY + "Shows this page!");
					p.sendMessage("");
				} else {
					p.sendMessage(Globals.Prefix + ChatColor.RED + "Invalid Arguments!");
				}
			} else {
				sender.sendMessage(Globals.Prefix + "Must be in-game to use command!");
			}
		} else if (args.length > 1){
			if (sender instanceof Player){
				Player p = (Player) sender;
				if (args[0].equalsIgnoreCase("name")){
					if (p.hasPermission("loresrus.name")){
						String s = "";
						for(int i = 1; i < args.length; i++){
				            s += args[i] + " ";
				        }
						LoreBuilder lb = new LoreBuilder(p);
						lb.setName(s, false);
						lb.build();
					} else {
						p.sendMessage(Globals.Prefix + ChatColor.RED + "You do not have permission to use this command!");
					}
				} else if (args[0].equalsIgnoreCase("meta")){
					if (p.hasPermission("loresrus.meta")){
						String s = "";
						for(int i = 1; i < args.length; i++){
				            s += args[i] + " ";
				        }
						LoreBuilder lb = new LoreBuilder(p);
						String[] s1 = s.split("~");
						for (int i2 = 0; i2 < s1.length; i2++){
							lb.addMeta(s1[i2], false);
						}
						lb.build();
					} else {
						p.sendMessage(Globals.Prefix + ChatColor.RED + "You do not have permission to use this command!");
					}
				} else {
					p.sendMessage(Globals.Prefix + ChatColor.RED + "Invalid Arguments!");
				}
			} else {
				sender.sendMessage(Globals.Prefix + "Must be in-game to use command!");
			}
		}
		return false;
	}

}
