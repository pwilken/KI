/*
 * Copyright © 2018 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.uni.ki.p3.MCL;

import java.util.Random;

public class MCLConfiguration
{
	public int initialParticleCount;
	
	public double initialParticlePosX = Double.MIN_VALUE;
	public double initialParticlePosY = Double.MIN_VALUE;
	public double initialParticlePosWidth = Double.MAX_VALUE;
	public double initialParticlePosHeight = Double.MAX_VALUE;
	
	public double minAngle = 0d;
	public double maxAngle = 360d;
	
	public Random random = new Random();
}
