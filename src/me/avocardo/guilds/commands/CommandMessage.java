package me.avocardo.guilds.commands;

import me.avocardo.guilds.GuildsBasic;
import me.avocardo.guilds.messages.Console;
import me.avocardo.guilds.messages.Message;
import me.avocardo.guilds.messages.MessageType;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandMessage {

	private GuildsBasic GuildsBasic;
	
	public CommandMessage(CommandSender sender, String[] args, GuildsBasic GuildsBasic) {
		
		this.GuildsBasic = GuildsBasic;
		
		if (sender instanceof Player) {
			Player(args, (Player) sender);
		} else {
			Console(args);
		}
		
	}
	
	private void Player(String[] args, Player player) {
		
		MessageType msg = null;
		
		if (player.hasPermission("guilds.admin.messages")) {
			if (args.length > 2) {
				for (MessageType m : MessageType.values()) {
					if (m.toString().equalsIgnoreCase(args[1])) {
						msg = m;
					}
				}
				if (msg != null) {
					boolean first = true;
					boolean second = true;
					String value = "";
					for (String str : args) {
						if (first) {
							first = false;
						} else if (second) {
							second = false;
						} else {
							value = value + str + " ";
						}
					}
					msg.setMessage(value);
					new Message(MessageType.MESSAGE_SET, player, msg, value, GuildsBasic);
				} else {
					new Message(MessageType.MESSAGE_NOT_RECOGNISED, player, args[1], GuildsBasic);
				}
			} else {
				new Message(MessageType.COMMAND_MESSAGE, player, GuildsBasic);
			}
		} else {
			new Message(MessageType.NO_PERMISSION, player, GuildsBasic);
		}
		
	}
	
	private void Console(String[] args) {
		
		MessageType msg = null;
		
		if (args.length > 2) {
			for (MessageType m : MessageType.values()) {
				if (m.toString().equalsIgnoreCase(args[1])) {
					msg = m;
				}
			}
			if (msg != null) {
				boolean first = true;
				boolean second = true;
				String value = "";
				for (String str : args) {
					if (first) {
						first = false;
					} else if (second) {
						second = false;
					} else {
						value = value + str + " ";
					}
				}
				msg.setMessage(value);
				new Console(MessageType.MESSAGE_SET, msg, value, GuildsBasic);
			} else {
				new Console(MessageType.MESSAGE_NOT_RECOGNISED, args[1], GuildsBasic);
			}
		} else {
			new Console(MessageType.COMMAND_MESSAGE, GuildsBasic);
		}
		
	}
	
}
