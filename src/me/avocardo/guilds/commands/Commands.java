package me.avocardo.guilds.commands;

import me.avocardo.guilds.Guild;
import me.avocardo.guilds.GuildsBasic;
import me.avocardo.guilds.messages.Console;
import me.avocardo.guilds.messages.Message;
import me.avocardo.guilds.messages.MessageType;
import me.avocardo.guilds.utilities.Scheduler;
import me.avocardo.guilds.utilities.Settings;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

	private GuildsBasic GuildsBasic;
	
	public Commands(GuildsBasic GuildsBasic) {
		this.GuildsBasic = GuildsBasic;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (sender instanceof Player) {
			
			if (label.equalsIgnoreCase("guilds")) {
			
				Player p = (Player) sender;
				
				if (args.length > 0) {
					
					System.out.println("[PLAYER_COMMAND] " + sender.getName() + ": /" + label + " " + args);
					
					if (args[0].equalsIgnoreCase("list")) {
						if (p.hasPermission("guilds.user.list")) {
							String msg = "";
							for (Guild g: GuildsBasic.GuildsList) {
								if (msg == "") {
									msg = g.getName();
								} else {
									msg = msg + ", " + g.getName();
								}
							}
							p.sendMessage(msg + ".");
						} else {
							new Message(MessageType.NO_PERMISSION, p, GuildsBasic);
						}
					}
					
					if (args[0].equalsIgnoreCase("save")) {
						if (p.hasPermission("guilds.admin.save")) {
							GuildsBasic.savePlayers();
							GuildsBasic.saveGuilds();
							GuildsBasic.saveSettings();
							new Message(MessageType.SAVE, p, GuildsBasic);
						} else {
							new Message(MessageType.NO_PERMISSION, p, GuildsBasic);
						}
					}
					
					if (args[0].equalsIgnoreCase("load")) {
						if (p.hasPermission("guilds.admin.load")) {
							GuildsBasic.loadGuilds();
							GuildsBasic.loadPlayers();
							GuildsBasic.loadSettings();
							new Message(MessageType.LOAD, p, GuildsBasic);
							GuildsBasic.clearScheduler();
						} else {
							new Message(MessageType.NO_PERMISSION, p, GuildsBasic);
						}
					}
					
					if (args[0].equalsIgnoreCase("join")) {
						if (args.length > 1) {
							if (GuildsBasic.getEnabled(Settings.ENABLE_GUILD_JOIN_PERMISSIONS)) {
								if (p.hasPermission("guilds.guild." + args[1])) {
									GuildsBasic.join(p.getName(), args[1], p);
								} else {
									new Message(MessageType.NO_PERMISSION_JOIN, p, args[1], GuildsBasic);
								}
							} else {
								if (p.hasPermission("guilds.user.join")) {
									GuildsBasic.join(p.getName(), args[1], p);
								} else {
									new Message(MessageType.NO_PERMISSION, p, GuildsBasic);
								}
							}
						} else {
							new Message(MessageType.COMMAND_JOIN, p, GuildsBasic);
						}
					}
					
					if (args[0].equalsIgnoreCase("add")) {
						if (args.length > 2) {
							if (p.hasPermission("guilds.admin.add")) {
								GuildsBasic.join(args[1], args[2], p);
							} else {
								new Message(MessageType.NO_PERMISSION, p, GuildsBasic);
							}
						} else {
							new Message(MessageType.COMMAND_ADD, p, GuildsBasic);
						}
					}
					
					if (args[0].equalsIgnoreCase("kick")) {
						if (args.length > 1) {
							if (p.hasPermission("guilds.admin.kick")) {
								GuildsBasic.leave(args[1], p);
							} else {
								new Message(MessageType.NO_PERMISSION, p, GuildsBasic);
							}
						} else {
							new Message(MessageType.COMMAND_KICK, p, GuildsBasic);
						}
					}
					
					if (args[0].equalsIgnoreCase("leave")) {
						if (p.hasPermission("guilds.user.leave")) {
							if (GuildsBasic.getEnabled(Settings.ENABLE_CHANGE_GUILD)) {
								GuildsBasic.leave(p.getName(), p);
							} else {
								new Message(MessageType.GUILD_CHOSEN, p, GuildsBasic);
							}
						} else {
							new Message(MessageType.NO_PERMISSION, p, GuildsBasic);
						}
					}
					
					if (args[0].equalsIgnoreCase("create")) {
						if (args.length > 1) {
							if (p.hasPermission("guilds.admin.create")) {
								GuildsBasic.create(args[1], p);
							} else {
								new Message(MessageType.NO_PERMISSION, p, GuildsBasic);
							}
						} else {
							new Message(MessageType.COMMAND_CREATE, p, GuildsBasic);
						}
					}
					
					if (args[0].equalsIgnoreCase("remove")) {
						if (args.length > 1) {
							if (p.hasPermission("guilds.admin.remove")) {
								GuildsBasic.remove(args[1], p);
							} else {
								new Message(MessageType.NO_PERMISSION, p, GuildsBasic);
							}
						} else {
							new Message(MessageType.COMMAND_REMOVE, p, GuildsBasic);
						}
					}
					
					if (args[0].equalsIgnoreCase("setbase")) {
						if (args.length > 1) {
							if (p.hasPermission("guilds.admin.setbase")) {
								GuildsBasic.setbase(args[1], p);
							} else {
								new Message(MessageType.NO_PERMISSION, p, GuildsBasic);
							}
						} else {
							new Message(MessageType.COMMAND_SETBASE, p, GuildsBasic);
						}
					}
					
				} else {
					help(p);
				}
				
				return true;
				
			}
			
			if (label.equalsIgnoreCase("base")) {
				
				Player p = (Player) sender;
				
				if (p.hasPermission("guilds.user.base")) {
					
					Guild g = GuildsBasic.getPlayerGuild(p);
					
					if (g != null) {
						if (GuildsBasic.getIntSetting(Settings.SET_BASE_TP_DELAY) == 0) {
							p.teleport(g.getBase());
						} else {
							GuildsBasic.BaseDelay.put(p, new Scheduler(GuildsBasic).base(p));
						}
					} else {
						new Message(MessageType.NOT_IN_GUILD, p, GuildsBasic);
					}
					
				} else {
					new Message(MessageType.NO_PERMISSION, p, GuildsBasic);
				}
				
				return true;
				
			}
			
		} else {
			
			if (label.equalsIgnoreCase("guilds")) {
				
				if (args.length > 0) {
					
					if (args[0].equalsIgnoreCase("list")) {
						String msg = "";
						for (Guild g: GuildsBasic.GuildsList) {
							if (msg == "") {
								msg = g.getName();
							} else {
								msg = msg + ", " + g.getName();
							}
						}
						GuildsBasic.sendConsole(msg + ".");
					} else if (args[0].equalsIgnoreCase("save")) {
						GuildsBasic.savePlayers();
						GuildsBasic.saveGuilds();
						GuildsBasic.saveSettings();
						new Console(MessageType.SAVE, GuildsBasic);
					} else if (args[0].equalsIgnoreCase("load")) {
						GuildsBasic.loadGuilds();
						GuildsBasic.loadPlayers();
						GuildsBasic.loadSettings();
						new Console(MessageType.LOAD, GuildsBasic);
						GuildsBasic.clearScheduler();
					} else if (args[0].equalsIgnoreCase("add")) {
						if (args.length > 2) {
							Player p = Bukkit.getPlayer(args[1]);
							Guild g = GuildsBasic.getGuild(args[2]);
							if (p != null) {
								if (g != null) {
									if (GuildsBasic.PlayerGuild.containsKey(p)) {
										GuildsBasic.PlayerGuild.remove(p);
									}
									GuildsBasic.PlayerGuild.put(p.getName(), g);
									GuildsBasic.savePlayers();
									GuildsBasic.loadPlayers();
									if (GuildsBasic.TasksWater.containsKey(p)) {
										Bukkit.getScheduler().cancelTask(GuildsBasic.TasksWater.get(p));
										GuildsBasic.TasksWater.remove(p);
									}
									if (GuildsBasic.TasksLand.containsKey(p)) {
										Bukkit.getScheduler().cancelTask(GuildsBasic.TasksLand.get(p));
										GuildsBasic.TasksLand.remove(p);
									}
									if (GuildsBasic.TasksSun.containsKey(p)) {
										Bukkit.getScheduler().cancelTask(GuildsBasic.TasksSun.get(p));
										GuildsBasic.TasksSun.remove(p);
									}
									if (GuildsBasic.TasksMoon.containsKey(p)) {
										Bukkit.getScheduler().cancelTask(GuildsBasic.TasksMoon.get(p));
										GuildsBasic.TasksMoon.remove(p);
									}
									if (GuildsBasic.TasksStorm.containsKey(p)) {
										Bukkit.getScheduler().cancelTask(GuildsBasic.TasksStorm.get(p));
										GuildsBasic.TasksStorm.remove(p);
									}
									if (GuildsBasic.TasksAltitude.containsKey(p)) {
										Bukkit.getScheduler().cancelTask(GuildsBasic.TasksAltitude.get(p));
										GuildsBasic.TasksAltitude.remove(p);
									}
									new Console(MessageType.PLAYER_GUILD_JOIN, p, g, GuildsBasic);
								} else {
									new Console(MessageType.GUILD_NOT_RECOGNISED, args[2], GuildsBasic);
								}
							} else {
								new Console(MessageType.PLAYER_NOT_RECOGNISED, args[1], GuildsBasic);
							}
						} else {
							new Console(MessageType.COMMAND_JOIN, GuildsBasic);
						}
					} else if (args[0].equalsIgnoreCase("kick")) {
						if (args.length > 1) {
							Player player = Bukkit.getPlayer(args[1]);
							if (player != null) {
								if (GuildsBasic.PlayerGuild.containsKey(player.getName())) {
									GuildsBasic.PlayerGuild.remove(player.getName());
									
								}
								new Console(MessageType.PLAYER_REMOVED_FROM_GUILD, player, GuildsBasic);
								new Message(MessageType.YOU_REMOVED_FROM_GUILD, player, GuildsBasic);
								GuildsBasic.savePlayers();
								GuildsBasic.loadPlayers();
								if (GuildsBasic.TasksWater.containsKey(player)) {
									Bukkit.getScheduler().cancelTask(GuildsBasic.TasksWater.get(player));
									GuildsBasic.TasksWater.remove(player);
								}
								if (GuildsBasic.TasksLand.containsKey(player)) {
									Bukkit.getScheduler().cancelTask(GuildsBasic.TasksLand.get(player));
									GuildsBasic.TasksLand.remove(player);
								}
								if (GuildsBasic.TasksSun.containsKey(player)) {
									Bukkit.getScheduler().cancelTask(GuildsBasic.TasksSun.get(player));
									GuildsBasic.TasksSun.remove(player);
								}
								if (GuildsBasic.TasksMoon.containsKey(player)) {
									Bukkit.getScheduler().cancelTask(GuildsBasic.TasksMoon.get(player));
									GuildsBasic.TasksMoon.remove(player);
								}
								if (GuildsBasic.TasksStorm.containsKey(player)) {
									Bukkit.getScheduler().cancelTask(GuildsBasic.TasksStorm.get(player));
									GuildsBasic.TasksStorm.remove(player);
								}
								if (GuildsBasic.TasksAltitude.containsKey(player)) {
									Bukkit.getScheduler().cancelTask(GuildsBasic.TasksAltitude.get(player));
									GuildsBasic.TasksAltitude.remove(player);
								}
							} else {
								new Console(MessageType.PLAYER_NOT_RECOGNISED, args[1], GuildsBasic);
							}
						} else {
							new Console(MessageType.COMMAND_KICK, GuildsBasic);
						}
					} else if (args[0].equalsIgnoreCase("create")) {
						if (args.length > 1) {
							Guild guild = GuildsBasic.getGuild(args[1]);
							if (guild != null) {
								new Console(MessageType.GUILD_EXISTS, guild, GuildsBasic);
							} else {
								World w = null;
								String world = "world";
								w = Bukkit.getWorld(world);
								if (w == null) {
									for (World wld : Bukkit.getWorlds()) {
										w = wld;
										break;
									}
								}
								Guild g = new Guild();
								g.setName(args[1]);
								g.New(GuildsBasic);
								g.setBase(new Location(w, 0, 0, 0, 0, 0));
								GuildsBasic.GuildsList.add(g);
								new Console(MessageType.GUILD_CREATED, g, GuildsBasic);
								GuildsBasic.saveGuilds();
								GuildsBasic.loadGuilds();
							}
						} else {
							new Console(MessageType.COMMAND_CREATE, GuildsBasic);
						}
					} else if (args[0].equalsIgnoreCase("remove")) {
						if (args.length > 1) {
							Guild guild = GuildsBasic.getGuild(args[1]);
							if (guild != null) {
								GuildsBasic.GuildsList.remove(guild);
								new Console(MessageType.GUILD_DELETED, guild, GuildsBasic);
								GuildsBasic.saveGuilds();
								GuildsBasic.loadGuilds();
								GuildsBasic.savePlayers();
								GuildsBasic.loadPlayers();
							} else {
								new Console(MessageType.GUILD_NOT_RECOGNISED, args[1], GuildsBasic);
							}
						} else {
							new Console(MessageType.COMMAND_REMOVE, GuildsBasic);
						}
					} else {
						GuildsBasic.sendConsole("Console Command not recognised...");
					}
					
				} else {
					GuildsBasic.sendConsole("GuildsBasic v" + GuildsBasic.v);
				}
				
				return true;
				
			}
			
			if (label.equalsIgnoreCase("base")) {
				
				Player p = (Player) sender;
				
				if (p.hasPermission("guilds.user.base")) {
					
					Guild g = GuildsBasic.getPlayerGuild(p);
					
					if (g != null) {
						p.teleport(g.getBase());
					} else {
						new Message(MessageType.NOT_IN_GUILD, p, GuildsBasic);
					}
					
				} else {
					new Message(MessageType.NO_PERMISSION, p, GuildsBasic);
				}
				
				return true;
				
			}
			
		}
		
		return false;
		
	}

	public void help(Player player) {
		player.sendMessage(ChatColor.YELLOW + "=============== Guilds v" + GuildsBasic.v + " ===============");
		player.sendMessage(ChatColor.AQUA + "/guilds save " + ChatColor.YELLOW + ": save to file.");
		player.sendMessage(ChatColor.AQUA + "/guilds load " + ChatColor.YELLOW + ": load from file.");
		player.sendMessage(ChatColor.AQUA + "/guilds join <guild> " + ChatColor.YELLOW + ": join guild.");
		player.sendMessage(ChatColor.AQUA + "/guilds leave " + ChatColor.YELLOW + ": leave current guild.");
		player.sendMessage(ChatColor.AQUA + "/guilds kick <player> <guild> " + ChatColor.YELLOW + ": kick player from guild.");
		player.sendMessage(ChatColor.AQUA + "/guilds add <player> <guild> " + ChatColor.YELLOW + ": add player to guild.");
		player.sendMessage(ChatColor.AQUA + "/guilds create <guild> " + ChatColor.YELLOW + ": create guild.");
		player.sendMessage(ChatColor.AQUA + "/guilds remove <guild> " + ChatColor.YELLOW + ": remove guild.");
		player.sendMessage(ChatColor.AQUA + "/guilds setbase <guild> " + ChatColor.YELLOW + ": set guilds base.");
		player.sendMessage(ChatColor.AQUA + "/base " + ChatColor.YELLOW + ": tp to your guild base.");
		player.sendMessage(ChatColor.YELLOW + "=============== Guilds v" + GuildsBasic.v + " ===============");
	}

}
