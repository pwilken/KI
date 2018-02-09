/*
 * Copyright © 2018 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.uni.ki.p3.mcl.map;

import java.util.List;

import de.uni.ki.p3.KIDistance;
import de.uni.ki.p3.mcl.Position;

public interface RangeMap
{
	public double distanceToWall(Position pos, double angle);
	public String strokeAt(Position pos);
	public double getWidth();
	public double getHeight();
	public boolean isClosed();
	public boolean isInside(Position pos);
	public List<MCLMarker> getMarkers();
	
	/**
	 * Measure the distance from the given pos to the given marker.<br>
	 * a distance of {@link Double#POSITIVE_INFINITY} indicates, that the marker
	 * cannot be seen from pos
	 * 
	 * @param pos the position to measure from
	 * @param marker the marker to measure to
	 * @return the distance measured to the given marker
	 */
	public KIDistance distanceToMarker(Position pos, MCLMarker marker);
}
