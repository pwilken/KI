/*
 * Copyright © 2018 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.uni.ki.p3.SVG;

public class SvgCircle implements SvgElement
{
	private String id;
	private double cx;
	private double cy;
	private double r;
	private String stroke;
	private String fill;
	
	public SvgCircle(String id, double cx, double cy, double r, String stroke,
					 String fill)
	{
		this.id = id;
		this.cx = cx;
		this.cy = cy;
		this.r = r;
		this.stroke = stroke;
		this.fill = fill;
	}

	public String getId()
	{
		return id;
	}

	public double getCx()
	{
		return cx;
	}

	public double getCy()
	{
		return cy;
	}

	public double getR()
	{
		return r;
	}

	public String getStroke()
	{
		return stroke;
	}

	public String getFill()
	{
		return fill;
	}
}
