package me.avocardo.guilds.commands;

import me.avocardo.guilds.GuildsBasic;
import me.avocardo.guilds.messages.Console;
import me.avocardo.guilds.messages.Message;
import me.avocardo.guilds.messages.MessageType;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandLoad {

	private GuildsBasic GuildsBasic;
	
	public CommandLoad(CommandSender sender, String[] args, GuildsBasic GuildsBasic) {
		
		this.GuildsBasic = GuildsBasic;
		
		if (sender instanceof Player) {
			Player(args, (Player) sender);
		} else {
			Console(args);
		}
		
	}
	
	private void Player(String[] args, Player player) {
		
		if (player.hasPermission("guilds.admin.load")) {
			GuildsBasic.loadGuilds();
			GuildsBasic.loadPlayers();
			GuildsBasic.loadSettings();
			GuildsBasic.loadMessages();
			GuildsBasic.clearScheduler();
			new Message(MessageType.LOAD, player, GuildsBasic);
		} else {
			new Message(MessageType.NO_PERMISSION, player, GuildsBasic);
		}
		
	}
	
	private void Console(String[] args) {
		
		GuildsBasic.loadGuilds();
		GuildsBasic.loadPlayers();
		GuildsBasic.loadSettings();
		GuildsBasic.loadMessages();
		GuildsBasic.clearScheduler();
		
		new Console(MessageType.LOAD, GuildsBasic);
		
	}
	
}
