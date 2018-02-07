/*
 * Copyright © 2018 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.uni.ki.p1.pixy;

import java.util.LinkedList;

public class ColorCode
{
	private LinkedList<Integer> signatures;

	public ColorCode(int decimalHigherNumber, int decimalLowerNumber)
	{
		this.signatures = new LinkedList<Integer>();

		addToSignatures(decimalLowerNumber);
		addToSignatures(decimalHigherNumber);
	}

	public ColorCode(String colorCode)
	{
		this.signatures = new LinkedList<Integer>();

		for(char pos : new StringBuffer(colorCode).reverse()
			.toString()
			.toCharArray())
		{
			int octalNumber = Character.getNumericValue(pos);
			if(octalNumber < 0 || octalNumber > 7)
			{
				throw new IllegalArgumentException(
					"Signature must be between 1 and 7");
			}
			signatures.push(octalNumber);
		}
	}

	private void addToSignatures(int decimalNumber)
	{
		while(decimalNumber != 0)
		{
			int signature = decimalNumber % 8;
			signatures.push(signature);
			decimalNumber /= 8;
		}
	}

	@Override
	public String toString()
	{
		String colorCode = "";

		if(signatures.isEmpty())
		{
			return colorCode;
		}

		for(int signature : signatures)
		{
			colorCode = colorCode + signature;
		}
		return colorCode;
	}
}