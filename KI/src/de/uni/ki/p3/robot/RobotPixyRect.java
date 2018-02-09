/*
 * Copyright © 2018 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.uni.ki.p3.robot;

public class RobotPixyRect
{
	private int[] colorCodes;
	private int x;
	private int y;
	private int width;
	private int height;
	private int angle;
	
	public RobotPixyRect(String colorCode, int x, int y, int width, int height, int angle)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.angle = angle;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	public int getWidth()
	{
		return width;
	}
	public int getHeight()
	{
		return height;
	}
	public int getAngle()
	{
		return angle;
	}
	
	public int[] getColorCodes()
	{
		return colorCodes;
	}
}
