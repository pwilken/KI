/*
 * Copyright © 2018 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.uni.ki.p3.MCL;

import java.util.*;

public class MCLLejosResampler implements MCLResampler
{

	@Override
	public List<Particle> resample(MCL mcl, List<Particle> particles)
	{
		final int maxIterations = 1000;
		final int numParticles = mcl.getConfig().initialParticleCount;

		// Rename particles as oldParticles and create a new set
		List<Particle> oldParticles = particles;
		particles = new ArrayList<>(oldParticles);

		// Continually pick a random number and select the particles with
		// weights greater than or equal to it until we have a full
		// set of particles.
		int count = 0;
		int iterations = 0;

		while(count < numParticles)
		{
			iterations++;
			if(iterations >= maxIterations)
			{
				if(count > 0)
				{ // Duplicate the ones we have so far
					for(int i = count; i < numParticles; i++)
					{
						particles.set(i, new Particle(
							particles.get(i % count).getPos(),
							particles.get(i % count).getTheta()));
						particles.get(i).setWeight(
							particles.get(i % count).getWeight());
					}
//					return false;
					return particles;
				}
				else
				{ // Completely lost - generate a new set of particles
					for(int i = 0; i < numParticles; i++)
					{
						particles.set(i, generateParticle(mcl));
					}
//					return true;
					return particles;
				}
			}
			double rand = mcl.getConfig().random.nextDouble();
			for(int i = 0; i < numParticles && count < numParticles; i++)
			{
				if(oldParticles.get(i).getWeight() >= rand)
				{
					Particle p = oldParticles.get(i);
					double x = p.getPos().getX();
					double y = p.getPos().getY();
					double angle = p.getTheta();

					// Create a new instance of the particle and set its weight
					particles.set(count,
						new Particle(new Position(x, y), angle));
					particles.get(count++)
						.setWeight(oldParticles.get(i).getWeight());
				}
			}
		}
//		return true;
		return particles;

	}

	private Particle generateParticle(MCL mcl)
	{
		double x, y, angle;
		// Rectangle innerRect = new Rectangle(boundingRect.x + border,
		// boundingRect.y + border,
		// boundingRect.width - border * 2, boundingRect.height - border * 2);
		// Generate x, y values in bounding rectangle
		for(;;)
		{ // infinite loop that we break out of when we have
		  // generated a particle within the mapped area
			x = (mcl.getConfig().random.nextDouble() * mcl.getMap().getWidth());
			y = (mcl.getConfig().random.nextDouble()
				 * mcl.getMap().getHeight());

			if(mcl.getMap().isInside(new Position(x, y)))
				break;
		}

		// Pick a random angle
		angle = mcl.getConfig().random.nextDouble() * 360;

		return new Particle(new Position(x, y), angle);

	}
}
