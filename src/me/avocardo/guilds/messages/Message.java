package me.avocardo.guilds.messages;

import me.avocardo.guilds.Guild;
import me.avocardo.guilds.GuildsBasic;
import me.avocardo.guilds.utilities.ProficiencyType;

import org.bukkit.entity.Player;

public class Message {

	public Message(MessageType msg, Player input1, GuildsBasic GuildsBasic) {
		
		String message = msg.getMessage();
		
		if (message.contains("/p/")) message = message.replaceAll("/p/", input1.getName());
		if (message.contains("/g/")) message = message.replaceAll("/g/", "");
		
		GuildsBasic.sendMessage(input1, message);
		
	}
	
	public Message(MessageType msg, Player player, String input1, GuildsBasic GuildsBasic) {
		
		String message = msg.getMessage();
		
		if (message.contains("/p/")) message = message.replaceAll("/p/", input1);
		if (message.contains("/g/")) message = message.replaceAll("/g/", input1);
		
		GuildsBasic.sendMessage(player, message);
		
	}
	
	public Message(MessageType msg, Player player, ProficiencyType input1, GuildsBasic GuildsBasic) {
		
		String message = msg.getMessage();
		
		if (message.contains("/s/")) message = message.replaceAll("/s/", input1.toString());
		if (message.contains("/g/")) message = message.replaceAll("/g/", "");
		if (message.contains("/p/")) message = message.replaceAll("/p/", "");
		
		GuildsBasic.sendMessage(player, message);
		
	}
	
	public Message(MessageType msg, Player player, Player input1, GuildsBasic GuildsBasic) {
		
		String message = msg.getMessage();
		
		if (message.contains("/p/")) message = message.replaceAll("/p/", input1.getName());
		if (message.contains("/g/")) message = message.replaceAll("/g/", "");
		
		GuildsBasic.sendMessage(player, message);
		
	}
	
	public Message(MessageType msg, Player player, Guild input1, GuildsBasic GuildsBasic) {
		
		String message = msg.getMessage();
		
		if (message.contains("/g/")) message = message.replaceAll("/g/", input1.getName());
		if (message.contains("/p/")) message = message.replaceAll("/p/", "");
		
		GuildsBasic.sendMessage(player, message);
		
	}
	
	public Message(MessageType msg, Player player, Player input1, Guild input2, GuildsBasic GuildsBasic) {
		
		String message = msg.getMessage();
		
		if (message.contains("/p/")) message = message.replaceAll("/p/", input1.getName());
		if (message.contains("/g/")) message = message.replaceAll("/g/", input2.getName());
		
		GuildsBasic.sendMessage(player, message);
		
	}
	
}
