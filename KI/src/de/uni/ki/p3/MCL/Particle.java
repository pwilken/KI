package de.uni.ki.p3.MCL;

import de.uni.ki.p3.KIUtil;

public class Particle implements Cloneable, Comparable<Particle>
{
	private Position pos;
	private double theta;
	private double weight;
	
	public Particle(Position pos, double theta)
	{
		this.pos = pos;
		this.theta = theta;
		weight = 1d;
	}
	
	public Position getPos()
	{
		return pos;
	}
	
	public double getTheta()
	{
		return theta;
	}
	
	public void move(double dist)
	{
		pos = new Position(
			pos.getX() + Math.cos(Math.toRadians(theta)) * dist,
			pos.getY() + Math.sin(Math.toRadians(theta)) * dist);
	}
	
	public void rotate(double angle)
	{
		theta = (theta + angle) % 360;
	}
	
	public double getWeight()
	{
		return weight;
	}
	
	public void setWeight(double weight)
	{
		this.weight = weight;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pos == null) ? 0 : pos.hashCode());
		long temp;
		temp = Double.doubleToLongBits(theta);
		result = prime * result + (int)(temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(weight);
		result = prime * result + (int)(temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof Particle))
		{
			return false;
		}
		
		Particle p = (Particle)obj;
		
		if(!pos.equals(p.pos))
		{
			return false;
		}
		
		if(!KIUtil.equals(theta, p.theta))
		{
			return false;
		}
		
		return true;
	}
	
	@Override
	protected Particle clone()
	{
		return new Particle(new Position(pos.getX(), pos.getY()), theta);
	}

	@Override
	public int compareTo(Particle o)
	{
		return Double.compare(weight, o.weight);
	}
	
	@Override
	public String toString()
	{
		return "Particle(" + pos + "/" + theta + ")";
	}
}