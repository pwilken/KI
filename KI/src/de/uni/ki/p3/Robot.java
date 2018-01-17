package de.uni.ki.p3;

import org.w3c.dom.svg.SVGDocument;

import de.uni.ki.p3.Drawing.*;
import javafx.scene.canvas.GraphicsContext;

public class Robot {
	private float positionX = -1;
	private float positionY = -1;
	private float measure = -1;
	private float heading = 0; //direction in which the bot is looking
	private MapObject map;

	public Robot(float positionX, float positionY, MapObject map) {
		this.positionX = positionX;
		this.positionY = positionY;
		this.map = map;
	}
	
	public Robot(float positionX, float positionY, float heading, MapObject map) {
		this.positionX = positionX;
		this.positionY = positionY;
		this.heading = heading;
		this.map = map;
	}

	public void move(float heading, float speed, GraphicsContext gc) {
		gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
		
		if(heading == 0)
			positionX--;
		else if(heading == 90)
			positionY--;
		else if(heading == 180)
			positionX++;
		else
			positionY++;
		
		
		// we just draw the robot if we know the position (or we are at least quite sure)
		// ToDo: Nach jedem Movement müssen die Partikel neu generiert werden
		if (positionX != -1 && positionY != -1) {
			Particle.Draw(positionX, positionY, heading, 0, gc, true);
			measure();
		}
	}

	public void measure() {
		// ToDo: implement measurement with SVGDocument, positionX, positionY and heading
		boolean infinity = true;
		
		for(Line l : map.getLines())
		{
			if(positionX*Main.DrawFactor > l.getX1() && positionX*Main.DrawFactor < l.getX2() && l.getY1()+l.getY2() > 0)
			{
				float distance = (float)(l.getY2() - positionY);
				System.out.println("Distance: " + distance);
				infinity = false;
				break;
			}
		}
		
		if(infinity)
			System.out.println("Distance: infinity");
		
		this.measure = -1;
	}
}
