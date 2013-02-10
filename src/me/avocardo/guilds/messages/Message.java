package me.avocardo.guilds.messages;

import me.avocardo.guilds.Guild;
import me.avocardo.guilds.GuildsBasic;
import me.avocardo.guilds.utilities.ProficiencyType;
import me.avocardo.guilds.utilities.Settings;

import org.bukkit.entity.Player;

public class Message {

	public Message(MessageType msg, Player input1, GuildsBasic GuildsBasic) {
		
		String message = msg.getMessage();
		
		if (message.contains("/p/")) message = message.replaceAll("/p/", input1.getName());
		if (message.contains("/g/")) message = message.replaceAll("/g/", "");
		
		GuildsBasic.sendMessage(input1, message);
		
	}
	
	public Message(MessageType msg, Player input1, Settings input2, String input3, GuildsBasic GuildsBasic) {
		
		String message = msg.getMessage();
		
		if (message.contains("/p/")) message = message.replaceAll("/p/", input1.getName());
		if (message.contains("/g/")) message = message.replaceAll("/g/", "");
		if (message.contains("/s/")) message = message.replaceAll("/s/", input2.toString());
		if (message.contains("/v/")) message = message.replaceAll("/v/", input3);
		
		GuildsBasic.sendMessage(input1, message);
		
	}
	
	public Message(MessageType msg, Player input1, MessageType input2, String input3, GuildsBasic GuildsBasic) {
		
		String message = msg.getMessage();
		
		if (message.contains("/p/")) message = message.replaceAll("/p/", input1.getName());
		if (message.contains("/g/")) message = message.replaceAll("/g/", "");
		if (message.contains("/m/")) message = message.replaceAll("/m/", input2.toString());
		if (message.contains("/v/")) message = message.replaceAll("/v/", input3);
		
		GuildsBasic.sendMessage(input1, message);
		
	}
	
	public Message(MessageType msg, Player player, String input1, GuildsBasic GuildsBasic) {
		
		String message = msg.getMessage();
		
		if (message.contains("/p/")) message = message.replaceAll("/p/", input1);
		if (message.contains("/g/")) message = message.replaceAll("/g/", input1);
		if (message.contains("/s/")) message = message.replaceAll("/s/", input1);
		if (message.contains("/m/")) message = message.replaceAll("/m/", input1);
		if (message.contains("/t/")) message = message.replaceAll("/t/", input1);
		if (message.contains("/a/")) message = message.replaceAll("/a/", input1);
		if (message.contains("/i/")) message = message.replaceAll("/i/", input1);
		if (message.contains("/b/")) message = message.replaceAll("/b/", input1);
		if (message.contains("/w/")) message = message.replaceAll("/w/", input1);
		
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
	
	public Message(MessageType msg, Player player, Guild guild, String input1, String input2, String input3, String input4, GuildsBasic GuildsBasic) {
		
		String message = msg.getMessage();
		
		message = message.replaceAll("/g/", guild.getName());
		message = message.replaceAll("/k/", input1);
		message = message.replaceAll("/d/", input2);
		message = message.replaceAll("/r/", input3);
		message = message.replaceAll("/e/", input4);
		
		GuildsBasic.sendMessage(player, message);
		
	}
	
	public Message(MessageType msg, Player player, String guild, String input1, String input2, String input3, String input4, GuildsBasic GuildsBasic) {
		
		String message = msg.getMessage();
		
		message = message.replaceAll("/g/", guild);
		message = message.replaceAll("/k/", input1);
		message = message.replaceAll("/d/", input2);
		message = message.replaceAll("/r/", input3);
		message = message.replaceAll("/e/", input4);
		
		GuildsBasic.sendMessage(player, message);
		
	}
	
	public Message(MessageType msg, Player player, Guild guild, String input1, String input2, GuildsBasic GuildsBasic) {
		
		String message = msg.getMessage();
		
		message = message.replaceAll("/g/", guild.getName());
		message = message.replaceAll("/p/", player.getName());
		message = message.replaceAll("/s/", input1);
		message = message.replaceAll("/t/", input1);
		message = message.replaceAll("/v/", input2);
		
		GuildsBasic.sendMessage(player, message);
		
	}

	public Message(MessageType msg, Player player, ProficiencyType input1, String input2, String input3, Guild input4, GuildsBasic GuildsBasic) {
		
		String message = msg.getMessage();
		
		message = message.replaceAll("/p/", input1.toString());
		message = message.replaceAll("/i/", input2);
		message = message.replaceAll("/v/", input3);
		message = message.replaceAll("/g/", input4.getName());
		
		GuildsBasic.sendMessage(player, message);
		
	}
	
}
