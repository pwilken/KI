/*
 * Copyright © 2018 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.uni.ki.p3.MCL;

public class MCLMarker
{
	private String id;
	private String stroke;
	
	public MCLMarker(String id, String stroke)
	{
		this.id = id;
		this.stroke = stroke;
	}
	
	public String getId()
	{
		return id;
	}
	
	public String getStroke()
	{
		return stroke;
	}
}
