package me.avocardo.guilds.commands;

import me.avocardo.guilds.GuildsBasic;
import me.avocardo.guilds.messages.Console;
import me.avocardo.guilds.messages.Message;
import me.avocardo.guilds.messages.MessageType;
import me.avocardo.guilds.utilities.Settings;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandLeave {

	private GuildsBasic GuildsBasic;
	
	public CommandLeave(CommandSender sender, String[] args, GuildsBasic GuildsBasic) {
		
		this.GuildsBasic = GuildsBasic;
		
		if (sender instanceof Player) {
			Player(args, (Player) sender);
		} else {
			Console(args);
		}
		
	}
	
	private void Player(String[] args, Player p) {
		
		if (p.hasPermission("guilds.user.leave")) {
			if (GuildsBasic.getEnabled(Settings.ENABLE_CHANGE_GUILD)) {
				if (GuildsBasic.PlayerGuild.containsKey(p)) {
					GuildsBasic.PlayerGuild.remove(p);
				}
				
				new Message(MessageType.GUILD_LEAVE, p, GuildsBasic);
				
				GuildsBasic.savePlayers();
				GuildsBasic.loadPlayers();
				GuildsBasic.clearPlayerScheduler(p);
				
			} else {
				new Message(MessageType.GUILD_CHOSEN, p, GuildsBasic);
			}
		} else {
			new Message(MessageType.NO_PERMISSION, p, GuildsBasic);
		}
		
	}
	
	private void Console(String[] args) {
		
		new Console(MessageType.CONSOLE_ERROR, GuildsBasic);
		
	}
	
}
