package de.uni.ki.p3.MCL;

public class MCLParticle {
	private float heading, x, y, weight;
	
	public MCLParticle(float heading, float x, float y, float weight)
	{
		this.heading = heading;
		this.x = x;
		this.y = y;
		this.weight = weight;
	}
	
	public float getHeading()
	{
		return heading;
	}
	
	public float getX()
	{
		return x;
	}
	
	public float getY()
	{
		return y;
	}
	
	public float getWeight()
	{
		return weight;
	}
}
