/*
 * Copyright © 2018 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.uni.ki.p3.SVG;

import java.io.*;
import java.util.*;

import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class SvgDocument
{
	private SvgElement root;
	
	private double width;
	private double height;

	public SvgDocument(String path)
	{
		fromSVGDocument(getSVGDocument(path));
	}

	private Document getSVGDocument(String path)
	{
		try
		{
			DocumentBuilderFactory factory  = DocumentBuilderFactory.newInstance();
		    DocumentBuilder        builder  = factory.newDocumentBuilder();
		    Document               document = builder.parse( new File( path ) );
		    
			return document;
		}
		catch(IOException | SAXException | ParserConfigurationException ex)
		{
			ex.printStackTrace();
		}

		return null;
	}

	private void fromSVGDocument(Document svg)
	{
		Element root = svg.getDocumentElement();
		
		width = Double.parseDouble(root.getAttribute("width"));
		height = Double.parseDouble(root.getAttribute("height"));

		this.root = parseList(root.getChildNodes());
	}
	
	private SvgElement parseList(NodeList nl)
	{
		List<SvgElement> elements = new ArrayList<>();
		
		for(int i = 0; i < nl.getLength(); i++)
		{
			if(nl.item(i) instanceof Element)
			{
    			Element e = (Element)nl.item(i);
    
    			elements.add(parseElement(e));
			}
		}
		
		return new SvgGroup(elements);
	}
	
	private SvgElement parseElement(Element e)
	{
		if(e.getNodeName().equals("line"))
		{
			return new SvgLine(
					Double.parseDouble(e.getAttribute("x1")),
					Double.parseDouble(e.getAttribute("y1")),
					Double.parseDouble(e.getAttribute("x2")),
					Double.parseDouble(e.getAttribute("y2")),
					e.getAttribute("stroke"));
		}
		else if(e.getNodeName().equals("rect"))
		{
			return new SvgRect(
					Double.parseDouble(e.getAttribute("x")),
					Double.parseDouble(e.getAttribute("y")),
					Double.parseDouble(e.getAttribute("width")),
					Double.parseDouble(e.getAttribute("height")),
					e.getAttribute("stroke"));
		}
		else if(e.getNodeName().equals("g"))
		{
			return parseList(e.getChildNodes());
		}
		else
		{
			// TODO $DeH
			throw new RuntimeException();
		}
	}
	
	public SvgElement getRoot()
	{
		return root;
	}
	
	public double getWidth()
	{
		return width;
	}
	
	public double getHeight()
	{
		return height;
	}
}
