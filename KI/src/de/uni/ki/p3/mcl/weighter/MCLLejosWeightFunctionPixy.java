/*
 * Copyright © 2018 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.uni.ki.p3.mcl.weighter;

import java.util.*;

import de.uni.ki.p3.KIDistance;
import de.uni.ki.p3.mcl.*;
import de.uni.ki.p3.mcl.map.MCLMarker;
import de.uni.ki.p3.robot.*;

public class MCLLejosWeightFunctionPixy implements MCLWeightFunction
{
	@Override
	public double calcWeight(MCL mcl, RobotMeasurement measurement, Particle p)
	{
		double divisor = mcl.getConfig().sigma;

		double weight = 1d;
		Position tempPos = p.getPos();
		if(!mcl.getMap().isInside(tempPos))
		{
			return 0d;
		}
		for(KIDistance d : measurement.getDistances())
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
		
		weight *= getMarkerWeight(mcl, measurement, p);

		return weight;
	}

	private double getMarkerWeight(MCL mcl, RobotMeasurement measurement, Particle p)
	{
		
		String stroke = getStroke(mcl, measurement.getRect());
		
		if(stroke.isEmpty())
		{
			// no color seen
			for(MCLMarker m : mcl.getMap().getMarkers())
			{
				KIDistance d = normalize(mcl.getMap().distanceToMarker(p.getPos(), m), p);
				
				if(!Double.isInfinite(d.getDist()) && d.getDistAngle() < 45)
				{
					// particle could have seen the marker
					return 0.8;
				}
			}
		}
		else
		{
			Map<String, KIDistance> map = buildStrokeMap(mcl, p);
			
			if(map.get(stroke).getDistAngle() > 45)
			{
				// to high angle, it should not be able to see it
				return 0.8;
			}
			else if(Double.isInfinite(map.get(stroke).getDist()))
			{
				// by calculation, the marker should not be visible
				// but we assume, the marker is a single point
				// it could be visible despite this measurement,
				// so just decrease the weight a tiny bit
				return 0.95;
			}
		}
		
		return 1;
	}

	private Map<String, KIDistance> buildStrokeMap(MCL mcl, Particle p)
	{
		Map<String, KIDistance> map = new HashMap<>();
		
		for(MCLMarker m : mcl.getMap().getMarkers())
		{
			KIDistance d = normalize(mcl.getMap().distanceToMarker(p.getPos(), m), p);
			
			if(!map.containsKey(m.getStroke()))
			{
				// currently no entry present, so just add it
				map.put(m.getStroke(), d);
			}
			else
			{
				if(Double.isInfinite(d.getDist()))
				{
					if(Double.isInfinite(map.get(m.getStroke()).getDist()) && d.getDistAngle() < map.get(m.getStroke()).getDistAngle())
					{
						// both the entry and map and the new one are not reachable
						// but the new one has a better angle
						map.put(m.getStroke(), d);
					}
				}
				else
				{
					if(Double.isInfinite(map.get(m.getStroke()).getDist())
						|| d.getDistAngle() < map.get(m.getStroke()).getDistAngle())
					{
						// either the old one is not reachable or the new one
						// has a better angle
						map.put(m.getStroke(), d);
					}
				}
			}
		}
		return map;
	}

	private KIDistance normalize(KIDistance d, Particle p)
	{
		// assuming the angle is always positive and between 0 - 360
		// where 0 means looking 'left'
		double angle = d.getDistAngle();
		angle -= p.getTheta();
		if(angle < 0)
		{
			angle += 360;
		}
		angle = Math.min(angle, 360 - angle);

		return new KIDistance(d.getDist(), angle);
	}

	private String getStroke(MCL mcl, RobotPixyRect rect)
	{
		return (rect.getX() | rect.getY() | rect.getWidth() | rect.getHeight()) == 0
						? ""
						: mcl.getConfig().mapColorCodeToStroke.get(rect.getColorCodes());
	}

}
