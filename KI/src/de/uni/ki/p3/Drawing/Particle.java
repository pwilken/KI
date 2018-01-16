package de.uni.ki.p3.Drawing;

import de.uni.ki.p3.Main;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Rotate;
import de.uni.ki.p3.MCL.*;

public class Particle {
	
	public static void DrawSet(MCLParticleSet mclParticleSet, GraphicsContext gc)
	{
		for(int i = 0; i < mclParticleSet.getCount(); i++)
		{
			MCLParticle particle = mclParticleSet.getParticle(i);
			
			// ToDo: Draw that shit
			
			// (x,y) with specified heading in degrees.
			float weight = particle.getWeight();
			
			float x = particle.getX();
			float y = particle.getY();
			float heading = particle.getHeading();
			
			Draw(x, y, heading, weight, gc, false);
		}
	}
	
	public static void Draw(double x, double y, double heading, double weight, GraphicsContext gc, boolean robot)
	{
		// ToDo: find good w and h value (calculate with weight?)
		double ovalWidth = 5;
		double ovalHeight = 5;
	
		if(robot)
		{
			ovalWidth *=2;
			ovalHeight *=2;
		}

		
		gc.fillOval(x*Main.DrawFactor, y*Main.DrawFactor, ovalWidth*Main.DrawFactor, ovalHeight*Main.DrawFactor);
		double startX = (x+ovalWidth/2)*Main.DrawFactor;
		double startY = (y+ovalHeight/2)*Main.DrawFactor;
		double endX = (x-ovalWidth/2)*Main.DrawFactor;
		double endY = (y+ovalHeight/2)*Main.DrawFactor;
	    
	    Rotate r = new Rotate(heading, startX, startY);
	    Point2D p = r.transform(endX, endY);
		
		gc.strokeLine(startX,startY,p.getX(),p.getY());
	}
}
