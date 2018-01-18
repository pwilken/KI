/*
 * Copyright © 2018 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.uni.ki.p3.MCL;

public interface RangeMap
{
	public double distanceToWall(Position pos, double angle);
	public String strokeAt(Position pos);
	public double getWidth();
	public double getHeight();
}
