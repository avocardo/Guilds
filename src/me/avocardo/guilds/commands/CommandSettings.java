package me.avocardo.guilds.commands;

import me.avocardo.guilds.Guild;
import me.avocardo.guilds.GuildsBasic;
import me.avocardo.guilds.messages.Console;
import me.avocardo.guilds.messages.Message;
import me.avocardo.guilds.messages.MessageType;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSettings {

	private GuildsBasic GuildsBasic;
	
	public CommandSettings(CommandSender sender, String[] args, GuildsBasic GuildsBasic) {
		
		this.GuildsBasic = GuildsBasic;
		
		if (sender instanceof Player) {
			Player(args, (Player) sender);
		} else {
			Console(args);
		}
		
	}
	
	private void Player(String[] args, Player player) {
		
		if (!player.hasPermission("guilds.admin.settings")) {
			new Message(MessageType.NO_PERMISSION, player, GuildsBasic);
			return;
		}
		
		if (!(args.length > 3)) {
			new Message(MessageType.COMMAND_SETTINGS, player, GuildsBasic);
			return;
		}
		
		Guild g = GuildsBasic.getGuild(args[1]);
		
		if (g == null) {
			new Message(MessageType.GUILD_NOT_RECOGNISED, player, args[1], GuildsBasic);
			return;
		}
		
		String[] Input = {"Color", "Prefix", "Suffix"};
		
		String i = null;
		
		for (String s : Input) {
			if (s.equalsIgnoreCase(args[2])) {
				i = s;
			}
		}
		
		if (i == null) {
			new Message(MessageType.INPUT_NOT_RECOGNISED, player, args[2], GuildsBasic);
			return;
		}
		
		String v = args[3];
		
		if (i.equalsIgnoreCase("Color")) {
			g.setColor(v);
			new Message(MessageType.SETTINGS_SET, player, g, i, v, GuildsBasic);
			return;
		}
		
		if (i.equalsIgnoreCase("Prefix")) {
			g.setPlayerPrefix(v);
			new Message(MessageType.SETTINGS_SET, player, g, i, v, GuildsBasic);
			return;
		}
		
		if (i.equalsIgnoreCase("Suffix")) {
			g.setPlayerSuffix(v);
			new Message(MessageType.SETTINGS_SET, player, g, i, v, GuildsBasic);
			return;
		}
		
	}
	
	private void Console(String[] args) {
		
		if (!(args.length > 3)) {
			new Console(MessageType.COMMAND_SETTINGS, GuildsBasic);
			return;
		}
		
		Guild g = GuildsBasic.getGuild(args[1]);
		
		if (g == null) {
			new Console(MessageType.GUILD_NOT_RECOGNISED, args[1], GuildsBasic);
			return;
		}
		
		String[] Input = {"Color", "Prefix", "Suffix"};
		
		String i = null;
		
		for (String s : Input) {
			if (s.equalsIgnoreCase(args[2])) {
				i = s;
			}
		}
		
		if (i == null) {
			new Console(MessageType.INPUT_NOT_RECOGNISED, args[2], GuildsBasic);
			return;
		}
		
		String v = args[3];
		
		if (i.equalsIgnoreCase("Color")) {
			g.setColor(v);
			new Console(MessageType.SETTINGS_SET, g, i, v, GuildsBasic);
			return;
		}
		
		if (i.equalsIgnoreCase("Prefix")) {
			g.setPlayerPrefix(v);
			new Console(MessageType.SETTINGS_SET, g, i, v, GuildsBasic);
			return;
		}
		
		if (i.equalsIgnoreCase("Suffix")) {
			g.setPlayerSuffix(v);
			new Console(MessageType.SETTINGS_SET, g, i, v, GuildsBasic);
			return;
		}
		
	}
	
}
