package me.avocardo.guilds.listeners;

import me.avocardo.guilds.Guild;
import me.avocardo.guilds.GuildsBasic;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

	private GuildsBasic plugin;
	
	public ChatListener(GuildsBasic plugin) {
		
		this.plugin = plugin;
        
    }
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerChat(final AsyncPlayerChatEvent event) {
		
		String format = event.getFormat();
		
		plugin.console(event.getFormat());
		
		if (plugin.allowGuildPrefix == true) {
			Player player = event.getPlayer();
			Guild guild = plugin.getPlayerGuild(player);
			if (guild != null) {
				format = format.replace("%1$s", guild.getPlayerPrefix() + "%1$s");
				format = format.replace("%1$s", "%1$s" + guild.getPlayerSuffix());
			}
		}
		
		if (plugin.allowChatFormat == true) {
			format = format.replace("<", plugin.chatPrefix);
			format = format.replace(">", plugin.chatSuffix);
		}
		
		if (plugin.allowChatColor == true) {
			format = format.replaceAll("&([0-9a-fk-or])", "\u00A7$1");
			event.setMessage(event.getMessage().replaceAll("&([0-9a-fk-or])", "\u00A7$1"));
		}
		
		event.setFormat(format);
		
	}
	
}
