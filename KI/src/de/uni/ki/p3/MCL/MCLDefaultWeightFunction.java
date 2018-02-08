/*
 * Copyright © 2018 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.uni.ki.p3.MCL;

import de.uni.ki.p3.*;
import de.uni.ki.p3.robot.*;

public class MCLDefaultWeightFunction implements MCLWeightFunction
{
	public static final boolean FULL_PROB = true;
	
	@Override
	public double calcWeight(MCL mcl, RobotMeasurement measurement, Particle p)
	{
		return p.getWeight() * newWeight(mcl, measurement, p);
	}

	private double newWeight(MCL mcl, RobotMeasurement measurement, Particle p)
	{
		double weight = 1d;
		for(KIDistance distance : measurement.getDistances())
		{
			double dist = mcl.getMap().distanceToWall(p.getPos(), p.getTheta() + distance.getDistAngle());
			double d = KIUtil.positiveDistance(dist, distance.getDist());
			d *= 4;
			
			if(FULL_PROB)
			{
    			// all distances are count as full
    			// has one a probability of 0 -> weight = 0
    			if(Double.isInfinite(dist) || Double.isInfinite(d))
    			{
    				weight = 0;
    			}
    			else if(d < distance.getDist())
    			{
    				double factor = d / distance.getDist();
    				weight *= factor;
    			}
    			else
    			{
    				weight = 0d;
    			}
			}
			else
			{
    			// each distance only affects a part of the weight
    			// a probability of 0 only leads to a high decrease, but it may still be possible
    			if(Double.isInfinite(dist) || Double.isInfinite(d))
    			{
    				weight -= (1d / measurement.getDistances().size());
    			}
    			else if(d < distance.getDist())
    			{
    				double factor = d / distance.getDist();
    				weight -= (factor / measurement.getDistances().size());
    			}
    			else
    			{
    				weight -= (1d / measurement.getDistances().size());
    			}
			}
		}
		return weight;
	}
	
	
//	public double calcWeight(MCL mcl, RobotMeasurement measurement, Particle p)
//	{
//		// max weight is 1000
//		double weight = 1000d;
//		
//		// 250 depends on color
//		if(measurement.getColorId() != Color.NONE 
//				^ mcl.getMap().strokeAt(p.getPos()) != null)
//		{
//			weight -= 250;
//			return 0;
//		}
//		
//		RobotDistance md = measurement.getDistances().get(0);
//		
//		// 750 depends on distance scan
//		double dist = mcl.getMap().distanceToWall(p.getPos(), p.getTheta() + md.getDistAngle());
//		double d = KIUtil.positiveDistance(dist, md.getDist());
//		d *= 4;
//		if(Double.isInfinite(dist) || Double.isInfinite(d))
//		{
//			weight = 0;
//		}
//		else if(d < md.getDist())
//		{
//			weight -= ((d / md.getDist()) * 1000);
//		}
//		else
//		{
//			weight -= 1000;
//		}
//		
//		return weight;
//	}
}
