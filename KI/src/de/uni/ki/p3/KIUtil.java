/*
 * Copyright © 2018 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.uni.ki.p3;

public class KIUtil
{
	public static boolean equals(double d1, double d2)
	{
		return positiveDistance(d1, d2) < 0.001;
	}
	
	public static double positiveDistance(double d1, double d2)
	{
		return d1 > d2 ? d1 - d2 : d2 - d1;
	}
}
