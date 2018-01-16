package de.uni.ki.p3;

import de.uni.ki.p3.Drawing.*;
import javafx.scene.canvas.GraphicsContext;

public class Robot {
	private float positionX = -1;
	private float positionY = -1;
	private float measure = -1;
	private float heading = 0; //direction in which the bot is looking

	public Robot(float positionX, float positionY) {
		this.positionX = positionX;
		this.positionY = positionY;
	}
	
	public Robot(float positionX, float positionY, float heading) {
		this.positionX = positionX;
		this.positionY = positionY;
		this.heading = heading;
	}

	public void move(float heading, float speed, GraphicsContext gc) {

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
		}
	}

	public void measure() {
		// ToDo: implement measurement with SVGDocument, positionX, positionY and heading
		this.measure = -1;
	}
}
