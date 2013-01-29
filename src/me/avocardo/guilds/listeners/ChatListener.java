package me.avocardo.guilds.listeners;

import me.avocardo.guilds.Guild;
import me.avocardo.guilds.GuildsBasic;
import me.avocardo.guilds.utilities.Settings;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

	private GuildsBasic GuildsBasic;
	
	public ChatListener(GuildsBasic GuildsBasic) {
		
		this.GuildsBasic = GuildsBasic;
        
    }
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerChat(final AsyncPlayerChatEvent event) {
		
		String format = event.getFormat();
		
		if (GuildsBasic.getEnabled(Settings.ENABLE_PLAYER_GUILD_PREFIX)) {
			Player player = event.getPlayer();
			Guild guild = GuildsBasic.getPlayerGuild(player);
			if (guild != null) {
				format = format.replace("%1$s", guild.getPlayerPrefix() + "%1$s");
				format = format.replace("%1$s", "%1$s" + guild.getPlayerSuffix());
			}
		}
		
		if (GuildsBasic.getEnabled(Settings.ENABLE_GUILD_CHAT_FORMAT)) {
			format = format.replace("<", GuildsBasic.getSetting(Settings.SET_CHAT_PREFIX));
			format = format.replace(">", GuildsBasic.getSetting(Settings.SET_CHAT_SUFFIX));
		}
		
		if (GuildsBasic.getEnabled(Settings.ENABLE_CHAT_COLOR)) {
			format = format.replaceAll("&([0-9a-fk-or])", "\u00A7$1");
			event.setMessage(event.getMessage().replaceAll("&([0-9a-fk-or])", "\u00A7$1"));
		}
		
		event.setFormat(format);
		
	}
	
}
