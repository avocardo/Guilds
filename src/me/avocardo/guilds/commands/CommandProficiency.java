package me.avocardo.guilds.commands;

import me.avocardo.guilds.Guild;
import me.avocardo.guilds.GuildsBasic;
import me.avocardo.guilds.messages.Console;
import me.avocardo.guilds.messages.Message;
import me.avocardo.guilds.messages.MessageType;
import me.avocardo.guilds.utilities.Proficiency;
import me.avocardo.guilds.utilities.ProficiencyType;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandProficiency {

	private GuildsBasic GuildsBasic;
	
	public CommandProficiency(CommandSender sender, String[] args, GuildsBasic GuildsBasic) {
		
		this.GuildsBasic = GuildsBasic;
		
		if (sender instanceof Player) {
			Player(args, (Player) sender);
		} else {
			Console(args);
		}
		
	}
	
	private void Player(String[] args, Player player) {
		
		if (!player.hasPermission("guilds.admin.proficiency")) {
			new Message(MessageType.NO_PERMISSION, player, GuildsBasic);
			return;
		}
		
		if (!(args.length > 4)) {
			new Message(MessageType.COMMAND_PROFICIENCY, player, GuildsBasic);
			return;
		}
		
		Guild g = GuildsBasic.getGuild(args[1]);
		
		if (g == null) {
			new Message(MessageType.GUILD_NOT_RECOGNISED, player, args[1], GuildsBasic);
			return;
		}
		
		ProficiencyType p = null;
		
		for (Proficiency pt : g.getProficiencies()) {
			if (pt.getProficiencyType().toString().equalsIgnoreCase(args[2])) {
				p = pt.getProficiencyType();
			}
		}
		
		if (p == null) {
			new Message(MessageType.PROFICIENCY_NOT_RECOGNISED, player, args[2], GuildsBasic);
			return;
		}

		String[] Input = {"Active", "Power", "CoolDown", "Ticks", "Minimum", "Maximum"};
		
		String i = null;
		
		for (String s : Input) {
			if (s.equalsIgnoreCase(args[3])) {
				i = s;
			}
		}
		
		if (i == null) {
			new Message(MessageType.INPUT_NOT_RECOGNISED, player, args[3], GuildsBasic);
			return;
		}
		
		String v = args[4];
		
		if (i.equalsIgnoreCase("Active")) {
			if (isBoolean(v)) {
				boolean value = Boolean.parseBoolean(v);
				g.getProficiency(p).setActive(value);
				new Message(MessageType.PROFICIENCY_INPUT_SET, player, p, i, args[4], g, GuildsBasic);
				return;
			} else {
				new Message(MessageType.NOT_BOOLEAN, player, GuildsBasic);
				return;
			}
		}
		
		if (i.equalsIgnoreCase("Power")) {
			if (isDouble(v)) {
				if (!p.hasPower()) {
					new Message(MessageType.WRONG_INPUT, player, p, i, "", g, GuildsBasic);
					return;
				}
				double value = Double.parseDouble(v);
				g.getProficiency(p).setPower(value);
				new Message(MessageType.PROFICIENCY_INPUT_SET, player, p, i, args[4], g, GuildsBasic);
				return;
			} else {
				new Message(MessageType.NOT_DOUBLE, player, GuildsBasic);
				return;
			}
		}
		
		if (i.equalsIgnoreCase("CoolDown")) {
			if (isLong(v)) {
				if (!p.hasCoolDown()) {
					new Message(MessageType.WRONG_INPUT, player, p, i, "", g, GuildsBasic);
					return;
				}
				long value = Long.parseLong(v);
				g.getProficiency(p).setCoolDown(value);
				new Message(MessageType.PROFICIENCY_INPUT_SET, player, p, i, args[4], g, GuildsBasic);
				return;
			} else {
				new Message(MessageType.NOT_LONG, player, GuildsBasic);
				return;
			}
		}
		
		if (i.equalsIgnoreCase("Ticks")) {
			if (isInteger(v)) {
				if (!p.hasTicks()) {
					new Message(MessageType.WRONG_INPUT, player, p, i, "", g, GuildsBasic);
					return;
				}
				int value = Integer.parseInt(v);
				g.getProficiency(p).setTicks(value);
				new Message(MessageType.PROFICIENCY_INPUT_SET, player, p, i, args[4], g, GuildsBasic);
				return;
			} else {
				new Message(MessageType.NOT_INT, player, GuildsBasic);
				return;
			}
		}
		
		if (i.equalsIgnoreCase("Minimum")) {
			if (isInteger(v)) {
				if (!p.hasMinMax()) {
					new Message(MessageType.WRONG_INPUT, player, p, i, "", g, GuildsBasic);
					return;
				}
				int value = Integer.parseInt(v);
				g.getProficiency(p).setMinimum(value);
				new Message(MessageType.PROFICIENCY_INPUT_SET, player, p, i, args[4], g, GuildsBasic);
				return;
			} else {
				new Message(MessageType.NOT_INT, player, GuildsBasic);
				return;
			}
		}

		if (i.equalsIgnoreCase("Maximum")) {
			if (isInteger(v)) {
				if (!p.hasMinMax()) {
					new Message(MessageType.WRONG_INPUT, player, p, i, "", g, GuildsBasic);
					return;
				}
				int value = Integer.parseInt(v);
				g.getProficiency(p).setMaximum(value);
				new Message(MessageType.PROFICIENCY_INPUT_SET, player, p, i, args[4], g, GuildsBasic);
				return;
			} else {
				new Message(MessageType.NOT_INT, player, GuildsBasic);
				return;
			}
		}
		
	}
	
	private void Console(String[] args) {
		
		if (!(args.length > 4)) {
			new Console(MessageType.COMMAND_PROFICIENCY, GuildsBasic);
			return;
		}
		
		Guild g = GuildsBasic.getGuild(args[1]);
		
		if (g == null) {
			new Console(MessageType.GUILD_NOT_RECOGNISED, args[1], GuildsBasic);
			return;
		}
		
		ProficiencyType p = null;
		
		for (Proficiency pt : g.getProficiencies()) {
			if (pt.getProficiencyType().toString().equalsIgnoreCase(args[2])) {
				p = pt.getProficiencyType();
			}
		}
		
		if (p == null) {
			new Console(MessageType.PROFICIENCY_NOT_RECOGNISED, args[2], GuildsBasic);
			return;
		}

		String[] Input = {"Active", "Power", "CoolDown", "Ticks", "Minimum", "Maximum"};
		
		String i = null;
		
		for (String s : Input) {
			if (s.equalsIgnoreCase(args[3])) {
				i = s;
			}
		}
		
		if (i == null) {
			new Console(MessageType.INPUT_NOT_RECOGNISED, args[3], GuildsBasic);
			return;
		}
		
		String v = args[4];
		
		if (i.equalsIgnoreCase("Active")) {
			if (isBoolean(v)) {
				boolean value = Boolean.parseBoolean(v);
				g.getProficiency(p).setActive(value);
				new Console(MessageType.PROFICIENCY_INPUT_SET, p, i, args[4], g, GuildsBasic);
				return;
			} else {
				new Console(MessageType.NOT_BOOLEAN, GuildsBasic);
				return;
			}
		}
		
		if (i.equalsIgnoreCase("Power")) {
			if (isDouble(v)) {
				if (!p.hasPower()) {
					new Console(MessageType.WRONG_INPUT, p, i, "", g, GuildsBasic);
					return;
				}
				double value = Double.parseDouble(v);
				g.getProficiency(p).setPower(value);
				new Console(MessageType.PROFICIENCY_INPUT_SET, p, i, args[4], g, GuildsBasic);
				return;
			} else {
				new Console(MessageType.NOT_DOUBLE, GuildsBasic);
				return;
			}
		}
		
		if (i.equalsIgnoreCase("CoolDown")) {
			if (isLong(v)) {
				if (!p.hasCoolDown()) {
					new Console(MessageType.WRONG_INPUT, p, i, "", g, GuildsBasic);
					return;
				}
				long value = Long.parseLong(v);
				g.getProficiency(p).setCoolDown(value);
				new Console(MessageType.PROFICIENCY_INPUT_SET, p, i, args[4], g, GuildsBasic);
				return;
			} else {
				new Console(MessageType.NOT_LONG, GuildsBasic);
				return;
			}
		}
		
		if (i.equalsIgnoreCase("Ticks")) {
			if (isInteger(v)) {
				if (!p.hasTicks()) {
					new Console(MessageType.WRONG_INPUT, p, i, "", g, GuildsBasic);
					return;
				}
				int value = Integer.parseInt(v);
				g.getProficiency(p).setTicks(value);
				new Console(MessageType.PROFICIENCY_INPUT_SET, p, i, args[4], g, GuildsBasic);
				return;
			} else {
				new Console(MessageType.NOT_INT, GuildsBasic);
				return;
			}
		}
		
		if (i.equalsIgnoreCase("Minimum")) {
			if (isInteger(v)) {
				if (!p.hasMinMax()) {
					new Console(MessageType.WRONG_INPUT, p, i, "", g, GuildsBasic);
					return;
				}
				int value = Integer.parseInt(v);
				g.getProficiency(p).setMinimum(value);
				new Console(MessageType.PROFICIENCY_INPUT_SET, p, i, args[4], g, GuildsBasic);
				return;
			} else {
				new Console(MessageType.NOT_INT, GuildsBasic);
				return;
			}
		}

		if (i.equalsIgnoreCase("Maximum")) {
			if (isInteger(v)) {
				if (!p.hasMinMax()) {
					new Console(MessageType.WRONG_INPUT, p, i, "", g, GuildsBasic);
					return;
				}
				int value = Integer.parseInt(v);
				g.getProficiency(p).setMaximum(value);
				new Console(MessageType.PROFICIENCY_INPUT_SET, p, i, args[4], g, GuildsBasic);
				return;
			} else {
				new Console(MessageType.NOT_INT, GuildsBasic);
				return;
			}
		}
		
	}
	
	private boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    return true;
	}
	
	private boolean isDouble(String s) {
	    try { 
	    	Double.parseDouble(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    return true;
	}
	
	private boolean isLong(String s) {
	    try { 
	    	Long.parseLong(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    return true;
	}
	
	private boolean isBoolean(String s) {
		if (s.equalsIgnoreCase("true")) return true;
		if (s.equalsIgnoreCase("false")) return true;
	    return false;
	}
	
}
