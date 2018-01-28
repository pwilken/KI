/*
 * Copyright © 2018 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.uni.ki.p3.MCL;

import de.uni.ki.p3.KIUtil;
import de.uni.ki.p3.robot.*;
import lejos.robotics.Color;

public class MCLDefaultWeightFunction implements MCLWeightFunction
{

	@Override
	public double calcWeight(MCL mcl, RobotMeasurement measurement, Particle p)
	{
		// max weight is 1000
		double weight = 1000d;
		
		// 250 depends on color
		if(measurement.getColorId() != Color.NONE 
				^ mcl.getMap().strokeAt(p.getPos()) != null)
		{
			weight -= 250;
			return 0;
		}
		
		RobotDistance md = measurement.getDistances().get(0);
		
		// 750 depends on distance scan
		double dist = mcl.getMap().distanceToWall(p.getPos(), p.getTheta() + md.getDistAngle());
		double d = KIUtil.positiveDistance(dist, md.getDist());
		d *= 4;
		if(Double.isInfinite(dist) || Double.isInfinite(d))
		{
			weight = 0;
		}
		else if(d < md.getDist())
		{
			weight -= ((d / md.getDist()) * 1000);
		}
		else
		{
			weight -= 1000;
		}
		
		return weight;
	}
}
