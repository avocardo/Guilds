package me.avocardo.guilds.commands;

import me.avocardo.guilds.Guild;
import me.avocardo.guilds.GuildsBasic;
import me.avocardo.guilds.messages.Console;
import me.avocardo.guilds.messages.Message;
import me.avocardo.guilds.messages.MessageType;
import me.avocardo.guilds.utilities.Settings;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandJoin {

	private GuildsBasic GuildsBasic;
	
	public CommandJoin(CommandSender sender, String[] args, GuildsBasic GuildsBasic) {
		
		this.GuildsBasic = GuildsBasic;
		
		if (sender instanceof Player) {
			Player(args, (Player) sender);
		} else {
			Console(args);
		}
		
	}
	
	private void Player(String[] args, Player player) {
		
		if (args.length > 1) {
			if (GuildsBasic.getEnabled(Settings.ENABLE_GUILD_JOIN_PERMISSIONS)) {
				if (player.hasPermission("guilds.guild." + args[1])) {
					join(player, args[1]);
				} else {
					new Message(MessageType.NO_PERMISSION_JOIN, player, args[1], GuildsBasic);
				}
			} else {
				if (player.hasPermission("guilds.user.join")) {
					join(player, args[1]);
				} else {
					new Message(MessageType.NO_PERMISSION, player, GuildsBasic);
				}
			}
		} else {
			new Message(MessageType.COMMAND_JOIN, player, GuildsBasic);
		}
		
	}
	
	private void Console(String[] args) {
		
		new Console(MessageType.CONSOLE_ERROR, GuildsBasic);
		
	}
	
	private void join(Player p, String g) {
		
		Guild guild = GuildsBasic.getGuild(g);

		if (guild != null) {
			if (GuildsBasic.getEnabled(Settings.ENABLE_CHANGE_GUILD)) {
				if (GuildsBasic.PlayerGuild.containsKey(p.getName())) {
					GuildsBasic.PlayerGuild.remove(p.getName());
				}
				GuildsBasic.PlayerGuild.put(p.getName(), guild);
				GuildsBasic.savePlayers();
				GuildsBasic.loadPlayers();
				GuildsBasic.clearPlayerScheduler(p);
				new Message(MessageType.GUILD_JOIN, p, p, guild, GuildsBasic);
			} else {
				if (GuildsBasic.getPlayerGuild(p) == null) {
					if (GuildsBasic.PlayerGuild.containsKey(p.getName())) {
						GuildsBasic.PlayerGuild.remove(p.getName());
					}
					GuildsBasic.PlayerGuild.put(p.getName(), guild);
					GuildsBasic.savePlayers();
					GuildsBasic.loadPlayers();
					GuildsBasic.clearPlayerScheduler(p);
					new Message(MessageType.GUILD_JOIN, p, p, guild, GuildsBasic);
				} else {
					new Message(MessageType.ALREADY_IN_GUILD, p, p, GuildsBasic);
				}
			}
		} else {
			new Message(MessageType.GUILD_NOT_RECOGNISED, p, g, GuildsBasic);
		}
		
	}
	
}
