/*
 * Copyright © 2018 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.uni.ki.p3.MCL;

import java.util.*;

public class MCLDefaultResampler implements MCLResampler
{

	@Override
	public List<Particle> resample(MCL mcl, List<Particle> particles)
	{
		Collections.sort(particles);
		int N = particles.size();
		List<Particle> newParticles = new ArrayList<Particle>();

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
			Particle p = creVariantOf(mcl, particles.get(index), mcl.getMap());
			
			newParticles.add(p);
		}
		return newParticles;
	}

	private Particle creVariantOf(MCL mcl, Particle p, RangeMap map)
	{
		for(int i = 0; i < 10; ++i)
		{
    		Particle pp = new Particle(
                			new Position(
                				p.getPos().getX() + (mcl.getConfig().random.nextDouble() * mcl.getConfig().xTolerance - (mcl.getConfig().xTolerance / 2)),
                				p.getPos().getY() + (mcl.getConfig().random.nextDouble() * mcl.getConfig().yTolerance - (mcl.getConfig().yTolerance / 2))),
//                			p.getTheta() + (Math.random() * 20 - 10));
                    		p.getTheta() + (mcl.getConfig().random.nextDouble() * mcl.getConfig().angleTolerance - (mcl.getConfig().angleTolerance / 2)));
    		
    		if(mcl.getMap().isInside(pp.getPos()))
    		{
    			return pp;
    		}
		}
		
		Particle pp = new Particle(
			new Position(
				p.getPos().getX(),
				p.getPos().getY()),
				p.getTheta());
		return pp;
	}

}
