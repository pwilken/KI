/*
 * Copyright © 2018 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.uni.ki.p3.MCL;

import java.util.*;

import de.uni.ki.p3.KIDistance;
import de.uni.ki.p3.SVG.*;

public class SvgLejosRangeMap implements RangeMap
{
	private double width;
	private double height;
	private List<Line> lines;
	private List<MCLMarker> markers;
	private List<SvgCircle> circles;

	public SvgLejosRangeMap(SvgDocument svg)
	{
		width = svg.getWidth();
		height = svg.getHeight();
		lines = new ArrayList<>();
		markers = new ArrayList<>();
		circles = new ArrayList<>();

		parse(svg.getRoot());
	}

	private void parse(SvgElement e)
	{
		if(e instanceof SvgLine)
		{
			SvgLine l = (SvgLine)e;
			lines.add(new Line(l.getX0(), l.getY0(), l.getX1(), l.getY1()));
		}
		else if(e instanceof SvgRect)
		{

		}
		else if(e instanceof SvgCircle)
		{
			SvgCircle c = (SvgCircle)e;
			circles.add(c);
			markers.add(new MCLMarker(c.getId(), c.getStroke()));
		}
		else if(e instanceof SvgGroup)
		{
			SvgGroup g = (SvgGroup)e;
			for(SvgElement ee : g)
			{
				parse(ee);
			}
		}
	}

	@Override
	public double distanceToWall(Position pos, double angle)
	{
		Line l = new Line(
			pos.getX(),
			pos.getY(),
			pos.getX() + 254f * Math.cos(Math.toRadians(angle)),
			pos.getY() + 254f * Math.sin(Math.toRadians(angle)));
		Line rl = null;

		for(Line ll : lines)
		{
			Position p = ll.intersectsAt(l);
			if(p == null)
			{
				continue; // Does not intersect
			}
			Line tl = new Line(pos.getX(), pos.getY(), p.getX(), p.getY());

			// If the range line intersects more than one map line
			// then take the shortest distance.
			if(rl == null || tl.length() < rl.length())
			{
				rl = tl;
			}
		}
		return (rl == null ? Double.POSITIVE_INFINITY : rl.length());
	}

	@Override
	public String strokeAt(Position pos)
	{
		return null;
	}

	@Override
	public double getWidth()
	{
		return width;
	}

	@Override
	public double getHeight()
	{
		return height;
	}

	@Override
	public boolean isClosed()
	{
		return true;
	}

	@Override
	public boolean isInside(Position pos)
	{
		if(pos.getX() < 0 || pos.getY() < 0)
		{
			return false;
		}
		if(pos.getX() > width
		   || pos.getY() > height)
		{
			return false;
		}

		// Create a line from the point to the left
		Line l = new Line(pos.getX(), pos.getY(), pos.getX() - width, pos.getY());
		Line l2 = new Line(pos.getX(), pos.getY(), pos.getX() + width, pos.getY());

		// Count intersections
		int count = 0;
		int count2 = 0;
		for(Line ll : lines)
		{
			if(ll.intersectsAt(l) != null)
			{
				count++;
			}
			if(ll.intersectsAt(l2) != null)
			{
				count2++;
			}
		}
		// We are inside if the number of intersections is odd
		return count % 2 == 1 || count2 % 2 == 1;
	}
	
	@Override
	public List<MCLMarker> getMarkers()
	{
		return markers;
	}
	
	@Override
	public KIDistance distanceToMarker(Position pos, MCLMarker marker)
	{
		SvgCircle c = getMarkerCircle(marker);
		Line line = new Line(pos.getX(), pos.getY(), c.getCx(), c.getCy());
		
		for(Line l : lines)
		{
			if(line.intersectsAt(l) != null)
			{
				return new KIDistance(Double.POSITIVE_INFINITY, -1);
			}
		}
		
		double dx = line.x2 - line.x1;
		double dy = line.y2 - line.y1;
		
		double angle =  Math.toDegrees(
							Math.atan2(dy, dx));
		
		if(angle < 0)
		{
			angle += 360;
		}
		
		return new KIDistance(Math.sqrt(dx * dx + dy * dy), angle);
	}

	private SvgCircle getMarkerCircle(MCLMarker marker)
	{
		for(SvgCircle c : circles)
		{
			if(c.getId().equals(marker.getId()))
			{
				return c;
			}
		}
		return null;
	}

	private class Line
	{
		public double x1;
		public double y1;
		public double x2;
		public double y2;

		public Line(double x1, double y1, double x2, double y2)
		{
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
		}

		/**
		 * Calculate the point of intersection of two lines.
		 * 
		 * @param l
		 *            the second line
		 * 
		 * @return the point of intersection or null if the lines do not
		 *         intercept or are coincident
		 */
		public Position intersectsAt(Line l)
		{
			double x, y, a1, a2, b1, b2;

			if(y2 == y1 && l.y2 == l.y1)
			{
				return null; // horizontal parallel
			}
			if(x2 == x1 && l.x2 == l.x1)
			{
				return null; // vertical parallel
			}

			// Find the point of intersection of the lines extended to infinity
			if(x1 == x2 && l.y1 == l.y2)
			{ // perpendicular
				x = x1;
				y = l.y1;
			}
			else if(y1 == y2 && l.x1 == l.x2)
			{ // perpendicular
				x = l.x1;
				y = y1;
			}
			else if(y2 == y1 || l.y2 == l.y1)
			{ // one line is horizontal
				a1 = (y2 - y1) / (x2 - x1);
				b1 = y1 - a1 * x1;
				a2 = (l.y2 - l.y1) / (l.x2 - l.x1);
				b2 = l.y1 - a2 * l.x1;

				if(a1 == a2)
				{
					return null; // parallel
				}
				x = (b2 - b1) / (a1 - a2);
				y = a1 * x + b1;
			}
			else
			{
				a1 = (x2 - x1) / (y2 - y1);
				b1 = x1 - a1 * y1;
				a2 = (l.x2 - l.x1) / (l.y2 - l.y1);
				b2 = l.x1 - a2 * l.y1;

				if(a1 == a2)
				{
					return null; // parallel
				}
				y = (b2 - b1) / (a1 - a2);
				x = a1 * y + b1;
			}

			// Check that the point of intersection is within both line segments
			if(!between(x, x1, x2))
			{
				return null;
			}
			if(!between(y, y1, y2))
			{
				return null;
			}
			if(!between(x, l.x1, l.x2))
			{
				return null;
			}
			if(!between(y, l.y1, l.y2))
			{
				return null;
			}

			return new Position(x, y);
		}

		/**
		 * Return true iff x is between x1 and x2
		 */
		private boolean between(double x, double x1, double x2)
		{
			if(x1 <= x2 && x >= x1 && x <= x2)
			{
				return true;
			}
			if(x2 < x1 && x >= x2 && x <= x1)
			{
				return true;
			}
			return false;
		}

		/**
		 * Return the length of the line
		 * 
		 * @return the length of the line
		 */
		public double length()
		{
			return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
		}
	}
}
