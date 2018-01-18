/*
 * Copyright © 2018 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.uni.ki.p3.SVG;

public class SvgRect implements SvgElement
{
	private double x;
	private double y;
	private double width;
	private double height;
	private String stroke;
	
	public SvgRect(double x, double y, double width, double height, String stroke)
	{
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.stroke = stroke;
	}
	
	public double getHeight()
	{
		return height;
	}
	
	public String getStroke()
	{
		return stroke;
	}
	
	public double getWidth()
	{
		return width;
	}
	
	public double getX()
	{
		return x;
	}
	
	public double getY()
	{
		return y;
	}

	public boolean contains(double x, double y)
	{
		return x >= this.x && x <= this.x + width
				&& y >= this.y && y <= this.y + height;
	}
}
