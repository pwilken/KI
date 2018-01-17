package de.uni.ki.p3.MCL;

import de.uni.ki.p3.Drawing.Drawable;
import de.uni.ki.p3.Main;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Rotate;

public class MCLParticle implements Drawable {
	float x, y, heading, weight;
	private GraphicsContext gc;

	public static final float WIDTH = 5;
    public static final float HEIGHT = 5;

    public MCLParticle(final float x, final float y, final float heading, final float weight, final GraphicsContext gc) {
        this.x = x;
        this.y = y;
        this.heading = heading;
        this.weight = weight;
        this.gc = gc;
    }

    @Override
    public void draw() {
        gc.fillOval(
            x * Main.DrawFactor, y * Main.DrawFactor, WIDTH * Main.DrawFactor, HEIGHT * Main.DrawFactor
        );
        float startX = (x + WIDTH / 2) * Main.DrawFactor;
        float startY = (y + HEIGHT / 2) * Main.DrawFactor;
        float endX = (x - WIDTH / 2) * Main.DrawFactor;
        float endY = (y + HEIGHT / 2) * Main.DrawFactor;

        Rotate r = new Rotate(heading, startX, startY);
        Point2D p = r.transform(endX, endY);

        gc.strokeLine(startX, startY, p.getX(), p.getY());
    }
}