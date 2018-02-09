/*
 * Copyright © 2018 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.uni.ki.p3.mcl;

import de.uni.ki.p3.KIUtil;

public class Position
{
	private final double x;
	private final double y;
	
	public Position(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	public double getX()
	{
		return x;
	}
	
	public double getY()
	{
		return y;
	}
	
	public Position add(Position p)
	{
		return new Position(
			x + p.x,
			y + p.y);
	}
	
	public Position subtract(Position p)
	{
		return new Position(
			x - p.x,
			y - p.y);
	}
	
	public double cross(Position p)
	{
		return x * p.y - p.x * y;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int)(temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int)(temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof Position))
		{
			return false;
		}
		
		Position p = (Position)obj;
		
		if(!KIUtil.equals(x, p.x))
		{
			return false;
		}
		
		if(!KIUtil.equals(y, p.y))
		{
			return false;
		}
		
		return true;
	}
	
	@Override
	public String toString()
	{
		return "Pos(" + x + "/" + y + ")";
	}
}
