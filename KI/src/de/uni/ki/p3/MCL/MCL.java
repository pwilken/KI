package de.uni.ki.p3.MCL;

import lejos.robotics.Color;

import java.util.*;

import de.uni.ki.p3.KIUtil;
import de.uni.ki.p3.robot.*;

public class MCL implements RobotListener
{
	private int numParticles;
	private List<Particle> particles;
	private RangeMap map;
	private List<MCLListener> listener;
	
	public MCL(int numParticles, RangeMap map, Robot robot)
	{
		particles = new ArrayList<>();
		this.numParticles = numParticles;
		this.map = map;
		listener = new ArrayList<>();
		
		robot.addRobotListener(this);
	}
	
	public void initializeParticles()
	{
		initializeParticles(0, 0, map.getWidth(), map.getHeight());
	}
	
	public void initializeParticles(double x, double y, double width,
					double height)
	{
		for(int i = 0; i < numParticles; ++i)
		{
			Particle p = new Particle(
				new Position(
    				x + Math.random() * width,
    				y + Math.random() * height),
//				Math.random() * 360);
				0);
			particles.add(p);
		}
		
		fireEvent();
	}
	
	@Override
	public void robotMoved(Robot robot, double dist)
	{
		for(Particle p : particles)
		{
			p.move(dist);
		}
		
		fireEvent();
	}
	
	@Override
	public void robotRotated(Robot robot, double angle)
	{
		for(Particle p : particles)
		{
			p.rotate(angle);
		}
		
		fireEvent();
	}
	
	@Override
	public void robotMeasured(Robot robot, RobotMeasurement measurement)
	{
		for(Particle p : particles)
		{
			p.setWeight(calcWeight(p, measurement));
		}
		
		resample();
		
		fireEvent();
	}

	private double calcWeight(Particle p, RobotMeasurement measurement)
	{
		// max weight is 1
		double weight = 1000d;
		
		// 250 depends on color
		if(measurement.getColorId() == Color.BLACK 
				^ map.strokeAt(p.getPos()) != null)
		{
			weight -= 250;
		}
		
		// 750 depends on distance scan
		double dist = map.distanceToWall(p.getPos(), p.getTheta() + measurement.getDistAngle());
		System.out.println(p.getPos() + " - " + dist);
		double d = KIUtil.positiveDistance(dist, measurement.getDist());
		if(d < measurement.getDist())
		{
			weight -= ((d / measurement.getDist()) * 750);
		}
		
		return weight;
	}

	private void resample()
	{
		int N = particles.size();
		List<Particle> new_particles = new ArrayList<Particle>();

		double incr = 0;
		int index = 0;

		for(int i = 0; i < N; i++)
		{
			incr += particles.get(i).getWeight();
		}

		incr = incr / 2.0 / N;
		double beta = incr;

		for(int i = 0; i < N; i++)
		{
			while(beta > particles.get(index).getWeight())
			{
				beta -= particles.get(index).getWeight();
				index = (index + 1) % N;
			}

			beta += incr;
			new_particles.add(particles.get(index).clone());
		}
		particles.clear();
		particles.addAll(new_particles);
	}

	public List<Particle> getParticles()
	{
		return Collections.unmodifiableList(particles);
	}
	
	public void addMclListener(MCLListener l)
	{
		listener.add(l);
	}
	
	public void removeMclListener(MCLListener l)
	{
		listener.remove(l);
	}
	
	public void fireEvent()
	{
		for(MCLListener l : listener)
		{
			l.particlesChanged(this);
		}
	}
}