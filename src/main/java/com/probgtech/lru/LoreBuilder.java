package com.probgtech.lru;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class LoreBuilder {
	
	List<String> meta = new ArrayList<String>();
	String name;
	Player p;
	
	private static final Pattern STRIP_FORMAT = Pattern.compile("(?i)" + String.valueOf(ChatColor.COLOR_CHAR) + "[K-OR]");
    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + String.valueOf(ChatColor.COLOR_CHAR) + "[0-9A-FK-OR]");
	
	public boolean useEco(){
		if (LoresRUs.economy != null){
			if (LoresRUs.cfg.getBoolean("Use_Money")){
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public boolean useKIC(){
		if (LoresRUs.instance.kic != null){
			if (LoresRUs.cfg.getBoolean("Use_KIC")){
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public LoreBuilder(Player player){
		p = player;
	}
	
	public void setName(String s, boolean dialog){
		if (useKIC()){
			Boolean result = LoresRUs.instance.kic.checkIsOffending(s, p, "Lores-R-Us");
			if (result){
				p.sendMessage(Globals.Prefix + "Name contains an offending word!");
				return;
			}
			s = checkColor(p, ChatColor.translateAlternateColorCodes('&',  s));
			name = s;
			p.sendMessage(Globals.Prefix + "Set name to: " + ChatColor.DARK_AQUA + s);
			if (dialog){
				p.sendMessage(ChatColor.GRAY + "  - Current Balance: " + ChatColor.GREEN + LoresRUs.economy.getBalance(p) + ChatColor.GRAY + " " + Globals.Economy_Name + " ");
				p.sendMessage(ChatColor.GRAY + "  - Current Cost: "+ ChatColor.RED + getLetterAmount()*Globals.Price_Per_Letter + ChatColor.GRAY + " " + Globals.Economy_Name + "");
				p.sendMessage(ChatColor.GRAY + "  - If you are done type " + ChatColor.YELLOW + "/done" + ChatColor.GRAY + " to finish or " + ChatColor.YELLOW + "/cancel" + ChatColor.GRAY + " to cancel.");
			}
		} else {
			s = checkColor(p, ChatColor.translateAlternateColorCodes('&',  s));
			name = s;
			p.sendMessage(Globals.Prefix + "Set name to: " + ChatColor.DARK_AQUA + s);
			if (dialog){
				p.sendMessage(ChatColor.GRAY + "  - Current Balance: " + ChatColor.GREEN + LoresRUs.economy.getBalance(p) + ChatColor.GRAY + " " + Globals.Economy_Name + " ");
				p.sendMessage(ChatColor.GRAY + "  - Current Cost: "+ ChatColor.RED + getLetterAmount()*Globals.Price_Per_Letter + ChatColor.GRAY + " " + Globals.Economy_Name + "");
				p.sendMessage(ChatColor.GRAY + "  - If you are done type " + ChatColor.YELLOW + "/done" + ChatColor.GRAY + " to finish or " + ChatColor.YELLOW + "/cancel" + ChatColor.GRAY + " to cancel.");
			}
		}
		
	}
	
	public void addMeta(String s, boolean dialog){
		if (meta != null && meta.size() < LoresRUs.cfg.getInt("Lore_Lines")){
			if (useKIC()){
				Boolean result = LoresRUs.instance.kic.checkIsOffending(s, p, "Lores-R-Us");
				if (result){
					p.sendMessage(Globals.Prefix + "Name contains an offending word!");
					return;
				}
				s = checkColor(p, ChatColor.translateAlternateColorCodes('&',  s));
				meta.add(s);
				p.sendMessage(Globals.Prefix + "Added line: " + ChatColor.DARK_AQUA + s);
				if (dialog){
					p.sendMessage(ChatColor.GRAY + "  - Current Balance: " + ChatColor.GREEN + LoresRUs.economy.getBalance(p) + ChatColor.GRAY + " " + Globals.Economy_Name + " Current Cost: "+ ChatColor.RED + getLetterAmount()*Globals.Price_Per_Letter + ChatColor.GRAY + " " + Globals.Economy_Name + "");
					p.sendMessage(ChatColor.GRAY + "  - If you are done type " + ChatColor.YELLOW + "/done" + ChatColor.GRAY + " to finish or " + ChatColor.YELLOW + "/cancel" + ChatColor.GRAY + " to cancel.");
				}
			} else {
				s = checkColor(p, ChatColor.translateAlternateColorCodes('&',  s));
				meta.add(s);
				p.sendMessage(Globals.Prefix + "Added line: " + ChatColor.DARK_AQUA + s);
				if (dialog){
					p.sendMessage(ChatColor.GRAY + "  - Current Balance: " + ChatColor.GREEN + LoresRUs.economy.getBalance(p) + ChatColor.GRAY + " " + Globals.Economy_Name + " Current Cost: "+ ChatColor.RED + getLetterAmount()*Globals.Price_Per_Letter + ChatColor.GRAY + " " + Globals.Economy_Name + "");
					p.sendMessage(ChatColor.GRAY + "  - If you are done type " + ChatColor.YELLOW + "/done" + ChatColor.GRAY + " to finish or " + ChatColor.YELLOW + "/cancel" + ChatColor.GRAY + " to cancel.");
				}
			}
		} else {
			p.sendMessage(Globals.Prefix + "You have reached the maximum amount of lines of metadata!");
		}
	}
	
	@SuppressWarnings("deprecation")
	public void build(){
		if(p.getItemInHand() != null && p.getItemInHand().getType() != Material.AIR){
			if(p.getItemInHand().getAmount() == 1){
				if (name != null && meta.size() > 0){
					if (useEco()){
						if(LoresRUs.economy.getBalance(p) >= getLetterAmount()*Globals.Price_Per_Letter){
							ItemStack is = p.getItemInHand();
							ItemMeta temp = is.getItemMeta();
							temp.setDisplayName("temporary");
							is.setItemMeta(temp);
							p.getInventory().remove(is);
							ItemMeta im = is.getItemMeta();
							im.setDisplayName(name);
							im.setLore(meta);
							is.setItemMeta(im);
							p.getInventory().addItem(is);
							p.updateInventory();
							LoresRUs.plist.remove(p);
							LoresRUs.lbarray.remove(p);
							p.sendMessage(Globals.Prefix + "You were charged: " + ChatColor.GREEN + getLetterAmount()*Globals.Price_Per_Letter + ChatColor.GRAY + " " + Globals.Economy_Name + "");
							p.sendMessage(ChatColor.GRAY + "  - Enjoy your new lore!");
							LoresRUs.economy.withdrawPlayer(p, getLetterAmount()*Globals.Price_Per_Letter);
						} else {
							p.sendMessage(Globals.Prefix + "You do not have enough for this lore!");
							p.sendMessage(ChatColor.GRAY + "  - Current Balance: " + ChatColor.GREEN + LoresRUs.economy.getBalance(p) + ChatColor.GRAY + " " + Globals.Economy_Name + "");
							p.sendMessage(ChatColor.GRAY + "  - Current Cost: "+ ChatColor.RED + getLetterAmount()*Globals.Price_Per_Letter + ChatColor.GRAY + " " + Globals.Economy_Name + "");
						}
					} else {
						ItemStack is = p.getItemInHand();
						ItemMeta temp = is.getItemMeta();
						temp.setDisplayName("temporary");
						is.setItemMeta(temp);
						p.getInventory().remove(is);
						ItemMeta im = is.getItemMeta();
						im.setDisplayName(name);
						im.setLore(meta);
						is.setItemMeta(im);
						p.getInventory().addItem(is);
						p.updateInventory();
						LoresRUs.plist.remove(p);
						LoresRUs.lbarray.remove(p);
						p.sendMessage(Globals.Prefix + "You were charged: " + ChatColor.GREEN + getLetterAmount()*Globals.Price_Per_Letter + ChatColor.GRAY + " " + Globals.Economy_Name + "");
						p.sendMessage(ChatColor.GRAY + "  - Enjoy your new lore!");
					}
				} else if (name != null){
					if (useEco()){
						if(LoresRUs.economy.getBalance(p) >= getLetterAmount()*Globals.Price_Per_Letter){
							ItemStack is = p.getItemInHand();
							p.getInventory().remove(is);
							ItemMeta im = is.getItemMeta();
							im.setDisplayName(name);
							is.setItemMeta(im);
							p.getInventory().addItem(is);
							LoresRUs.plist.remove(p);
							LoresRUs.lbarray.remove(p);
							p.sendMessage(Globals.Prefix + "You were charged: " + ChatColor.GREEN + getLetterAmount()*Globals.Price_Per_Letter + ChatColor.GRAY + " " + Globals.Economy_Name + "");
							p.sendMessage(ChatColor.GRAY + "  - Enjoy your new lore!");
							LoresRUs.economy.withdrawPlayer(p, getLetterAmount()*Globals.Price_Per_Letter);
						} else {
							p.sendMessage(Globals.Prefix + "You do not have enough for this lore!");
							p.sendMessage(ChatColor.GRAY + "  - Current Balance: " + ChatColor.GREEN + LoresRUs.economy.getBalance(p) + ChatColor.GRAY + " " + Globals.Economy_Name + " ");
							p.sendMessage(ChatColor.GRAY + "  - Current Cost: "+ ChatColor.RED + getLetterAmount()*Globals.Price_Per_Letter + ChatColor.GRAY + " " + Globals.Economy_Name + "");
						}
					} else {
						ItemStack is = p.getItemInHand();
						p.getInventory().remove(is);
						ItemMeta im = is.getItemMeta();
						im.setDisplayName(name);
						is.setItemMeta(im);
						p.getInventory().addItem(is);
						LoresRUs.plist.remove(p);
						LoresRUs.lbarray.remove(p);
						p.sendMessage(Globals.Prefix + "Enjoy your new lore!");
					}
				} else if (meta.size() > 0){
					if (useEco()){
						if(LoresRUs.economy.getBalance(p) >= getLetterAmount()*Globals.Price_Per_Letter){
							ItemStack is = p.getItemInHand();
							p.getInventory().remove(is);
							ItemMeta im = is.getItemMeta();
							im.setLore(meta);
							is.setItemMeta(im);
							p.getInventory().addItem(is);
							LoresRUs.plist.remove(p);
							LoresRUs.lbarray.remove(p);
							p.sendMessage(Globals.Prefix + "You were charged: " + ChatColor.GREEN + getLetterAmount()*Globals.Price_Per_Letter + ChatColor.GRAY + " " + Globals.Economy_Name + "");
							p.sendMessage(ChatColor.GRAY + "  - Enjoy your new lore!");
							LoresRUs.economy.withdrawPlayer(p, getLetterAmount()*Globals.Price_Per_Letter);
						} else {
							p.sendMessage(Globals.Prefix + "You do not have enough for this lore!");
							p.sendMessage(ChatColor.GRAY + "  - Current Balance: " + ChatColor.GREEN + LoresRUs.economy.getBalance(p) + ChatColor.GRAY + " " + Globals.Economy_Name + "");
							p.sendMessage(ChatColor.GRAY + "  - Current Cost: "+ ChatColor.RED + getLetterAmount()*Globals.Price_Per_Letter + ChatColor.GRAY + " " + Globals.Economy_Name + "");
						}
					} else {
						ItemStack is = p.getItemInHand();
						p.getInventory().remove(is);
						ItemMeta im = is.getItemMeta();
						im.setLore(meta);
						is.setItemMeta(im);
						p.getInventory().addItem(is);
						LoresRUs.plist.remove(p);
						LoresRUs.lbarray.remove(p);
						p.sendMessage(Globals.Prefix + "You were charged: " + ChatColor.GREEN + getLetterAmount()*Globals.Price_Per_Letter + ChatColor.GRAY + " " + Globals.Economy_Name + "");
						p.sendMessage(ChatColor.GRAY + "  - Enjoy your new lore!");
					}
				} else {
					p.sendMessage(Globals.Prefix + "No Name or Metadata specified!");
				}
			} else {
				p.sendMessage(Globals.Prefix + "You may only apply lores to one item at a time!");
			}
		} else {
			p.sendMessage(Globals.Prefix + "You must have an item in your hand to apply lores!");
		}
	}
	
	public int getLetterAmount(){
		//set 0
		int i = 0;
		//get number of chars in name
		if (name != null){
			String name1 = ChatColor.stripColor(name);
			name1 = name1.replaceAll(" ", "");
			i = i + name1.length();
		}
		if (!meta.isEmpty()){
			//get number of chars in lores
			for(String lore1 : meta){
				lore1 = ChatColor.stripColor(lore1);
				lore1 = lore1.replaceAll(" ", "");
				i = i + lore1.length();
			}
		}
		//return number of chars
		return i;
	}
	
	public String checkColor(Player p, String msg){
		if (p.hasPermission("essentials.chat.color")) {
            msg = STRIP_FORMAT.matcher(msg).replaceAll("");
        } else {
            msg = STRIP_COLOR_PATTERN.matcher(msg).replaceAll("");
        }
		return msg;
	}
}
