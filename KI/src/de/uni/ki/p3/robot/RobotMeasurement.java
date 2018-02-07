/*
 * Copyright © 2018 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.uni.ki.p3.robot;

import java.util.*;

import de.uni.ki.p3.KIUtil;

public class RobotMeasurement
{
	private int colorId;
	private List<RobotDistance> distances;
	private RobotPixyRect rect;
	
	public RobotMeasurement(int colorId, List<RobotDistance> distances,
							RobotPixyRect rect)
	{
		this.colorId = colorId;
		this.distances = Collections.unmodifiableList(distances);
		this.rect = rect;
	}

	public int getColorId()
	{
		return colorId;
	}
	
	public List<RobotDistance> getDistances()
	{
		return distances;
	}
	
	public RobotDistance getDistance(double angle)
	{
		for(RobotDistance dist : distances)
		{
			if(KIUtil.equals(angle, dist.getDistAngle()))
			{
				return dist;
			}
		}
		
		return null;
	}
	
	public RobotPixyRect getRect()
	{
		return rect;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof RobotMeasurement)
		{
			RobotMeasurement m = (RobotMeasurement)obj;
			
			return colorId == m.colorId && distances.equals(m.distances) && rect.equals(m.rect);
		}
		
		return false;
	}

	@Override
	public String toString()
	{
		return "Measurment(" + colorId + ", " + distances + ", " + rect + ")";
	}
}
