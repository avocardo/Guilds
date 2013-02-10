package me.avocardo.guilds.commands;

import me.avocardo.guilds.Guild;
import me.avocardo.guilds.GuildsBasic;
import me.avocardo.guilds.messages.Console;
import me.avocardo.guilds.messages.Message;
import me.avocardo.guilds.messages.MessageType;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandWorld {

	private GuildsBasic GuildsBasic;
	
	public CommandWorld(CommandSender sender, String[] args, GuildsBasic GuildsBasic) {
		
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
			new Message(MessageType.COMMAND_WORLD, player, GuildsBasic);
			return;
		}
		
		Guild g = GuildsBasic.getGuild(args[1]);
		
		if (g == null) {
			new Message(MessageType.GUILD_NOT_RECOGNISED, player, args[1], GuildsBasic);
			return;
		}
		
		World w = Bukkit.getWorld(args[2]);
		
		if (w == null) {
			new Message(MessageType.WORLD_NOT_RECOGNISED, player, args[2], GuildsBasic);
			return;
		}
		
		if (g.getWorlds().contains(w)) {
			g.removeWorld(w);
			new Message(MessageType.TOGGLE_SET, player, g, w.getName(), "false", GuildsBasic);
			return;
		} else {
			g.addWorld(w);
			new Message(MessageType.TOGGLE_SET, player, g, w.getName(), "true", GuildsBasic);
			return;
		}
		
	}
	
	private void Console(String[] args) {
		
		if (!(args.length > 2)) {
			new Console(MessageType.COMMAND_WORLD, GuildsBasic);
			return;
		}
		
		Guild g = GuildsBasic.getGuild(args[1]);
		
		if (g == null) {
			new Console(MessageType.GUILD_NOT_RECOGNISED, args[1], GuildsBasic);
			return;
		}
		
		World w = Bukkit.getWorld(args[2]);
		
		if (w == null) {
			new Console(MessageType.WORLD_NOT_RECOGNISED, args[2], GuildsBasic);
			return;
		}
		
		if (g.getWorlds().contains(w)) {
			g.removeWorld(w);
			new Console(MessageType.TOGGLE_SET, g, w.getName(), "false", GuildsBasic);
			return;
		} else {
			g.addWorld(w);
			new Console(MessageType.TOGGLE_SET, g, w.getName(), "true", GuildsBasic);
			return;
		}
		
	}
	
}
