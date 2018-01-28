/*
 * Copyright © 2018 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.uni.ki.p3.robot;

import de.uni.ki.p3.KIUtil;

public class RobotDistance
{
	private double dist;
	private double distAngle;
	
	public RobotDistance(double dist, double distAngle)
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
		if(!(obj instanceof RobotDistance))
		{
			return false;
		}
		
		RobotDistance o = (RobotDistance)obj;
		
		return KIUtil.equals(dist, o.dist) && KIUtil.equals(distAngle, o.distAngle);
	}
}
