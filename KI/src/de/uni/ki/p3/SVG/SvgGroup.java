/*
 * Copyright © 2018 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.uni.ki.p3.SVG;

import java.util.*;

public class SvgGroup implements SvgElement, Iterable<SvgElement>
{
	private List<SvgElement> elements;
	
	public SvgGroup(List<SvgElement> elements)
	{
		this.elements = Collections.unmodifiableList(elements);
	}
	
	@Override
	public Iterator<SvgElement> iterator()
	{
		return elements.iterator();
	}
	
	public List<SvgElement> getElements()
	{
		return elements;
	}
}
