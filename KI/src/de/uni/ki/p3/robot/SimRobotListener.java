/*
 * Copyright © 2018 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.uni.ki.p3.robot;

import de.uni.ki.p3.MCL.Position;

public interface SimRobotListener
{
	public void posChanged(SimRobot robot, Position pos);
	public void thetaChanged(SimRobot robot, double theta);
}
