/*
 * Copyright © 2018 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.uni.ki.p3.gui.nodes;

import de.uni.ki.p3.MCL.Particle;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;

public class ParticleNode extends Group
{
	public ParticleNode(Particle p)
	{
		Circle c = new Circle(p.getPos().getX(), p.getPos().getY(), 1d, Color.BLUE);
		Color col = new Color(Color.BLUE.getRed(), Color.BLUE.getGreen(), Color.BLUE.getBlue(), p.getWeight() / 1000d);
		c.setFill(col);
		Line l = new Line(0d, 0d, 1d, 1d);
		l.setStroke(Color.YELLOW);
		l.setStrokeWidth(0.25);
		l.startXProperty().bind(c.centerXProperty());
		l.startYProperty().bind(c.centerYProperty());
		l.endXProperty().bind(l.startXProperty().add(c.radiusProperty().add(1)));
		l.endYProperty().bind(l.startYProperty());
		l.setRotationAxis(Rotate.Z_AXIS);
		l.getTransforms().add(new Rotate(p.getTheta(),l.startXProperty().floatValue(),l.startYProperty().floatValue()));
		//l.setRotate(p.getTheta());
		
		getChildren().addAll(c, l);
	}
}
