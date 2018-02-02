/*
 * Copyright © 2018 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.uni.ki.p3.robot;

public interface RobotListener
{
	public void robotMoved(Robot robot, double dist);
	public void robotRotated(Robot robot, double angle);
	public void robotMeasured(Robot robot, RobotMeasurement measurement);
	public void robotTerminated(Robot ev3RobotDecorator);
}
