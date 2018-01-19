/*
 * Copyright © 2018 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.uni.ki.p3.robot;

import de.uni.ki.p3.KIUtil;

public class RobotMeasurement
{
	private int colorId;
	private double dist;
	private double distAngle;
	
	public RobotMeasurement(int colorId, double dist, double distAngle)
	{
		this.colorId = colorId;
		this.dist = dist;
		this.distAngle = distAngle;
	}
	
	public int getColorId()
	{
		return colorId;
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
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + colorId;
		long temp;
		temp = Double.doubleToLongBits(dist);
		result = prime * result + (int)(temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(distAngle);
		result = prime * result + (int)(temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof RobotMeasurement))
		{
			return false;
		}
		
		RobotMeasurement r = (RobotMeasurement)obj;
		
		if(!KIUtil.equals(dist, r.dist))
		{
			return false;
		}
		
		if(!KIUtil.equals(distAngle, r.distAngle))
		{
			return false;
		}
		
		return true;
	}
	
	@Override
	public String toString()
	{
		return "Measurment(" + colorId + ", " + dist + ", " + distAngle + ")";
	}
}
