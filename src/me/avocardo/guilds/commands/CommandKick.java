package me.avocardo.guilds.commands;

import me.avocardo.guilds.GuildsBasic;
import me.avocardo.guilds.messages.Console;
import me.avocardo.guilds.messages.Message;
import me.avocardo.guilds.messages.MessageType;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandKick {

	private GuildsBasic GuildsBasic;
	
	public CommandKick(CommandSender sender, String[] args, GuildsBasic GuildsBasic) {
		
		this.GuildsBasic = GuildsBasic;
		
		if (sender instanceof Player) {
			Player(args, (Player) sender);
		} else {
			Console(args);
		}
		
	}
	
	private void Player(String[] args, Player p) {
		
		if (args.length > 1) {
			if (p.hasPermission("guilds.admin.kick")) {
				Player player = Bukkit.getPlayer(args[1]);
				if (player != null) {
					if (GuildsBasic.getPlayerGuild(player) != null) {
						GuildsBasic.getPlayerGuild(player).subtractOnline();
					}
					if (GuildsBasic.PlayerGuild.containsKey(player.getName())) {
						GuildsBasic.PlayerGuild.remove(player.getName());
					}
					GuildsBasic.savePlayers();
					GuildsBasic.loadPlayers();
					GuildsBasic.clearPlayerScheduler(player);
					new Message(MessageType.PLAYER_REMOVED_FROM_GUILD, p, player, GuildsBasic);
					if (!player.equals(p))
							new Message(MessageType.YOU_REMOVED_FROM_GUILD, player, GuildsBasic);
				} else {
					new Message(MessageType.PLAYER_NOT_RECOGNISED, p, args[1], GuildsBasic);
				}
			} else {
				new Message(MessageType.NO_PERMISSION, p, GuildsBasic);
			}
		} else {
			new Message(MessageType.COMMAND_KICK, p, GuildsBasic);
		}
		
	}
	
	private void Console(String[] args) {
		
		if (args.length > 1) {
			Player player = Bukkit.getPlayer(args[1]);
			if (player != null) {
				if (GuildsBasic.getPlayerGuild(player) != null) {
					GuildsBasic.getPlayerGuild(player).subtractOnline();
				}
				if (GuildsBasic.PlayerGuild.containsKey(args[1])) {
					GuildsBasic.PlayerGuild.remove(args[1]);
				}
				GuildsBasic.savePlayers();
				GuildsBasic.loadPlayers();
				GuildsBasic.clearPlayerScheduler(player);
				new Console(MessageType.PLAYER_REMOVED_FROM_GUILD, player, GuildsBasic);
				new Message(MessageType.YOU_REMOVED_FROM_GUILD, player, GuildsBasic);
			} else {
				new Console(MessageType.PLAYER_NOT_RECOGNISED, args[1], GuildsBasic);
			}
		} else {
			new Console(MessageType.COMMAND_KICK, GuildsBasic);
		}
		
	}
	
}
