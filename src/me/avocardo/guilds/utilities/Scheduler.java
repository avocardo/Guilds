package me.avocardo.guilds.utilities;

import me.avocardo.guilds.GuildsBasic;

import org.bukkit.entity.Player;

public class Scheduler {

	private GuildsBasic plugin;
	
	public Scheduler(GuildsBasic plugin) {
	
		this.plugin = plugin;
		
	}
	
	public int base(final Player p) {
		int i = plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				p.teleport(plugin.getPlayerGuild(p).getBase());
				plugin.BaseDelay.remove(p);
			}
		}, plugin.setBaseDelay);
		return i;
	}
	
	public int invisible(final Player p) {
		int i = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			public void run() {
				if (!p.isSneaking()) {
					plugin.showPlayer(p);
				}
			}
		}, 0L, 50L);
		return i;
	}
	
	public int sun(final Player p) {
		int i = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			public void run() {
				p.damage((int) plugin.getPlayerGuild(p).getProficiency(ProficiencyType.SUNLIGHT).getPower());
				plugin.msg(17, p, "", "");
			}
		}, 0L, 50L);
		return i;
	}
	
	public int moon(final Player p) {
		int i = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			public void run() {
				p.damage((int) plugin.getPlayerGuild(p).getProficiency(ProficiencyType.MOONLIGHT).getPower());
				plugin.msg(18, p, "", "");
			}
		}, 0L, 50L);
		return i;
	}
	
	public int storm(final Player p) {
		int i = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			public void run() {
				p.damage((int) plugin.getPlayerGuild(p).getProficiency(ProficiencyType.STORM).getPower());
				plugin.msg(19, p, "", "");
			}
		}, 0L, 50L);
		return i;
	}
	
	public int land(final Player p) {
		int i = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			public void run() {
				p.damage((int) plugin.getPlayerGuild(p).getProficiency(ProficiencyType.LANDDAMAGE).getPower());
				plugin.msg(20, p, "", "");
			}
		}, 0L, 50L);
		return i;
	}
	
	public int water(final Player p) {
		int i = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			public void run() {
				p.damage((int) plugin.getPlayerGuild(p).getProficiency(ProficiencyType.WATERDAMAGE).getPower());
				plugin.msg(21, p, "", "");
			}
		}, 0L, 50L);
		return i;
	}

}
