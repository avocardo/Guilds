package me.avocardo.guilds.commands;

import me.avocardo.guilds.Guild;
import me.avocardo.guilds.GuildsBasic;
import me.avocardo.guilds.messages.Console;
import me.avocardo.guilds.messages.Message;
import me.avocardo.guilds.messages.MessageType;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandRestrictions {

	private GuildsBasic GuildsBasic;
	
	public CommandRestrictions(CommandSender sender, String[] args, GuildsBasic GuildsBasic) {
		
		this.GuildsBasic = GuildsBasic;
		
		if (sender instanceof Player) {
			Player(args, (Player) sender);
		} else {
			Console(args);
		}
		
	}
	
	private void Player(String[] args, Player player) {
		
		if (!player.hasPermission("guilds.admin.toggle")) {
			new Message(MessageType.NO_PERMISSION, player, GuildsBasic);
			return;
		}
		
		if (!(args.length > 2)) {
			new Message(MessageType.COMMAND_RESTRICTIONS, player, GuildsBasic);
			return;
		}
		
		Guild g = GuildsBasic.getGuild(args[1]);
		
		if (g == null) {
			new Message(MessageType.GUILD_NOT_RECOGNISED, player, args[1], GuildsBasic);
			return;
		}
		
		Material m = null;
		
		if (isNumber(args[2])) {
			m = Material.getMaterial(Integer.parseInt(args[2])); 
		} else {
			m = Material.getMaterial(args[2]);
		}
		
		if (m == null) {
			new Message(MessageType.RESTRICTION_NOT_RECOGNISED, player, args[2], GuildsBasic);
			return;
		}
		
		if (g.getRestrictions().contains(m.getId())) {
			g.removeRestriction(m.getId());
			new Message(MessageType.TOGGLE_SET, player, g, m.toString(), "enabled", GuildsBasic);
			return;
		} else {
			g.addRestriction(m.getId());
			new Message(MessageType.TOGGLE_SET, player, g, m.toString(), "disabled", GuildsBasic);
			return;
		}
		
	}
	
	private void Console(String[] args) {
		
		if (!(args.length > 2)) {
			new Console(MessageType.COMMAND_RESTRICTIONS, GuildsBasic);
			return;
		}
		
		Guild g = GuildsBasic.getGuild(args[1]);
		
		if (g == null) {
			new Console(MessageType.GUILD_NOT_RECOGNISED, args[1], GuildsBasic);
			return;
		}
		
		Material m = null;
		
		if (isNumber(args[2])) {
			m = Material.getMaterial(Integer.parseInt(args[2])); 
		} else {
			m = Material.getMaterial(args[2]);
		}
		
		if (m == null) {
			new Console(MessageType.RESTRICTION_NOT_RECOGNISED, args[2], GuildsBasic);
			return;
		}
		
		if (g.getRestrictions().contains(m.getId())) {
			g.removeRestriction(m.getId());
			new Console(MessageType.TOGGLE_SET, g, m.toString(), "enabled", GuildsBasic);
			return;
		} else {
			g.addRestriction(m.getId());
			new Console(MessageType.TOGGLE_SET, g, m.toString(), "disabled", GuildsBasic);
			return;
		}
		
	}
	
	private boolean isNumber(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    return true;
	}
	
}
