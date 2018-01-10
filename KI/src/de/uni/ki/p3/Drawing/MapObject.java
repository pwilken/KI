package de.uni.ki.p3.Drawing;

import java.util.ArrayList;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

public class MapObject {
	enum Type {
		NONE,
		LINE,
		RECT
	};
	
	private ArrayList<Line> lines = new ArrayList<Line>();
	private ArrayList<Rect> rects = new ArrayList<Rect>();
	
	public void parseSVGDocument(SVGDocument svgDocument) {
		Type nodeType;
		Line line = new Line();
		Rect rect = new Rect();
		// iterate "g" nodes
		NodeList nl = svgDocument.getElementsByTagName("g");
		for(int i = 0; i < nl.getLength(); i++) {
			Node node = nl.item(i);
			System.out.println(node.getNodeName());
			
			// iterate "#text, rect and line" nodes
			NodeList nodeList = node.getChildNodes();
			for(int x = 0; x < nodeList.getLength(); x++) {
				System.out.println(nodeList.item(x).getNodeName());
				
				if(nodeList.item(x).getNodeName().equals("line")) {
					nodeType = Type.LINE;
					line = new Line();
				}
				else if (nodeList.item(x).getNodeName().equals("rect")) {
					nodeType = Type.RECT;
					rect = new Rect();
				}
				else
					nodeType = Type.NONE;
				
				// iterate "x1, x2, y1, y2, height, width" attributes
				NamedNodeMap nnm = nodeList.item(x).getAttributes();
				if(nnm != null) {
					for(int y = 0; y < nnm.getLength(); y++) {
						System.out.println(nnm.item(y).getNodeName() + " | " + nnm.item(y).getNodeValue());
						
						if(nodeType == Type.LINE)
						{
							switch(nnm.item(y).getNodeName())
							{
								case "x1": line.setX1(Double.parseDouble(nnm.item(y).getNodeValue()));
									break;
								case "x2": line.setX2(Double.parseDouble(nnm.item(y).getNodeValue()));
									break;
								case "y1": line.setY1(Double.parseDouble(nnm.item(y).getNodeValue()));
									break;
								case "y2": line.setY2(Double.parseDouble(nnm.item(y).getNodeValue()));
									break;
							}
						}
						else if(nodeType == Type.RECT)
						{	
							switch(nnm.item(y).getNodeName())
							{
								case "x": rect.setX(Double.parseDouble(nnm.item(y).getNodeValue()));
									break;
								case "y": rect.setY(Double.parseDouble(nnm.item(y).getNodeValue()));
									break;
								case "height": rect.setHeight(Double.parseDouble(nnm.item(y).getNodeValue()));
									break;
								case "width": rect.setWidth(Double.parseDouble(nnm.item(y).getNodeValue()));
									break;
							}
						}
					}
					
					if(nodeType == Type.RECT)
						rects.add(rect);
					else if(nodeType == Type.LINE)
						lines.add(line);
				}
			}
		}
	}
	
	public static void drawMapObject(GraphicsContext gc, MapObject mapObject) {
        
        for(Line line: mapObject.getLines()) {
        	double x1 = line.getX1();
        	double x2 = line.getX2();
        	double y1 = line.getY1();
        	double y2 = line.getY2();
        	gc.strokePolyline(new double[]{x1,x2}, new double[]{y1,y2}, 2);
        }
        
        for(Rect rect: mapObject.getRects()) {
        	double x = rect.getX();
        	double y = rect.getY();
        	double width = rect.getWidth();
        	double height = rect.getHeight();
        	System.out.println("x: " + x);
        	System.out.println("y: " + y);
        	System.out.println("width: " + width);
        	System.out.println("height: " + height);
        	gc.fillRect(x, y, width, height);
        }
	}

	public ArrayList<Line> getLines() {
		return lines;
	}

	public void setLines(ArrayList<Line> lines) {
		this.lines = lines;
	}

	public ArrayList<Rect> getRects() {
		return rects;
	}

	public void setRects(ArrayList<Rect> rects) {
		this.rects = rects;
	}
}
