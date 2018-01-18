/*
 * Copyright © 2018 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.uni.ki.p3.SVG;

public class SvgLine implements SvgElement
{
	private double x0;
	private double y0;
	private double x1;
	private double y1;
	private String stroke;
	
	public SvgLine(double x0, double y0, double x1, double y1, String stroke)
	{
		super();
		this.x0 = x0;
		this.y0 = y0;
		this.x1 = x1;
		this.y1 = y1;
		this.stroke = stroke;
	}
	
	public String getStroke()
	{
		return stroke;
	}
	
	public double getX0()
	{
		return x0;
	}
	
	public double getX1()
	{
		return x1;
	}
	
	public double getY0()
	{
		return y0;
	}
	
	public double getY1()
	{
		return y1;
	}
}
