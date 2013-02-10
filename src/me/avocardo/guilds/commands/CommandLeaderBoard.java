package me.avocardo.guilds.commands;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import me.avocardo.guilds.Guild;
import me.avocardo.guilds.GuildsBasic;
import me.avocardo.guilds.messages.Message;
import me.avocardo.guilds.messages.Console;
import me.avocardo.guilds.messages.MessageType;
import me.avocardo.guilds.utilities.ValueComparator;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandLeaderBoard {

	private GuildsBasic GuildsBasic;
	
	public CommandLeaderBoard(CommandSender sender, String[] args, GuildsBasic GuildsBasic) {
		
		this.GuildsBasic = GuildsBasic;
		
		if (sender instanceof Player) {
			Player(args, (Player) sender);
		} else {
			Console(args);
		}
		
	}
	
	private void Player(String[] args, Player player) {
		
		if (!player.hasPermission("guilds.user.leaderboard")) {
			new Message(MessageType.NO_PERMISSION, player, GuildsBasic);
			return;
		}
		
		HashMap<Guild, Double> map = new HashMap<Guild, Double>();
        ValueComparator bvc =  new ValueComparator(map);
        TreeMap<Guild, Double> sorted_map = new TreeMap<Guild, Double> (bvc);

        for (Guild g : GuildsBasic.GuildsList) {
			map.put(g, g.getRATIO());
		}
        
        sorted_map.putAll(map);
        
        new Message(MessageType.LEADERBOARD, player, "Guild", "Kills", "Deaths", "Ratio", "Exp", GuildsBasic);
        
        DecimalFormat df = new DecimalFormat("00.00");
        
        for (Map.Entry<Guild, Double> ratio : sorted_map.entrySet()) {
        	double value = 0;
        	if (ratio.getValue() > 99.99) value = (double) 99.99;
        	else value = ratio.getValue();
        	String v = df.format(value);
        	new Message(MessageType.LEADERBOARD, player, ratio.getKey(), "" + ratio.getKey().getKILLS(), "" + ratio.getKey().getDEATHS(), v, "" + ratio.getKey().getEXP(), GuildsBasic);
        }
		
	}
	
	private void Console(String[] args) {
		
		HashMap<Guild, Double> map = new HashMap<Guild, Double>();
        ValueComparator bvc =  new ValueComparator(map);
        TreeMap<Guild, Double> sorted_map = new TreeMap<Guild, Double> (bvc);

        for (Guild g : GuildsBasic.GuildsList) {
			map.put(g, g.getRATIO());
		}
        
        sorted_map.putAll(map);
        
        new Console(MessageType.LEADERBOARD, "Guild", "Kills", "Deaths", "Ratio", "Exp", GuildsBasic);
        
        DecimalFormat df = new DecimalFormat("00.00");
        
        for (Map.Entry<Guild, Double> ratio : sorted_map.entrySet()) {
        	double value = 0;
        	if (ratio.getValue() > 99.99) value = (double) 99.99;
        	else value = ratio.getValue();
        	String v = df.format(value);
        	new Console(MessageType.LEADERBOARD, ratio.getKey(), "" + ratio.getKey().getKILLS(), "" + ratio.getKey().getDEATHS(), v, "" + ratio.getKey().getEXP(), GuildsBasic);
        }
        
	}
	
}
