package de.uni.ki.p3;

import de.uni.ki.p3.Drawing.Drawable;
import de.uni.ki.p3.Drawing.Line;
import de.uni.ki.p3.Drawing.MapObject;
import de.uni.ki.p3.MCL.MCL;
import de.uni.ki.p3.MCL.Particle;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

public class Robot implements Drawable {
    public static final float WIDTH = Particle.WIDTH * 2;
    public static final float HEIGHT = Particle.HEIGHT * 2;
	private float x = -1;
	private float y = -1;
	private float measure = -1;
	private float heading = 0; //direction in which the bot is looking
    private float sensorAngle = 180;
	private MapObject map;
	private GraphicsContext gc;
	private MCL mcl;

	public Robot(float x, float y, MapObject map, GraphicsContext gc, final MCL mcl) {
		this.x = x;
		this.y = y;
		this.map = map;
		this.gc = gc;
        this.mcl = mcl;
    }
	
	public Robot(float x, float y, float heading, MapObject map, GraphicsContext gc) {
		this.x = x;
		this.y = y;
		this.heading = heading;
		this.map = map;
		this.gc = gc;
	}


	public Robot(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Robot(float x, float y, float heading) {
		this.x = x;
		this.y = y;
		this.heading = heading;
		this.map = map;
	}

	public void move(float heading, float speed) {
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
			draw();
			measure();
			mcl.resample();
		}
	}

	public void rotateSensor(float angle) {
	    float end = sensorAngle + angle;

	    new Thread(() -> {
            while (sensorAngle != end) {
                if (sensorAngle < end) {
                    sensorAngle++;
                } else if (sensorAngle > end) {
                    sensorAngle--;
                }

                Platform.runLater(this::draw);

                try {
                    Thread.sleep(1000/60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

	public void measure() {
		boolean infinity = true;
		
		for(Line l : map.getLines())
		{
			if(x*Main.DrawFactor > l.getX1() && x*Main.DrawFactor < l.getX2() && l.getY1()+l.getY2() > 0)
			{
				float distance = (float)(l.getY2() - y);
				System.out.println("Distance: " + distance);
				infinity = false;
				break;
			}
		}
		
		if(infinity)
			System.out.println("Distance: infinity");
	}

	@Override
	public void draw() {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        gc.setFill(Color.RED);
		gc.setStroke(Color.BROWN);

		gc.fillOval(
            x * Main.DrawFactor, y * Main.DrawFactor, WIDTH * Main.DrawFactor, HEIGHT * Main.DrawFactor
        );
        float startX = (x + WIDTH / 2) * Main.DrawFactor;
        float startY = (y + HEIGHT / 2) * Main.DrawFactor;
        float endX = (x - WIDTH / 2) * Main.DrawFactor;
        float endY = (y + HEIGHT / 2) * Main.DrawFactor;

        Rotate r = new Rotate(sensorAngle, startX, startY);
        Point2D p = r.transform(endX, endY);

        gc.strokeLine(startX, startY, p.getX(), p.getY());
	}
}
