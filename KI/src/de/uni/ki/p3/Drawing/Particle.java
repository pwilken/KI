package de.uni.ki.p3.Drawing;

import de.uni.ki.p3.Main;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Rotate;
import lejos.robotics.localization.MCLParticle;
import lejos.robotics.localization.MCLParticleSet;
import lejos.robotics.navigation.Pose;
import javafx.scene.transform.Rotate;

public class Particle {
	
	public static void DrawSet(MCLParticleSet mclParticleSet, GraphicsContext gc)
	{
		for(int i = 0; i < mclParticleSet.numParticles(); i++)
		{
			MCLParticle particle = mclParticleSet.getParticle(i);
			
			// ToDo: Draw that shit
			
			// (x,y) with specified heading in degrees.
			Pose pose = particle.getPose();
			float weight = particle.getWeight();
			
			float x = pose.getX();
			float y = pose.getY();
			float heading = pose.getHeading();
			
			Draw(x, y, heading, weight, gc);
		}
	}
	
	public static void Draw(double x, double y, double heading, double weight, GraphicsContext gc)
	{
		// ToDo: find good w and h value (calculate with weight?)
		gc.fillOval(x*Main.DrawFactor, y*Main.DrawFactor, 5*Main.DrawFactor, 5*Main.DrawFactor);
		double startX = (x+2.5)*Main.DrawFactor;
		double startY = (y+2.5)*Main.DrawFactor;
		double endX = (x-2.5)*Main.DrawFactor;
		double endY = (y+2.5)*Main.DrawFactor;
	    
	    Rotate r = new Rotate(heading, startX, startY);
	    Point2D p = r.transform(endX, endY);
		
		gc.strokeLine(startX,startY,p.getX(),p.getY());
	}
}
