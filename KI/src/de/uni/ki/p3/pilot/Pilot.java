/*
 * Copyright © 2018 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.uni.ki.p3.pilot;

import java.util.*;

import de.uni.ki.p3.KIDistance;
import de.uni.ki.p3.MCL.*;
import de.uni.ki.p3.robot.*;

public class Pilot implements RobotListener
{
	private Robot robot;
	private MCL mcl;
	
	private Position pos;
	private double theta;
	private RangeMap map;
	private List<PilotListener> listener;
	
	private volatile boolean run;
	private volatile boolean step;
	private volatile boolean automatic;
	
	private volatile RobotMeasurement lastMeasurement;
	private double moveDist;
	private double sameDistanceTolerance = 2.5;
	private double oldDist;
	private double rotateAngle;
	private double minDistFromWall;
	
	private Thread stepper;
	
	public Pilot(Robot robot, RangeMap map, MCLConfiguration config)
	{
		this.robot = robot;
		this.map = map;
		this.mcl = new MCL(map, config);
		mcl.initializeParticles();
		this.pos = new Position(0, 0);
		
		robot.addRobotListener(mcl);
		robot.addRobotListener(this);
		listener = new ArrayList<>();
		
		run = true;
		step = false;
		automatic = false;
		
		moveDist = 10d;
		rotateAngle = 30d;
		minDistFromWall = 10;
		
		stepper = new Thread(new Runnable()
    		{
    			@Override
    			public void run()
    			{
    				runLoop();
    			}
    		},
    		"Pilot-Stepper");
		stepper.setDaemon(true);
		stepper.start();
	}
	
	public void terminate()
	{
		run = false;
	}
	
	public void nextStep()
	{
		step = true;
		automatic = false;
		stepper.interrupt();
	}
	
	public void start()
	{
		automatic = true;
	}
	
	public void stop()
	{
		automatic = false;
		step = false;
	}
	
	public boolean isRunning()
	{
		return isAlive() && (automatic || step);
	}
	
	public boolean isAlive()
	{
		return stepper.isAlive() && run;
	}
	
	private void runLoop()
	{
		while(run)
		{
			long time = System.currentTimeMillis();
			if(step || automatic)
			{
				step = false;
				
				nextStepInt();
			}
			
			try
			{
				// only 1 update per second
				long delta = System.currentTimeMillis() - time;
				if(delta < 1000)
				{
					Thread.sleep(1000 - delta);
				}
			}
			catch(InterruptedException e)
			{
				if(!step)
				{
					e.printStackTrace();
				}
				Thread.interrupted();
			}
		}
	}
	
	private void nextStepInt()
	{
		// TODO $DeH
		if(lastMeasurement != null)
		{
			KIDistance d = lastMeasurement.getDistance(0d);
			
			if(d == null)
			{
				// TODO $DeH
				// handle this case if it ever happens
			}
			else
			{
				if (Math.random() >= 0.95)
				{
					double angle = Math.random() * 180 + 90;
					if(angle > 180)
					{
						angle = angle - 360;
					}
					robot.rotate(angle);
				}
				else if(oldDist >= d.getDist() - sameDistanceTolerance && oldDist <= d.getDist() + sameDistanceTolerance)
				{
					robot.rotate(Math.random() * 270 + 90);
				}
				else if(d.getDist() > moveDist + minDistFromWall)
				{
					robot.move(moveDist);
				}
				else
				{
					double angle = Math.random() * 180 + 90;
					if(angle > 180)
					{
						angle = angle - 360;
					}
					robot.rotate(angle);
				}
			}
			oldDist = d.getDist();
		}
		robot.measure();
		Particle best = mcl.getBest();
		pos = best.getPos();
		theta = best.getTheta();
		
		for(PilotListener l : listener)
		{
			l.pilotStepped(this);
		}
	}
	
	@Override
	public void robotMeasured(Robot robot, RobotMeasurement measurement)
	{
		lastMeasurement = measurement;
	}
	
	public void addPilotListener(PilotListener l)
	{
		listener.add(l);
	}
	
	public void removePilotListener(PilotListener l)
	{
		listener.remove(l);
	}
	
	public MCL getMcl()
	{
		return mcl;
	}
	
	public Robot getRobot()
	{
		return robot;
	}
	
	public RangeMap getMap()
	{
		return map;
	}
	
	public Position getPos()
	{
		return pos;
	}
	
	public double getTheta()
	{
		return theta;
	}
	
	public double getMoveDist()
	{
		return moveDist;
	}
	
	public void setMoveDist(double moveDist)
	{
		this.moveDist = moveDist;
	}
	
	public double getRotateAngle()
	{
		return rotateAngle;
	}
	
	public void setRotateAngle(double rotateAngle)
	{
		this.rotateAngle = rotateAngle;
	}
	
	public double getMinDistFromWall()
	{
		return minDistFromWall;
	}
	
	public void setMinDistFromWall(double minDistFromWall)
	{
		this.minDistFromWall = minDistFromWall;
	}

	@Override
	public void robotMoved(Robot robot, double dist)
	{
	}

	@Override
	public void robotRotated(Robot robot, double angle)
	{
	}

	@Override
	public void robotTerminated(Robot ev3RobotDecorator)
	{
	}
}
