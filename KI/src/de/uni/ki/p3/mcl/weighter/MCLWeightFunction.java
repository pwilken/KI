/*
 * Copyright © 2018 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.uni.ki.p3.mcl.weighter;

import de.uni.ki.p3.mcl.*;
import de.uni.ki.p3.robot.RobotMeasurement;

public interface MCLWeightFunction
{
	public double calcWeight(MCL mcl, RobotMeasurement measurement,  Particle p);
}
