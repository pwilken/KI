/*
 * Copyright © 2018 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.uni.ki.p3.MCL;

import de.uni.ki.p3.robot.*;

public class MCLLejosWeightFunction implements MCLWeightFunction
{

	@Override
	public double calcWeight(MCL mcl, RobotMeasurement measurement, Particle p)
	{
		double divisor = 400f;

		double weight = 1;
		Position tempPos = p.getPos();
		for(RobotDistance d : measurement.getDistances())
		{
			if(!mcl.getMap().isInside(tempPos))
			{
				weight = 0;
			}
			else
			{
				double angle = d.getDistAngle();

				double robotReading = d.getDist();
				double range = mcl.getMap().distanceToWall(tempPos,
					p.getTheta() + angle);
				if(range < 0)
				{
					weight = 0;
				}
				else
				{
					double diff = robotReading - range;
					weight *= Math.exp(-(diff * diff) / divisor);
				}
			}
		}

		return weight;
	}

}
