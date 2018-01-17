package de.uni.ki.p3;

import de.uni.ki.p3.Drawing.*;
import de.uni.ki.p3.MCL.MCLParticle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Robot implements Drawable {
	private float x = -1;
	private float y = -1;
	private float measure = -1;
	private float heading = 0; //direction in which the bot is looking

    public static final float WIDTH = MCLParticle.WIDTH * 2;
    public static final float HEIGHT = MCLParticle.HEIGHT * 2;

	public Robot(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Robot(float x, float y, float heading) {
		this.x = x;
		this.y = y;
		this.heading = heading;
	}

	public void move(float heading, float speed, GraphicsContext gc) {
		gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
		
		if(heading == 0)
			x--;
		else if(heading == 90)
			y--;
		else if(heading == 180)
			x++;
		else
			y++;
		
		
		// we just draw the robot if we know the position (or we are at least quite sure)
		// ToDo: Nach jedem Movement müssen die Partikel neu generiert werden
		if (x != -1 && y != -1) {
//			Particle.Draw(x, y, heading, 0, gc, true);
		}
	}

	public void measure() {
		// ToDo: implement measurement with SVGDocument, x, y and heading
		this.measure = -1;
	}

	@Override
	public void draw(final GraphicsContext gc) {
		gc.setFill(Color.RED);
		gc.setStroke(Color.BROWN);

		gc.fillOval(
            x * Main.DrawFactor, y * Main.DrawFactor, WIDTH * Main.DrawFactor, HEIGHT * Main.DrawFactor
        );
	}
}
