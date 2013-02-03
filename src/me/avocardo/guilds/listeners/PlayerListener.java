package me.avocardo.guilds.listeners;

import java.util.ArrayList;
import java.util.List;

import me.avocardo.guilds.Guild;
import me.avocardo.guilds.GuildsBasic;
import me.avocardo.guilds.User;
import me.avocardo.guilds.messages.Message;
import me.avocardo.guilds.messages.MessageType;
import me.avocardo.guilds.utilities.Proficiency;
import me.avocardo.guilds.utilities.ProficiencyType;
import me.avocardo.guilds.utilities.Scheduler;
import me.avocardo.guilds.utilities.Settings;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class PlayerListener implements Listener {

	private GuildsBasic GuildsBasic;

	public PlayerListener(GuildsBasic GuildsBasic) {
		this.GuildsBasic = GuildsBasic;
    }
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		GuildsBasic.PlayerUser.put(p, GuildsBasic.newPlayerUser(p));
		Guild g = GuildsBasic.getPlayerGuild(p);
		World w = p.getWorld();
		Biome b = p.getLocation().getBlock().getBiome();
		if (g != null) {
			g.addOnline();
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
		
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
		Player p = event.getPlayer();
		Guild g = GuildsBasic.getPlayerGuild(p);
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
 	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player p = event.getPlayer();
		if (GuildsBasic.getIntSetting(Settings.SET_BASE_TP_DELAY) > 0) {
			if (GuildsBasic.BaseDelay.containsKey(p)) {
				Bukkit.getScheduler().cancelTask(GuildsBasic.BaseDelay.get(p));
				GuildsBasic.BaseDelay.remove(p);
			}
		}
		
		Guild g = GuildsBasic.getPlayerGuild(p);
		
		if (g != null) g.subtractOnline();
		
		if (GuildsBasic.PlayerUser.containsKey(p)) {
			GuildsBasic.PlayerUser.remove(p);
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerKick(PlayerKickEvent event) {
		Player p = event.getPlayer();
		if (GuildsBasic.getIntSetting(Settings.SET_BASE_TP_DELAY) > 0) {
			if (GuildsBasic.BaseDelay.containsKey(p)) {
				Bukkit.getScheduler().cancelTask(GuildsBasic.BaseDelay.get(p));
				GuildsBasic.BaseDelay.remove(p);
			}
		}
		if (GuildsBasic.PlayerUser.containsKey(p))
			GuildsBasic.PlayerUser.remove(p);
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onMobTarget(EntityTargetEvent event) {

		if (event.getTarget() instanceof Player) {
			if (event.getEntity() instanceof LivingEntity) {
				Player p = (Player) event.getTarget();
				Guild g = GuildsBasic.getPlayerGuild(p);
				if (g != null) {
					if (g.getWorlds().contains(p.getWorld()) && g.getBiomes().contains(p.getLocation().getBlock().getBiome())) {
						Proficiency MOBTARGET = g.getProficiency(ProficiencyType.MOBTARGET);
						if (MOBTARGET.getActive()) {
							if (event.getReason().equals(TargetReason.CLOSEST_PLAYER)) {
								event.setCancelled(true);
								return;
							}
						}
					}
				}
			}
		}
		
		return;
		
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onInventoryClose(InventoryCloseEvent event) {
		
		if (!GuildsBasic.getEnabled(Settings.ENABLE_RESTRICTIONS)) {
			return;
		}
		
		if (event.getView().getType().equals(InventoryType.CRAFTING)) {
		
			Player p = (Player) event.getPlayer();
			Guild g = GuildsBasic.getPlayerGuild(p);
			
			if (g != null) {
				World w = p.getWorld();
				Biome b = p.getLocation().getBlock().getBiome();
				if (g.getWorlds().contains(w) && g.getBiomes().contains(b)) {
					if (g.getRestrictions().size() > 0) {
						PlayerInventory inv = p.getInventory();
						if(inv.getItemInHand() != null) {
							if (g.getRestrictions().contains(inv.getItemInHand().getTypeId())) {
								p.getWorld().dropItem(p.getLocation(), p.getInventory().getItemInHand());
								p.getInventory().setItem(p.getInventory().getHeldItemSlot(), new ItemStack(0));
								p.setItemInHand(new ItemStack(0));
								new Message(MessageType.ITEM_RESTRICTED, p, GuildsBasic);
							}
						}
						if(inv.getHelmet() != null) {
							if (g.getRestrictions().contains(inv.getHelmet().getTypeId())) {
								if(inv.firstEmpty() >= 9) {
				    				inv.addItem(inv.getHelmet());
				    				inv.setHelmet(null);
				    			} else {
				    				p.getWorld().dropItem(p.getLocation(), inv.getHelmet());
				    				inv.setHelmet(null);
				    			}
								new Message(MessageType.ARMOUR_RESTRICTED, p, GuildsBasic);
							}
						}
						if(inv.getChestplate() != null) {
							if (g.getRestrictions().contains(inv.getChestplate().getTypeId())) {
								if(inv.firstEmpty() >= 9) {
				    				inv.addItem(inv.getChestplate());
				    				inv.setChestplate(null);
				    			} else {
				    				p.getWorld().dropItem(p.getLocation(), inv.getChestplate());
				    				inv.setChestplate(null);
				    			}
								new Message(MessageType.ARMOUR_RESTRICTED, p, GuildsBasic);
							}
						}
						if(inv.getLeggings() != null) {
							if (g.getRestrictions().contains(inv.getLeggings().getTypeId())) {
								if(inv.firstEmpty() >= 9) {
				    				inv.addItem(inv.getLeggings());
				    				inv.setLeggings(null);
				    			} else {
				    				p.getWorld().dropItem(p.getLocation(), inv.getLeggings());
				    				inv.setLeggings(null);
				    			}
								new Message(MessageType.ARMOUR_RESTRICTED, p, GuildsBasic);
							}
						}
						if(inv.getBoots() != null) {
							if (g.getRestrictions().contains(inv.getBoots().getTypeId())) {
								if(inv.firstEmpty() >= 9) {
				    				inv.addItem(inv.getBoots());
				    				inv.setBoots(null);
				    			} else {
				    				p.getWorld().dropItem(p.getLocation(), inv.getBoots());
				    				inv.setBoots(null);
				    			}
								new Message(MessageType.ARMOUR_RESTRICTED, p, GuildsBasic);
							}
						}
					}
				}
			}
			
		}

		return;
		
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerHealth(EntityRegainHealthEvent event) {
		
		if (event.getEntity() instanceof Player) {
			
			Player p = (Player) event.getEntity();
			Guild g = GuildsBasic.getPlayerGuild(p);
			World w = p.getWorld();
			Biome b = p.getLocation().getBlock().getBiome();
			if (g != null) {
				if (g.getWorlds().contains(w) && g.getBiomes().contains(b)) {
					Proficiency RECOVERHEALTH = g.getProficiency(ProficiencyType.RECOVERHEALTH);
					Proficiency MAX_HEALTH = g.getProficiency(ProficiencyType.MAX_HEALTH);
					if (event.getRegainReason() == RegainReason.REGEN || event.getRegainReason() == RegainReason.SATIATED) {
						if (!RECOVERHEALTH.getActive()) {
							event.setCancelled(true);
							return;
						}
					}
					if (MAX_HEALTH.getActive()) {
						if (MAX_HEALTH.getPower() < 20 && MAX_HEALTH.getPower() > 0) {
							if ((p.getHealth() + event.getAmount()) > (int) MAX_HEALTH.getPower()) {
								event.setCancelled(true);
								return;
							}
						}
					}
				}
			}
			
		}
		
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onExpChange(PlayerExpChangeEvent event) {

		Player p = event.getPlayer();
		
		Guild g = GuildsBasic.getPlayerGuild(p);
		
		if (g != null) {
			if (g.getWorlds().contains(p.getWorld()) && g.getBiomes().contains(p.getLocation().getBlock().getBiome())) {
				Proficiency XP_MULTIPLIER = g.getProficiency(ProficiencyType.XP_MULTIPLIER);
				if (XP_MULTIPLIER.getActive()) {
					if (XP_MULTIPLIER.getPower() != (double) 1) {
						if (XP_MULTIPLIER.getPower() == 0) {
							event.setAmount(0);
							return;
						} else {
							event.setAmount((int) Math.round((double) event.getAmount() * XP_MULTIPLIER.getPower()));
							return;
						}
					}
				}
			}
		}
		
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerRespawn(PlayerRespawnEvent event) {

		Player p = event.getPlayer();
		User u = GuildsBasic.getPlayerUser(p);
		Guild g = GuildsBasic.getPlayerGuild(p);
		
		if (u.Inventory.containsKey(p)) {
			for (ItemStack i : u.Inventory.get(p)) {
				p.getInventory().addItem(i);
			}
			u.Inventory.remove(p);
		}
		
		if (GuildsBasic.getIntSetting(Settings.SET_BASE_TP_DELAY) > 0) {
			if (GuildsBasic.BaseDelay.containsKey(p)) {
				Bukkit.getScheduler().cancelTask(GuildsBasic.BaseDelay.get(p));
				GuildsBasic.BaseDelay.remove(p);
			}
		}
		
		if (GuildsBasic.getEnabled(Settings.ENABLE_BASE_ON_DEATH)) {
			if (g != null) {
				if (g.getWorlds().contains(p.getLocation().getWorld())) {
					event.setRespawnLocation(g.getBase());
				}
			}
		}
		
		if (g != null) {
			if (g.getWorlds().contains(p.getWorld())) {
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
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerDeath(PlayerDeathEvent event) {
		
		if (event.getEntity() instanceof Player) {
			Player p = event.getEntity();
			User u = GuildsBasic.getPlayerUser(p);
			if (GuildsBasic.getIntSetting(Settings.SET_BASE_TP_DELAY) > 0) {
				if (GuildsBasic.BaseDelay.containsKey(p)) {
					Bukkit.getScheduler().cancelTask(GuildsBasic.BaseDelay.get(p));
					GuildsBasic.BaseDelay.remove(p);
				}
			}
			Guild g = GuildsBasic.getPlayerGuild(p);
			World w = p.getWorld();
			Biome b = p.getLocation().getBlock().getBiome();
			if (g != null) {
				if (g.getWorlds().contains(w) && g.getBiomes().contains(b)) {
					Proficiency EXPLODEDEATH = g.getProficiency(ProficiencyType.EXPLODEDEATH);
					Proficiency RECOVEREXP = g.getProficiency(ProficiencyType.RECOVEREXP);
					Proficiency RECOVERITEMS = g.getProficiency(ProficiencyType.RECOVERITEMS);
					if (EXPLODEDEATH.getActive()) {
						p.getWorld().createExplosion(p.getLocation(), (int) EXPLODEDEATH.getPower());
					}
					if (RECOVEREXP.getActive()) {
						event.setNewExp(p.getTotalExperience());
						event.setDroppedExp(0);
					}
					if (RECOVERITEMS.getActive()) {
						u.Inventory.put(p, event.getDrops());
						event.getDrops().clear();
					}
				}
			}
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
		}
		
		return;
		
	}
		
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
		
		Player p = event.getPlayer();
		Guild g = GuildsBasic.getPlayerGuild(p);
		User u = GuildsBasic.getPlayerUser(p);
		
		if (g != null) {
			if (event.isSneaking()) {
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
			} else {
				GuildsBasic.showPlayer(p);
			}
		}
		
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerMove(PlayerMoveEvent event) {
		
		Player p = event.getPlayer();
		Guild g = GuildsBasic.getPlayerGuild(p);
		
		if (g != null) {
			if (!GuildsBasic.getEnabled(Settings.ENABLE_ENEMY_ENTER_PROTECTION_BARRIER)) {
				for (Guild guild : GuildsBasic.GuildsList) {
					if (!guild.equals(g)) {
						if (guild.getBase().getWorld() == event.getTo().getWorld()) {
							if (event.getFrom().distance(guild.getBase()) > event.getTo().distance(guild.getBase())) {
								if (p.getLocation().distance(guild.getBase()) <= (GuildsBasic.getIntSetting(Settings.SET_PROTECTION_BARRIER) + 1)) {
									event.setCancelled(true);
									new Message(MessageType.PROTECTED_BARRIER_WARNING, p, g, GuildsBasic);
									return;
								}
							}
						}
					}
				}
			}
			World w = p.getWorld();
			Biome b = p.getLocation().getBlock().getBiome();
			if (g.getWorlds().contains(w) && g.getBiomes().contains(b)) {
				Long time = p.getWorld().getTime();
				Proficiency FLIGHT = g.getProficiency(ProficiencyType.FLIGHT);
				Proficiency HIGHJUMP = g.getProficiency(ProficiencyType.HIGHJUMP);
				Proficiency SUNLIGHT = g.getProficiency(ProficiencyType.SUNLIGHT);
				Proficiency MOONLIGHT = g.getProficiency(ProficiencyType.MOONLIGHT);
				Proficiency STORM = g.getProficiency(ProficiencyType.STORM);
				Proficiency WATERDAMAGE = g.getProficiency(ProficiencyType.WATERDAMAGE);
				Proficiency LANDDAMAGE = g.getProficiency(ProficiencyType.LANDDAMAGE);
				Proficiency ALTITUDE = g.getProficiency(ProficiencyType.ALTITUDE);
				Proficiency SWIMMER = g.getProficiency(ProficiencyType.SWIMMER);
				Proficiency SPEED = g.getProficiency(ProficiencyType.SPEED);
				Proficiency OXYGEN = g.getProficiency(ProficiencyType.OXYGEN);
				if (FLIGHT.getActive()) {
					if (p.isSneaking()) p.setVelocity(p.getLocation().getDirection().multiply(FLIGHT.getPower()));
				}
				if (HIGHJUMP.getActive()) {
					p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 10, (int) HIGHJUMP.getPower()));
				}
				if (OXYGEN.getActive()) {
					if (p.getLocation().getBlock().getType().equals(Material.STATIONARY_WATER) || p.getLocation().add(0, 1, 0).getBlock().getType().equals(Material.STATIONARY_WATER)) {
						p.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 10, (int) OXYGEN.getPower()));
					}
				}
				if (SPEED.getActive()) {
					p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10, (int) SPEED.getPower()));
				}
				if (SWIMMER.getActive()) {
					if (p.getLocation().getBlock().getType() == Material.STATIONARY_WATER || p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.STATIONARY_WATER) {
						Vector dir = p.getLocation().getDirection();
						Vector vec = new Vector(dir.getX() * 0.4D, 0, dir.getZ() * 0.4D);
						p.setVelocity(vec);
					}
				}
				if (SUNLIGHT.getActive()) {
					if (time > 0 && time < 13000) {
						if ((p.getWorld().getBlockAt(p.getLocation()).getLightFromSky()) > 8) {
							if (!GuildsBasic.TasksSun.containsKey(p)) {
								GuildsBasic.TasksSun.put(p, new Scheduler(GuildsBasic).sun(p));
							}
						} else {
							if (GuildsBasic.TasksSun.containsKey(p)) {
								Bukkit.getScheduler().cancelTask(GuildsBasic.TasksSun.get(p));
								GuildsBasic.TasksSun.remove(p);
							}
						}
					} else {
						if (GuildsBasic.TasksSun.containsKey(p)) {
							Bukkit.getScheduler().cancelTask(GuildsBasic.TasksSun.get(p));
							GuildsBasic.TasksSun.remove(p);
						}
					}
				}
				if (MOONLIGHT.getActive()) {
					if (time > 13000 && time < 23000) {
						if ((p.getWorld().getBlockAt(p.getLocation()).getLightFromSky()) > 8) {
							if (!GuildsBasic.TasksMoon.containsKey(p)) {
								GuildsBasic.TasksMoon.put(p, new Scheduler(GuildsBasic).moon(p));
							}
						} else {
							if (GuildsBasic.TasksMoon.containsKey(p)) {
								Bukkit.getScheduler().cancelTask(GuildsBasic.TasksMoon.get(p));
								GuildsBasic.TasksMoon.remove(p);
							}
						}
					} else {
						if (GuildsBasic.TasksMoon.containsKey(p)) {
							Bukkit.getScheduler().cancelTask(GuildsBasic.TasksMoon.get(p));
							GuildsBasic.TasksMoon.remove(p);
						}
					}
				}
				if (STORM.getActive()) {
					if (p.getWorld().hasStorm() == true) {
						if (p.getWorld().getHighestBlockAt(p.getLocation().getBlock().getLocation()).getY() <= p.getLocation().getBlock().getLocation().getY()) {
							if (!GuildsBasic.TasksStorm.containsKey(p)) {
								GuildsBasic.TasksStorm.put(p, new Scheduler(GuildsBasic).storm(p));
							}
						} else {
							if (GuildsBasic.TasksStorm.containsKey(p)) {
								Bukkit.getScheduler().cancelTask(GuildsBasic.TasksStorm.get(p));
								GuildsBasic.TasksStorm.remove(p);
							}
						}
					} else {
						if (GuildsBasic.TasksStorm.containsKey(p)) {
							Bukkit.getScheduler().cancelTask(GuildsBasic.TasksStorm.get(p));
							GuildsBasic.TasksStorm.remove(p);
						}
					}
				}
				if (WATERDAMAGE.getActive()) {
					if (p.getLocation().getBlock().getTypeId() == 8 || p.getLocation().getBlock().getTypeId() == 9) {
						if (!GuildsBasic.TasksWater.containsKey(p)) {
							GuildsBasic.TasksWater.put(p, new Scheduler(GuildsBasic).water(p));
						}
					} else {
						if (GuildsBasic.TasksWater.containsKey(p)) {
							Bukkit.getScheduler().cancelTask(GuildsBasic.TasksWater.get(p));
							GuildsBasic.TasksWater.remove(p);
						}
					}
				}
				if (ALTITUDE.getActive()) {
					if (p.getLocation().getBlockY() > ALTITUDE.getMaximum() || p.getLocation().getBlockY() < ALTITUDE.getMinimum()) {
						if (!GuildsBasic.TasksAltitude.containsKey(p)) {
							GuildsBasic.TasksAltitude.put(p, new Scheduler(GuildsBasic).altitude(p));
						}
					} else {
						if (GuildsBasic.TasksAltitude.containsKey(p)) {
							Bukkit.getScheduler().cancelTask(GuildsBasic.TasksAltitude.get(p));
							GuildsBasic.TasksAltitude.remove(p);
						}
					}
				}
				if (LANDDAMAGE.getActive()) {
					
					List<Integer> blocks = new ArrayList<Integer>();
					
					blocks.add(p.getLocation().getBlock().getTypeId());
					blocks.add(p.getLocation().add(0, 1, 0).getBlock().getTypeId());
					blocks.add(p.getLocation().subtract(0, 1, 0).getBlock().getTypeId());
					
					if (blocks.contains(8) == false && blocks.contains(9) == false) {
						if (!GuildsBasic.TasksLand.containsKey(p)) {
							GuildsBasic.TasksLand.put(p, new Scheduler(GuildsBasic).land(p));
						}
					} else {
						if (GuildsBasic.TasksLand.containsKey(p)) {
							Bukkit.getScheduler().cancelTask(GuildsBasic.TasksLand.get(p));
							GuildsBasic.TasksLand.remove(p);
						}
					}
				}
			}
		} else {
			if (!GuildsBasic.getEnabled(Settings.ENABLE_NO_GUILD)) {
				if (event.getFrom().getX() != event.getTo().getX() || event.getFrom().getZ() != event.getTo().getZ()) {
					event.setTo(event.getFrom());
					new Message(MessageType.MUST_JOIN_GUILD, p, GuildsBasic);
					new Message(MessageType.JOIN_COMMAND, p, GuildsBasic);
					return;
				}
			}
		}
		
		return;
		
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onProjectileHit(ProjectileHitEvent event) {

		Entity entity = event.getEntity();
		
		if (entity instanceof Arrow) {
			Arrow arrow = (Arrow) entity;
			Entity shooter = arrow.getShooter();
			if (shooter instanceof Player) {
		        Player p = (Player) shooter;
		       	Guild g = GuildsBasic.getPlayerGuild(p);
		       	User u = GuildsBasic.getPlayerUser(p);
		        if (g != null) {
		        	World w = p.getWorld();
					Biome b = p.getLocation().getBlock().getBiome();
					if (g.getWorlds().contains(w) && g.getBiomes().contains(b)) {
		        		Location loc = arrow.getLocation();
		        		Proficiency EXPLOSIVEARROW = g.getProficiency(ProficiencyType.EXPLOSIVEARROW);
						Proficiency ZOMBIEARROW = g.getProficiency(ProficiencyType.ZOMBIEARROW);
						Proficiency LIGHTNINGARROW = g.getProficiency(ProficiencyType.LIGHTNINGARROW);
						Proficiency TPARROW = g.getProficiency(ProficiencyType.TPARROW);
		        		if (EXPLOSIVEARROW.getActive()) {
		        			if (EXPLOSIVEARROW.getUseProficiency(u)) w.createExplosion(loc, (int) EXPLOSIVEARROW.getPower());                		
	                	}
		        		if (ZOMBIEARROW.getActive()) {
		        			if (ZOMBIEARROW.getUseProficiency(u)) w.spawnEntity(loc, EntityType.ZOMBIE);
	                	}
	                	if (LIGHTNINGARROW.getActive()) {
	                		if (LIGHTNINGARROW.getUseProficiency(u)) w.strikeLightning(loc);
	                	}
	                	if (TPARROW.getActive()) {
	                		if (TPARROW.getUseProficiency(u)) p.teleport(loc);
	                	}
		        	}
		        }
			}
		}
		
		return;
		
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {

		if (!GuildsBasic.getEnabled(Settings.ENABLE_RESTRICTIONS)) {
			return;
		}
		
		if (!GuildsBasic.getEnabled(Settings.ENABLE_PICKUP_RESTRICTIONS)) {
		
			Player p = event.getPlayer();
		    Guild g = GuildsBasic.getPlayerGuild(p);
		    if (g != null) {
		    	World w = p.getWorld();
				Biome b = p.getLocation().getBlock().getBiome();
				if (g.getWorlds().contains(w) && g.getBiomes().contains(b)) {
					if (g.getRestrictions().size() > 0) {
						if (g.getRestrictions().contains(event.getItem().getType().getTypeId())) {
							event.setCancelled(true);
							new Message(MessageType.ITEM_RESTRICTED, p, GuildsBasic);
						}
					}
				}
			}
		    
		}
	    
		return;
		
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerItemHeldEvent(PlayerItemHeldEvent event) {

		if (!GuildsBasic.getEnabled(Settings.ENABLE_RESTRICTIONS)) {
			return;
		}
		
		Player p = event.getPlayer();
	    Guild g = GuildsBasic.getPlayerGuild(p);
	    if (g != null) {
	    	World w = p.getWorld();
			Biome b = p.getLocation().getBlock().getBiome();
			if (g.getWorlds().contains(w) && g.getBiomes().contains(b)) {
				if (g.getRestrictions().size() > 0) {
					ItemStack newSlot = p.getInventory().getItem(event.getNewSlot());
					if (newSlot != null) {
						if (g.getRestrictions().contains(newSlot.getTypeId())) {
							new Message(MessageType.ITEM_RESTRICTED, p, GuildsBasic);
							p.getWorld().dropItem(p.getLocation(), newSlot);
							p.getInventory().setItem(event.getNewSlot(), new ItemStack(0));
							p.setItemInHand(new ItemStack(0));
						}
					}
				}
			}
		}
	    
		return;
		
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerInteract(PlayerInteractEvent event) {
		
		if (!GuildsBasic.getEnabled(Settings.ENABLE_RESTRICTIONS)) {
			return;
		}
		
		Player p = event.getPlayer();
	    Guild g = GuildsBasic.getPlayerGuild(p);
	    if (g != null) {
	    	World w = p.getWorld();
			Biome b = p.getLocation().getBlock().getBiome();
			if (g.getWorlds().contains(w) && g.getBiomes().contains(b)) {
				if (g.getRestrictions().size() > 0) {
					ItemStack inHand = p.getItemInHand();
					if (inHand != null) {
						if (g.getRestrictions().contains(inHand.getTypeId())) {
							new Message(MessageType.ARMOUR_RESTRICTED, p, GuildsBasic);
							p.getWorld().dropItem(p.getLocation(), inHand);
							p.getInventory().setItemInHand(new ItemStack(0));
							p.setItemInHand(new ItemStack(0));
							event.setCancelled(true);
						}
					}
				}
				if (GuildsBasic.getEnabled(Settings.ENABLE_GUILD_PROTECTION_BARRIER)) {
					if (GuildsBasic.getEnabled(Settings.ENABLE_ENEMY_ENTER_PROTECTION_BARRIER)) {
						for (Guild guild : GuildsBasic.GuildsList) {
							if (GuildsBasic.getEnabled(Settings.ENABLE_GUILD_PROTECTION_BARRIER_VOID)) {
								if (guild.getOnline() >= GuildsBasic.getIntSetting(Settings.SET_PROTECTION_BARRIER_VOID)) {
									continue;
								}
							}
							Location base = guild.getBase();
							if (base.getWorld().equals(p.getWorld())) {
								if (base.distance(p.getLocation()) > (double) GuildsBasic.getIntSetting(Settings.SET_PROTECTION_BARRIER)) {
									continue;
								} else {
									event.setCancelled(true);
								}
							}
						}
					}
				}
			}
		}
		
		return;
		
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onEntityShootBowEvent(EntityShootBowEvent event) {
		
		if (event.getEntity() instanceof Player) { 	
			Player p = (Player) event.getEntity();
			Guild g = GuildsBasic.getPlayerGuild(p);
			User u = GuildsBasic.getPlayerUser(p);
		    if (g != null) {
		    	World w = p.getWorld();
				Biome b = p.getLocation().getBlock().getBiome();
				if (g.getWorlds().contains(w) && g.getBiomes().contains(b)) {
					Proficiency FIREARROW = g.getProficiency(ProficiencyType.FIREARROW);
					Proficiency STRAIGHTARROW = g.getProficiency(ProficiencyType.STRAIGHTARROW);
					Arrow arrow = (Arrow) event.getProjectile();
					if (FIREARROW.getActive()) {
						if (FIREARROW.getUseProficiency(u)) arrow.setFireTicks(FIREARROW.getTicks());
					}
					if (STRAIGHTARROW.getActive()) {
						if (STRAIGHTARROW.getUseProficiency(u)) arrow.setVelocity(arrow.getVelocity().multiply(STRAIGHTARROW.getPower()));
					}
				}
			}
		}
		
		return;
		
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		
		if (event.getRightClicked() instanceof LivingEntity) {
			Player p = event.getPlayer();
		    Guild g = GuildsBasic.getPlayerGuild(p);
		    User u = GuildsBasic.getPlayerUser(p);
		    if (g != null) {
		    	World w = p.getWorld();
				Biome b = p.getLocation().getBlock().getBiome();
				if (g.getWorlds().contains(w) && g.getBiomes().contains(b)) {
					Proficiency KNOCKBACK = g.getProficiency(ProficiencyType.KNOCKBACK);
					if (KNOCKBACK.getActive()) {
						if (KNOCKBACK.getUseProficiency(u)) {
							event.getRightClicked().setVelocity(event.getRightClicked().getVelocity().add(event.getRightClicked().getLocation().toVector().subtract(event.getPlayer().getLocation().toVector()).normalize().multiply(KNOCKBACK.getPower())));
							event.getRightClicked().getWorld().playEffect(event.getRightClicked().getLocation(), Effect.SMOKE, 25);
						}
					}
				}
			}
		}
		
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onEntityDamage(EntityDamageEvent event) {

		if (event instanceof EntityDamageByEntityEvent) {
			return;
		}
		
		if (event.getEntity() instanceof Player) {
			Player p = (Player) event.getEntity();
			Guild g = GuildsBasic.getPlayerGuild(p);
			double damage = (double) event.getDamage();
			if (g != null) {
				if (GuildsBasic.getIntSetting(Settings.SET_BASE_TP_DELAY) > 0) {
					if (GuildsBasic.BaseDelay.containsKey(p)) {
						Bukkit.getScheduler().cancelTask(GuildsBasic.BaseDelay.get(p));
						GuildsBasic.BaseDelay.remove(p);
					}
				}
				World w = p.getWorld();
				Biome b = p.getLocation().getBlock().getBiome();
				if (g.getWorlds().contains(w) && g.getBiomes().contains(b)) {
					if (event.getCause().equals(DamageCause.FALL)) {
						Proficiency FLIGHT = g.getProficiency(ProficiencyType.FLIGHT);
						Proficiency HIGHJUMP = g.getProficiency(ProficiencyType.HIGHJUMP);
						if (FLIGHT.getActive()) {
							if (p.isSneaking()) {
								if (p.isFlying()) {
									event.setCancelled(true);
									return;
								}
							}
						} else if (HIGHJUMP.getActive()) {
							if (!p.isSneaking()) {
								if (p.hasPotionEffect(PotionEffectType.JUMP)) {
									event.setCancelled(true);
									return;
								}
							}
						}
					}
					damage = traitDamage(damage, event.getCause(), g, false);
				}
			}
			damage = Math.ceil(damage);
			event.setDamage((int) damage);
			return;
		} else {
			return;
		}
		
	}
		
	private double traitDamage(double d, DamageCause dc, Guild g, boolean player) {
	
		if (g != null) {
			
			double value = 0;
			
			switch (dc) {
				case CONTACT:
					Proficiency DEFENSE_CONTACT = g.getProficiency(ProficiencyType.DEFENSE_CONTACT);
					if (!DEFENSE_CONTACT.getActive()) return d;
					value = DEFENSE_CONTACT.getPower();
				break;
				case ENTITY_ATTACK:
					Proficiency DEFENSE_MELEE = g.getProficiency(ProficiencyType.DEFENSE_MELEE);
					if (!DEFENSE_MELEE.getActive()) return d;
					value = DEFENSE_MELEE.getPower();
				break;
				case PROJECTILE:
					Proficiency DEFENSE_PROJECTILE = g.getProficiency(ProficiencyType.DEFENSE_PROJECTILE);
					if (!DEFENSE_PROJECTILE.getActive()) return d;
					value = DEFENSE_PROJECTILE.getPower();
				break;
				case SUFFOCATION:
					Proficiency DEFENSE_SUFFOCATION = g.getProficiency(ProficiencyType.DEFENSE_SUFFOCATION);
					if (!DEFENSE_SUFFOCATION.getActive()) return d;
					value = DEFENSE_SUFFOCATION.getPower();
				break;
				case FALL:
					Proficiency DEFENSE_FALL = g.getProficiency(ProficiencyType.DEFENSE_FALL);
					if (!DEFENSE_FALL.getActive()) return d;
					value = DEFENSE_FALL.getPower();
				break;
				case FIRE:
					Proficiency DEFENSE_FIRE = g.getProficiency(ProficiencyType.DEFENSE_FIRE);
					if (!DEFENSE_FIRE.getActive()) return d;
					value = DEFENSE_FIRE.getPower();
				break;
				case FIRE_TICK:
					Proficiency DEFENSE_FIRE_2 = g.getProficiency(ProficiencyType.DEFENSE_FIRE);
					if (!DEFENSE_FIRE_2.getActive()) return d;
					value = DEFENSE_FIRE_2.getPower();
				break;
				case LAVA:
					Proficiency DEFENSE_LAVA = g.getProficiency(ProficiencyType.DEFENSE_LAVA);
					if (!DEFENSE_LAVA.getActive()) return d;
					value = DEFENSE_LAVA.getPower();
				break;
				case DROWNING:
					Proficiency DEFENSE_DROWNING = g.getProficiency(ProficiencyType.DEFENSE_DROWNING);
					if (!DEFENSE_DROWNING.getActive()) return d;
					value = DEFENSE_DROWNING.getPower();
				break;
				case BLOCK_EXPLOSION:
					Proficiency DEFENSE_EXPLOSION = g.getProficiency(ProficiencyType.DEFENSE_EXPLOSION);
					if (!DEFENSE_EXPLOSION.getActive()) return d;
					value = DEFENSE_EXPLOSION.getPower();
				break;
				case ENTITY_EXPLOSION:
					Proficiency DEFENSE_EXPLOSION_2 = g.getProficiency(ProficiencyType.DEFENSE_EXPLOSION);
					if (!DEFENSE_EXPLOSION_2.getActive()) return d;
					value = DEFENSE_EXPLOSION_2.getPower();
				break;
				case LIGHTNING:
					Proficiency DEFENSE_LIGHTNING = g.getProficiency(ProficiencyType.DEFENSE_LIGHTNING);
					if (!DEFENSE_LIGHTNING.getActive()) return d;
					value = DEFENSE_LIGHTNING.getPower();
				break;
				case STARVATION:
					Proficiency DEFENSE_STARVATION = g.getProficiency(ProficiencyType.DEFENSE_STARVATION);
					if (!DEFENSE_STARVATION.getActive()) return d;
					value = DEFENSE_STARVATION.getPower();
				break;
				case POISON:
					Proficiency DEFENSE_POISON = g.getProficiency(ProficiencyType.DEFENSE_POISON);
					if (!DEFENSE_POISON.getActive()) return d;
					value = DEFENSE_POISON.getPower();
				break;
				case MAGIC:
					Proficiency DEFENSE_MAGIC = g.getProficiency(ProficiencyType.DEFENSE_MAGIC);
					if (!DEFENSE_MAGIC.getActive()) return d;
					value = DEFENSE_MAGIC.getPower();
				break;
				case WITHER:
					Proficiency DEFENSE_WITHER = g.getProficiency(ProficiencyType.DEFENSE_WITHER);
					if (!DEFENSE_WITHER.getActive()) return d;
					value = DEFENSE_WITHER.getPower();
				break;
			}
			
			if (value == 0) {
				d = 0;
			} else if (value == 1) {
				d = d * 1;
				d = Math.ceil(d);
			} else {
				d = d * (value);
				d = Math.ceil(d);
			}
			
		}
	
		return d;
		
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		
		double damage = (double) event.getDamage();
		
		if (event.getDamager() instanceof Arrow) {
			Player defender = null;
			Guild defenderGuild = null;
			if (event.getEntity() instanceof Player) {
				defender = (Player) event.getEntity();
				defenderGuild = GuildsBasic.getPlayerGuild(defender);
				if (GuildsBasic.getIntSetting(Settings.SET_BASE_TP_DELAY) > 0) {
					if (GuildsBasic.BaseDelay.containsKey(defender)) {
						Bukkit.getScheduler().cancelTask(GuildsBasic.BaseDelay.get(defender));
						GuildsBasic.BaseDelay.remove(defender);
					}
				}
			}
			Arrow arrow = (Arrow) event.getDamager();
			if (arrow.getShooter() instanceof Player) {
				Player damager = (Player) arrow.getShooter();
				Guild damagerGuild = GuildsBasic.getPlayerGuild(damager);
				if (damagerGuild != null) {
					World w = event.getEntity().getWorld();
					Biome b = damager.getLocation().getBlock().getBiome();
					if (damagerGuild.getWorlds().contains(w) && damagerGuild.getBiomes().contains(b)) {
						Proficiency ATTACK_PROJECTILE = damagerGuild.getProficiency(ProficiencyType.ATTACK_PROJECTILE);
						if (ATTACK_PROJECTILE.getActive()) {
							if (ATTACK_PROJECTILE.getPower() == 0) {
								event.setCancelled(true);
								return;
							} else if (ATTACK_PROJECTILE.getPower() != 1) {
								damage = damage * (ATTACK_PROJECTILE.getPower());
							}
						}
						if (defender != null) {
							if (defenderGuild != null) {
								if (!GuildsBasic.getEnabled(Settings.ENABLE_FRIENDLY_FIRE_PVP)) {
									if (damagerGuild.equals(defenderGuild)) {
										event.setCancelled(true);
										new Message(MessageType.NO_FRIENDLY_FIRE, damager, GuildsBasic);
										return;
									}
								}
								damage = traitDamage(damage, event.getCause(), defenderGuild, true);
							}
							Proficiency PEACEKEEPER = damagerGuild.getProficiency(ProficiencyType.PEACEKEEPER);
							Proficiency INVISIBLE = damagerGuild.getProficiency(ProficiencyType.INVISIBLE);
							Proficiency POISONARROW = damagerGuild.getProficiency(ProficiencyType.POISONARROW);
							Proficiency BLINDNESSARROW = damagerGuild.getProficiency(ProficiencyType.BLINDNESSARROW);
							Proficiency CONFUSIONARROW = damagerGuild.getProficiency(ProficiencyType.CONFUSIONARROW);
							Proficiency MOBARROW = damagerGuild.getProficiency(ProficiencyType.MOBARROW);
							if (PEACEKEEPER.getActive()) {
								new Message(MessageType.PEACEKEEPER, damager, GuildsBasic);
								event.setCancelled(true);
								return;
							}
							if (INVISIBLE.getActive()) {
								if (damager.isSneaking()) {
									if (INVISIBLE.getPower() == 0) {
										event.setCancelled(true);
										return;
									} else {
										damage = damage * (INVISIBLE.getPower());
									}
								}
							}
							if (POISONARROW.getActive()) {
								defender.addPotionEffect(new PotionEffect(PotionEffectType.POISON, POISONARROW.getTicks(), 1));
							}
							if (BLINDNESSARROW.getActive()) {
								defender.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, BLINDNESSARROW.getTicks(), 1));
							}
							if (CONFUSIONARROW.getActive()) {
								defender.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, CONFUSIONARROW.getTicks(), 1));
							}
							if (MOBARROW.getActive()) {
								for (Entity e : event.getEntity().getNearbyEntities(20.0d, 20.0d, 20.0d)) {
									if (e instanceof Creature) ((Creature) e).setTarget((LivingEntity) event.getEntity());
								}
							}
						}
						Proficiency FIREARROW = damagerGuild.getProficiency(ProficiencyType.FIREARROW);
						if (FIREARROW.getActive()) {
							event.getEntity().setFireTicks(FIREARROW.getTicks());
						}
					}
				}
			} else {
				if (defenderGuild != null) {
					damage = traitDamage(damage, event.getCause(), defenderGuild, false);
				}
			}
		} else {
			Player defender = null;
			Guild defenderGuild = null;
			Player damager = null;
			Guild damagerGuild = null;
			if (event.getEntity() instanceof Player) {
				defender = (Player) event.getEntity();
				defenderGuild = GuildsBasic.getPlayerGuild(defender);
			}
			if (event.getDamager() instanceof Player) {
				damager = (Player) event.getDamager();
				damagerGuild = GuildsBasic.getPlayerGuild(damager);
			}
			if (defender == null && damager == null) {
				return;
			}
			if (defenderGuild != null) {
				World w = event.getEntity().getWorld();
				Biome b = event.getEntity().getLocation().getBlock().getBiome();
				if (defenderGuild.getWorlds().contains(w) && defenderGuild.getBiomes().contains(b)) {
					if (damagerGuild != null) {
						if (!GuildsBasic.getEnabled(Settings.ENABLE_FRIENDLY_FIRE_PVP)) {
							if (damagerGuild.equals(defenderGuild)) {
								event.setCancelled(true);
								new Message(MessageType.NO_FRIENDLY_FIRE, damager, GuildsBasic);
								return;
							}
						}
						damage = traitDamage(damage, event.getCause(), defenderGuild, true);
					} else {
						damage = traitDamage(damage, event.getCause(), defenderGuild, false);
					}
					Proficiency REFLECT = defenderGuild.getProficiency(ProficiencyType.REFLECT);
					if (REFLECT.getActive()) {
						if (event.getEntity() instanceof LivingEntity) {
							LivingEntity le = (LivingEntity) event.getEntity();
							le.damage((int) REFLECT.getPower());
							if (damager != null) {
								new Message(MessageType.REFLECTED, damager, GuildsBasic);
							}
						}
					}
					Proficiency PEACEKEEPER = defenderGuild.getProficiency(ProficiencyType.PEACEKEEPER);
					if (PEACEKEEPER.getActive()) {
						if (damager != null) {
							new Message(MessageType.PEACEKEEPER, defender, GuildsBasic);
							event.setCancelled(true);
							return;
						}
					}
				}
			}
			if (damagerGuild != null) {
				World w = event.getEntity().getWorld();
				Biome b = damager.getLocation().getBlock().getBiome();
				if (damagerGuild.getWorlds().contains(w) && damagerGuild.getBiomes().contains(b)) {
					if (damagerGuild.getRestrictions().size() > 0) {
						ItemStack inHand = damager.getItemInHand();
						if (inHand != null) {
							if (damagerGuild.getRestrictions().contains(inHand.getTypeId())) {
								new Message(MessageType.ARMOUR_RESTRICTED, damager, GuildsBasic);
								event.setCancelled(true);
								return;
							}
						}
					}
					Proficiency PEACEKEEPER = damagerGuild.getProficiency(ProficiencyType.PEACEKEEPER);
					if (PEACEKEEPER.getActive()) {
						if (defender != null) {
							new Message(MessageType.PEACEKEEPER, damager, GuildsBasic);
							event.setCancelled(true);
							return;
						}
					}
					Proficiency ATTACK_MELEE = damagerGuild.getProficiency(ProficiencyType.ATTACK_MELEE);
					if (ATTACK_MELEE.getActive()) {
						if (ATTACK_MELEE.getPower() == 0) {
							damage = (double) 0;
						} else if (ATTACK_MELEE.getPower() != 1) {
							damage = damage * (ATTACK_MELEE.getPower());
						}
					}
					Proficiency FIREBLADE = damagerGuild.getProficiency(ProficiencyType.FIREBLADE);
					if (FIREBLADE.getActive()) {
						if (FIREBLADE.getUseProficiency(GuildsBasic.getPlayerUser(damager))) {
							if (GuildsBasic.isBlade(damager.getItemInHand().getTypeId())) {
								event.getEntity().setFireTicks(FIREBLADE.getTicks());
							}
						}
					}
					Proficiency FIREPUNCH = damagerGuild.getProficiency(ProficiencyType.FIREPUNCH);
					if (FIREPUNCH.getActive()) {
						if (FIREPUNCH.getUseProficiency(GuildsBasic.getPlayerUser(damager))) {
							if (damager.getItemInHand().getType().equals(Material.AIR)) {
								event.getEntity().setFireTicks(FIREPUNCH.getTicks());
							}
						}
					}
					Proficiency POISONBLADE = damagerGuild.getProficiency(ProficiencyType.POISONBLADE);
					if (POISONBLADE.getActive()) {
						if (POISONBLADE.getUseProficiency(GuildsBasic.getPlayerUser(damager))) {
							if (GuildsBasic.isBlade(damager.getItemInHand().getTypeId())) {
								if (defender != null) {
									defender.addPotionEffect(new PotionEffect(PotionEffectType.POISON, POISONBLADE.getTicks(), 1));
								}
							}
						}
					}
				}
			}
		}
		
		if (damage == 0) {
			if (GuildsBasic.getEnabled(Settings.ENABLE_DAMAGE_ANIMATION_ON_ZERO)) {
				event.setDamage(0);
				return;
			} else {
				event.setDamage(0);
				event.setCancelled(true);
				return;
			}
		}
		
		damage = Math.ceil(damage);
		event.setDamage((int) damage);
		
		return;
		
	}
	
}
