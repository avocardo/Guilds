package me.avocardo.guilds.commands;

import me.avocardo.guilds.Guild;
import me.avocardo.guilds.GuildsBasic;
import me.avocardo.guilds.messages.Console;
import me.avocardo.guilds.messages.Message;
import me.avocardo.guilds.messages.MessageType;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSetBase {

	private GuildsBasic GuildsBasic;
	
	public CommandSetBase(CommandSender sender, String[] args, GuildsBasic GuildsBasic) {
		
		this.GuildsBasic = GuildsBasic;
		
		if (sender instanceof Player) {
			Player(args, (Player) sender);
		} else {
			Console(args);
		}
		
	}
	
	private void Player(String[] args, Player p) {
		
		if (p.hasPermission("guilds.admin.setbase")) {
			Guild guild = null;
			if (args.length > 1) {
				guild = GuildsBasic.getGuild(args[1]);
			} else {
				guild = GuildsBasic.getPlayerGuild(p);
			}
			if (guild != null) {
				guild.setBase(p.getLocation());
				new Message(MessageType.BASE_SET, p, guild, GuildsBasic);
				GuildsBasic.saveGuilds();
				GuildsBasic.loadGuilds();
			} else {
				new Message(MessageType.GUILD_NOT_RECOGNISED, p, args[1], GuildsBasic);
			}
		} else {
			new Message(MessageType.NO_PERMISSION, p, GuildsBasic);
		}
		
	}
	
	private void Console(String[] args) {
		
		new Console(MessageType.CONSOLE_ERROR, GuildsBasic);
		
	}

}
