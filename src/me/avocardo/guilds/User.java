package me.avocardo.guilds;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.avocardo.guilds.utilities.ProficiencyType;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class User {

	private Player Player;
	
	private int ToolDurability;
	
	public Map <ProficiencyType, Long> ProficiencyCoolDown = new HashMap <ProficiencyType, Long>();	
	
	public Map <Player, List<ItemStack>> Inventory = new HashMap <Player, List<ItemStack>>();

	public User(Player Player) {
		this.Player = Player;
	}
	
	public Player getPlayer() {
		return Player;
	}

	public void setPlayer(Player Player) {
		this.Player = Player;
	}
	
	public boolean useDurability(int Power) {
		++ToolDurability;
		if (ToolDurability >= Power) {
			ToolDurability = 0;
			return false;
		} else if (ToolDurability > 10) {
			return false;
		} else {
			return true;
		}
	}
	
}
