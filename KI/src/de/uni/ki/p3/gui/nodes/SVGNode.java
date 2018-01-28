/*
 * Copyright © 2018 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.uni.ki.p3.gui.nodes;

import de.uni.ki.p3.SVG.*;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

public class SVGNode extends Group
{
	private ObjectProperty<SvgDocument> svg;

	public SVGNode()
	{
		svg = new SimpleObjectProperty<SvgDocument>(this, "svg", null);

		svg.addListener(
			new ChangeListener<SvgDocument>()
			{
				@Override
				public void changed(
								ObservableValue<? extends SvgDocument> observable,
								SvgDocument oldValue, SvgDocument newValue)
				{
					rebuild();
				}
			});
	}

	public SVGNode(SvgDocument doc)
	{
		this();

		setSvg(doc);
	}

	private void rebuild()
	{
		getChildren().clear();
		
		if(getSvg() == null)
		{
			return;
		}
		
		getChildren().add(buildNode(getSvg().getRoot()));
	}

	private Node buildNode(SvgElement e)
	{
		if(e instanceof SvgLine)
		{
			SvgLine line = (SvgLine)e;
			return new Line(line.getX0(), line.getY0(), line.getX1(), line.getY1());
		}
		else if(e instanceof SvgRect)
		{
			SvgRect rect = (SvgRect)e;
			Rectangle r = new Rectangle(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
			r.setFill(Color.BLACK);
			return r;
		}
		else if(e instanceof SvgGroup)
		{
			Group g = new Group();
			for(SvgElement ee : ((SvgGroup)e))
			{
				g.getChildren().add(buildNode(ee));
			}
			return g;
		}
		else
		{
			throw new RuntimeException();
		}
	}

	public ObjectProperty<SvgDocument> svgProperty()
	{
		return svg;
	}

	public void setSvg(SvgDocument value)
	{
		svg.set(value);
	}

	public SvgDocument getSvg()
	{
		return svg.get();
	}
}
