package me.avocardo.guilds.commands;

import me.avocardo.guilds.Guild;
import me.avocardo.guilds.GuildsBasic;
import me.avocardo.guilds.utilities.Scheduler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

	private GuildsBasic plugin;
	
	public Commands(GuildsBasic plugin) {
		this.plugin = plugin;
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
							for (Guild g: plugin.GuildsList) {
								if (msg == "") {
									msg = g.getName();
								} else {
									msg = msg + ", " + g.getName();
								}
							}
							p.sendMessage(msg + ".");
						} else {
							plugin.msg(25, p, "", "");
						}
					}
					
					if (args[0].equalsIgnoreCase("save")) {
						if (p.hasPermission("guilds.admin.save")) {
							plugin.savePlayers();
							plugin.saveGuilds();
							plugin.saveSettings();
							plugin.msg(26, p, "", "");
						} else {
							plugin.msg(25, p, "", "");
						}
					}
					
					if (args[0].equalsIgnoreCase("load")) {
						if (p.hasPermission("guilds.admin.load")) {
							plugin.loadGuilds();
							plugin.loadPlayers();
							plugin.loadSettings();
							plugin.msg(27, p, "", "");
							plugin.clearScheduler();
						} else {
							plugin.msg(25, p, "", "");
						}
					}
					
					if (args[0].equalsIgnoreCase("join")) {
						if (args.length > 1) {
							if (plugin.allowJoinPermissions) {
								if (p.hasPermission("guilds.guild." + args[1])) {
									plugin.join(p.getName(), args[1], p);
								} else {
									plugin.msg(38, p, "", args[1]);
								}
							} else {
								if (p.hasPermission("guilds.user.join")) {
									plugin.join(p.getName(), args[1], p);
								} else {
									plugin.msg(25, p, "", "");
								}
							}
						} else {
							plugin.msg(35, p, "", "");
						}
					}
					
					if (args[0].equalsIgnoreCase("add")) {
						if (args.length > 2) {
							if (p.hasPermission("guilds.admin.add")) {
								plugin.join(args[1], args[2], p);
							} else {
								plugin.msg(25, p, "", "");
							}
						} else {
							plugin.msg(34, p, "", "");
						}
					}
					
					if (args[0].equalsIgnoreCase("kick")) {
						if (args.length > 1) {
							if (p.hasPermission("guilds.admin.kick")) {
								plugin.leave(args[1], p);
							} else {
								plugin.msg(25, p, "", "");
							}
						} else {
							plugin.msg(33, p, "", "");
						}
					}
					
					if (args[0].equalsIgnoreCase("leave")) {
						if (p.hasPermission("guilds.user.leave")) {
							if (plugin.allowChangeGuild) {
								plugin.leave(p.getName(), p);
							} else {
								plugin.msg(32, p, "", "");
							}
						} else {
							plugin.msg(25, p, "", "");
						}
					}
					
					if (args[0].equalsIgnoreCase("create")) {
						if (args.length > 1) {
							if (p.hasPermission("guilds.admin.create")) {
								plugin.create(args[1], p);
							} else {
								plugin.msg(25, p, "", "");
							}
						} else {
							plugin.msg(31, p, "", "");
						}
					}
					
					if (args[0].equalsIgnoreCase("remove")) {
						if (args.length > 1) {
							if (p.hasPermission("guilds.admin.remove")) {
								plugin.remove(args[1], p);
							} else {
								plugin.msg(25, p, "", "");
							}
						} else {
							plugin.msg(30, p, "", "");
						}
					}
					
					if (args[0].equalsIgnoreCase("setbase")) {
						if (args.length > 1) {
							if (p.hasPermission("guilds.admin.setbase")) {
								plugin.setbase(args[1], p);
							} else {
								plugin.msg(25, p, "", "");
							}
						} else {
							plugin.msg(29, p, "", "");
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
					
					Guild g = plugin.getPlayerGuild(p);
					
					if (g != null) {
						if (plugin.setBaseDelay == 0) {
							p.teleport(g.getBase());
						} else {
							plugin.BaseDelay.put(p, new Scheduler(plugin).base(p));
						}
					} else {
						plugin.msg(28, p, "", "");
					}
					
				} else {
					plugin.msg(25, p, "", "");
				}
				
				return true;
				
			}
			
		} else {
			
			if (label.equalsIgnoreCase("guilds")) {
				
				if (args.length > 0) {
					
					if (args[0].equalsIgnoreCase("list")) {
						String msg = "";
						for (Guild g: plugin.GuildsList) {
							if (msg == "") {
								msg = g.getName();
							} else {
								msg = msg + ", " + g.getName();
							}
						}
						plugin.console(msg + ".");
					} else if (args[0].equalsIgnoreCase("save")) {
						plugin.savePlayers();
						plugin.saveGuilds();
						plugin.saveSettings();
						plugin.msg2(26, "", "");
					} else if (args[0].equalsIgnoreCase("load")) {
						plugin.loadGuilds();
						plugin.loadPlayers();
						plugin.loadSettings();
						plugin.msg2(27, "", "");
						plugin.clearScheduler();
					} else if (args[0].equalsIgnoreCase("add")) {
						if (args.length > 2) {
							Player p = Bukkit.getPlayer(args[1]);
							Guild g = plugin.getGuild(args[2]);
							if (p != null) {
								if (g != null) {
									if (plugin.PlayerGuild.containsKey(p)) {
										plugin.PlayerGuild.remove(p);
									}
									plugin.PlayerGuild.put(p.getName(), g);
									plugin.savePlayers();
									plugin.loadPlayers();
									if (plugin.TasksWater.containsKey(p)) {
										Bukkit.getScheduler().cancelTask(plugin.TasksWater.get(p));
										plugin.TasksWater.remove(p);
									}
									if (plugin.TasksLand.containsKey(p)) {
										Bukkit.getScheduler().cancelTask(plugin.TasksLand.get(p));
										plugin.TasksLand.remove(p);
									}
									if (plugin.TasksSun.containsKey(p)) {
										Bukkit.getScheduler().cancelTask(plugin.TasksSun.get(p));
										plugin.TasksSun.remove(p);
									}
									if (plugin.TasksMoon.containsKey(p)) {
										Bukkit.getScheduler().cancelTask(plugin.TasksMoon.get(p));
										plugin.TasksMoon.remove(p);
									}
									if (plugin.TasksStorm.containsKey(p)) {
										Bukkit.getScheduler().cancelTask(plugin.TasksStorm.get(p));
										plugin.TasksStorm.remove(p);
									}
									plugin.msg2(37, p.getName(), g.getName());
								} else {
									plugin.msg2(2, args[1], args[2]);
								}
							} else {
								plugin.msg2(3, args[1], args[2]);
							}
						} else {
							plugin.msg2(34, "", "");
						}
					} else if (args[0].equalsIgnoreCase("kick")) {
						if (args.length > 1) {
							Player player = Bukkit.getPlayer(args[1]);
							if (player != null) {
								if (plugin.PlayerGuild.containsKey(player.getName())) {
									plugin.PlayerGuild.remove(player.getName());
									
								}
								plugin.msg2(5, player.getName(), "");
								plugin.msg(6, player, "", "");
								plugin.savePlayers();
								plugin.loadPlayers();
								if (plugin.TasksWater.containsKey(player)) {
									Bukkit.getScheduler().cancelTask(plugin.TasksWater.get(player));
									plugin.TasksWater.remove(player);
								}
								if (plugin.TasksLand.containsKey(player)) {
									Bukkit.getScheduler().cancelTask(plugin.TasksLand.get(player));
									plugin.TasksLand.remove(player);
								}
								if (plugin.TasksSun.containsKey(player)) {
									Bukkit.getScheduler().cancelTask(plugin.TasksSun.get(player));
									plugin.TasksSun.remove(player);
								}
								if (plugin.TasksMoon.containsKey(player)) {
									Bukkit.getScheduler().cancelTask(plugin.TasksMoon.get(player));
									plugin.TasksMoon.remove(player);
								}
								if (plugin.TasksStorm.containsKey(player)) {
									Bukkit.getScheduler().cancelTask(plugin.TasksStorm.get(player));
									plugin.TasksStorm.remove(player);
								}
							} else {
								plugin.msg2(3, args[1], "");
							}
						} else {
							plugin.msg2(33, "", "");
						}
					} else if (args[0].equalsIgnoreCase("create")) {
						if (args.length > 1) {
							Guild guild = plugin.getGuild(args[1]);
							if (guild != null) {
								plugin.msg2(9, "", args[1]);
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
								g.setBase(new Location(w, 0, 0, 0, 0, 0));
								plugin.GuildsList.add(g);
								plugin.msg2(8, "", args[1]);
								plugin.saveGuilds();
								plugin.loadGuilds();
							}
						} else {
							plugin.msg2(31, "", "");
						}
					} else if (args[0].equalsIgnoreCase("remove")) {
						if (args.length > 1) {
							Guild guild = plugin.getGuild(args[1]);
							if (guild != null) {
								plugin.GuildsList.remove(guild);
								plugin.msg2(10, "", args[1]);
								plugin.saveGuilds();
								plugin.loadGuilds();
								plugin.savePlayers();
								plugin.loadPlayers();
							} else {
								plugin.msg2(2, "", args[1]);
							}
						} else {
							plugin.msg2(30, "", "");
						}
					} else {
						plugin.console(ChatColor.YELLOW + "Console Command not recognised...");
					}
					
				} else {
					plugin.console(ChatColor.YELLOW + "GuildsBasic v" + plugin.v);
				}
				
				return true;
				
			}
			
			if (label.equalsIgnoreCase("base")) {
				
				Player p = (Player) sender;
				
				if (p.hasPermission("guilds.user.base")) {
					
					Guild g = plugin.getPlayerGuild(p);
					
					if (g != null) {
						p.teleport(g.getBase());
					} else {
						plugin.msg(28, p, "", "");
					}
					
				} else {
					plugin.msg(25, p, "", "");
				}
				
				return true;
				
			}
			
		}
		
		return false;
		
	}

	public void help(Player player) {
		player.sendMessage(ChatColor.YELLOW + "=============== Guilds v" + plugin.v + " ===============");
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
		player.sendMessage(ChatColor.YELLOW + "=============== Guilds v" + plugin.v + " ===============");
	}

}
