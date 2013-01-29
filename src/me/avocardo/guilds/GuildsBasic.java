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
import me.avocardo.guilds.listeners.TagListener;
import me.avocardo.guilds.messages.Message;
import me.avocardo.guilds.messages.MessageType;
import me.avocardo.guilds.utilities.Proficiency;
import me.avocardo.guilds.utilities.ProficiencyType;
import me.avocardo.guilds.utilities.Scheduler;
import me.avocardo.guilds.utilities.Settings;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class GuildsBasic extends JavaPlugin  {

	private PluginManager PM;
	
	public String chatFormat;
	
	public String v = "0.0.0";
		
	private FileConfiguration SettingsConfig = null;
	private File SettingsConfigFile = null;
	private FileConfiguration PlayersConfig = null;
	private File PlayersConfigFile = null;
	private FileConfiguration GuildsConfig = null;
	private File GuildsConfigFile = null;
	private FileConfiguration MessagesConfig = null;
	private File MessagesConfigFile = null;

	public Map <String, Guild> PlayerGuild = new HashMap <String, Guild>();
	public Map <Player, User> PlayerUser = new HashMap <Player, User>();
	
	public Map <Player, Integer> TasksSun = new HashMap <Player, Integer>();
	public Map <Player, Integer> TasksWater = new HashMap <Player, Integer>();
	public Map <Player, Integer> TasksStorm = new HashMap <Player, Integer>();
	public Map <Player, Integer> TasksLand = new HashMap <Player, Integer>();
	public Map <Player, Integer> TasksMoon = new HashMap <Player, Integer>();
	public Map <Player, Integer> BaseDelay = new HashMap <Player, Integer>();
	public Map <Player, Integer> TasksInvisible = new HashMap <Player, Integer>();
	public Map <Player, Integer> TasksAltitude = new HashMap <Player, Integer>();
	
	public List <Guild> GuildsList = new ArrayList <Guild>();
		
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
               
        PM.registerEvents(new PlayerListener(this), this);
        PM.registerEvents(new BlockListener(this), this);
        PM.registerEvents(new ChatListener(this), this);
        
        for (Plugin p : PM.getPlugins()) {
        	 if (p.getName().equalsIgnoreCase("TagAPI")) {
             	PM.registerEvents(new TagListener(this), this);
             	sendConsole("TagAPI listener activated...");
             }
        }
        
        getCommand("guilds").setExecutor(new Commands(this));
        getCommand("base").setExecutor(new Commands(this));
        
        v = this.getDescription().getVersion();
        
        clearScheduler();
        
        PlayerUser.clear();

        for (Player p : Bukkit.getOnlinePlayers()) {
    		PlayerUser.put(p, new User(p));
    		Guild g = getPlayerGuild(p);
    		World w = p.getWorld();
    		Biome b = p.getLocation().getBlock().getBiome();
    		if (g != null) {
    			if (g.getWorlds().contains(w) && g.getBiomes().contains(b)) {
    				Proficiency MAX_HEALTH = g.getProficiency(ProficiencyType.MAX_HEALTH);
    				if (MAX_HEALTH.getActive()) {
    					if (MAX_HEALTH.getPower() < 20 && MAX_HEALTH.getPower() > 0) {
							if (p.getHealth() > (int) MAX_HEALTH.getPower()) {
								p.setHealth((int) MAX_HEALTH.getPower());
							}
    					}
    				}
    			}
    		}
        }
		
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
	
	public User getPlayerUser(Player p) {		
		return PlayerUser.get(p);
	}
		
	public Guild getPlayerGuild(Player p) {
		
		if (PlayerGuild.get(p.getName()) != null)
			return PlayerGuild.get(p.getName());
		else
			if (getEnabled(Settings.ENABLE_DEFAULT_GUILD))
				if (getGuild(getSetting(Settings.SET_DEFAULT_GUILD)) != null)
					return getGuild(getSetting(Settings.SET_DEFAULT_GUILD));
		
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
		
		sendConsole("Loading messages.yml");
		
		MessagesConfigFile = new File(getDataFolder(), "messages.yml");
		
		MessagesConfig = YamlConfiguration.loadConfiguration(MessagesConfigFile);
			
		InputStream defConfigStream = getResource("messages.yml");
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			MessagesConfig.setDefaults(defConfig);
		}
		
		for (MessageType m : MessageType.values()) {
			if (SettingsConfig.isSet(m.toString())) {
				m.setMessage(SettingsConfig.getString(m.toString()));
			}
			
		}
		
	}
	
	public void loadSettings() {
		
		sendConsole("Loading settings.yml");
		
		SettingsConfigFile = new File(getDataFolder(), "settings.yml");
			
		SettingsConfig = YamlConfiguration.loadConfiguration(SettingsConfigFile);
			
		InputStream defConfigStream = getResource("settings.yml");
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			SettingsConfig.setDefaults(defConfig);
		}
		
		for (Settings s : Settings.values()) {
			if (SettingsConfig.isSet(s.toString())) {
				if (s.isBoolean())
					s.setSetting(SettingsConfig.getBoolean(s.toString()));
				if (s.isInteger())
					s.setSetting(SettingsConfig.getInt(s.toString()));
				if (s.isString())
					s.setSetting(SettingsConfig.getString(s.toString()));
				if (s.isLong())
					s.setSetting(SettingsConfig.getLong(s.toString()));
			}
		}
		
	}
	
	public void saveMessages() {
		
		sendConsole("Saving messages.yml");
		
		MessagesConfigFile = new File(getDataFolder(), "messages.yml");
		MessagesConfig = YamlConfiguration.loadConfiguration(MessagesConfigFile);
			
		InputStream defConfigStream = getResource("messages.yml");
		if (defConfigStream != null) {
		    YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
		    MessagesConfig.setDefaults(defConfig);
		}
		
		Set<String> keys = MessagesConfig.getConfigurationSection("").getKeys(false);
		
		for (String str : keys) {
			MessagesConfig.set(str, null);
		}
		
		for (MessageType m : MessageType.values()) {
			MessagesConfig.set(m.toString(), m.getMessage());
		}
		
		try {
			MessagesConfig.options().copyDefaults(true);
			MessagesConfig.save(MessagesConfigFile);
		} catch (IOException ex) {
			this.getLogger().log(Level.SEVERE, "Could not save config to " + MessagesConfigFile, ex);
		}
		
	}
	
	public void saveSettings() {
	
		sendConsole("Saving settings.yml");
		
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
		
		for (Settings s : Settings.values()) {
			SettingsConfig.set(s.toString(), s.getSetting());
		}
		
		try {
			SettingsConfig.options().copyDefaults(true);
			SettingsConfig.save(SettingsConfigFile);
		} catch (IOException ex) {
			this.getLogger().log(Level.SEVERE, "Could not save config to " + SettingsConfigFile, ex);
		}
		
	}
	
	public void loadGuilds() {
		
		sendConsole("Loading guilds.yml");
		
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
		
		String path = "";
		
		Guild guild = new Guild();
		GuildsList.add(guild);
		
		guild.setName(str);
		
		path = str + ".Settings";
	
		if (GuildsConfig.isSet(path)) {
			guild.setColor(GuildsConfig.getString(str + ".Settings.Color", ""));
			guild.setPlayerPrefix(GuildsConfig.getString(str + ".Settings.Prefix", ""));
			guild.setPlayerSuffix(GuildsConfig.getString(str + ".Settings.Suffix", ""));
		} else {
			guild.setColor(GuildsConfig.getString(str + ".settings.color", ""));
			guild.setPlayerPrefix(GuildsConfig.getString(str + ".settings.prefix", ""));
			guild.setPlayerSuffix(GuildsConfig.getString(str + ".settings.suffix", ""));
		}
		
		path = str + ".Base";
		
		if (GuildsConfig.isSet(path)) {
			World world = Bukkit.getWorld(GuildsConfig.getString(str + ".Base.World", "world"));
			if (world == null) world = Bukkit.getWorlds().get(0);
			double x = GuildsConfig.getDouble(str + ".Base.X", 0);
			double y = GuildsConfig.getDouble(str + ".Base.Y", 0);
			double z = GuildsConfig.getDouble(str + ".Base.Z", 0);
			float yaw = (float) GuildsConfig.getDouble(str + ".Base.Yaw", 0);
			float pitch = (float) GuildsConfig.getDouble(str + ".Base.Pitch", 0);
			guild.setBase(new Location(world, x, y, z, yaw, pitch));
		} else {
			World world = Bukkit.getWorld(GuildsConfig.getString(str + ".base.world", "world"));
			if (world == null) world = Bukkit.getWorlds().get(0);
			double x = GuildsConfig.getDouble(str + ".base.x", 0);
			double y = GuildsConfig.getDouble(str + ".base.y", 0);
			double z = GuildsConfig.getDouble(str + ".base.z", 0);
			float yaw = (float) GuildsConfig.getDouble(str + ".base.yaw", 0);
			float pitch = (float) GuildsConfig.getDouble(str + ".base.pitch", 0);
			guild.setBase(new Location(world, x, y, z, yaw, pitch));
		}
		
		path = str + ".Worlds";
		
		if (GuildsConfig.isSet(path)) {
			for (World w : Bukkit.getServer().getWorlds()) {
				if (GuildsConfig.getBoolean(str + ".Worlds." + w.getName(), false)) {
					guild.addWorld(w);
				}
			}
		} else {
			for (World w : Bukkit.getServer().getWorlds()) {
				if (GuildsConfig.getBoolean(str + ".worlds." + w.getName(), false)) {
					guild.addWorld(w);
				}
			}
		}
		
		path = str + ".Biomes";
		
		if (GuildsConfig.isSet(path)) {
			for (Biome b : Biome.values()) {
				if (GuildsConfig.getBoolean(str + ".Biomes." + b.name(), false)) {
					guild.addBiome(b);
				}
			}
		} else {
			for (Biome b : Biome.values()) {
				if (GuildsConfig.getBoolean(str + ".biomes." + b.name(), false)) {
					guild.addBiome(b);
				}
			}
		}
		
		path = str + ".Proficiencies";
		
		if (GuildsConfig.isSet(path)) {
			path = str + ".Proficiencies.";
			for (ProficiencyType p : ProficiencyType.values()) {		
				guild.addProficiency(new Proficiency(p, GuildsConfig.getBoolean(path + p.toString() + ".Active", false), GuildsConfig.getDouble(path + p.toString() + ".Power", 1), GuildsConfig.getLong(path + p.toString() + ".CoolDown", 0), GuildsConfig.getInt(path + p.toString() + ".Ticks", 0), GuildsConfig.getInt(path + p.toString() + ".Minimum", 0), GuildsConfig.getInt(path + p.toString() + ".Maximum", 0), GuildsConfig.getInt(path + p.toString() + ".Item", 0), this));
			}
		} else {
			for (ProficiencyType p : ProficiencyType.values()) {
				if (p.wasAttribute()) {
					path = str + ".attributes.";
					guild.addProficiency(new Proficiency(p, GuildsConfig.getBoolean(path + p.toString() + ".Active", false), GuildsConfig.getDouble(path + p.toString(), 1), GuildsConfig.getLong(path + p.toString() + ".CoolDown", 0), GuildsConfig.getInt(path + p.toString() + ".Ticks", 0), GuildsConfig.getInt(path + p.toString() + ".Minimum", 0), GuildsConfig.getInt(path + p.toString() + ".Maximum", 0), GuildsConfig.getInt(path + p.toString() + ".Item", 0), this));
				} else {
					path = str + ".proficiencies.";
					guild.addProficiency(new Proficiency(p, GuildsConfig.getBoolean(path + p.toString() + ".Active", false), GuildsConfig.getDouble(path + p.toString() + ".Power", 1), GuildsConfig.getLong(path + p.toString() + ".CoolDown", 0), GuildsConfig.getInt(path + p.toString() + ".Ticks", 0), GuildsConfig.getInt(path + p.toString() + ".Minimum", 0), GuildsConfig.getInt(path + p.toString() + ".Maximum", 0), GuildsConfig.getInt(path + p.toString() + ".Item", 0), this));
				}
			}
		}
		
		path = str + ".Restrictions";
		
		if (GuildsConfig.isSet(path)) {
			for (Integer i : GuildsConfig.getIntegerList(str + ".Restrictions")) {
				guild.addRestriction(i);
			}
		} else {
			for (Integer i : GuildsConfig.getIntegerList(str + ".restrictions")) {
				guild.addRestriction(i);
			}
		}
		
	}
	
	public void saveGuilds() {

		sendConsole("Saving guilds.yml");
		
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
				
				GuildsConfig.set(name + ".Settings.Color", g.getColor());
				GuildsConfig.set(name + ".Settings.Prefix", g.getPlayerPrefix());
				GuildsConfig.set(name + ".Settings.Suffix", g.getPlayerSuffix());
								
				for (Proficiency p : g.getProficiencies()) {
					GuildsConfig.set(name + ".Proficiencies." + p.getProficiencyType().toString() + ".Active", p.getActive());
					if (p.getProficiencyType().hasPower()) {
						GuildsConfig.set(name + ".Proficiencies." + p.getProficiencyType().toString() + ".Power", p.getPower());
					}
					if (p.getProficiencyType().hasCoolDown()) {
						GuildsConfig.set(name + ".Proficiencies." + p.getProficiencyType().toString() + ".CoolDown", p.getCoolDown());
					}
					if (p.getProficiencyType().hasTicks()) {
						GuildsConfig.set(name + ".Proficiencies." + p.getProficiencyType().toString() + ".Ticks", p.getTicks());
					}
					if (p.getProficiencyType().hasMinMax()) {
						GuildsConfig.set(name + ".Proficiencies." + p.getProficiencyType().toString() + ".Minimum", p.getMinimum());
						GuildsConfig.set(name + ".Proficiencies." + p.getProficiencyType().toString() + ".Maximum", p.getMaximum());
					}
				}

				for (World w : Bukkit.getServer().getWorlds()) {
					if (g.getWorlds().contains(w)) GuildsConfig.set(name + ".Worlds." + w.getName(), true);
					else GuildsConfig.set(name + ".Worlds." + w.getName(), false);
				}
				
				for (Biome b : Biome.values()) {
					if (g.getBiomes().contains(b)) GuildsConfig.set(name + ".Biomes." + b.toString(), true);
					else GuildsConfig.set(name + ".Biomes." + b.toString(), false);				
				}
				
				Integer[] saveRestrictions = new Integer[ g.getRestrictions().size() ];
				saveRestrictions = g.getRestrictions().toArray(saveRestrictions);
				GuildsConfig.set(name + ".Restrictions", null);
				GuildsConfig.set(name + ".Restrictions", Arrays.asList(saveRestrictions));
				
				GuildsConfig.set(name + ".Base.World", g.getBase().getWorld().getName());
				GuildsConfig.set(name + ".Base.X", g.getBase().getX());
				GuildsConfig.set(name + ".Base.Y", g.getBase().getY());
				GuildsConfig.set(name + ".Base.Z", g.getBase().getZ());
				GuildsConfig.set(name + ".Base.Yaw", g.getBase().getYaw());
				GuildsConfig.set(name + ".Base.Pitch", g.getBase().getPitch());				
				
			}
			
		}
		
		try {
			GuildsConfig.save(GuildsConfigFile);
		} catch (IOException ex) {
			this.getLogger().log(Level.SEVERE, "Could not save config to " + GuildsConfigFile, ex);
		}
		
	}
	
	public void loadPlayers() {
		
		sendConsole("Loading players.yml");
		
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

		sendConsole("Saving players.yml");
		
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
			new Message(MessageType.GUILD_DELETED, sender, guild, this);
			saveGuilds();
			loadGuilds();
			savePlayers();
			loadPlayers();
		} else {
			new Message(MessageType.GUILD_NOT_RECOGNISED, sender, guild, this);
		}
		
	}
	
	public void create(String g, Player sender) {
		
		Guild guild = getGuild(g);
		
		if (guild != null) {
			new Message(MessageType.GUILD_EXISTS, sender, guild, this);
		} else {
			Guild gld = new Guild();
			gld.setName(g);
			gld.New(this);
			GuildsList.add(gld);
			gld.setBase(sender.getLocation());
			new Message(MessageType.GUILD_CREATED, sender, guild, this);
			saveGuilds();
			loadGuilds();
		}
		
	}
	
	public void setbase(String g, Player sender) {
		
		Guild guild = getGuild(g);
		
		if (guild != null) {
			guild.setBase(sender.getLocation());
			new Message(MessageType.BASE_SET, sender, guild, this);
			saveGuilds();
			loadGuilds();
		} else {
			new Message(MessageType.GUILD_NOT_RECOGNISED, sender, guild, this);
		}
		
	}
	
	public void leave(String p, Player sender) {
		
		Player player = Bukkit.getPlayer(p);
		
		if (player != null) {
			if (PlayerGuild.containsKey(p)) {
				PlayerGuild.remove(p);
				
			}
			if (player.equals(sender)) {
				new Message(MessageType.GUILD_LEAVE, sender, this);
			} else {
				new Message(MessageType.PLAYER_REMOVED_FROM_GUILD, sender, player, this);
				new Message(MessageType.YOU_REMOVED_FROM_GUILD, player, this);
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
			if (TasksAltitude.containsKey(p)) {
				Bukkit.getScheduler().cancelTask(TasksAltitude.get(p));
				TasksAltitude.remove(p);
			}
		} else {
			if (sender != null) new Message(MessageType.PLAYER_NOT_RECOGNISED, sender, p, this);
		}
		
	}
	
	public void join(String p, String g, Player sender) {
		
		Player player = Bukkit.getPlayer(p);
		
		Guild guild = getGuild(g);
		
		if (player != null) {
			if (guild != null) {
				if (getEnabled(Settings.ENABLE_CHANGE_GUILD)) {
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
					if (TasksAltitude.containsKey(p)) {
						Bukkit.getScheduler().cancelTask(TasksAltitude.get(p));
						TasksAltitude.remove(p);
					}
					new Message(MessageType.GUILD_JOIN, player, player, guild, this);
					if (sender != null) new Message(MessageType.PLAYER_GUILD_JOIN, sender, player, guild, this);
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
						if (TasksAltitude.containsKey(p)) {
							Bukkit.getScheduler().cancelTask(TasksAltitude.get(p));
							TasksAltitude.remove(p);
						}
						new Message(MessageType.GUILD_JOIN, player, player, guild, this);
						if (sender != null) new Message(MessageType.PLAYER_GUILD_JOIN, sender, player, guild, this);
					} else {
						if (sender != null) new Message(MessageType.ALREADY_IN_GUILD, sender, player, this);
					}
				}
			} else {
				if (sender != null) new Message(MessageType.GUILD_NOT_RECOGNISED, sender, g, this);
			}
		} else {
			if (sender != null) new Message(MessageType.PLAYER_NOT_RECOGNISED, sender, p, this);
		}
		
	}
	
	public void sendMessage(Player p, String msg) {

		String prefix = "";
		
		if (getEnabled(Settings.ENABLE_CHAT_COLOR)) {
			msg = msg.replaceAll("&([0-9a-fk-or])", "\u00A7$1");
		}
		
		if (getEnabled(Settings.ENABLE_GUILD_NAME_ON_BROADCAST)) {
			Guild g = getPlayerGuild(p);
			if (g != null) {
				prefix = getSetting(Settings.SET_GUILDS_BROADCAST_COLOR) + "[" + g.getName() + "] ";
			} else {
				prefix = getSetting(Settings.SET_GUILDS_BROADCAST_COLOR) + "[Guilds] ";
			}
		} else {
			prefix = getSetting(Settings.SET_GUILDS_BROADCAST_COLOR) + "[Guilds] ";
		}
		
		prefix = prefix.replaceAll("&([0-9a-fk-or])", "\u00A7$1");
		
		p.sendMessage(prefix + msg);
		
	}
	
	public void sendConsole(String msg) {

		msg = "[Guilds] " + msg;
		msg = msg.replaceAll("&([0-9a-fk-or])", "");
		System.out.println(msg);
		
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
			if (!getPlayerGuild(p).getProficiency(ProficiencyType.SEE_INVISIBLE).getActive()) online.hidePlayer(p);
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
	
	public boolean isPick(int i) {
		
		if (i == 274) return true;
		if (i == 270) return true;
		if (i == 257) return true;
		if (i == 285) return true;
		if (i == 278) return true;
	
		return false;

	}
	
	public boolean isAxe(int i) {
		
		if (i == 271) return true;
		if (i == 275) return true;
		if (i == 258) return true;
		if (i == 286) return true;
		if (i == 279) return true;
	
		return false;

	}
	
	public boolean isShear(int i) {
		
		if (i == 359) return true;
	
		return false;

	}
	
	public boolean isShovel(int i) {
		
		if (i == 269) return true;
		if (i == 273) return true;
		if (i == 256) return true;
		if (i == 284) return true;
		if (i == 277) return true;
	
		return false;

	}
	
	public boolean getEnabled(Settings s) {
		Object setting = s.getSetting();
		if (s.isBoolean())
			return ((Boolean) setting).booleanValue();
		else
			return false;
	}
	
	public int getIntSetting(Settings s) {
		Object setting = s.getSetting();
		if (s.isInteger())
			return ((Integer) setting).intValue();
		else
			return 0;
	}
	
	public long getLongSetting(Settings s) {
		Object setting = s.getSetting();
		if (s.isLong())
			return ((Long) setting).longValue();
		else
			return 0;
	}
	
	public String getSetting(Settings s) {
		Object setting = s.getSetting();
		if (s.isString())
			return ((String) setting).toString();
		else
			return "";
	}
	
	public void printGuild(Guild g) {
		sendConsole(g.getName());
		for (Proficiency p : g.getProficiencies()) {
			sendConsole("-" + p.getProficiencyType().toString());
			sendConsole("  -" + p.getActive());
			sendConsole("  -" + p.getPower());
		}
	}
	
}
