package me.avocardo.guilds.utilities;

public class Attribute {

	private AttributeType AttributeType;
	private double Power;
	
	public Attribute(AttributeType AttributeType, double Power) {
		this.AttributeType = AttributeType;
		this.Power = Power;
	}
	
	public AttributeType getAttributeType() {
		return AttributeType;
	}
	
	public double getPower() {
		return Power;
	}
	
	public void setPower(double Power) {
		this.Power = Power;
	}
	
}
