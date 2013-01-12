package me.avocardo.guilds.listeners;

import java.util.ArrayList;
import java.util.List;

import me.avocardo.guilds.Guild;
import me.avocardo.guilds.GuildsBasic;
import me.avocardo.guilds.utilities.Attribute;
import me.avocardo.guilds.utilities.AttributeType;
import me.avocardo.guilds.utilities.Proficiency;
import me.avocardo.guilds.utilities.ProficiencyType;
import me.avocardo.guilds.utilities.Scheduler;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
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
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerListener implements Listener {

	private GuildsBasic plugin;

	public PlayerListener(GuildsBasic plugin) {
		
		this.plugin = plugin;
        
    }
 	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player p = event.getPlayer();
		if (plugin.setBaseDelay > 0) {
			if (plugin.BaseDelay.containsKey(p)) {
				Bukkit.getScheduler().cancelTask(plugin.BaseDelay.get(p));
				plugin.BaseDelay.remove(p);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerKick(PlayerKickEvent event) {
		Player p = event.getPlayer();
		if (plugin.setBaseDelay > 0) {
			if (plugin.BaseDelay.containsKey(p)) {
				Bukkit.getScheduler().cancelTask(plugin.BaseDelay.get(p));
				plugin.BaseDelay.remove(p);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onMobTarget(EntityTargetEvent event) {

		if (event.isCancelled()) {
            return;
        }
		
		if (event.getTarget() instanceof Player) {
			if (event.getEntity() instanceof LivingEntity) {
				Player p = (Player) event.getTarget();
				Guild g = plugin.getPlayerGuild(p);
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

		if (!plugin.allowGuildRestrictions) {
			return;
		}
		
		if (event.getView().getType().equals(InventoryType.CRAFTING)) {
		
			Player p = (Player) event.getPlayer();
			Guild g = plugin.getPlayerGuild(p);
			
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
								plugin.msg(39, p, "", "");
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
								plugin.msg(11, p, "", "");
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
								plugin.msg(11, p, "", "");
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
								plugin.msg(11, p, "", "");
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
								plugin.msg(11, p, "", "");
							}
						}
					}
				}
			}
			
		}

		return;
		
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerHealth(EntityRegainHealthEvent event) {
		
		if (event.isCancelled()) {
            return;
        }
		
		if (event.getEntity() instanceof Player) {
			Player p = (Player) event.getEntity();
			Guild g = plugin.getPlayerGuild(p);
			World w = p.getWorld();
			Biome b = p.getLocation().getBlock().getBiome();
			if (g != null) {
				if (g.getWorlds().contains(w) && g.getBiomes().contains(b)) {
					Proficiency RECOVERHEALTH = g.getProficiency(ProficiencyType.RECOVERHEALTH);
					if (RECOVERHEALTH.getActive()) {
						return;
					} else {
						event.setCancelled(true);
						return;
					}
				}
			}
		}
		
		return;
		
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onExpChange(PlayerExpChangeEvent event) {

		Player p = event.getPlayer();
		
		Guild g = plugin.getPlayerGuild(p);
		
		if (g != null) {
			if (g.getWorlds().contains(p.getWorld()) && g.getBiomes().contains(p.getLocation().getBlock().getBiome())) {
				Attribute XP_MULTIPLIER = g.getAttribute(AttributeType.XP_MULTIPLIER);
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
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerRespawn(PlayerRespawnEvent event) {

		Player p = event.getPlayer();
		
		if (plugin.Inventory.containsKey(p)) {
			for (ItemStack i : plugin.Inventory.get(p)) {
				p.getInventory().addItem(i);
			}
			plugin.Inventory.remove(p);
		}
		
		if (plugin.setBaseDelay > 0) {
			if (plugin.BaseDelay.containsKey(p)) {
				Bukkit.getScheduler().cancelTask(plugin.BaseDelay.get(p));
				plugin.BaseDelay.remove(p);
			}
		}
		
		if (plugin.allowBaseOnDeath) {
		
			Guild g = plugin.getPlayerGuild(p);
		
			if (g != null) {
				event.setRespawnLocation(g.getBase());
			}
			
		}

	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerDeath(PlayerDeathEvent event) {
		
		if (event.getEntity() instanceof Player) {
			Player p = event.getEntity();
			if (plugin.setBaseDelay > 0) {
				if (plugin.BaseDelay.containsKey(p)) {
					Bukkit.getScheduler().cancelTask(plugin.BaseDelay.get(p));
					plugin.BaseDelay.remove(p);
				}
			}
			Guild g = plugin.getPlayerGuild(p);
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
						plugin.Inventory.put(p, event.getDrops());
						event.getDrops().clear();
					}
				}
			}
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
		}
		
		return;
		
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerMove(PlayerMoveEvent event) {
				
		if (event.isCancelled()) {
            return;
        }
		
		Player p = event.getPlayer();
		Guild g = plugin.getPlayerGuild(p);
		
		if (g != null) {
			if (plugin.TasksInvisible.containsKey(p)) {
				if (!p.isSneaking()) {
					Bukkit.getScheduler().cancelTask(plugin.TasksInvisible.get(p));
					plugin.TasksInvisible.remove(p);
					plugin.showPlayer(p);
				}
			}
			if (!plugin.allowOtherGuildWithinProtection) {
				for (Guild guild : plugin.GuildsList) {
					if (!guild.equals(g)) {
						if (event.getFrom().distance(guild.getBase()) > event.getTo().distance(guild.getBase())) {
							if (p.getLocation().distance(guild.getBase()) <= (plugin.setGuildProtectionBarrier + 1)) {
								event.setCancelled(true);
								plugin.msg(40, p, "", guild.getName());
								return;
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
				Proficiency INVISIBLE = g.getProficiency(ProficiencyType.INVISIBLE);
				if (FLIGHT.getActive()) {
					if (p.isSneaking()) p.setVelocity(p.getLocation().getDirection().multiply(FLIGHT.getPower()));
				}
				if (INVISIBLE.getActive()) {
					if (p.isSneaking()) {
						plugin.hidePlayer(p);
					}
				}
				if (FLIGHT.getActive()) {
					if (p.isSneaking()) p.setVelocity(p.getLocation().getDirection().multiply(FLIGHT.getPower()));
				}
				if (HIGHJUMP.getActive()) {
					p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 10, (int) HIGHJUMP.getPower()));
				}
				if (SUNLIGHT.getActive()) {
					if (time > 0 && time < 13000) {
						if ((p.getWorld().getBlockAt(p.getLocation()).getLightFromSky()) > 8) {
							if (!plugin.TasksSun.containsKey(p)) {
								plugin.TasksSun.put(p, new Scheduler(plugin).sun(p));
							}
						} else {
							if (plugin.TasksSun.containsKey(p)) {
								Bukkit.getScheduler().cancelTask(plugin.TasksSun.get(p));
								plugin.TasksSun.remove(p);
							}
						}
					} else {
						if (plugin.TasksSun.containsKey(p)) {
							Bukkit.getScheduler().cancelTask(plugin.TasksSun.get(p));
							plugin.TasksSun.remove(p);
						}
					}
				}
				if (MOONLIGHT.getActive()) {
					if (time > 13000 && time < 23000) {
						if ((p.getWorld().getBlockAt(p.getLocation()).getLightFromSky()) > 8) {
							if (!plugin.TasksMoon.containsKey(p)) {
								plugin.TasksMoon.put(p, new Scheduler(plugin).moon(p));
							}
						} else {
							if (plugin.TasksMoon.containsKey(p)) {
								Bukkit.getScheduler().cancelTask(plugin.TasksMoon.get(p));
								plugin.TasksMoon.remove(p);
							}
						}
					} else {
						if (plugin.TasksMoon.containsKey(p)) {
							Bukkit.getScheduler().cancelTask(plugin.TasksMoon.get(p));
							plugin.TasksMoon.remove(p);
						}
					}
				}
				if (STORM.getActive()) {
					if ((p.getWorld().getBlockAt(p.getLocation()).getLightFromSky()) > 8) {
						if (p.getWorld().hasStorm() == true) {
							if (!plugin.TasksStorm.containsKey(p)) {
								plugin.TasksStorm.put(p, new Scheduler(plugin).storm(p));
							}
						} else {
							if (plugin.TasksStorm.containsKey(p)) {
								Bukkit.getScheduler().cancelTask(plugin.TasksStorm.get(p));
								plugin.TasksStorm.remove(p);
							}
						}
					} else {
						if (plugin.TasksStorm.containsKey(p)) {
							Bukkit.getScheduler().cancelTask(plugin.TasksStorm.get(p));
							plugin.TasksStorm.remove(p);
						}
					}
				}
				if (WATERDAMAGE.getActive()) {
					if (p.getLocation().getBlock().getTypeId() == 8 || p.getLocation().getBlock().getTypeId() == 9) {
						if (!plugin.TasksWater.containsKey(p)) {
							plugin.TasksWater.put(p, new Scheduler(plugin).water(p));
						}
					} else {
						if (plugin.TasksWater.containsKey(p)) {
							Bukkit.getScheduler().cancelTask(plugin.TasksWater.get(p));
							plugin.TasksWater.remove(p);
						}
					}
				}
				if (LANDDAMAGE.getActive()) {
					
					List<Integer> blocks = new ArrayList<Integer>();
					
					blocks.add(p.getLocation().getBlock().getTypeId());
					blocks.add(p.getLocation().add(0, 1, 0).getBlock().getTypeId());
					blocks.add(p.getLocation().subtract(0, 1, 0).getBlock().getTypeId());
					
					if (blocks.contains(8) == false && blocks.contains(9) == false) {
						if (!plugin.TasksLand.containsKey(p)) {
							plugin.TasksLand.put(p, new Scheduler(plugin).land(p));
						}
					} else {
						if (plugin.TasksLand.containsKey(p)) {
							Bukkit.getScheduler().cancelTask(plugin.TasksLand.get(p));
							plugin.TasksLand.remove(p);
						}
					}
				}
			}
		} else {
			if (!plugin.allowNoGuild) {
				if (event.getFrom().getX() != event.getTo().getX() || event.getFrom().getZ() != event.getTo().getZ()) {
					event.setTo(event.getFrom());
					plugin.msg(12, p, "", "");
					plugin.msg(13, p, "", "");
					return;
				}
			}
		}
		
		return;
		
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onProjectileHit(ProjectileHitEvent event) {

		Entity entity = event.getEntity();
		
		if (entity instanceof Arrow) {
			Arrow arrow = (Arrow) entity;
			Entity shooter = arrow.getShooter();
			if (shooter instanceof Player) {
		        Player p = (Player) shooter;
		       	Guild g = plugin.getPlayerGuild(p);
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
		        			if (EXPLOSIVEARROW.getUseProficiency(p)) w.createExplosion(loc, (int) EXPLOSIVEARROW.getPower());                		
	                	}
		        		if (ZOMBIEARROW.getActive()) {
		        			if (ZOMBIEARROW.getUseProficiency(p)) w.spawnEntity(loc, EntityType.ZOMBIE);
	                	}
	                	if (LIGHTNINGARROW.getActive()) {
	                		if (LIGHTNINGARROW.getUseProficiency(p)) w.strikeLightning(loc);
	                	}
	                	if (TPARROW.getActive()) {
	                		if (TPARROW.getUseProficiency(p)) p.teleport(loc);
	                	}
		        	}
		        }
			}
		}
		
		return;
		
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		
		if (event.isCancelled()) {
            return;
        }
		
		if (!plugin.allowGuildRestrictions) {
			return;
		}
		
		if (!plugin.allowPickUpRestrictions) {
		
			Player p = event.getPlayer();
		    Guild g = plugin.getPlayerGuild(p);
		    if (g != null) {
		    	World w = p.getWorld();
				Biome b = p.getLocation().getBlock().getBiome();
				if (g.getWorlds().contains(w) && g.getBiomes().contains(b)) {
					if (g.getRestrictions().size() > 0) {
						if (g.getRestrictions().contains(event.getItem().getType().getTypeId())) {
							event.setCancelled(true);
							plugin.msg(39, p, "", "");
						}
					}
				}
			}
		    
		}
	    
		return;
		
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerItemHeldEvent(PlayerItemHeldEvent event) {

		if (!plugin.allowGuildRestrictions) {
			return;
		}
		
		Player p = event.getPlayer();
	    Guild g = plugin.getPlayerGuild(p);
	    if (g != null) {
	    	World w = p.getWorld();
			Biome b = p.getLocation().getBlock().getBiome();
			if (g.getWorlds().contains(w) && g.getBiomes().contains(b)) {
				if (g.getRestrictions().size() > 0) {
					ItemStack newSlot = p.getInventory().getItem(event.getNewSlot());
					if (newSlot != null) {
						if (g.getRestrictions().contains(newSlot.getTypeId())) {
							plugin.msg(39, p, "", "");
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

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerInteract(PlayerInteractEvent event) {
		
		if (event.isCancelled()) {
            return;
        }
		
		if (!plugin.allowGuildRestrictions) {
			return;
		}
		
		Player p = event.getPlayer();
	    Guild g = plugin.getPlayerGuild(p);
	    if (g != null) {
	    	World w = p.getWorld();
			Biome b = p.getLocation().getBlock().getBiome();
			if (g.getWorlds().contains(w) && g.getBiomes().contains(b)) {
				if (g.getRestrictions().size() > 0) {
					ItemStack inHand = p.getItemInHand();
					if (inHand != null) {
						if (g.getRestrictions().contains(inHand.getTypeId())) {
							plugin.msg(11, p, "", "");
							p.getWorld().dropItem(p.getLocation(), inHand);
							p.getInventory().setItemInHand(new ItemStack(0));
							p.setItemInHand(new ItemStack(0));
							event.setCancelled(true);
						}
					}
				}
			}
		}
		
		return;
		
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityShootBowEvent(EntityShootBowEvent event) {
		
		if (event.isCancelled()) {
            return;
        }
		
		if (event.getEntity() instanceof Player) { 	
			Player p = (Player) event.getEntity();
			Guild g = plugin.getPlayerGuild(p);
		    if (g != null) {
		    	World w = p.getWorld();
				Biome b = p.getLocation().getBlock().getBiome();
				if (g.getWorlds().contains(w) && g.getBiomes().contains(b)) {
					Proficiency FIREARROW = g.getProficiency(ProficiencyType.FIREARROW);
					Proficiency STRAIGHTARROW = g.getProficiency(ProficiencyType.STRAIGHTARROW);
					Arrow arrow = (Arrow) event.getProjectile();
					if (FIREARROW.getActive()) {
						if (FIREARROW.getUseProficiency(p)) arrow.setFireTicks(FIREARROW.getTicks());
					}
					if (STRAIGHTARROW.getActive()) {
						if (STRAIGHTARROW.getUseProficiency(p)) arrow.setVelocity(arrow.getVelocity().multiply(STRAIGHTARROW.getPower()));
					}
				}
			}
		}
		
		return;
		
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {

		if (event.isCancelled()) {
            return;
        }
		
		if (event.getRightClicked() instanceof LivingEntity) {
			Player p = event.getPlayer();
		    Guild g = plugin.getPlayerGuild(p);
		    if (g != null) {
		    	World w = p.getWorld();
				Biome b = p.getLocation().getBlock().getBiome();
				if (g.getWorlds().contains(w) && g.getBiomes().contains(b)) {
					Proficiency KNOCKBACK = g.getProficiency(ProficiencyType.KNOCKBACK);
					if (KNOCKBACK.getActive()) {
						if (KNOCKBACK.getUseProficiency(p)) {
							event.getRightClicked().setVelocity(event.getRightClicked().getVelocity().add(event.getRightClicked().getLocation().toVector().subtract(event.getPlayer().getLocation().toVector()).normalize().multiply(KNOCKBACK.getPower())));
							event.getRightClicked().getWorld().playEffect(event.getRightClicked().getLocation(), Effect.SMOKE, 25);
						}
					}
				}
			}
		}
		
		return;
		
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityDamage(EntityDamageEvent event) {

		if (event instanceof EntityDamageByEntityEvent) {
			return;
		}
		
		if (event.isCancelled()) {
            return;
        }
		
		if (event.getEntity() instanceof Player) {
			Player p = (Player) event.getEntity();
			Guild g = plugin.getPlayerGuild(p);
			double damage = (double) event.getDamage();
			if (g != null) {
				if (plugin.setBaseDelay > 0) {
					if (plugin.BaseDelay.containsKey(p)) {
						Bukkit.getScheduler().cancelTask(plugin.BaseDelay.get(p));
						plugin.BaseDelay.remove(p);
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
					Attribute KNOCKBACK = g.getAttribute(AttributeType.DEFENSE_CONTACT);
					value = KNOCKBACK.getPower();
				break;
				case ENTITY_ATTACK:
					Attribute DEFENSE_MELEE = g.getAttribute(AttributeType.DEFENSE_MELEE);
					value = DEFENSE_MELEE.getPower();
				break;
				case PROJECTILE:
					Attribute DEFENSE_PROJECTILE = g.getAttribute(AttributeType.DEFENSE_PROJECTILE);
					value = DEFENSE_PROJECTILE.getPower();
				break;
				case SUFFOCATION:
					Attribute DEFENSE_SUFFOCATION = g.getAttribute(AttributeType.DEFENSE_SUFFOCATION);
					value = DEFENSE_SUFFOCATION.getPower();
				break;
				case FALL:
					Attribute DEFENSE_FALL = g.getAttribute(AttributeType.DEFENSE_FALL);
					value = DEFENSE_FALL.getPower();
				break;
				case FIRE:
					Attribute DEFENSE_FIRE = g.getAttribute(AttributeType.DEFENSE_FIRE);
					value = DEFENSE_FIRE.getPower();
				break;
				case FIRE_TICK:
					Attribute DEFENSE_FIRE_2 = g.getAttribute(AttributeType.DEFENSE_FIRE);
					value = DEFENSE_FIRE_2.getPower();
				break;
				case LAVA:
					Attribute DEFENSE_LAVA = g.getAttribute(AttributeType.DEFENSE_LAVA);
					value = DEFENSE_LAVA.getPower();
				break;
				case DROWNING:
					Attribute DEFENSE_DROWNING = g.getAttribute(AttributeType.DEFENSE_DROWNING);
					value = DEFENSE_DROWNING.getPower();
				break;
				case BLOCK_EXPLOSION:
					Attribute DEFENSE_EXPLOSION = g.getAttribute(AttributeType.DEFENSE_EXPLOSION);
					value = DEFENSE_EXPLOSION.getPower();
				break;
				case ENTITY_EXPLOSION:
					Attribute DEFENSE_EXPLOSION_2 = g.getAttribute(AttributeType.DEFENSE_EXPLOSION);
					value = DEFENSE_EXPLOSION_2.getPower();
				break;
				case LIGHTNING:
					Attribute DEFENSE_LIGHTNING = g.getAttribute(AttributeType.DEFENSE_LIGHTNING);
					value = DEFENSE_LIGHTNING.getPower();
				break;
				case STARVATION:
					Attribute DEFENSE_STARVATION = g.getAttribute(AttributeType.DEFENSE_STARVATION);
					value = DEFENSE_STARVATION.getPower();
				break;
				case POISON:
					Attribute DEFENSE_POISON = g.getAttribute(AttributeType.DEFENSE_POISON);
					value = DEFENSE_POISON.getPower();
				break;
				case MAGIC:
					Attribute DEFENSE_MAGIC = g.getAttribute(AttributeType.DEFENSE_MAGIC);
					value = DEFENSE_MAGIC.getPower();
				break;
				case WITHER:
					Attribute DEFENSE_WITHER = g.getAttribute(AttributeType.DEFENSE_WITHER);
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
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
				
		if (event.isCancelled()) {
            return;
        }
		
		double damage = (double) event.getDamage();
		
		if (event.getDamager() instanceof Arrow) {
			Player defender = null;
			Guild defenderGuild = null;
			if (event.getEntity() instanceof Player) {
				defender = (Player) event.getEntity();
				defenderGuild = plugin.getPlayerGuild(defender);
				if (plugin.setBaseDelay > 0) {
					if (plugin.BaseDelay.containsKey(defender)) {
						Bukkit.getScheduler().cancelTask(plugin.BaseDelay.get(defender));
						plugin.BaseDelay.remove(defender);
					}
				}
			}
			Arrow arrow = (Arrow) event.getDamager();
			if (arrow.getShooter() instanceof Player) {
				Player damager = (Player) arrow.getShooter();
				Guild damagerGuild = plugin.getPlayerGuild(damager);
				if (damagerGuild != null) {
					World w = event.getEntity().getWorld();
					Biome b = damager.getLocation().getBlock().getBiome();
					if (damagerGuild.getWorlds().contains(w) && damagerGuild.getBiomes().contains(b)) {
						Attribute ATTACK_PROJECTILE = damagerGuild.getAttribute(AttributeType.ATTACK_PROJECTILE);
						if (ATTACK_PROJECTILE.getPower() == 0) {
							event.setCancelled(true);
							return;
						} else if (ATTACK_PROJECTILE.getPower() != 1) {
							damage = damage * (ATTACK_PROJECTILE.getPower());
						}
						if (defender != null) {
							if (defenderGuild != null) {
								if (plugin.allowGuildPVP == false) {
									if (damagerGuild.equals(defenderGuild)) {
										event.setCancelled(true);
										plugin.msg(14, damager, "", "");
										return;
									}
								}
								damage = traitDamage(damage, event.getCause(), defenderGuild, true);
							}
							Proficiency PEACEKEEPER = damagerGuild.getProficiency(ProficiencyType.PEACEKEEPER);
							Proficiency POISONARROW = damagerGuild.getProficiency(ProficiencyType.POISONARROW);
							Proficiency BLINDNESSARROW = damagerGuild.getProficiency(ProficiencyType.BLINDNESSARROW);
							Proficiency CONFUSIONARROW = damagerGuild.getProficiency(ProficiencyType.CONFUSIONARROW);
							Proficiency MOBARROW = damagerGuild.getProficiency(ProficiencyType.MOBARROW);
							if (PEACEKEEPER.getActive()) {
								plugin.msg(15, damager, "", "");
								event.setCancelled(true);
								return;
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
				defenderGuild = plugin.getPlayerGuild(defender);
			}
			if (event.getDamager() instanceof Player) {
				damager = (Player) event.getDamager();
				damagerGuild = plugin.getPlayerGuild(damager);
			}
			if (defender == null && damager == null) {
				return;
			}
			if (defenderGuild != null) {
				World w = event.getEntity().getWorld();
				Biome b = event.getEntity().getLocation().getBlock().getBiome();
				if (defenderGuild.getWorlds().contains(w) && defenderGuild.getBiomes().contains(b)) {
					if (damagerGuild != null) {
						if (plugin.allowGuildPVP == false) {
							if (damagerGuild.equals(defenderGuild)) {
								event.setCancelled(true);
								plugin.msg(14, damager, "", "");
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
								plugin.msg(16, damager, "", "");
							}
						}
					}
					Proficiency PEACEKEEPER = defenderGuild.getProficiency(ProficiencyType.PEACEKEEPER);
					if (PEACEKEEPER.getActive()) {
						if (damager != null) {
							plugin.msg(15, defender, "", "");
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
								plugin.msg(11, damager, "", "");
								event.setCancelled(true);
								return;
							}
						}
					}
					Proficiency PEACEKEEPER = damagerGuild.getProficiency(ProficiencyType.PEACEKEEPER);
					if (PEACEKEEPER.getActive()) {
						if (defender != null) {
							plugin.msg(16, damager, "", "");
							event.setCancelled(true);
							return;
						}
					}
					Attribute ATTACK_MELEE = damagerGuild.getAttribute(AttributeType.ATTACK_MELEE);
					if (ATTACK_MELEE.getPower() == 0) {
						damage = (double) 0;
					} else if (ATTACK_MELEE.getPower() != 1) {
						damage = damage * (ATTACK_MELEE.getPower());
					}
					Proficiency FIREBLADE = damagerGuild.getProficiency(ProficiencyType.FIREBLADE);
					if (FIREBLADE.getActive()) {
						if (plugin.isBlade(damager.getItemInHand().getTypeId())) {
							event.getEntity().setFireTicks(FIREBLADE.getTicks());
						}
					}
					Proficiency FIREPUNCH = damagerGuild.getProficiency(ProficiencyType.FIREPUNCH);
					if (FIREPUNCH.getActive()) {
						if (damager.getItemInHand().getType().equals(Material.AIR)) {
							event.getEntity().setFireTicks(FIREPUNCH.getTicks());
						}
					}
					Proficiency POISONBLADE = damagerGuild.getProficiency(ProficiencyType.POISONBLADE);
					if (POISONBLADE.getActive()) {
						if (plugin.isBlade(damager.getItemInHand().getTypeId())) {
							if (defender != null) {
								defender.addPotionEffect(new PotionEffect(PotionEffectType.POISON, POISONBLADE.getTicks(), 1));
							}
						}
					}
				}
			}
		}
		
		if (damage == 0) {
			if (plugin.allowDamageAnimationOnZero) {
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
