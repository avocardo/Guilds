package me.avocardo.guilds.messages;

import me.avocardo.guilds.Guild;
import me.avocardo.guilds.GuildsBasic;
import me.avocardo.guilds.utilities.ProficiencyType;

import org.bukkit.entity.Player;

public class Message {

	public Message(MessageType msg, Player input1, GuildsBasic GuildsBasic) {
		
		String message = msg.getMessage();
		
		message = message.replaceAll("/p/", input1.getName());
		
		GuildsBasic.sendMessage(input1, message);
		
	}
	
	public Message(MessageType msg, Player player, String input1, GuildsBasic GuildsBasic) {
		
		String message = msg.getMessage();
		
		message = message.replaceAll("/p/", input1);
		
		GuildsBasic.sendMessage(player, message);
		
	}
	
	public Message(MessageType msg, Player player, ProficiencyType input1, GuildsBasic GuildsBasic) {
		
		String message = msg.getMessage();
		
		message = message.replaceAll("/p/", input1.toString());
		
		GuildsBasic.sendMessage(player, message);
		
	}
	
	public Message(MessageType msg, Player player, Player input1, GuildsBasic GuildsBasic) {
		
		String message = msg.getMessage();
		
		message = message.replaceAll("/p/", input1.getName());
		
		GuildsBasic.sendMessage(player, message);
		
	}
	
	public Message(MessageType msg, Player player, Guild input1, GuildsBasic GuildsBasic) {
		
		String message = msg.getMessage();
		
		message = message.replaceAll("/g/", input1.getName());
		
		GuildsBasic.sendMessage(player, message);
		
	}
	
	public Message(MessageType msg, Player player, Player input1, Guild input2, GuildsBasic GuildsBasic) {
		
		String message = msg.getMessage();
		
		message = message.replaceAll("/p/", input1.getName());
		message = message.replaceAll("/g/", input2.getName());
		
		GuildsBasic.sendMessage(player, message);
		
	}
	
}
