/*
 * Copyright © 2017 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.uni.ki.p1;

import lejos.robotics.Color;

public class Ev3Main
{
	public static void main(String[] args) throws Exception
	{
		LocalRobot r = new LocalRobot();
		
		r.connect();
	}
	
	public static String ColorToString(int i)
	{
		switch(i)
		{
			case Color.RED:
				return "RED";
			case Color.GREEN:
				return "GREEN";
			case Color.BLUE:
				return "BLUE";
			case Color.YELLOW:
				return "YELLOW";
			case Color.MAGENTA:
				return "MAGENTA";
			case Color.ORANGE:
				return "ORANGE";
			case Color.WHITE:
				return "WHITE";
		    case Color.BLACK:
		    	return "BLACK";
		    case Color.PINK:
		    	return "PINK";
		    case Color.GRAY:
		    	return "GRAY";
		    case Color.LIGHT_GRAY:
		    	return "LIGHT_GRAY";
		    case Color.DARK_GRAY:
		    	return "DARK_GRAY";
		    case Color.CYAN:
		    	return "CYAN";
		    case Color.BROWN:
		    	return "BROWN";
	    	default:
	    		return "None";
		}
	}
}
