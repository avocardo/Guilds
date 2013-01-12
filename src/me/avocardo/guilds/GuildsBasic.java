package me.avocardo.guilds;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import me.avocardo.guilds.commands.Commands;
import me.avocardo.guilds.listeners.BlockListener;
import me.avocardo.guilds.listeners.ChatListener;
import me.avocardo.guilds.listeners.PlayerListener;
import me.avocardo.guilds.utilities.Attribute;
import me.avocardo.guilds.utilities.AttributeType;
import me.avocardo.guilds.utilities.Proficiency;
import me.avocardo.guilds.utilities.ProficiencyType;
import me.avocardo.guilds.utilities.Scheduler;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class GuildsBasic extends JavaPlugin  {

	private PluginManager PM;
	
	public String chatFormat;
	
	public String v = "0.0.0";
	
	public String[] messages = new String[50];
	
	public boolean allowChangeGuild = true;
	public boolean allowNoGuild = true;
	public boolean allowGuildPVP = true;
	public boolean allowBaseOnDeath = true;
	public boolean allowGuildProtection = true;
	public boolean allowDamageAnimationOnZero = true;
	public boolean allowJoinPermissions = false;
	public boolean allowPickUpRestrictions = true;
	public boolean allowOtherGuildWithinProtection = true;
	public boolean allowChatColor = true;
	public boolean allowGuildPrefix = true;
	public boolean allowChatFormat = true;
	public boolean allowJoinDefaultGuild = false;
	public boolean allowGuildRestrictions = true;
	public boolean allowGuildNameOnBroadcast = false;
	
	public String chatPrefix = "<";
	public String chatSuffix = "> ";
	public String guildsColor = "&b";
	public String defaultGuild = "test";
	
	public int setGuildProtectionBarrier = 50;
	public int setBaseDelay = 0;
	
	private FileConfiguration SettingsConfig = null;
	private File SettingsConfigFile = null;
	private FileConfiguration PlayersConfig = null;
	private File PlayersConfigFile = null;
	private FileConfiguration GuildsConfig = null;
	private File GuildsConfigFile = null;
	private FileConfiguration MessagesConfig = null;
	private File MessagesConfigFile = null;

	public Map <String, Guild> PlayerGuild = new HashMap <String, Guild>();
	public Map <Player, List<ItemStack>> Inventory = new HashMap <Player, List<ItemStack>>();
	public Map <Player, Integer> TasksSun = new HashMap <Player, Integer>();
	public Map <Player, Integer> TasksWater = new HashMap <Player, Integer>();
	public Map <Player, Integer> TasksStorm = new HashMap <Player, Integer>();
	public Map <Player, Integer> TasksLand = new HashMap <Player, Integer>();
	public Map <Player, Integer> TasksMoon = new HashMap <Player, Integer>();
	public Map <Player, Integer> BaseDelay = new HashMap <Player, Integer>();
	public Map <Player, Integer> TasksInvisible = new HashMap <Player, Integer>();
	
	public List <Guild> GuildsList = new ArrayList <Guild>();
	
	public void console(String msg) {
		
		msg = "[Guilds] " + msg;
		msg = msg.replaceAll("&([0-9a-fk-or])", "");
		System.out.println(msg);
		
	}
	
	public void onEnable() {
		
		PM = getServer().getPluginManager();
		
		File SettingsFile = new File(getDataFolder(), "settings.yml");
		File PlayersFile = new File(getDataFolder(), "players.yml");
		File GuildsFile = new File(getDataFolder(), "guilds.yml");
		File MessagesFile = new File(getDataFolder(), "messages.yml");
		
        if (!SettingsFile.exists()) {
            defaultConfig("settings.yml");
        }
        
        if (!PlayersFile.exists()) {
        	defaultConfig("players.yml");
        }
        
        if (!GuildsFile.exists()) {
        	defaultConfig("guilds.yml");
        }
        
        if (!MessagesFile.exists()) {
        	defaultConfig("messages.yml");
        }
        
        loadSettings();
        loadMessages();
        loadGuilds();
        loadPlayers();
        
        saveMessages();
        loadMessages();
        
        PM.registerEvents(new PlayerListener(this), this);
        PM.registerEvents(new BlockListener(this), this);
        PM.registerEvents(new ChatListener(this), this);
        
        getCommand("guilds").setExecutor(new Commands(this));
        getCommand("base").setExecutor(new Commands(this));
        
        v = this.getDescription().getVersion();
        
        clearScheduler();
		
	}
	
	public void onDisable() {
		
		saveGuilds();
		savePlayers();
		saveSettings();
		saveMessages();
		
		clearScheduler();
		
	}
	
	public void defaultConfig(String file) {
		
		if (!new File(getDataFolder(), file).exists()) {
			saveResource(file, false);
		}
		
	}
		
	public Guild getPlayerGuild(Player p) {
		
		if (PlayerGuild.get(p.getName()) != null)
			return PlayerGuild.get(p.getName());
		else
			if (allowJoinDefaultGuild)
				if (getGuild(defaultGuild) != null)
					return getGuild(defaultGuild);
		
		return null;
		
	}
	
	public void setPlayerGuild(Player p, Guild g) {
		
		if (PlayerGuild.containsKey(p.getName())) 
			PlayerGuild.remove(p.getName());
		PlayerGuild.put(p.getName(), g);
		
	}
	
	public Guild getGuild(String g) {
		for (Guild guild : GuildsList)
			if (guild.getName().equalsIgnoreCase(g))
				return guild;
		return null;
	}
	
	public void loadMessages() {
		
		console("Loading messages.yml");
		
		MessagesConfigFile = new File(getDataFolder(), "messages.yml");
		
		MessagesConfig = YamlConfiguration.loadConfiguration(MessagesConfigFile);
			
		InputStream defConfigStream = getResource("messages.yml");
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			MessagesConfig.setDefaults(defConfig);
		}
		
		Set<String> m = MessagesConfig.getConfigurationSection("").getKeys(false);
		
		int mess = 0;
		
		if (m.isEmpty()) {
			// No Messages
		} else {
			for (String str : m) {
				mess = Integer.parseInt(str);
				if (mess <= 50) {
					messages[mess] = MessagesConfig.getString(str);
				}
			}
		}
		
	}
	
	public void loadSettings() {
		
		console("Loading settings.yml");
		
		SettingsConfigFile = new File(getDataFolder(), "settings.yml");
			
		SettingsConfig = YamlConfiguration.loadConfiguration(SettingsConfigFile);
			
		InputStream defConfigStream = getResource("settings.yml");
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			SettingsConfig.setDefaults(defConfig);
		}
		
		Set<String> s = SettingsConfig.getConfigurationSection("").getKeys(false);
		
		if (s.isEmpty()) {
			// No Settings
		} else {
				
			allowChangeGuild = SettingsConfig.getBoolean("allowChangeGuild", true);
			allowNoGuild = SettingsConfig.getBoolean("allowNoGuild", true);
			allowGuildPVP = SettingsConfig.getBoolean("allowGuildPVP", true);
			allowBaseOnDeath = SettingsConfig.getBoolean("allowBaseOnDeath", true);
			allowGuildProtection = SettingsConfig.getBoolean("allowGuildProtection", true);
			allowDamageAnimationOnZero = SettingsConfig.getBoolean("allowDamageAnimationOnZero", true);
			allowChatColor = SettingsConfig.getBoolean("allowChatColor", true);
			allowGuildPrefix = SettingsConfig.getBoolean("allowGuildPrefix", true);
			allowChatFormat = SettingsConfig.getBoolean("allowChatFormat", true);
			allowJoinPermissions = SettingsConfig.getBoolean("allowJoinPermissions", false);
			allowPickUpRestrictions = SettingsConfig.getBoolean("allowPickUpRestrictions", true);
			allowOtherGuildWithinProtection = SettingsConfig.getBoolean("allowOtherGuildWithinProtection", true);
			allowJoinDefaultGuild = SettingsConfig.getBoolean("allowJoinDefaultGuild", true);
			allowGuildRestrictions = SettingsConfig.getBoolean("allowGuildRestrictions", true);
			allowGuildNameOnBroadcast = SettingsConfig.getBoolean("allowGuildNameOnBroadcast", true);

			setGuildProtectionBarrier = SettingsConfig.getInt("setGuildProtectionBarrier", 50);
			setBaseDelay = SettingsConfig.getInt("setBaseDelay", 0);
			
			chatPrefix = SettingsConfig.getString("chatPrefix", "<");
			chatSuffix = SettingsConfig.getString("chatSuffix", "> ");
			guildsColor = SettingsConfig.getString("guildsColor", "&b");
			defaultGuild = SettingsConfig.getString("defaultGuild", "test");
			
		}
		
	}
	
	public void saveMessages() {
		
		console("Saving messages.yml");
		
		MessagesConfigFile = new File(getDataFolder(), "messages.yml");
		MessagesConfig = YamlConfiguration.loadConfiguration(MessagesConfigFile);
			
		InputStream defConfigStream = getResource("messages.yml");
		if (defConfigStream != null) {
		    YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
		    MessagesConfig.setDefaults(defConfig);
		}
		
		try {
			MessagesConfig.options().copyDefaults(true);
			MessagesConfig.save(MessagesConfigFile);
		} catch (IOException ex) {
			this.getLogger().log(Level.SEVERE, "Could not save config to " + MessagesConfigFile, ex);
		}
		
	}
	
	public void saveSettings() {
	
		console("Saving settings.yml");
		
		SettingsConfigFile = new File(getDataFolder(), "settings.yml");
		SettingsConfig = YamlConfiguration.loadConfiguration(SettingsConfigFile);
			
		InputStream defConfigStream = getResource("settings.yml");
		if (defConfigStream != null) {
		    YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
		    SettingsConfig.setDefaults(defConfig);
		}
		
		Set<String> keys = SettingsConfig.getConfigurationSection("").getKeys(false);
			
		for (String str : keys) {
				
			SettingsConfig.set(str, null);

		}
		
		SettingsConfig.set("allowChangeGuild", allowChangeGuild);
		SettingsConfig.set("allowNoGuild", allowNoGuild);
		SettingsConfig.set("allowGuildPVP", allowGuildPVP);
		SettingsConfig.set("allowBaseOnDeath", allowBaseOnDeath);
		SettingsConfig.set("allowGuildProtection", allowGuildProtection);
		SettingsConfig.set("allowDamageAnimationOnZero", allowDamageAnimationOnZero);
		SettingsConfig.set("allowChatColor", allowChatColor);
		SettingsConfig.set("allowGuildPrefix", allowGuildPrefix);
		SettingsConfig.set("allowChatFormat", allowChatFormat);
		SettingsConfig.set("allowJoinPermissions", allowJoinPermissions);
		SettingsConfig.set("allowPickUpRestrictions", allowPickUpRestrictions);
		SettingsConfig.set("allowOtherGuildWithinProtection", allowOtherGuildWithinProtection);
		SettingsConfig.set("allowJoinDefaultGuild", allowJoinDefaultGuild);
		SettingsConfig.set("allowGuildRestrictions", allowGuildRestrictions);
		SettingsConfig.set("allowGuildNameOnBroadcast", allowGuildNameOnBroadcast);
		
		SettingsConfig.set("setGuildProtectionBarrier", setGuildProtectionBarrier);
		SettingsConfig.set("setBaseDelay", setBaseDelay);
				
		SettingsConfig.set("chatPrefix", chatPrefix);
		SettingsConfig.set("chatSuffix", chatSuffix);
		SettingsConfig.set("guildsColor", guildsColor);
		SettingsConfig.set("defaultGuild", defaultGuild);
		
		try {
			SettingsConfig.options().copyDefaults(true);
			SettingsConfig.save(SettingsConfigFile);
		} catch (IOException ex) {
			this.getLogger().log(Level.SEVERE, "Could not save config to " + SettingsConfigFile, ex);
		}
		
	}
	
	public void loadGuilds() {
		
		console("Loading guilds.yml");
		
		GuildsList.clear();
		
		GuildsConfigFile = new File(getDataFolder(), "guilds.yml");
		GuildsConfig = YamlConfiguration.loadConfiguration(GuildsConfigFile);
			
		InputStream defConfigStream = getResource("guilds.yml");
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			GuildsConfig.setDefaults(defConfig);
		}
		
		Set<String> g = GuildsConfig.getConfigurationSection("").getKeys(false);

		if (g.isEmpty()) {
			// No Guilds
		} else {
			for (String str : g) {
				loadGuild(str);
			}
		}
		
	}
	
	public void loadGuild(String str) {
		
		Guild guild = new Guild();
		GuildsList.add(guild);
		
		guild.setName(str);
		guild.setPlayerPrefix(GuildsConfig.getString(str + ".settings.prefix", ""));
		guild.setPlayerSuffix(GuildsConfig.getString(str + ".settings.suffix", ""));
		
		World world = Bukkit.getWorld(GuildsConfig.getString(str + ".base.world", "world"));
		if (world == null) world = Bukkit.getWorlds().get(0);
		
		double x = GuildsConfig.getDouble(str + ".base.x", 0);
		double y = GuildsConfig.getDouble(str + ".base.y", 0);
		double z = GuildsConfig.getDouble(str + ".base.z", 0);
		float yaw = (float) GuildsConfig.getDouble(str + ".base.yaw", 0);
		float pitch = (float) GuildsConfig.getDouble(str + ".base.pitch", 0);
		
		guild.setBase(new Location(world, x, y, z, yaw, pitch));
		
		for (World w : Bukkit.getServer().getWorlds()) {
			if (GuildsConfig.getBoolean(str + ".worlds." + w.getName(), false)) {
				guild.addWorld(w);
			}
		}
		
		for (Biome b : Biome.values()) {
			if (GuildsConfig.getBoolean(str + ".biomes." + b.name(), false)) {
				guild.addBiome(b);
			}
		}
		
		for (ProficiencyType p : ProficiencyType.values()) {
			guild.addProficiency(new Proficiency(p, GuildsConfig.getBoolean(str + ".proficiencies." + p.toString() + ".Active", false), GuildsConfig.getDouble(str + ".proficiencies." + p.toString() + ".Power", 1.0), GuildsConfig.getLong(str + ".proficiencies." + p.toString() + ".CoolDown", (long) 0), GuildsConfig.getInt(str + ".proficiencies." + p.toString() + ".Ticks", 0)));
		}
		
		for (AttributeType a : AttributeType.values()) {
			guild.addAttribute(new Attribute(a, GuildsConfig.getDouble(str + ".attributes." + a.toString(), 1)));
		}
		
		for (Integer i : GuildsConfig.getIntegerList(str + ".restrictions")) {
			guild.addRestriction(i);
		}
		
	}
	
	public void saveGuilds() {

		console("Saving guilds.yml");
		
		GuildsConfigFile = new File(getDataFolder(), "guilds.yml");
		GuildsConfig = YamlConfiguration.loadConfiguration(GuildsConfigFile);
			
		InputStream defConfigStream = getResource("guilds.yml");
		if (defConfigStream != null) {
		    YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
		    GuildsConfig.setDefaults(defConfig);
		}
		
		if (!GuildsList.isEmpty()) {
			
			Set<String> keys = GuildsConfig.getConfigurationSection("").getKeys(false);
			
			for (String str : keys) {
				GuildsConfig.set(str, null);
			}
			
			for (Guild g : GuildsList) {
				
				String name = g.getName();
				
				GuildsConfig.set(name + ".settings.prefix", g.getPlayerPrefix());
				GuildsConfig.set(name + ".settings.suffix", g.getPlayerSuffix());
				
				for (Attribute a : g.getAttributes()) {
					GuildsConfig.set(name + ".attributes." + a.getAttributeType().toString(), a.getPower());
				}
				
				for (Proficiency p : g.getProficiencies()) {
					GuildsConfig.set(name + ".proficiencies." + p.getProficiencyType().toString() + ".Active", p.getActive());
					GuildsConfig.set(name + ".proficiencies." + p.getProficiencyType().toString() + ".Power", p.getPower());
					GuildsConfig.set(name + ".proficiencies." + p.getProficiencyType().toString() + ".CoolDown", p.getCoolDown());
					GuildsConfig.set(name + ".proficiencies." + p.getProficiencyType().toString() + ".Ticks", p.getTicks());
				}

				for (World w : Bukkit.getServer().getWorlds()) {
					if (g.getWorlds().contains(w)) GuildsConfig.set(name + ".worlds." + w.getName(), true);
					else GuildsConfig.set(name + ".worlds." + w.getName(), false);
				}
				
				for (Biome b : Biome.values()) {
					if (g.getBiomes().contains(b)) GuildsConfig.set(name + ".biomes." + b.toString(), true);
					else GuildsConfig.set(name + ".biomes." + b.toString(), false);				
				}
				
				Integer[] saveRestrictions = new Integer[ g.getRestrictions().size() ];
				saveRestrictions = g.getRestrictions().toArray(saveRestrictions);
				GuildsConfig.set(name + ".restrictions", null);
				GuildsConfig.set(name + ".restrictions", Arrays.asList(saveRestrictions));
				
				GuildsConfig.set(name + ".base.world", g.getBase().getWorld().getName());
				GuildsConfig.set(name + ".base.x", g.getBase().getX());
				GuildsConfig.set(name + ".base.y", g.getBase().getY());
				GuildsConfig.set(name + ".base.z", g.getBase().getZ());
				GuildsConfig.set(name + ".base.yaw", g.getBase().getYaw());
				GuildsConfig.set(name + ".base.pitch", g.getBase().getPitch());				
				
			}
			
		}
		
		try {
			GuildsConfig.save(GuildsConfigFile);
		} catch (IOException ex) {
			this.getLogger().log(Level.SEVERE, "Could not save config to " + GuildsConfigFile, ex);
		}
		
	}
	
	public void loadPlayers() {
		
		console("Loading players.yml");
		
		PlayersConfigFile = new File(getDataFolder(), "players.yml");
		PlayersConfig = YamlConfiguration.loadConfiguration(PlayersConfigFile);
			
		InputStream defConfigStream = getResource("players.yml");
		if (defConfigStream != null) {
		    YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
		    PlayersConfig.setDefaults(defConfig);
		}
		
		Set<String> p = PlayersConfig.getConfigurationSection("").getKeys(false);
		
		PlayerGuild.clear();
		
		if (p.isEmpty()) {
			return; // No Players
		} else {
			for (String str : p) {
				Guild gld = null;
				for (Guild g : GuildsList) {
					if (g.getName().equalsIgnoreCase(PlayersConfig.getString(str))) gld = g;
				}
				if (gld == null) {
					PlayerGuild.put(str, null);	
				} else {
					PlayerGuild.put(str, gld);
				}
			}
		}
		
	}
	
	public void savePlayers() {

		console("Saving players.yml");
		
		PlayersConfigFile = new File(getDataFolder(), "players.yml");
		PlayersConfig = YamlConfiguration.loadConfiguration(PlayersConfigFile);
			
		InputStream defConfigStream = getResource("players.yml");
		if (defConfigStream != null) {
		    YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
		    PlayersConfig.setDefaults(defConfig);
		}
		
		Set<String> ply = PlayersConfig.getConfigurationSection("").getKeys(false);
		
		for (String str : ply) {
			PlayersConfig.set(str, null);
		}
		
		if (!PlayerGuild.isEmpty()) {
			String key = null;
			String value = null;
			for (Map.Entry<String, Guild> p : PlayerGuild.entrySet()) {
				key = p.getKey();
				if (p.getValue() == null) {
					value = null;
				} else {
					value = p.getValue().getName();
				}
				PlayersConfig.set(key, value);
			}
		}
		
		try {
			PlayersConfig.save(PlayersConfigFile);
		} catch (IOException ex) {
			this.getLogger().log(Level.SEVERE, "Could not save config to " + PlayersConfigFile, ex);
		}
		
	}
	
	public void remove(String g, Player sender) {
		
		Guild guild = getGuild(g);
		
		if (guild != null) {
			GuildsList.remove(guild);
			msg(10, sender, "", g);
			saveGuilds();
			loadGuilds();
			savePlayers();
			loadPlayers();
		} else {
			msg(2, sender, "", g);
		}
		
	}
	
	public void create(String g, Player sender) {
		
		Guild guild = getGuild(g);
		
		if (guild != null) {
			msg(9, sender, "", g);
		} else {
			Guild gld = new Guild();
			gld.setName(g);
			GuildsList.add(gld);
			gld.setBase(sender.getLocation());
			msg(8, sender, "", g);
			saveGuilds();
			loadGuilds();
		}
		
	}
	
	public void setbase(String g, Player sender) {
		
		Guild guild = getGuild(g);
		
		if (guild != null) {
			guild.setBase(sender.getLocation());
			msg(7, sender, "", g);
			saveGuilds();
			loadGuilds();
		} else {
			msg(2, sender, "", g);
		}
		
	}
	
	public void leave(String p, Player sender) {
		
		Player player = Bukkit.getPlayer(p);
		
		if (player != null) {
			if (PlayerGuild.containsKey(p)) {
				PlayerGuild.remove(p);
				
			}
			if (player.equals(sender)) {
				msg(4, sender, "", "");
			} else {
				msg(5, sender, p, "");
				msg(6, player, "", "");
			}
			savePlayers();
			loadPlayers();
			if (TasksWater.containsKey(p)) {
				Bukkit.getScheduler().cancelTask(TasksWater.get(p));
				TasksWater.remove(p);
			}
			if (TasksLand.containsKey(p)) {
				Bukkit.getScheduler().cancelTask(TasksLand.get(p));
				TasksLand.remove(p);
			}
			if (TasksSun.containsKey(p)) {
				Bukkit.getScheduler().cancelTask(TasksSun.get(p));
				TasksSun.remove(p);
			}
			if (TasksMoon.containsKey(p)) {
				Bukkit.getScheduler().cancelTask(TasksMoon.get(p));
				TasksMoon.remove(p);
			}
			if (TasksStorm.containsKey(p)) {
				Bukkit.getScheduler().cancelTask(TasksStorm.get(p));
				TasksStorm.remove(p);
			}
		} else {
			if (sender != null) msg(3, sender, p, "");
		}
		
	}
	
	public void join(String p, String g, Player sender) {
		
		Player player = Bukkit.getPlayer(p);
		
		Guild guild = getGuild(g);
		
		if (player != null) {
			if (guild != null) {
				if (allowChangeGuild) {
					if (PlayerGuild.containsKey(p)) {
						PlayerGuild.remove(p);
					}
					PlayerGuild.put(p, guild);
					savePlayers();
					loadPlayers();
					if (TasksWater.containsKey(p)) {
						Bukkit.getScheduler().cancelTask(TasksWater.get(p));
						TasksWater.remove(p);
					}
					if (TasksLand.containsKey(p)) {
						Bukkit.getScheduler().cancelTask(TasksLand.get(p));
						TasksLand.remove(p);
					}
					if (TasksSun.containsKey(p)) {
						Bukkit.getScheduler().cancelTask(TasksSun.get(p));
						TasksSun.remove(p);
					}
					if (TasksMoon.containsKey(p)) {
						Bukkit.getScheduler().cancelTask(TasksMoon.get(p));
						TasksMoon.remove(p);
					}
					if (TasksStorm.containsKey(p)) {
						Bukkit.getScheduler().cancelTask(TasksStorm.get(p));
						TasksStorm.remove(p);
					}
					msg(36, player, p, g);
					if (sender != null) msg(37, sender, p, g);
				} else {
					if (getPlayerGuild(player) == null) {
						if (PlayerGuild.containsKey(p)) {
							PlayerGuild.remove(p);
						}
						PlayerGuild.put(p, guild);
						savePlayers();
						loadPlayers();
						if (TasksWater.containsKey(p)) {
							Bukkit.getScheduler().cancelTask(TasksWater.get(p));
							TasksWater.remove(p);
						}
						if (TasksLand.containsKey(p)) {
							Bukkit.getScheduler().cancelTask(TasksLand.get(p));
							TasksLand.remove(p);
						}
						if (TasksSun.containsKey(p)) {
							Bukkit.getScheduler().cancelTask(TasksSun.get(p));
							TasksSun.remove(p);
						}
						if (TasksMoon.containsKey(p)) {
							Bukkit.getScheduler().cancelTask(TasksMoon.get(p));
							TasksMoon.remove(p);
						}
						if (TasksStorm.containsKey(p)) {
							Bukkit.getScheduler().cancelTask(TasksStorm.get(p));
							TasksStorm.remove(p);
						}
						msg(36, player, p, g);
						if (sender != null) msg(37, sender, p, g);
					} else {
						if (sender != null) msg(1, sender, p, "");
					}
				}
			} else {
				if (sender != null) msg(2, sender, "", g);
			}
		} else {
			if (sender != null) msg(3, sender, p, "");
		}
		
	}

	public void msg(int m, Player s, String p, String g) {
		
		String message = messages[m];
		String player = p;
		String guild = g;
		Guild gld = getGuild(g);
		
		if (message == null) {
			message = "&emessage " + m + " missing from messages.yml";
		}
		
		message = message.replaceAll("/p/", player);
		message = message.replaceAll("/g/", guild);
		if (gld != null) {
			if (allowGuildNameOnBroadcast) {
				message = guildsColor + "[" + gld.getName() + "] " + message;
			} else {
				message = guildsColor + "[Guilds] " + message;
			}
		} else {
			message = guildsColor + "[Guilds] " + message;
		}
		message = message.replaceAll("&([0-9a-fk-or])", "\u00A7$1");

		s.sendMessage(message);
		
		return;
		
	}
	
	public void msg2(int m, String p, String g) {
		
		String message = messages[m];
		String player = p;
		String guild = g;
		
		if (message == null) {
			message = "&emessage " + m + " missing from messages.yml";
		}
		
		message = message.replaceAll("/p/", player);
		message = message.replaceAll("/g/", guild);

		console(message);
		
		return;
		
	}

	public void clearScheduler() {
		Bukkit.getScheduler().cancelAllTasks();
	}
		
	public void showPlayer(Player p) {
		for (Player online : Bukkit.getOnlinePlayers()) {
			online.showPlayer(p);
		}
	}
	
	public void hidePlayer(Player p) {
		for (Player online : Bukkit.getOnlinePlayers()) {
			online.hidePlayer(p);
		}
		if (!TasksInvisible.containsKey(p)) {
			TasksInvisible.put(p, new Scheduler(this).invisible(p));
		}
	}
		
	public boolean isBlade(int i) {
		
		if (i == 267) return true;
		if (i == 268) return true;
		if (i == 272) return true;
		if (i == 276) return true;
		if (i == 283) return true;

		return false;
	
	}
	
}
