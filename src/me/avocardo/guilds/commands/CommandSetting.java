package me.avocardo.guilds.commands;

import me.avocardo.guilds.GuildsBasic;
import me.avocardo.guilds.messages.Console;
import me.avocardo.guilds.messages.Message;
import me.avocardo.guilds.messages.MessageType;
import me.avocardo.guilds.utilities.Settings;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSetting {

	private GuildsBasic GuildsBasic;
	
	public CommandSetting(CommandSender sender, String[] args, GuildsBasic GuildsBasic) {
		
		this.GuildsBasic = GuildsBasic;
		
		if (sender instanceof Player) {
			Player(args, (Player) sender);
		} else {
			Console(args);
		}
		
	}
	
	private void Player(String[] args, Player player) {
		
		Settings setting = null;
		if (player.hasPermission("guilds.admin.setting")) {
			if (args.length > 2) {
				for (Settings s : Settings.values()) {
					if (s.findSettings(args[1])) {
						setting = s;
					}
				}
				if (setting != null) {
					boolean first = true;
					boolean second = true;
					String value = "";
					for (String str : args) {
						if (first) {
							first = false;
						} else if (second) {
							second = false;
						} else {
							value = value + str + " ";
						}
					}
					if (setting.isBoolean()) {
						if (isBoolean(args[2])) {
							boolean v = Boolean.parseBoolean(args[2]);
							setting.setSetting(v);
							new Message(MessageType.SETTING_SET, player, setting, value, GuildsBasic);
						} else {
							new Message(MessageType.NOT_BOOLEAN, player, GuildsBasic);
						}
					} else if (setting.isInteger()) {
						if (isInteger(args[2])) {
							int v = Integer.parseInt(args[2]);
							setting.setSetting(v);
							new Message(MessageType.SETTING_SET, player, setting, value, GuildsBasic);
						} else {
							new Message(MessageType.NOT_INT, player, GuildsBasic);
						}
					} else if (setting.isLong()) {
						if (isInteger(args[2])) {
							long v = (long) Integer.parseInt(args[2]);
							setting.setSetting(v);
							new Message(MessageType.SETTING_SET, player, setting, value, GuildsBasic);
						} else {
							new Message(MessageType.NOT_INT, player, GuildsBasic);
						}
					} else {
						setting.setSetting(value);
						new Message(MessageType.SETTING_SET, player, setting, value, GuildsBasic);
					}
				} else {
					new Message(MessageType.SETTING_NOT_RECOGNISED, player, args[1], GuildsBasic);
				}
			} else {
				new Message(MessageType.COMMAND_SETTING, player, GuildsBasic);
			}
		} else {
			new Message(MessageType.NO_PERMISSION, player, GuildsBasic);
		}
		
	}
	
	private void Console(String[] args) {
		
		Settings setting = null;
		
		if (args.length > 2) {
			for (Settings s : Settings.values()) {
				if (s.findSettings(args[1])) {
					setting = s;
				}
			}
			if (setting != null) {
				boolean first = true;
				boolean second = true;
				String value = "";
				for (String str : args) {
					if (first) {
						first = false;
					} else if (second) {
						second = false;
					} else {
						value = value + str + " ";
					}
				}
				if (setting.isBoolean()) {
					if (isBoolean(args[2])) {
						boolean v = Boolean.parseBoolean(args[2]);
						setting.setSetting(v);
						new Console(MessageType.SETTING_SET, setting, value, GuildsBasic);
					} else {
						new Console(MessageType.NOT_BOOLEAN, GuildsBasic);
					}
				} else if (setting.isInteger()) {
					if (isInteger(args[2])) {
						int v = Integer.parseInt(args[2]);
						setting.setSetting(v);
						new Console(MessageType.SETTING_SET, setting, value, GuildsBasic);
					} else {
						new Console(MessageType.NOT_INT, GuildsBasic);
					}
				} else if (setting.isLong()) {
					if (isInteger(args[2])) {
						long v = (long) Integer.parseInt(args[2]);
						setting.setSetting(v);
						new Console(MessageType.SETTING_SET, setting, value, GuildsBasic);
					} else {
						new Console(MessageType.NOT_INT, GuildsBasic);
					}
				} else {
					setting.setSetting(value);
					new Console(MessageType.SETTING_SET, setting, value, GuildsBasic);
				}
			} else {
				new Console(MessageType.SETTING_NOT_RECOGNISED, args[1], GuildsBasic);
			}
		} else {
			new Console(MessageType.COMMAND_SETTING, GuildsBasic);
		}
		
	}
	
	private boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    return true;
	}
	
	private boolean isBoolean(String s) {
		if (s.equalsIgnoreCase("true")) return true;
		if (s.equalsIgnoreCase("false")) return true;
	    return false;
	}
	
}
