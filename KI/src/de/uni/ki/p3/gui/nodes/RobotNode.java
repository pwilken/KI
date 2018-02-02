/*
 * Copyright © 2018 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.uni.ki.p3.gui.nodes;

import de.uni.ki.p3.MCL.Position;
import de.uni.ki.p3.robot.*;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;

public class RobotNode extends Group implements SimRobotListener
{
	private Circle body;
	private Line l;
	private Rotate angle;
	
	public RobotNode(SimRobot robot)
	{
		robot.addSimListener(this);
		
		body = new Circle(robot.getPos().getX(), robot.getPos().getY(), 5,
			new Color(
				Color.RED.getRed(),
				Color.RED.getGreen(),
				Color.RED.getBlue(),
				0.5));
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
		angle.setAngle(robot.getTheta());
		l.getTransforms().add(angle);
		getChildren().addAll(body, l);
	}
	
	@Override
	public void posChanged(SimRobot robot, Position pos)
	{
		body.setCenterX(pos.getX());
		body.setCenterY(pos.getY());
	}
	
	@Override
	public void thetaChanged(SimRobot robot, double theta)
	{
		angle.setAngle(theta);
	}
}
