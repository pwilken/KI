/*
 * Copyright © 2018 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.uni.ki.p3.MCL;

public class MCLLejosParticleGenerator implements MCLParticleGenerator
{

	@Override
	public Particle generateParticle(MCL mcl)
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
