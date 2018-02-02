/*
 * Copyright © 2018 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.uni.ki.p3.robot;

public interface Robot
{
	public void move(double dist);
	public void rotate(double angle);
	public void measure();
	public void terminate();
	
	public void addRobotListener(RobotListener l);
	public void removeRobotListener(RobotListener l);
}
