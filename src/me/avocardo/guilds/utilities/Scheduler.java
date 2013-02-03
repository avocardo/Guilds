package me.avocardo.guilds.utilities;

import me.avocardo.guilds.GuildsBasic;
import me.avocardo.guilds.messages.Message;
import me.avocardo.guilds.messages.MessageType;

import org.bukkit.entity.Player;

public class Scheduler {

	private GuildsBasic GuildsBasic;
	
	public Scheduler(GuildsBasic GuildsBasic) {
	
		this.GuildsBasic = GuildsBasic;
		
	}
	
	public int base(final Player p) {
		int i = GuildsBasic.getServer().getScheduler().scheduleSyncDelayedTask(GuildsBasic, new Runnable() {
			public void run() {
				p.teleport(GuildsBasic.getPlayerGuild(p).getBase());
				GuildsBasic.BaseDelay.remove(p);
			}
		}, GuildsBasic.getIntSetting(Settings.SET_BASE_TP_DELAY));
		return i;
	}
	
	public int sun(final Player p) {
		int i = GuildsBasic.getServer().getScheduler().scheduleSyncRepeatingTask(GuildsBasic, new Runnable() {
			public void run() {
				p.damage((int) GuildsBasic.getPlayerGuild(p).getProficiency(ProficiencyType.SUNLIGHT).getPower());
				new Message(MessageType.SUNLIGHT, p, GuildsBasic);
			}
		}, 0L, 50L);
		return i;
	}
	
	public int altitude(final Player p) {
		int i = GuildsBasic.getServer().getScheduler().scheduleSyncRepeatingTask(GuildsBasic, new Runnable() {
			public void run() {
				p.damage((int) GuildsBasic.getPlayerGuild(p).getProficiency(ProficiencyType.ALTITUDE).getPower());
				new Message(MessageType.ALTITUDE, p, GuildsBasic);
			}
		}, 0L, 50L);
		return i;
	}
	
	public int moon(final Player p) {
		int i = GuildsBasic.getServer().getScheduler().scheduleSyncRepeatingTask(GuildsBasic, new Runnable() {
			public void run() {
				p.damage((int) GuildsBasic.getPlayerGuild(p).getProficiency(ProficiencyType.MOONLIGHT).getPower());
				new Message(MessageType.MOONLIGHT, p, GuildsBasic);
			}
		}, 0L, 50L);
		return i;
	}
	
	public int storm(final Player p) {
		int i = GuildsBasic.getServer().getScheduler().scheduleSyncRepeatingTask(GuildsBasic, new Runnable() {
			public void run() {
				p.damage((int) GuildsBasic.getPlayerGuild(p).getProficiency(ProficiencyType.STORM).getPower());
				new Message(MessageType.RAIN, p, GuildsBasic);
			}
		}, 0L, 50L);
		return i;
	}
	
	public int land(final Player p) {
		int i = GuildsBasic.getServer().getScheduler().scheduleSyncRepeatingTask(GuildsBasic, new Runnable() {
			public void run() {
				p.damage((int) GuildsBasic.getPlayerGuild(p).getProficiency(ProficiencyType.LANDDAMAGE).getPower());
				new Message(MessageType.LAND, p, GuildsBasic);
			}
		}, 0L, 50L);
		return i;
	}
	
	public int water(final Player p) {
		int i = GuildsBasic.getServer().getScheduler().scheduleSyncRepeatingTask(GuildsBasic, new Runnable() {
			public void run() {
				p.damage((int) GuildsBasic.getPlayerGuild(p).getProficiency(ProficiencyType.WATERDAMAGE).getPower());
				new Message(MessageType.WATER, p, GuildsBasic);
			}
		}, 0L, 50L);
		return i;
	}

}
