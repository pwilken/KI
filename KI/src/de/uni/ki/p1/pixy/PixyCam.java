/*
 * Copyright © 2018 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.uni.ki.p1.pixy;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.I2CSensor;

// http://cmucam.org/attachments/1290/Pixy_LEGO_Protocol_1.0.pdf

public class PixyCam extends I2CSensor
{

	private static final byte VERSION_QUERY_ADDRESS = 0x00;
	private static final byte VENDOR_ID_QUERY_ADDRESS = 0x08;
	private static final byte DEVICE_ID_QUERY_ADDRESS = 0x10;
	private static final byte GENERAL_QUERY_ADDRESS = 0x50;
	private static final byte COLOR_CODE_QUERY_ADDRESS = 0x58;
	private static final byte ANGLE_QUERY_ADDRESS = 0x60;

	public PixyCam(Port port)
	{
		super(port, DEFAULT_I2C_ADDRESS);
	}

	@Override
	public String getVersion()
	{
		return fetchString(VERSION_QUERY_ADDRESS, 8);
	}

	@Override
	public String getVendorID()
	{
		return fetchString(VENDOR_ID_QUERY_ADDRESS, 8);
	}

	public String getDeviceID()
	{
		return fetchString(DEVICE_ID_QUERY_ADDRESS, 8);
	}

	public PixySignatureRectangle getLargestDetectedBlock()
	{
		byte[] buffer = new byte[7];
		getData(GENERAL_QUERY_ADDRESS, buffer, 6);
		return new PixySignatureRectangle(
			(buffer[0] & 0xFF),
			(buffer[2] & 0xFF),
			(buffer[3] & 0xFF),
			(buffer[4] & 0xFF),
			(buffer[5] & 0xFF));
	}

	public PixyColorCodeRectangle getLargestDetectedColorCodeBlock()
	{
		byte[] buffer = new byte[7];
		getData(GENERAL_QUERY_ADDRESS, buffer, 6);
		return new PixyColorCodeRectangle(
			new ColorCode(((buffer[1] & 0xFF) >> 1), (buffer[0] & 0xFF)),
			getAngle(),
			(buffer[2] & 0xFF),
			(buffer[3] & 0xFF),
			(buffer[4] & 0xFF),
			(buffer[5] & 0xFF));
	}

	public PixySignatureRectangle getSignature(int i)
	{
		if(i > 7 || i < 0)
		{
			throw new IllegalArgumentException(
				"signature must be between 1 and 7");
		}

		byte[] buffer = new byte[7];

		getData(GENERAL_QUERY_ADDRESS + i, buffer, 5);

		return new PixySignatureRectangle(
			(buffer[0] & 0xFF),
			(buffer[1] & 0xFF),
			(buffer[2] & 0xFF),
			(buffer[3] & 0xFF),
			(buffer[4] & 0xFF));
	}

	/*
	 * public PixyColorCodeRectangle getColorCode(ColorCode colorCode)
	 * {
	 * TODO: need to be implemented (use COLOR_CODE_QUERY_ADDRESS)
	 * }
	 */

	private int getAngle()
	{
		byte[] buffer = new byte[1];
		getData(ANGLE_QUERY_ADDRESS, buffer, 1);
		return buffer[0] & 0xFF;
	}
}