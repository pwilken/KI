/*
 * Copyright © 2018 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.uni.ki.p3.robot;

import java.util.*;

import de.uni.ki.p3.KIDistance;
import de.uni.ki.p3.MCL.*;
import lejos.robotics.Color;

public class SimRobot implements Robot
{
	private List<RobotListener> listener;
	private List<SimRobotListener> simListener;
	private Position pos;
	private double theta;
	private RangeMap map;
	
	private Random random;
	private double moveTolerance;
	private double rotateTolerance;
	private double measureAngleTolerance;
	private double measureDistTolerance;
	
	private int[] measureAngles;
	
	public SimRobot()
	{
		listener = new ArrayList<>();
		simListener = new ArrayList<>();
		pos = new Position(0, 0);
		
		measureAngles = new int[] {0};
		random = new Random();
		moveTolerance = 0d;
		rotateTolerance = 0d;
		measureAngleTolerance = 0d;
		measureDistTolerance = 0d;
	}

	@Override
	public void move(double dist)
	{
		dist = dist + (random.nextDouble() * moveTolerance) - (moveTolerance / 2);
		setPos(new Position(
			pos.getX() + Math.cos(Math.toRadians(theta)) * dist,
			pos.getY() + Math.sin(Math.toRadians(theta)) * dist));
		
		for(RobotListener l : listener)
		{
			l.robotMoved(this, dist);
		}
	}

	@Override
	public void rotate(double angle)
	{
		angle = angle + (random.nextDouble() * rotateTolerance) - (rotateTolerance / 2);
		double theta = this.theta + angle % 360;
		if(theta < 0)
		{
			theta += 360;
		}
		
		setTheta(theta);
		
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
			getDistances(pos, theta, measureAngles),
			new RobotPixyRect("",
				0,
				0,
				0,
				0,
				0));
		
		for(RobotListener l : listener)
		{
			l.robotMeasured(this, measurement);
		}
	}

	private List<KIDistance> getDistances(Position pos, double theta, int... angles)
	{
		List<KIDistance> distances = new ArrayList<>();
		
		for(int angle : angles)
		{
			double aangle = theta + angle + (random.nextDouble() * measureAngleTolerance) - (measureAngleTolerance / 2);
			// we expect a tolerance in distance to only be negative i. e. the measurement is not farther than the real distance
			double dist = map.distanceToWall(pos, aangle) - (random.nextDouble() * measureDistTolerance);
			distances.add(
				new KIDistance(dist, angle));
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
		
		for(SimRobotListener l : simListener)
		{
			l.posChanged(this, pos);
		}
	}
	
	public double getTheta()
	{
		return theta;
	}
	
	public void setTheta(double theta)
	{
		this.theta = theta;
		
		for(SimRobotListener l : simListener)
		{
			l.thetaChanged(this, theta);
		}
	}
	
	public RangeMap getMap()
	{
		return map;
	}
	
	public void setMap(RangeMap map)
	{
		this.map = map;
	}
	
	public int[] getMeasureAngles()
	{
		return measureAngles;
	}
	
	public void setMeasureAngles(int... angles)
	{
		measureAngles = angles;
	}
	
	public double getMoveTolerance()
	{
		return moveTolerance;
	}
	
	public void setMoveTolerance(double moveTolerance)
	{
		this.moveTolerance = moveTolerance;
	}
	
	public double getRotateTolerance()
	{
		return rotateTolerance;
	}
	
	public void setRotateTolerance(double rotateTolerance)
	{
		this.rotateTolerance = rotateTolerance;
	}
	
	public Random getRandom()
	{
		return random;
	}
	
	public void setRandom(Random random)
	{
		this.random = random;
	}
	
	public double getMeasureAngleTolerance()
	{
		return measureAngleTolerance;
	}
	
	public void setMeasureAngleTolerance(double measureAngleTolerance)
	{
		this.measureAngleTolerance = measureAngleTolerance;
	}
	
	public double getMeasureDistTolerance()
	{
		return measureDistTolerance;
	}
	
	public void setMeasureDistTolerance(double measureDistTolerance)
	{
		this.measureDistTolerance = measureDistTolerance;
	}

	@Override
	public void terminate()
	{
	}
	
	public void addSimListener(SimRobotListener l)
	{
		simListener.add(l);
	}
	
	public void removeSimListener(SimRobotListener l)
	{
		simListener.remove(l);
	}
}
