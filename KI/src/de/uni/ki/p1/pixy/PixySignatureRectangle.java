/*
 * Copyright © 2018 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.uni.ki.p1.pixy;

import lejos.robotics.geometry.RectangleInt32;

// http://cmucam.org/attachments/1290/Pixy_LEGO_Protocol_1.0.pdf

public class PixySignatureRectangle extends RectangleInt32
{
	private int signature;

	public PixySignatureRectangle(int signature, int x, int y, int width,
								  int height)
	{
		super(x, y, width, height);
		this.signature = signature;
	}

	public int getSignature()
	{
		return signature;
	}

	public void setSignature(int signature)
	{
		this.signature = signature;
	}
}