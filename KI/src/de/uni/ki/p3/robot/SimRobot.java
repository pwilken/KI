/*
 * Copyright © 2018 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.uni.ki.p3.robot;

import java.util.*;

import de.uni.ki.p3.MCL.*;
import lejos.robotics.Color;

public class SimRobot implements Robot
{
	private List<RobotListener> listener;
	private Position pos;
	private double theta;
	private RangeMap map;
	
	public SimRobot()
	{
		listener = new ArrayList<>();
		pos = new Position(0, 0);
	}

	@Override
	public void move(double dist)
	{
		pos = new Position(
			pos.getX() + Math.cos(Math.toRadians(theta)) * dist,
			pos.getY() + Math.sin(Math.toRadians(theta)) * dist);
		
		for(RobotListener l : listener)
		{
			l.robotMoved(this, dist);
		}
	}

	@Override
	public void rotate(double angle)
	{
		theta += angle % 360;
		if(theta < 0)
		{
			theta += 360;
		}
		
		for(RobotListener l : listener)
		{
			l.robotRotated(this, angle);
		}
	}

	@Override
	public void measure()
	{
		RobotMeasurement measurement = new RobotMeasurement(
			map.strokeAt(pos) == null ? Color.NONE : Color.BLACK,
			getDistances(pos, theta, -90, -45, 0, 45, 90));
		
		for(RobotListener l : listener)
		{
			l.robotMeasured(this, measurement);
		}
	}

	private List<RobotDistance> getDistances(Position pos, double theta, int... angles)
	{
		List<RobotDistance> distances = new ArrayList<>();
		
		for(int angle : angles)
		{
			distances.add(
				new RobotDistance(
					map.distanceToWall(pos, theta + angle),
					theta + angle));
		}
		
		return distances;
	}

	@Override
	public void addRobotListener(RobotListener l)
	{
		listener.add(l);
	}

	@Override
	public void removeRobotListener(RobotListener l)
	{
		listener.remove(l);
	}
	
	public Position getPos()
	{
		return pos;
	}
	
	public void setPos(Position pos)
	{
		if(pos.equals(this.pos))
		{
			return;
		}
		
		this.pos = pos;
	}
	
	public double getTheta()
	{
		return theta;
	}
	
	public void setTheta(double theta)
	{
		this.theta = theta;
	}
	
	public RangeMap getMap()
	{
		return map;
	}
	
	public void setMap(RangeMap map)
	{
		this.map = map;
	}

	@Override
	public void terminate()
	{
	}
}
