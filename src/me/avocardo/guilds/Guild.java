package me.avocardo.guilds;

import java.util.ArrayList;
import java.util.List;

import me.avocardo.guilds.utilities.Attribute;
import me.avocardo.guilds.utilities.AttributeType;
import me.avocardo.guilds.utilities.Proficiency;
import me.avocardo.guilds.utilities.ProficiencyType;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;

public class Guild {
	
	private String NAME;
	private String PLAYER_PREFIX;
	private String PLAYER_SUFFIX;
	
	private Location LOCATION;
	
	private List<Integer> RESTRICTIONS = new ArrayList<Integer>();	
	private List<World> WORLDS = new ArrayList<World>();	
	private List<Biome> BIOMES = new ArrayList<Biome>();
	private List<Proficiency> PROFICIENCIES = new ArrayList<Proficiency>();
	private List<Attribute> ATTRIBUTES = new ArrayList<Attribute>();

	public List<Integer> getRestrictions() {
		return RESTRICTIONS;
	}
	
	public void addRestriction(int i) {
		RESTRICTIONS.add(i);
	}
	
	public List<World> getWorlds() {
		return WORLDS;
	}
	
	public void addWorld(World w) {
		WORLDS.add(w);
	}
	
	public List<Biome> getBiomes() {
		return BIOMES;
	}
	
	public void addBiome(Biome b) {
		BIOMES.add(b);
	}
	
	public List<Proficiency> getProficiencies() {
		return PROFICIENCIES;
	}
	
	public void addProficiency(Proficiency p) {
		PROFICIENCIES.add(p);
	}
	
	public Proficiency getProficiency(ProficiencyType ProficiencyType) {
		for (Proficiency p : PROFICIENCIES)
			if (p.getProficiencyType().equals(ProficiencyType))
				return p;
		return null;
	}
	
	public List<Attribute> getAttributes() {
		return ATTRIBUTES;
	}
	
	public void addAttribute(Attribute a) {
		ATTRIBUTES.add(a);
	}
	
	public Attribute getAttribute(AttributeType AttributeType) {
		for (Attribute p : ATTRIBUTES)
			if (p.getAttributeType().equals(AttributeType))
				return p;
		return null;
	}
	
	public Location getBase() {
		return LOCATION;
	}
	
	public void setBase(Location l) {
		LOCATION = l;
	}
	
	public void setBaseWorld(World w) {
		LOCATION.setWorld(w);
	}

	public String getName() {
		return NAME;
	}

	public void setName(String NAME) {
		this.NAME = NAME;
	}

	public String getPlayerPrefix() {
		return PLAYER_PREFIX;
	}

	public void setPlayerPrefix(String PLAYER_PREFIX) {
		this.PLAYER_PREFIX = PLAYER_PREFIX;
	}

	public String getPlayerSuffix() {
		return PLAYER_SUFFIX;
	}

	public void setPlayerSuffix(String PLAYER_SUFFIX) {
		this.PLAYER_SUFFIX = PLAYER_SUFFIX;
	}

}
