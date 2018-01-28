/*
 * Copyright © 2018 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.uni.ki.p3.gui.nodes;

import java.util.*;

import de.uni.ki.p3.MCL.*;
import de.uni.ki.p3.pilot.*;
import javafx.application.Platform;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;

public class PilotNode extends Group implements PilotListener, MCLListener
{
	private Circle body;
	private Line l;
	private Rotate angle;
	
	public PilotNode(Pilot pilot)
	{
		pilot.addPilotListener(this);
		pilot.getMcl().addMclListener(this);
		
		body = new Circle(pilot.getPos().getX(), pilot.getPos().getY(), 5,
			Color.RED);
		l = new Line(0d, 0d, 1d, 1d);
		l.setStroke(Color.YELLOW);
		l.setStrokeWidth(0.25);
		l.startXProperty().bind(body.centerXProperty());
		l.startYProperty().bind(body.centerYProperty());
		l.endXProperty().bind(l.startXProperty().add(body.radiusProperty().add(1)));
		l.endYProperty().bind(l.startYProperty());
		l.setRotationAxis(Rotate.Z_AXIS);
		angle = new Rotate();
		angle.pivotXProperty().bind(l.startXProperty());
		angle.pivotYProperty().bind(l.startYProperty());
		angle.setAngle(pilot.getTheta());
		l.getTransforms().add(angle);
		getChildren().addAll(body, l);
	}

	@Override
	public void pilotStepped(final Pilot pilot)
	{
		Platform.runLater(new Runnable()
    		{
    			@Override
    			public void run()
    			{
    				body.setCenterX(pilot.getPos().getX());
    				body.setCenterY(pilot.getPos().getY());
    				angle.setAngle(pilot.getTheta());
    			}
    		});
	}
	
	@Override
	public void particlesChanged(final MCL mcl)
	{
		Platform.runLater(new Runnable()
    		{
    			@Override
    			public void run()
    			{
    				List<Node> children = new ArrayList<>();
    				children.add(body);
    				children.add(l);
    				
    				for(Particle p : mcl.getParticles())
    				{
    					children.add(new ParticleNode(p));
    				}
    				
    				getChildren().setAll(children);
    			}
    		});
	}
}
