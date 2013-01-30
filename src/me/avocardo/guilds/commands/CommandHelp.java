package me.avocardo.guilds.commands;

import me.avocardo.guilds.GuildsBasic;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHelp {

	private GuildsBasic GuildsBasic;
	
	public CommandHelp(CommandSender sender, String[] args, GuildsBasic GuildsBasic) {
		
		this.GuildsBasic = GuildsBasic;
		
		if (sender instanceof Player) {
			Player(args, (Player) sender);
		} else {
			Console(args);
		}
		
	}
	
	private void Player(String[] args, Player player) {
		
		player.sendMessage(ChatColor.YELLOW + "=============== Guilds v" + GuildsBasic.v + " ===============");
		player.sendMessage(ChatColor.AQUA + "/guilds save " + ChatColor.YELLOW + ": save to file.");
		player.sendMessage(ChatColor.AQUA + "/guilds load " + ChatColor.YELLOW + ": load from file.");
		player.sendMessage(ChatColor.AQUA + "/guilds join <guild> " + ChatColor.YELLOW + ": join guild.");
		player.sendMessage(ChatColor.AQUA + "/guilds leave " + ChatColor.YELLOW + ": leave current guild.");
		player.sendMessage(ChatColor.AQUA + "/guilds kick <player> <guild> " + ChatColor.YELLOW + ": kick player from guild.");
		player.sendMessage(ChatColor.AQUA + "/guilds add <player> <guild> " + ChatColor.YELLOW + ": add player to guild.");
		player.sendMessage(ChatColor.AQUA + "/guilds create <guild> " + ChatColor.YELLOW + ": create guild.");
		player.sendMessage(ChatColor.AQUA + "/guilds remove <guild> " + ChatColor.YELLOW + ": remove guild.");
		player.sendMessage(ChatColor.AQUA + "/guilds setbase <guild> " + ChatColor.YELLOW + ": set guilds base.");
		player.sendMessage(ChatColor.AQUA + "/base " + ChatColor.YELLOW + ": tp to your guild base.");
		player.sendMessage(ChatColor.YELLOW + "=============== Guilds v" + GuildsBasic.v + " ===============");
		
	}
	
	private void Console(String[] args) {
		
		GuildsBasic.sendConsole(ChatColor.YELLOW + "=============== Guilds v" + GuildsBasic.v + " ===============");
		GuildsBasic.sendConsole(ChatColor.AQUA + "/guilds save " + ChatColor.YELLOW + ": save to file.");
		GuildsBasic.sendConsole(ChatColor.AQUA + "/guilds load " + ChatColor.YELLOW + ": load from file.");
		GuildsBasic.sendConsole(ChatColor.AQUA + "/guilds join <guild> " + ChatColor.YELLOW + ": join guild.");
		GuildsBasic.sendConsole(ChatColor.AQUA + "/guilds leave " + ChatColor.YELLOW + ": leave current guild.");
		GuildsBasic.sendConsole(ChatColor.AQUA + "/guilds kick <player> <guild> " + ChatColor.YELLOW + ": kick player from guild.");
		GuildsBasic.sendConsole(ChatColor.AQUA + "/guilds add <player> <guild> " + ChatColor.YELLOW + ": add player to guild.");
		GuildsBasic.sendConsole(ChatColor.AQUA + "/guilds create <guild> " + ChatColor.YELLOW + ": create guild.");
		GuildsBasic.sendConsole(ChatColor.AQUA + "/guilds remove <guild> " + ChatColor.YELLOW + ": remove guild.");
		GuildsBasic.sendConsole(ChatColor.AQUA + "/guilds setbase <guild> " + ChatColor.YELLOW + ": set guilds base.");
		GuildsBasic.sendConsole(ChatColor.AQUA + "/base " + ChatColor.YELLOW + ": tp to your guild base.");
		GuildsBasic.sendConsole(ChatColor.YELLOW + "=============== Guilds v" + GuildsBasic.v + " ===============");
		
	}
	
}
