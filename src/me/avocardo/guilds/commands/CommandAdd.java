package me.avocardo.guilds.commands;

import me.avocardo.guilds.Guild;
import me.avocardo.guilds.GuildsBasic;
import me.avocardo.guilds.messages.Console;
import me.avocardo.guilds.messages.Message;
import me.avocardo.guilds.messages.MessageType;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandAdd {

	private GuildsBasic GuildsBasic;
	
	public CommandAdd(CommandSender sender, String[] args, GuildsBasic GuildsBasic) {
		
		this.GuildsBasic = GuildsBasic;
		
		if (sender instanceof Player) {
			Player(args, (Player) sender);
		} else {
			Console(args);
		}
		
	}
	
	private void Player(String[] args, Player player) {
		
		if (args.length > 2) {
			if (player.hasPermission("guilds.admin.add")) {
				Player p = Bukkit.getPlayer(args[1]);
				Guild g = GuildsBasic.getGuild(args[2]);
				if (p != null) {
					if (g != null) {
						if (GuildsBasic.getPlayerGuild(p) != null) {
							GuildsBasic.getPlayerGuild(p).subtractOnline();
						}
						if (GuildsBasic.PlayerGuild.containsKey(p.getName())) {
							GuildsBasic.PlayerGuild.remove(p.getName());
						}
						if (GuildsBasic.PlayerJoined.containsKey(p.getName())) {
							GuildsBasic.PlayerJoined.remove(p.getName());
						}
						g.addOnline();
						GuildsBasic.PlayerGuild.put(p.getName(), g);
						GuildsBasic.PlayerJoined.put(p.getName(), System.currentTimeMillis());
						GuildsBasic.savePlayers();
						GuildsBasic.loadPlayers();
						GuildsBasic.clearPlayerScheduler(player);
						new Message(MessageType.PLAYER_GUILD_JOIN, player, p, g, GuildsBasic);
						if (!player.equals(p))
							new Message(MessageType.PLAYER_GUILD_JOIN, p, p, g, GuildsBasic);
					} else {
						new Message(MessageType.GUILD_NOT_RECOGNISED, player, args[2], GuildsBasic);
					}
				} else {
					new Message(MessageType.PLAYER_NOT_RECOGNISED, player, args[1], GuildsBasic);
				}
			} else {
				new Message(MessageType.NO_PERMISSION, player, GuildsBasic);
			}
		} else {
			new Message(MessageType.COMMAND_ADD, player, GuildsBasic);
		}
		
	}
	
	private void Console(String[] args) {
		
		if (args.length > 2) {
			Player p = Bukkit.getPlayer(args[1]);
			Guild g = GuildsBasic.getGuild(args[2]);
			if (p != null) {
				if (g != null) {
					if (GuildsBasic.getPlayerGuild(p) != null) {
						GuildsBasic.getPlayerGuild(p).subtractOnline();
					}
					if (GuildsBasic.PlayerGuild.containsKey(p)) {
						GuildsBasic.PlayerGuild.remove(p);
					}
					if (GuildsBasic.PlayerJoined.containsKey(p.getName())) {
						GuildsBasic.PlayerJoined.remove(p.getName());
					}
					g.addOnline();
					GuildsBasic.PlayerGuild.put(p.getName(), g);
					GuildsBasic.PlayerJoined.put(p.getName(), System.currentTimeMillis());
					GuildsBasic.savePlayers();
					GuildsBasic.loadPlayers();
					GuildsBasic.clearPlayerScheduler(p);
					new Console(MessageType.PLAYER_GUILD_JOIN, p, g, GuildsBasic);
					new Message(MessageType.PLAYER_GUILD_JOIN, p, p, g, GuildsBasic);
				} else {
					new Console(MessageType.GUILD_NOT_RECOGNISED, args[2], GuildsBasic);
				}
			} else {
				new Console(MessageType.PLAYER_NOT_RECOGNISED, args[1], GuildsBasic);
			}
		} else {
			new Console(MessageType.COMMAND_ADD, GuildsBasic);
		}
		
	}
	
}
