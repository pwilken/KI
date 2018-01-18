/*
 * Copyright © 2018 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.uni.ki.p3.MCL;

import java.util.*;

import de.uni.ki.p3.SVG.*;

public class SvgRangeMap implements RangeMap
{
	private SvgDocument svg;
	private List<Line> lines;
	private List<SvgRect> rects;
	
	public SvgRangeMap(SvgDocument svg)
	{
		lines = new ArrayList<>();
		rects = new ArrayList<>();
		
		this.svg = svg;
		
		parse(svg.getRoot());
	}
	
	private void parse(SvgElement e)
	{
		if(e instanceof SvgLine)
		{
			lines.add(new Line((SvgLine)e));
		}
		else if(e instanceof SvgRect)
		{
			rects.add((SvgRect)e);
		}
		else if(e instanceof SvgGroup)
		{
			for(SvgElement ee : ((SvgGroup)e))
			{
				parse(ee);
			}
		}
		else
		{
			throw new RuntimeException();
		}
	}

	@Override
	public double distanceToWall(Position pos, double angle)
	{
		double minDist = 0d;
		
		for(Line line : lines)
		{
			double dist = calcDist(pos, angle, line);
			
			minDist = Math.min(minDist, dist);
		}
		
		return minDist;
	}

	private double calcDist(Position pos, double angle, Line line)
	{
		// https://stackoverflow.com/questions/563198/whats-the-most-efficent-way-to-calculate-where-two-line-segments-intersect
		Line l2 = new Line(pos, angle);
		
		Position p = l2.pos;
		Position r = l2.delta;
		Position q = line.pos;
		Position s = line.delta;
		
		if(Math.abs(r.cross(s)) < 0.001)
		{
			if(Math.abs(q.subtract(p).cross(r)) < 0.001)
            {
            	return Double.POSITIVE_INFINITY;
            }
			else
			{
				return Double.POSITIVE_INFINITY;
			}
		}
		else
		{
			double u = q.subtract(p).cross(r) / r.cross(s);
			double t = q.subtract(p).cross(s) / r.cross(s);
			
			if(u <= 1.001 && t <= 1.001)
			{
				double meetX = l2.pos.getX() + t * l2.delta.getX();
				double meetY = l2.pos.getY() + t * l2.delta.getY();
				return Math.sqrt(
					meetX * meetX + meetY * meetY);
			}
			else
			{
				return Double.POSITIVE_INFINITY;
			}
		}
	}

	@Override
	public String strokeAt(Position pos)
	{
		for(SvgRect rect : rects)
		{
			if(rect.contains(pos.getX(), pos.getY()))
			{
				return rect.getStroke();
			}
		}
		
		return null;
	}

	@Override
	public double getWidth()
	{
		return svg.getWidth();
	}

	@Override
	public double getHeight()
	{
		return svg.getHeight();
	}

	private static class Line
	{
		private Position pos;
		private Position delta;
		
		public Line(SvgLine line)
		{
			pos = new Position(line.getX0(), line.getY0());
			delta = new Position(
    			line.getX1() > line.getX0() ? line.getX1() - line.getX0() : -line.getX1() + line.getX0(),
    			line.getY1() > line.getY0() ? line.getY1() - line.getY0() : -line.getY1() + line.getY0());
		}
		
		public Line(Position pos, double angle)
		{
			this.pos = pos;
			delta = new Position(
				pos.getX() * Math.cos(Math.toRadians(angle)),
				pos.getY() * Math.sin(Math.toRadians(angle)));
		}
	}
}
