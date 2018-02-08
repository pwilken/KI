/*
 * Copyright © 2018 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.uni.ki.p3.MCL;

import java.util.*;

public class MCLShrinkingLejosResampler implements MCLResampler
{
	@Override
	public List<Particle> resample(MCL mcl, List<Particle> particles)
	{
		final int maxIterations = mcl.getConfig().maxIterations;

		// save the old particles for later usage
		List<Particle> oldParticles = particles;
		
		particles = new ArrayList<>();

		for(int iterations = 0;
			iterations < maxIterations && particles.size() < oldParticles.size();
			++iterations)
		{
			tryCreateParticles(mcl, oldParticles, particles);
		}
		
		if(particles.isEmpty())
		{
			// Completely lost - generate a new set of particles
			generateNewList(mcl, particles, mcl.getConfig().initialParticleCount);
		}
		
		removeDuplicates(particles);
		
		return particles;

	}
	
	private void removeDuplicates(List<Particle> particles)
	{
		for(int i = 0; i < particles.size(); i++)
		{
			Particle p1 = particles.get(i);
			for(int j = i + 1; j < particles.size(); j++)
			{
				Particle p2 = particles.get(j);
				
				if(p1.equals(p2))
				{
					particles.remove(j);
					j--;
				}
			}
		}
		
	}

	private void tryCreateParticles(MCL mcl, List<Particle> oldParticles, List<Particle> particles)
	{
		double rand = mcl.getConfig().random.nextDouble();
		for(int i = 0; i < oldParticles.size() && particles.size() < oldParticles.size(); i++)
		{
			if(oldParticles.get(i).getWeight() >= rand)
			{
				particles.add(copy(oldParticles.get(i)));
			}
		}
	}

	private Particle copy(Particle p)
	{
		// Create a new instance of the particle and set its weight
		Particle newP = new Particle(new Position(p.getPos().getX(), p.getPos().getY()), p.getTheta());
		newP.setWeight(p.getWeight());
		return newP;
	}

	private void generateNewList(MCL mcl, List<Particle> particles, int numParticles)
	{
		particles.clear();
		for(int i = 0; i < numParticles; i++)
		{
			particles.add(mcl.getGenerator().generateParticle(mcl));
		}
	}
}
