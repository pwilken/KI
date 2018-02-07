/*
 * Copyright © 2018 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.uni.ki.p1.pixy;

import lejos.robotics.geometry.RectangleInt32;

// http://cmucam.org/attachments/1290/Pixy_LEGO_Protocol_1.0.pdf

public class PixyColorCodeRectangle extends RectangleInt32
{
	private ColorCode colorCode;
	private int angle;

	public PixyColorCodeRectangle(ColorCode colorCode, int angle, int x, int y,
								  int width, int height)
	{
		super(x, y, width, height);
		this.angle = angle;
		this.colorCode = colorCode;
	}

	public ColorCode getColorCode()
	{
		return colorCode;
	}

	public void setSignature(ColorCode colorCode)
	{
		this.colorCode = colorCode;
	}

	public int getAngle()
	{
		return angle;
	}

	public void setAngle(int angle)
	{
		this.angle = angle;
	}
}