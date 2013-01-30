package me.avocardo.guilds.commands;

import me.avocardo.guilds.Guild;
import me.avocardo.guilds.GuildsBasic;
import me.avocardo.guilds.messages.Console;
import me.avocardo.guilds.messages.Message;
import me.avocardo.guilds.messages.MessageType;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandRemove {

	private GuildsBasic GuildsBasic;
	
	public CommandRemove(CommandSender sender, String[] args, GuildsBasic GuildsBasic) {
		
		this.GuildsBasic = GuildsBasic;
		
		if (sender instanceof Player) {
			Player(args, (Player) sender);
		} else {
			Console(args);
		}
		
	}
	
	private void Player(String[] args, Player p) {
		
		if (args[0].equalsIgnoreCase("remove")) {
			if (args.length > 1) {
				if (p.hasPermission("guilds.admin.remove")) {
					Guild guild = GuildsBasic.getGuild(args[1]);
					if (guild != null) {
						GuildsBasic.GuildsList.remove(guild);
						new Message(MessageType.GUILD_DELETED, p, guild, GuildsBasic);
						GuildsBasic.saveGuilds();
						GuildsBasic.loadGuilds();
						GuildsBasic.savePlayers();
						GuildsBasic.loadPlayers();
					} else {
						new Message(MessageType.GUILD_NOT_RECOGNISED, p, args[1], GuildsBasic);
					}
				} else {
					new Message(MessageType.NO_PERMISSION, p, GuildsBasic);
				}
			} else {
				new Message(MessageType.COMMAND_REMOVE, p, GuildsBasic);
			}
		}
		
	}
	
	private void Console(String[] args) {
		
		if (args.length > 1) {
			Guild guild = GuildsBasic.getGuild(args[1]);
			if (guild != null) {
				GuildsBasic.GuildsList.remove(guild);
				new Console(MessageType.GUILD_DELETED, guild, GuildsBasic);
				GuildsBasic.saveGuilds();
				GuildsBasic.loadGuilds();
				GuildsBasic.savePlayers();
				GuildsBasic.loadPlayers();
			} else {
				new Console(MessageType.GUILD_NOT_RECOGNISED, args[1], GuildsBasic);
			}
		} else {
			new Console(MessageType.COMMAND_REMOVE, GuildsBasic);
		}
		
	}	
	
}
