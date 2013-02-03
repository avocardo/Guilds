package me.avocardo.guilds;

import java.util.ArrayList;
import java.util.List;

import me.avocardo.guilds.utilities.Proficiency;
import me.avocardo.guilds.utilities.ProficiencyType;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;

public class Guild {
	
	private int ONLINE;
	
	private String NAME;
	private String COLOR;
	private String PLAYER_PREFIX;
	private String PLAYER_SUFFIX;
	
	private Location LOCATION;
	
	private List<Integer> RESTRICTIONS = new ArrayList<Integer>();	
	private List<World> WORLDS = new ArrayList<World>();	
	private List<Biome> BIOMES = new ArrayList<Biome>();
	private List<Proficiency> PROFICIENCIES = new ArrayList<Proficiency>();

	public void New(GuildsBasic GuildsBasic) {
		setColor("");
		setPlayerPrefix("");
		setPlayerSuffix("");
		for (ProficiencyType p : ProficiencyType.values()) {
			if (p.equals(ProficiencyType.RECOVERHEALTH)) {
				this.addProficiency(new Proficiency(p, true, 1, 0, 0, 0, 0, 0, GuildsBasic));
			} else {
				this.addProficiency(new Proficiency(p, false, 1, 0, 0, 0, 0, 0, GuildsBasic));
			}
		}
	}
	
	public List<Integer> getRestrictions() {
		return RESTRICTIONS;
	}
	
	public int getOnline() {
		return ONLINE;
	}
	
	public void addOnline() {
		ONLINE++;
	}
	
	public void subtractOnline() {
		ONLINE--;
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
	
	public String getColor() {
		return COLOR;
	}

	public void setColor(String COLOR) {
		this.COLOR = COLOR;
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
