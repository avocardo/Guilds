package me.avocardo.guilds.commands;

import me.avocardo.guilds.Guild;
import me.avocardo.guilds.GuildsBasic;
import me.avocardo.guilds.messages.Console;
import me.avocardo.guilds.messages.Message;
import me.avocardo.guilds.messages.MessageType;

import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandToggle {

	private GuildsBasic GuildsBasic;
	
	public CommandToggle(CommandSender sender, String[] args, GuildsBasic GuildsBasic) {
		
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
			new Message(MessageType.COMMAND_TOGGLE, player, GuildsBasic);
			return;
		}
		
		Guild g = GuildsBasic.getGuild(args[1]);
		
		if (g == null) {
			new Message(MessageType.GUILD_NOT_RECOGNISED, player, args[1], GuildsBasic);
			return;
		}
		
		String[] Input = {"World", "Biome"};
		
		String i = null;
		
		for (String s : Input) {
			if (s.equalsIgnoreCase(args[2])) {
				i = s;
			}
		}
		
		if (i == null) {
			new Message(MessageType.TOGGLE_NOT_RECOGNISED, player, args[2], GuildsBasic);
			return;
		}
		
		if (i.equalsIgnoreCase("World")) {
			World w = player.getWorld();
			if (g.getWorlds().contains(w)) {
				g.removeWorld(w);
				new Message(MessageType.TOGGLE_SET, player, g, w.toString(), "false", GuildsBasic);
				return;
			} else {
				g.addWorld(w);
				new Message(MessageType.TOGGLE_SET, player, g, w.toString(), "true", GuildsBasic);
				return;
			}
		}
		
		if (i.equalsIgnoreCase("Biome")) {
			Biome b = player.getLocation().getBlock().getBiome();
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
		
	}
	
	private void Console(String[] args) {
		
		new Console(MessageType.CONSOLE_ERROR, GuildsBasic);
		
	}
	
}
