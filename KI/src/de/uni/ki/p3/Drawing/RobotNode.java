/*
 * Copyright © 2018 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.uni.ki.p3.Drawing;

import de.uni.ki.p3.robot.*;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;

public class RobotNode extends Group implements RobotListener
{
	private Circle c;
	private Line l;
	private Line lSensor;
	
	private Rotate rl;
	private Rotate rlSensor;
	
	public RobotNode(Robot robot)
	{
		c = new Circle(robot.getPos().getX(), robot.getPos().getY(), 5, Color.RED);
		l = new Line(0d, 0d, 1d, 0d);
		l.startXProperty().bind(c.centerXProperty());
		l.startYProperty().bind(c.centerYProperty());
		l.endXProperty().bind(l.startXProperty().add(c.radiusProperty()));
		l.endYProperty().bind(l.startYProperty());
		l.setStroke(Color.BROWN);
//		l.setRotationAxis(Rotate.Z_AXIS);
//		l.setRotate(robot.getTheta());
		rl = new Rotate(robot.getTheta(), 0, 0, 0, Rotate.Z_AXIS);
		l.getTransforms().add(rl);
		
		lSensor = new Line(0, 0, 1, 0);
		lSensor.setStroke(Color.YELLOW);
		lSensor.startXProperty().bind(c.centerXProperty());
		lSensor.startYProperty().bind(c.centerYProperty());
		lSensor.endXProperty().bind(lSensor.startXProperty().add(c.radiusProperty()));
		lSensor.endYProperty().bind(lSensor.startYProperty());
//		lSensor.setRotationAxis(Rotate.Z_AXIS);
//		lSensor.setRotate(robot.getDistAngle() + robot.getTheta());
		rlSensor = new Rotate(robot.getTheta(),l.startXProperty().floatValue(),l.startYProperty().floatValue());

		lSensor.getTransforms().add(rlSensor);
		
		getChildren().addAll(c, l, lSensor);
		
		robot.addRobotListener(this);
	}
	
	public RobotNode(SimRobot robot)
	{
		this((Robot)robot);
	}
	
	@Override
	public void robotMeasured(Robot robot, RobotMeasurement measurement)
	{
	}
	
	@Override
	public void robotMoved(final Robot robot, double dist)
	{
		Platform.runLater(new Runnable()
    		{
    			@Override
    			public void run()
    			{
    				c.setCenterX(robot.getPos().getX());
    				c.setCenterY(robot.getPos().getY());
    				
    				rlSensor.setAngle(robot.getDistAngle() + robot.getTheta());
    			}
    		});
    	}
	
	@Override
	public void robotRotated(final Robot robot, double angle)
	{
		Platform.runLater(new Runnable()
    		{
    			@Override
    			public void run()
    			{
    				rl.setAngle(robot.getTheta());
    				
    				rlSensor.setAngle(robot.getDistAngle() + robot.getTheta());
    			}
    		});
	}
}
