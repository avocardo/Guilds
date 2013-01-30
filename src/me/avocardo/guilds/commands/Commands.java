package me.avocardo.guilds.commands;

import me.avocardo.guilds.GuildsBasic;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

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
				
				if (args[0].equalsIgnoreCase("save")) {
					new CommandSave(sender, args, GuildsBasic);
				}
				
				if (args[0].equalsIgnoreCase("load")) {
					new CommandLoad(sender, args, GuildsBasic);
				}
				
				if (args[0].equalsIgnoreCase("join")) {
					new CommandJoin(sender, args, GuildsBasic);
				}
				
				if (args[0].equalsIgnoreCase("add")) {
					new CommandAdd(sender, args, GuildsBasic);
				}
				
				if (args[0].equalsIgnoreCase("kick")) {
					new CommandKick(sender, args, GuildsBasic);
				}
				
				if (args[0].equalsIgnoreCase("leave")) {
					new CommandLeave(sender, args, GuildsBasic);
				}
				
				if (args[0].equalsIgnoreCase("create")) {
					new CommandCreate(sender, args, GuildsBasic);
				}
				
				if (args[0].equalsIgnoreCase("remove")) {
					new CommandRemove(sender, args, GuildsBasic);
				}
				
				if (args[0].equalsIgnoreCase("setbase")) {
					new CommandSetBase(sender, args, GuildsBasic);
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
