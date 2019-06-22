package com.cpx1989.loresrus;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandListener implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCommandEvent(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		String[] args = e.getMessage().split(" ");

		if (LoresRUs.plist.contains(p)) {
			e.setCancelled(true);
			LoreBuilder lb = LoresRUs.lbarray.get(p);
			if (args[0].equalsIgnoreCase("/meta")){
				String meta = e.getMessage().replace(args[0]+" ", "");
				lb.addMeta(meta, true);
			} else if (args[0].equalsIgnoreCase("/name")){
				String name = e.getMessage().replace(args[0]+" ", "");
				lb.setName(name, true);
			} else if(args[0].equalsIgnoreCase("/done")){
				lb.build();
			} else if (args[0].equalsIgnoreCase("/cancel")){
				LoresRUs.plist.remove(p);
				LoresRUs.lbarray.remove(p);
				p.sendMessage(Globals.Prefix + "We're sorry you didnt like your lore.");
			} else if (args[0].equalsIgnoreCase("/help")){
				p.sendMessage(ChatColor.GRAY + "Type the folowing to get started:");
				p.sendMessage(ChatColor.GRAY + "   /name <string> - " + ChatColor.YELLOW + "Sets the items name");
				p.sendMessage(ChatColor.GRAY + "   /meta <string> - " + ChatColor.YELLOW + "Adds a line of Metadata");
				p.sendMessage(ChatColor.GRAY + "   /done - " + ChatColor.YELLOW + "Applies the lore to the item");
				p.sendMessage(ChatColor.GRAY + "   /cancel - " + ChatColor.YELLOW + "Exits the lore builder");
				p.sendMessage(ChatColor.GRAY + "   /help - " + ChatColor.YELLOW + "Displays lore builder commands");
			} else {
				if (p.hasPermission("loresrus.admin")){
					e.setCancelled(false);
					return;
				}
				p.sendMessage(Globals.Prefix + "You have entered an incorrect command!");
				p.sendMessage(Globals.Prefix + "If you would like to leave the creator please type '/cancel'");
			}
		}
	}

}
