package me.avocardo.guilds.listeners;

import me.avocardo.guilds.Guild;
import me.avocardo.guilds.GuildsBasic;
import me.avocardo.guilds.User;
import me.avocardo.guilds.utilities.Proficiency;
import me.avocardo.guilds.utilities.ProficiencyType;
import me.avocardo.guilds.utilities.Settings;

import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.kitteh.tag.PlayerReceiveNameTagEvent;

public class TagListener implements Listener {
	
	private GuildsBasic GuildsBasic;

	public TagListener(GuildsBasic GuildsBasic) {
		this.GuildsBasic = GuildsBasic;
    }

	@EventHandler(priority = EventPriority.NORMAL)
	public void onNameTag(PlayerReceiveNameTagEvent event) {
		
		Player p = event.getNamedPlayer();
		Guild g = GuildsBasic.getPlayerGuild(p);
		User u = GuildsBasic.getPlayerUser(p);
		
		if (g != null) {
			if (GuildsBasic.getEnabled(Settings.ENABLE_PLAYER_LISTNAME_GUILD)) {
				event.setTag(g.getName());
			}
			if (GuildsBasic.getEnabled(Settings.ENABLE_PLAYER_LISTNAME_COLOR)) {
				event.setTag((g.getColor() + event.getTag()).replaceAll("&([0-9a-fk-or])", "\u00A7$1"));
			}
			
			if (p.isSneaking()) {
				World w = p.getWorld();
				Biome b = p.getLocation().getBlock().getBiome();
				if (g.getWorlds().contains(w) && g.getBiomes().contains(b)) {
					Proficiency INVISIBLE = g.getProficiency(ProficiencyType.INVISIBLE);
					if (INVISIBLE.getActive()) {
						if (INVISIBLE.getUseProficiency(u)) {
							GuildsBasic.hidePlayer(p);
						}
					}
				}
			}
			
		}
		
	}
	
}
