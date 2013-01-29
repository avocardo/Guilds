package me.avocardo.guilds.utilities;

import me.avocardo.guilds.GuildsBasic;
import me.avocardo.guilds.User;
import me.avocardo.guilds.messages.Message;
import me.avocardo.guilds.messages.MessageType;

public class Proficiency {

	private GuildsBasic GuildsBasic;
	private ProficiencyType ProficiencyType;
	private boolean Active;
	private double Power;
	private long CoolDown;
	private int Ticks;
	private int Minimum;
	private int Maximum;

	public Proficiency(ProficiencyType ProficiencyType, boolean Active, double Power, long CoolDown, int Ticks, int Minimum, int Maximum, int Item, GuildsBasic GuildsBasic) {
		this.GuildsBasic = GuildsBasic;
		this.ProficiencyType = ProficiencyType;
		this.Active = Active;
		this.Power = Power;
		this.CoolDown = CoolDown;
		this.Ticks = Ticks;
		this.setMinimum(Minimum);
		this.setMaximum(Maximum);
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
	
	public void setUseProficiency(User u) {
		if (u.ProficiencyCoolDown.containsKey(this.ProficiencyType))
			u.ProficiencyCoolDown.remove(this.ProficiencyType);
		u.ProficiencyCoolDown.put(this.ProficiencyType, System.currentTimeMillis());
	}
	
	public boolean getUseProficiency(User u) {
		if (!ProficiencyType.hasCoolDown()) return true;
		if (CoolDown == 0) return true;
		if (u.ProficiencyCoolDown.containsKey(this.ProficiencyType)) {
			if ((System.currentTimeMillis() - u.ProficiencyCoolDown.get(this.ProficiencyType)) < Ticks) {
				return true; // Still Active
			} else if ((System.currentTimeMillis() - u.ProficiencyCoolDown.get(this.ProficiencyType)) < (Ticks + CoolDown)) {
				new Message(MessageType.COOLDOWN, u.getPlayer(), ProficiencyType, GuildsBasic);
				return false; // Cooling Down
			} else {
				setUseProficiency(u);
				return true; // Reactivate
			}
		} else {
			setUseProficiency(u);
			return true; // Activate
		}
		
	}

	public int getMinimum() {
		return Minimum;
	}

	public void setMinimum(int minimum) {
		Minimum = minimum;
	}

	public int getMaximum() {
		return Maximum;
	}

	public void setMaximum(int maximum) {
		Maximum = maximum;
	}
	
}
