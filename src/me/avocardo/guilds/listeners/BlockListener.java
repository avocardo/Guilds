package me.avocardo.guilds.listeners;

import me.avocardo.guilds.Guild;
import me.avocardo.guilds.GuildsBasic;
import me.avocardo.guilds.User;
import me.avocardo.guilds.messages.Message;
import me.avocardo.guilds.messages.MessageType;
import me.avocardo.guilds.utilities.Proficiency;
import me.avocardo.guilds.utilities.ProficiencyType;
import me.avocardo.guilds.utilities.Settings;

import org.bukkit.Location;
import org.bukkit.Material;
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

	private GuildsBasic GuildsBasic;

	public BlockListener(GuildsBasic GuildsBasic) {
		
		this.GuildsBasic = GuildsBasic;
        
    }
	
	private boolean anyToolBlock(Material Material, int Item) {
		if (Material.isSolid()) {
			switch (Material) {
			case CACTUS: // ANY TOOL
			case TNT: //ANY TOOL
			case SPONGE: // ANY TOOL
			case GLASS: // ANY TOOL
			case CAKE_BLOCK: // ANY TOOL
			case BEACON: // ANY TOOL
			case THIN_GLASS: // ANY TOOL
			case GLOWSTONE: // ANY TOOL
			case BED_BLOCK: // ANY TOOL
			case MONSTER_EGGS: // ANY TOOL
			case REDSTONE_LAMP_OFF: // ANY TOOL
			case REDSTONE_LAMP_ON: // ANY TOOL
			case COMMAND: // ANY TOOL
				if (GuildsBasic.isAxe(Item)) return true;
				if (GuildsBasic.isShear(Item)) return true;
				if (GuildsBasic.isPick(Item)) return true;
				if (GuildsBasic.isShovel(Item)) return true;
				return false;
			default:
				return false;
			}
		}
		return false;
	}
	
	private boolean axeToolBlock(Material Material, int Item) {
		if (Material.isSolid()) {
			switch (Material) {
			case WOOD:
			case LOG:
			case PUMPKIN:
			case NOTE_BLOCK:
			case BOOKSHELF:
			case CHEST:
			case WORKBENCH:
			//case SIGN_POST:
			//case WALL_SIGN:
			case JUKEBOX:
			case FENCE:
			case JUNGLE_WOOD_STAIRS:
			case BIRCH_WOOD_STAIRS:
			case SPRUCE_WOOD_STAIRS:
			case WOOD_STEP:
			case WOOD_DOUBLE_STEP:
			case FENCE_GATE:
			case JACK_O_LANTERN:
			case HUGE_MUSHROOM_1:
			case HUGE_MUSHROOM_2:
			//case TRAP_DOOR:
			//case LOCKED_CHEST:
			//case WOOD_PLATE:
			case WOOD_STAIRS:
			case DOUBLE_STEP:
				if (GuildsBasic.isAxe(Item)) return true;
				return false;
			default:
				return false;
			}
		}
		return false;
	}
	
	private boolean pickToolBlock(Material Material, int Item) {
		if (Material.isSolid()) {
			switch (Material) {
			case STONE:
			case COBBLESTONE:
			case GOLD_ORE:
			case IRON_ORE:
			case COAL_ORE:
			case LAPIS_ORE:
			case LAPIS_BLOCK:
			case SANDSTONE:
			case GOLD_BLOCK:
			case IRON_BLOCK:
			case BRICK:
			case ICE:
			case FURNACE:
			case DISPENSER:
			case OBSIDIAN:
			//case PISTON_STICKY_BASE:
			//case PISTON_BASE:
			//case PISTON_EXTENSION:
			//case PISTON_MOVING_PIECE:
			case MOSSY_COBBLESTONE:
			//case MOB_SPAWNER:
			case DIAMOND_ORE:
			case DIAMOND_BLOCK:
			case BURNING_FURNACE:
			case REDSTONE_ORE:
			case GLOWING_REDSTONE_ORE:
			case NETHERRACK:
			case NETHER_BRICK:
			case NETHER_FENCE:
			case NETHER_BRICK_STAIRS:
			//case CAULDRON:
			//case ANVIL:
			//case BREWING_STAND:
			//case ENCHANTMENT_TABLE:
			//case ENDER_CHEST:
			case COBBLE_WALL:
			case EMERALD_BLOCK:
			case EMERALD_ORE:
			case SANDSTONE_STAIRS:
			case SMOOTH_STAIRS:
			case BRICK_STAIRS:
			case MELON_BLOCK:
			case IRON_FENCE:
			case SMOOTH_BRICK:
			case COBBLESTONE_STAIRS:
				if (GuildsBasic.isPick(Item)) return true;
				return false;
			default:
				return false;
			}
		}
		return false;
	}
	
	private boolean shovelToolBlock(Material Material, int Item) {
		if (Material.isSolid()) {
			switch (Material) {
			case GRASS:
			case DIRT:
			case SAND:
			case GRAVEL:
			case SOIL:
			case CLAY:
			case SNOW_BLOCK:
			case SOUL_SAND:
			case MYCEL:
				if (GuildsBasic.isShovel(Item)) return true;
				return false;
			default:
				return false;
			}
		}
		return false;
	}
	
	private boolean shearToolBlock(Material Material, int Item) {
		if (Material.isSolid()) {
			switch (Material) {
			case LEAVES:
			case WOOL:
				if (GuildsBasic.isShear(Item)) return true;
				return false;
			default:
				return false;
			}
		}
		return false;
	}
	
	private boolean allowBlock(Player p, Location loc, MessageType MessageType) {
		
		if (p.hasPermission("guilds.admin.protectionbarrier")) {
			return true;
		}
		
		if (!GuildsBasic.getEnabled(Settings.ENABLE_GUILD_PROTECTION_BARRIER)) {
			return true;
		}
		
		for (Guild g : GuildsBasic.GuildsList) {
			
			Location base = g.getBase();
			
			if (base.distance(loc) > (double) GuildsBasic.getIntSetting(Settings.SET_PROTECTION_BARRIER)) {
				// Outside distance... 
			} else {
				if (GuildsBasic.getPlayerGuild(p) != g) {
					new Message(MessageType, p, g, GuildsBasic);
					return false;
				}
			}
			
		}
		
		return true;
		
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event) {

		Location loc = event.getBlockPlaced().getLocation();
		Player p = event.getPlayer();
		
		if (!allowBlock(p, loc, MessageType.PROTECTED_BLOCK)) {
			event.setCancelled(true);
		}
				
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event) {

		Location loc = event.getBlock().getLocation();
		Player p = event.getPlayer();
		
		if (!allowBlock(p, loc, MessageType.PROTECTED_BLOCK)) {
			event.setCancelled(true);
		}
		
		Guild guild = GuildsBasic.getPlayerGuild(p);
		World w = p.getWorld();
		User u = GuildsBasic.getPlayerUser(p);
		
		if (guild != null) {
			if (guild.getWorlds().contains(w)) {
				Proficiency TOOL_DURABILITY = guild.getProficiency(ProficiencyType.TOOL_DURABILITY);
				if (TOOL_DURABILITY.getActive()) {
					short before = p.getItemInHand().getDurability();
					if (u != null) {
						int Power = (int) (TOOL_DURABILITY.getPower() * (double) 10);
						if (!u.useDurability(Power)) {
							short value = (short) (before - 1);
					        p.getItemInHand().setDurability(value);
						}
					}
				}
			}
		}
				
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onBlockDamage(BlockDamageEvent event) {

		Location loc = event.getBlock().getLocation();
		Player p = event.getPlayer();
		
		if (!allowBlock(p, loc, MessageType.PROTECTED_BLOCK)) {
			event.setCancelled(true);
			return;
		}
		
		if (event.getBlock().getType().equals(Material.BEDROCK)) {
			return;
		}
		
		Guild guild = GuildsBasic.getPlayerGuild(p);
		World w = p.getWorld();
		Biome b = event.getBlock().getBiome();
		User u = GuildsBasic.getPlayerUser(p);

		if (guild != null) {
			if (guild.getWorlds().contains(w) && guild.getBiomes().contains(b)) {
				Proficiency INSTA_MINE = guild.getProficiency(ProficiencyType.INSTA_MINE);
				Proficiency INSTA_DIG = guild.getProficiency(ProficiencyType.INSTA_DIG);
				Proficiency INSTA_AXE = guild.getProficiency(ProficiencyType.INSTA_AXE);
				Proficiency INSTA_SHEAR = guild.getProficiency(ProficiencyType.INSTA_SHEAR);
				
				int i = p.getItemInHand().getTypeId();
				Material m = event.getBlock().getType();
				if (INSTA_MINE.getActive()) {
					if (u != null) {
						if (pickToolBlock(m, i) || anyToolBlock(m, i)) {
							if (INSTA_MINE.getUseProficiency(u)) {
								event.setInstaBreak(true);
							}
						}
					}
				}
				if (INSTA_DIG.getActive()) {
					if (u != null) {
						if (shovelToolBlock(m, i) || anyToolBlock(m, i)) {
							if (INSTA_DIG.getUseProficiency(u)) {
								event.setInstaBreak(true);
							}
						}
					}
				}
				if (INSTA_AXE.getActive()) {
					if (u != null) {
						if (axeToolBlock(m, i) || anyToolBlock(m, i)) {
							if (INSTA_AXE.getUseProficiency(u)) {
								event.setInstaBreak(true);
							}
						}
					}
				}
				if (INSTA_SHEAR.getActive()) {
					if (u != null) {
						if (shearToolBlock(m, i) || anyToolBlock(m, i)) {
							if (INSTA_SHEAR.getUseProficiency(u)) {
								event.setInstaBreak(true);
							}
						}
					}
				}
			}
		}
		
	}
	
}
