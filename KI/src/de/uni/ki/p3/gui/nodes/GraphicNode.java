/*
 * Copyright © 2018 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.uni.ki.p3.gui.nodes;

import de.uni.ki.p3.SVG.SvgDocument;
import de.uni.ki.p3.pilot.Pilot;
import de.uni.ki.p3.robot.*;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.*;
import javafx.scene.*;
import javafx.scene.layout.Pane;

public class GraphicNode
{
	private final Pane pane;
	
	private SVGNode svgNode;
	private RobotNode simRobotNode;
	private PilotNode pilotNode;
	
	public GraphicNode(Pane pane, ObjectProperty<SvgDocument> svgDocument, ObjectProperty<Robot> robot, ObjectProperty<Pilot> pilot)
	{
		this.pane = pane;
		
		svgDocument.addListener(new ChangeListener<SvgDocument>()
			{
				@Override
				public void changed(
								ObservableValue<? extends SvgDocument> observable,
								SvgDocument oldValue, SvgDocument newValue)
				{
					newSvg(newValue);
				}
			});
		
		robot.addListener(new ChangeListener<Robot>()
			{
				@Override
				public void changed(ObservableValue<? extends Robot> observable,
								Robot oldValue, Robot newValue)
				{
					newRobot(newValue);
				}
			});
		
		pilot.addListener(new ChangeListener<Pilot>()
		{
			@Override
			public void changed(ObservableValue<? extends Pilot> observable,
							Pilot oldValue, Pilot newValue)
			{
				newPilot(newValue);
			}
		});
		
		ChangeListener<Number> cl = new ChangeListener<Number>()
    		{
    			@Override
    			public void changed(ObservableValue<? extends Number> observable,
    							Number oldValue, Number newValue)
    			{
    				fillPane();
    			}
    		};
		pane.widthProperty().addListener(cl);
		pane.heightProperty().addListener(cl);
	}
	
	private void newSvg(SvgDocument document)
	{
		if(document == null)
		{
			svgNode = null;
		}
		else
		{
			svgNode = new SVGNode(document);
			svgNode.setManaged(false);
		}
		
		fillPane();
	}
	
	private void newRobot(Robot robot)
	{
		if(robot instanceof SimRobot)
		{
			simRobotNode = new RobotNode((SimRobot)robot);
			simRobotNode.setManaged(false);
		}
		else
		{
			simRobotNode = null;
		}
		
		fillPane();
	}
	
	private void newPilot(Pilot pilot)
	{
		if(pilot == null)
		{
			pilotNode = null;
		}
		else
		{
			pilotNode = new PilotNode(pilot);
			pilotNode.setManaged(false);
		}
		
		fillPane();
	}
	
	private void fillPane()
	{
		Platform.runLater(new Runnable()
		{
			@Override
			public void run()
			{
				Group g = new Group();
				g.setManaged(false);
				pane.getChildren().clear();
				
				if(svgNode != null)
				{
					g.getChildren().add(svgNode);
					
					double ratX = svgNode.getBoundsInLocal().getWidth() / pane.getWidth();
					double ratY = svgNode.getBoundsInLocal().getHeight() / pane.getHeight();
					
					g.setScaleX(1 / Math.max(ratX, ratY));
					g.setScaleY(g.getScaleX());
					g.setTranslateX((pane.getWidth() - svgNode.getBoundsInLocal().getWidth()) / 2);
					g.setTranslateY((pane.getHeight() - svgNode.getBoundsInLocal().getHeight()) / 2);
				}
				if(simRobotNode != null)
				{
					g.getChildren().add(simRobotNode);
				}
				if(pilotNode != null)
				{
					g.getChildren().add(pilotNode);
				}
				
				pane.getChildren().add(g);
			}
		});
	}
}
