package me.avocardo.guilds.commands;

import me.avocardo.guilds.GuildsBasic;
import me.avocardo.guilds.messages.Console;
import me.avocardo.guilds.messages.Message;
import me.avocardo.guilds.messages.MessageType;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSave {

	private GuildsBasic GuildsBasic;
	
	public CommandSave(CommandSender sender, String[] args, GuildsBasic GuildsBasic) {
		
		this.GuildsBasic = GuildsBasic;
		
		if (sender instanceof Player) {
			Player(args, (Player) sender);
		} else {
			Console(args);
		}
		
	}
	
	private void Player(String[] args, Player player) {
		
		if (player.hasPermission("guilds.admin.save")) {
			GuildsBasic.saveGuilds();
			GuildsBasic.savePlayers();
			GuildsBasic.saveSettings();
			GuildsBasic.saveMessages();
			new Message(MessageType.SAVE, player, GuildsBasic);
		} else {
			new Message(MessageType.NO_PERMISSION, player, GuildsBasic);
		}
		
	}
	
	private void Console(String[] args) {
		
		GuildsBasic.saveGuilds();
		GuildsBasic.savePlayers();
		GuildsBasic.saveSettings();
		GuildsBasic.saveMessages();
		
		new Console(MessageType.SAVE, GuildsBasic);
		
	}
	
}
