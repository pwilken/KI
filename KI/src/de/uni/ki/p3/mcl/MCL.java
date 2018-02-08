package de.uni.ki.p3.mcl;

import java.util.*;

import de.uni.ki.p3.mcl.generator.*;
import de.uni.ki.p3.mcl.map.RangeMap;
import de.uni.ki.p3.mcl.resampler.*;
import de.uni.ki.p3.mcl.weighter.*;
import de.uni.ki.p3.robot.*;

public class MCL implements RobotListener
{
	private List<Particle> particles;
	private MCLConfiguration config;
	private MCLParticleGenerator generator;
	private MCLWeightFunction weighter;
	private MCLResampler resampler;
	private RangeMap map;
	private List<MCLListener> listener;
	
	public MCL(RangeMap map, MCLConfiguration config)
	{
		particles = new Vector<>();
		this.config = config;
		generator = new MCLLejosParticleGenerator();
		weighter = new MCLLejosWeightFunctionPixy();
		resampler = new MCLShrinkingLejosResampler();
		this.map = map;
		listener = new ArrayList<>();
	}
	
	public void initializeParticles()
	{
		for(int i = 0; i < config.initialParticleCount; ++i)
		{
			Particle p = generator.generateParticle(this);
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
	public void robotTerminated(Robot ev3RobotDecorator)
	{
	}
	
	@Override
	public void robotMeasured(Robot robot, RobotMeasurement measurement)
	{
		for(Particle p : particles)
		{
			p.setWeight(weighter.calcWeight(this, measurement, p));
		}
		
		particles = resampler.resample(this, particles);
		
		fireEvent();
	}
	
	public void normalizeWeights()
	{
		double max = 0d;
		
		for(Particle p : particles)
		{
			max = Math.max(max, p.getWeight());
		}
		
		for(Particle p : particles)
		{
			p.setWeight(p.getWeight() / max);
		}
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
	
	public MCLParticleGenerator getGenerator()
	{
		return generator;
	}
	
	public void setGenerator(MCLParticleGenerator generator)
	{
		this.generator = generator;
	}
	
	public MCLWeightFunction getWeighter()
	{
		return weighter;
	}
	
	public void setWeighter(MCLWeightFunction weighter)
	{
		this.weighter = weighter;
	}
	
	public MCLResampler getResampler()
	{
		return resampler;
	}
	
	public void setResampler(MCLResampler resampler)
	{
		this.resampler = resampler;
	}
	
	public MCLConfiguration getConfig()
	{
		return config;
	}
	
	public RangeMap getMap()
	{
		return map;
	}
	
	public Particle getBest()
	{
		final int numParticles = particles.size();
//		final int numParticles = config.initialParticleCount;
		
	    double totalWeights = 0;
	    double estimatedX = 0;
	    double estimatedY = 0;
	    double estimatedAngle = 0;
//	    double varX = 0;
//	    double varY = 0;
//	    double varH = 0;
	    double minX = 1000000d;
	    double minY = 1000000d;
	    double maxX = -1000000d;
	    double maxY = -1000000d;

	    for (int i = 0; i < numParticles; i++)
	    {
	      Particle p = particles.get(i);
	      double x = p.getPos().getX();
	      double y = p.getPos().getY();
	      //float weight = particles.getParticle(i).getWeight();
	      double weight = 1; // weight is historic at this point, as resample has been done
	      estimatedX += (x * weight);
//	      varX += (x * x * weight);
	      estimatedY += (y * weight);
//	      varY += (y * y * weight);
	      double head = p.getTheta();
	      estimatedAngle += (head * weight);
//	      varH += (head * head * weight);
	      totalWeights += weight;

	      if (x < minX)  minX = x;

	      if (x > maxX)maxX = x;
	      if (y < minY)minY = y;
	      if (y > maxY)   maxY = y;
	    }

	    estimatedX /= totalWeights;
//	    varX /= totalWeights;
//	    varX -= (estimatedX * estimatedX);
	    estimatedY /= totalWeights;
//	    varY /= totalWeights;
//	    varY -= (estimatedY * estimatedY);
	    estimatedAngle /= totalWeights;
//	    varH /= totalWeights;
//	    varH -= (estimatedAngle * estimatedAngle);
	    
	    // Normalize angle
	    while (estimatedAngle > 180) estimatedAngle -= 360;
	    while (estimatedAngle < -180) estimatedAngle += 360;
	    
	    return new Particle(new Position(estimatedX, estimatedY),  estimatedAngle);
	  }
}