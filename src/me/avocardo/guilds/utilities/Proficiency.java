package me.avocardo.guilds.utilities;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

public class Proficiency {

	private ProficiencyType ProficiencyType;
	private boolean Active;
	private double Power;
	private long CoolDown;
	private int Ticks;
	
	private Map <Player, Long> PlayerCoolDown = new HashMap <Player, Long>();
	
	public Proficiency(ProficiencyType ProficiencyType, boolean Active, double Power, long CoolDown, int Ticks) {
		this.ProficiencyType = ProficiencyType;
		this.Active = Active;
		this.Power = Power;
		this.CoolDown = CoolDown;
		this.Ticks = Ticks;
	}
	
	public ProficiencyType getProficiencyType() {
		return ProficiencyType;
	}
	
	public double getPower() {
		return Power;
	}
	
	public void setPower(double Power) {
		this.Power = Power;
	}
	
	public boolean getActive() {
		return Active;
	}
	
	public void setActive(boolean Active) {
		this.Active = Active;
	}
	
	public long getCoolDown() {
		return CoolDown;
	}
	
	public void setCoolDown(long CoolDown) {
		this.CoolDown = CoolDown;
	}
	
	public int getTicks() {
		return Ticks;
	}
	
	public void setTicks(int Ticks) {
		this.Ticks = Ticks;
	}
	
	public void setUseProficiency(Player p) {
		if (PlayerCoolDown.containsKey(p))
			PlayerCoolDown.remove(p);
		PlayerCoolDown.put(p, System.currentTimeMillis());
	}
	
	public boolean getUseProficiency(Player p) {
		if (PlayerCoolDown.containsKey(p)) {
			if ((System.currentTimeMillis() - PlayerCoolDown.get(p)) < Ticks) {
				return true; // Still Active
			} else if ((System.currentTimeMillis() - PlayerCoolDown.get(p)) < (Ticks + CoolDown)) {
				//MESSAGE HERE
				return false; // Cooling Down
			} else {
				setUseProficiency(p);
				return true; // Reactivate
			}
		} else {
			setUseProficiency(p);
			return true; // Activate
		}
		
	}
	
}
