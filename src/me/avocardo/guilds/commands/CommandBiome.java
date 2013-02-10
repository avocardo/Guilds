package me.avocardo.guilds.commands;

import me.avocardo.guilds.Guild;
import me.avocardo.guilds.GuildsBasic;
import me.avocardo.guilds.messages.Console;
import me.avocardo.guilds.messages.Message;
import me.avocardo.guilds.messages.MessageType;

import org.bukkit.block.Biome;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandBiome {

	private GuildsBasic GuildsBasic;
	
	public CommandBiome(CommandSender sender, String[] args, GuildsBasic GuildsBasic) {
		
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
			new Message(MessageType.COMMAND_BIOME, player, GuildsBasic);
			return;
		}
		
		Guild g = GuildsBasic.getGuild(args[1]);
		
		if (g == null) {
			new Message(MessageType.GUILD_NOT_RECOGNISED, player, args[1], GuildsBasic);
			return;
		}
		
		Biome b = null;
		
		for (Biome biome : Biome.values()) {
			if (biome.toString().equalsIgnoreCase(args[2])) {
				b = biome;
			}
		}
		
		if (b == null) {
			new Message(MessageType.BIOME_NOT_RECOGNISED, player, args[2], GuildsBasic);
			return;
		}
		
		if (g.getBiomes().contains(b)) {
			g.removeBiome(b);
			new Message(MessageType.TOGGLE_SET, player, g, b.toString(), "false", GuildsBasic);
			return;
		} else {
			g.addBiome(b);
			new Message(MessageType.TOGGLE_SET, player, g, b.toString(), "true", GuildsBasic);
			return;
		}
		
	}
	
	private void Console(String[] args) {
		
		if (!(args.length > 2)) {
			new Console(MessageType.COMMAND_BIOME, GuildsBasic);
			return;
		}
		
		Guild g = GuildsBasic.getGuild(args[1]);
		
		if (g == null) {
			new Console(MessageType.GUILD_NOT_RECOGNISED, args[1], GuildsBasic);
			return;
		}
		
		Biome b = null;
		
		for (Biome biome : Biome.values()) {
			if (biome.toString().equalsIgnoreCase(args[2])) {
				b = biome;
			}
		}
		
		if (b == null) {
			new Console(MessageType.BIOME_NOT_RECOGNISED, args[2], GuildsBasic);
			return;
		}
		
		if (g.getBiomes().contains(b)) {
			g.removeBiome(b);
			new Console(MessageType.TOGGLE_SET, g, b.toString(), "false", GuildsBasic);
			return;
		} else {
			g.addBiome(b);
			new Console(MessageType.TOGGLE_SET, g, b.toString(), "true", GuildsBasic);
			return;
		}
		
	}
	
}
