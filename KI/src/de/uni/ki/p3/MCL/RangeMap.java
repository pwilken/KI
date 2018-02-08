/*
 * Copyright © 2018 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.uni.ki.p3.MCL;

import java.util.List;

import de.uni.ki.p3.KIDistance;

public interface RangeMap
{
	public double distanceToWall(Position pos, double angle);
	public String strokeAt(Position pos);
	public double getWidth();
	public double getHeight();
	public boolean isClosed();
	public boolean isInside(Position pos);
	public List<MCLMarker> getMarkers();
	public KIDistance distanceToMarker(Position pos, MCLMarker marker);
}
