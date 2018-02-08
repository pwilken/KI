/*
 * Copyright © 2018 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.uni.ki.p3;

public class KIDistance
{
	private double dist;
	private double distAngle;
	
	public KIDistance(double dist, double distAngle)
	{
		this.dist = dist;
		this.distAngle = distAngle;
	}
	
	public double getDist()
	{
		return dist;
	}
	
	public double getDistAngle()
	{
		return distAngle;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof KIDistance))
		{
			return false;
		}
		
		KIDistance o = (KIDistance)obj;
		
		return KIUtil.equals(dist, o.dist) && KIUtil.equals(distAngle, o.distAngle);
	}
	
	@Override
	public String toString()
	{
		return "distance<" + dist + "> on angle<" + distAngle + ">";
	}
}
