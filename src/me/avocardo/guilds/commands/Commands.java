package me.avocardo.guilds.commands;

import me.avocardo.guilds.GuildsBasic;
import me.avocardo.guilds.messages.Console;
import me.avocardo.guilds.messages.Message;
import me.avocardo.guilds.messages.MessageType;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

	private GuildsBasic GuildsBasic;
	
	public Commands(GuildsBasic GuildsBasic) {
		this.GuildsBasic = GuildsBasic;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (label.equalsIgnoreCase("guilds")) {
						
			if (args.length > 0) {
					
				if (args[0].equalsIgnoreCase("list")) {
					new CommandList(sender, args, GuildsBasic);
				}
				
				else if (args[0].equalsIgnoreCase("save")) {
					new CommandSave(sender, args, GuildsBasic);
				}
				
				else if (args[0].equalsIgnoreCase("load")) {
					new CommandLoad(sender, args, GuildsBasic);
				}
				
				else if (args[0].equalsIgnoreCase("join")) {
					new CommandJoin(sender, args, GuildsBasic);
				}
				
				else if (args[0].equalsIgnoreCase("add")) {
					new CommandAdd(sender, args, GuildsBasic);
				}
				
				else if (args[0].equalsIgnoreCase("kick")) {
					new CommandKick(sender, args, GuildsBasic);
				}
				
				else if (args[0].equalsIgnoreCase("leave")) {
					new CommandLeave(sender, args, GuildsBasic);
				}
				
				else if (args[0].equalsIgnoreCase("create")) {
					new CommandCreate(sender, args, GuildsBasic);
				}
				
				else if (args[0].equalsIgnoreCase("remove")) {
					new CommandRemove(sender, args, GuildsBasic);
				}
				
				else if (args[0].equalsIgnoreCase("setbase")) {
					new CommandSetBase(sender, args, GuildsBasic);
				}
				
				else if (args[0].equalsIgnoreCase("setting")) {
					new CommandSetting(sender, args, GuildsBasic);
				}
				
				else if (args[0].equalsIgnoreCase("settings")) {
					new CommandSettings(sender, args, GuildsBasic);
				}
				
				else if (args[0].equalsIgnoreCase("message")) {
					new CommandMessage(sender, args, GuildsBasic);
				}
				
				else if (args[0].equalsIgnoreCase("leaderboard")) {
					new CommandLeaderBoard(sender, args, GuildsBasic);
				}
				
				else if (args[0].equalsIgnoreCase("proficiency")) {
					new CommandProficiency(sender, args, GuildsBasic);
				}
				
				else if (args[0].equalsIgnoreCase("toggle")) {
					new CommandToggle(sender, args, GuildsBasic);
				}
				
				else if (args[0].equalsIgnoreCase("world")) {
					new CommandWorld(sender, args, GuildsBasic);
				}
				
				else if (args[0].equalsIgnoreCase("biome")) {
					new CommandBiome(sender, args, GuildsBasic);
				}
				
				else if (args[0].equalsIgnoreCase("restrictions")) {
					new CommandRestrictions(sender, args, GuildsBasic);
				}
				
				else {
					if (sender instanceof Player) new Message(MessageType.COMMAND_NOT_RECOGNISED, (Player) sender, args[0], GuildsBasic);
					else new Console(MessageType.COMMAND_NOT_RECOGNISED, args[0], GuildsBasic);
				}
				
			} else {
				new CommandHelp(sender, args, GuildsBasic);
			}
				
			return true;
				
		}
			
		if (label.equalsIgnoreCase("base")) {
			
			new CommandBase(sender, args, GuildsBasic);
			
			return true;
			
		}
			
		return false;
		
	}

	

}
