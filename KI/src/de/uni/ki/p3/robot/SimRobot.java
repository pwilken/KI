/*
 * Copyright © 2018 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.uni.ki.p3.robot;

import java.util.*;

import de.uni.ki.p3.KIUtil;
import de.uni.ki.p3.MCL.*;
import lejos.robotics.Color;

public class SimRobot implements Robot
{
	private List<RobotListener> listener;
	private Position pos;
	private Position simPos;
	private double theta;
	private double distAngle;
	private RangeMap map;
	
	public SimRobot()
	{
		listener = new ArrayList<>();
		pos = new Position(0, 0);
		simPos = new Position(0, 0);
	}

	@Override
	public void move(double dist)
	{
		pos = new Position(
			pos.getX() + Math.cos(Math.toRadians(theta)) * dist,
			pos.getY() + Math.sin(Math.toRadians(theta)) * dist);
		simPos = new Position(
			simPos.getX() + Math.cos(Math.toRadians(theta)) * dist,
			simPos.getY() + Math.sin(Math.toRadians(theta)) * dist);
		
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
		System.out.println(simPos);
		
		RobotMeasurement measurement = new RobotMeasurement(
			map.strokeAt(simPos) == null ? Color.NONE : Color.BLACK,
			map.distanceToWall(simPos, theta + distAngle),
			distAngle);
		
		for(RobotListener l : listener)
		{
			l.robotMeasured(this, measurement);
		}
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
	
	@Override
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
		
		double dist = Math.sqrt(
						KIUtil.positiveDistance(pos.getX(), this.pos.getX())
							* KIUtil.positiveDistance(pos.getX(), this.pos.getX())
						+ KIUtil.positiveDistance(pos.getY(), this.pos.getY())
							* KIUtil.positiveDistance(pos.getY(), this.pos.getY()));
		
		this.pos = pos;
		
		for(RobotListener l : listener)
		{
			// TODO $DeH
//			l.robotMoved(this, dist);
		}
	}
	
	public Position getSimPos()
	{
		return simPos;
	}
	
	public void setSimPos(Position simPos)
	{
		this.simPos = simPos;
	}
	
	@Override
	public double getTheta()
	{
		return theta;
	}
	
	public void setTheta(double theta)
	{
		double angle = KIUtil.positiveDistance(theta, this.theta);
		
		this.theta = theta;
		
		for(RobotListener l : listener)
		{
			l.robotRotated(this, angle);
		}
	}
	
	@Override
	public double getDistAngle()
	{
		return distAngle;
	}
	
	public void setDistAngle(double distAngle)
	{
		this.distAngle = distAngle;
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
	public List<RobotListener> getRobotListener() {
		return listener;
	}
}
