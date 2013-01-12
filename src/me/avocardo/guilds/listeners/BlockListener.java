package me.avocardo.guilds.listeners;

import java.util.Random;

import me.avocardo.guilds.Guild;
import me.avocardo.guilds.GuildsBasic;
import me.avocardo.guilds.utilities.Proficiency;
import me.avocardo.guilds.utilities.ProficiencyType;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListener implements Listener {

	private GuildsBasic plugin;

	public BlockListener(GuildsBasic plugin) {
		
		this.plugin = plugin;
        
    }
	
	public boolean allowBlock(Player p, Location loc, int m) {
		
		if (p.hasPermission("guilds.admin.protectionbarrier")) {
			return true;
		}
		
		if (!plugin.allowGuildProtection) {
			return true;
		}
		
		for (Guild g : plugin.GuildsList) {
			
			Location base = g.getBase();
			
			if (base.distance(loc) > (double) plugin.setGuildProtectionBarrier) {
				// Outside distance... 
			} else {
				if (plugin.getPlayerGuild(p) != g) {
					if (m == 23) plugin.msg(23, p, "", g.getName());
					if (m == 24) plugin.msg(24, p, "", g.getName());
					return false;
				}
			}
			
		}
		
		return true;
		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockPlace(BlockPlaceEvent event) {
		
		if (event.isCancelled()) {
			return;
		}
		
		Location loc = event.getBlockPlaced().getLocation();
		Player p = event.getPlayer();
		
		if (!allowBlock(p, loc, 23)) {
			event.setCancelled(true);
		}
				
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent event) {
		
		if (event.isCancelled()) {
			return;
		}
		
		Location loc = event.getBlock().getLocation();
		Player p = event.getPlayer();
		
		if (!allowBlock(p, loc, 24)) {
			event.setCancelled(true);
		}
				
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockDamage(BlockDamageEvent event) {
		
		if (event.isCancelled()) {
			return;
		}
		
		Location loc = event.getBlock().getLocation();
		Player p = event.getPlayer();
		
		if (!allowBlock(p, loc, 24)) {
			event.setCancelled(true);
			return;
		}
		
		Guild guild = plugin.getPlayerGuild(p);
		World w = p.getWorld();
		Biome b = event.getBlock().getBiome();
		
		if (guild != null) {
			if (guild.getWorlds().contains(w) && guild.getBiomes().contains(b)) {
				Proficiency INSTABREAK = guild.getProficiency(ProficiencyType.INSTABREAK);
				if (INSTABREAK.getActive()) {
					if (INSTABREAK.getUseProficiency(p)) {
						short before = event.getItemInHand().getDurability();
						event.setInstaBreak(true);
						Random random = new Random();
						int i = random.nextInt(2);
			            if (i > 0 && before > 0) {
			            	short value = (short) (before - 1);
			            	event.getItemInHand().setDurability(value);
			            }
					}
				}
			}
		}
		
	}
	
}
