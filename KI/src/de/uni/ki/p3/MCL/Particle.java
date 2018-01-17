package de.uni.ki.p3.MCL;

import de.uni.ki.p3.Drawing.Drawable;
import de.uni.ki.p3.Main;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

public class Particle implements Drawable {
	float x, y, heading, weight;
	private GraphicsContext gc;

	public static final float WIDTH = 2;
    public static final float HEIGHT = 2;

    public Particle(final float x, final float y, final float heading, final float weight, final GraphicsContext gc) {
        this.x = x;
        this.y = y;
        this.heading = heading;
        this.weight = weight;
        this.gc = gc;
    }

    @Override
    public void draw() {
    	gc.setFill(Color.BLUE);
    	gc.setStroke(Color.YELLOW);
    	
        gc.fillOval(
            x, y, WIDTH * Main.DrawFactor, HEIGHT * Main.DrawFactor
        );
        float startX = (x + WIDTH * Main.DrawFactor/ 2);
        float startY = (y + HEIGHT * Main.DrawFactor / 2);
        float endX = (x - WIDTH * Main.DrawFactor / 2) ;
        float endY = (y + HEIGHT * Main.DrawFactor / 2) ;

        Rotate r = new Rotate(heading, startX, startY);
        Point2D p = r.transform(endX, endY);

        gc.strokeLine(startX, startY, p.getX(), p.getY());
    }
}