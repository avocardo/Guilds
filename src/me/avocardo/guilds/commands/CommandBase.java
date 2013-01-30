package me.avocardo.guilds.commands;

import me.avocardo.guilds.Guild;
import me.avocardo.guilds.GuildsBasic;
import me.avocardo.guilds.messages.Console;
import me.avocardo.guilds.messages.Message;
import me.avocardo.guilds.messages.MessageType;
import me.avocardo.guilds.utilities.Scheduler;
import me.avocardo.guilds.utilities.Settings;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandBase {

	private GuildsBasic GuildsBasic;
	
	public CommandBase(CommandSender sender, String[] args, GuildsBasic GuildsBasic) {
		
		this.GuildsBasic = GuildsBasic;
		
		if (sender instanceof Player) {
			Player(args, (Player) sender);
		} else {
			Console(args);
		}
		
	}
	
	private void Player(String[] args, Player p) {
		
		if (p.hasPermission("guilds.user.base")) {
			Guild g = GuildsBasic.getPlayerGuild(p);
			if (g != null) {
				if (GuildsBasic.getIntSetting(Settings.SET_BASE_TP_DELAY) == 0) {
					p.teleport(g.getBase());
				} else {
					GuildsBasic.BaseDelay.put(p, new Scheduler(GuildsBasic).base(p));
				}
			} else {
				new Message(MessageType.NOT_IN_GUILD, p, GuildsBasic);
			}
		} else {
			new Message(MessageType.NO_PERMISSION, p, GuildsBasic);
		}
		
	}
	
	private void Console(String[] args) {
		
		new Console(MessageType.CONSOLE_ERROR, GuildsBasic);
		
	}
	
}
