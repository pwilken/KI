/*
 * Copyright © 2018 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.uni.ki.p3.MCL;

import java.util.*;

public class MCLConfiguration
{
	public int initialParticleCount;
	
	public double initialParticlePosX = Double.MIN_VALUE;
	public double initialParticlePosY = Double.MIN_VALUE;
	public double initialParticlePosWidth = Double.MAX_VALUE;
	public double initialParticlePosHeight = Double.MAX_VALUE;
	
	public double sigma = 400d;
	public int maxIterations = 1000;
	
	public double minAngle = 0d;
	public double maxAngle = 360d;
	
	public double xTolerance = 0d;
	public double yTolerance = 0d;
	public double angleTolerance = 0;
	
	public final Map<Integer, String> mapColorCodeToStroke = new HashMap<>();
	
	public Random random = new Random();
}
