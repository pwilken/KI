package de.uni.ki.p3;

import java.util.*;

import de.uni.ki.p3.Drawing.*;
import de.uni.ki.p3.MCL.*;
import de.uni.ki.p3.SVG.SvgDocument;
import de.uni.ki.p3.robot.*;
import javafx.application.*;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.concurrent.Task;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application implements MCLListener
{
	public static float DrawFactor = 3;

	public static void main(String[] args)
	{
		launch(args);
	}
	
	@FXML
	private Button btnStart;
	@FXML
	private Pane pane;
	@FXML
	private TextField txtNum;
	
	private RangeMap map;
	
	private ObjectProperty<MCL> mcl;
	private SimRobot simRobot;
	private Robot robot;
	
	private Group grpMcl;
	
	private MoveTask task;

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Gui.fxml"));
		loader.setController(this);
		primaryStage.setScene(new Scene((Parent)loader.load()));
		primaryStage.setTitle("KIsches Wunder");
		primaryStage.show();
	}
	
	@FXML
	private void initialize()
	{
		SvgDocument doc = new SvgDocument("img/street.svg");
		map = new SvgRangeMap(doc);
		simRobot = new SimRobot();
		robot = robot;
		grpMcl = new Group();
//		pane.setPrefWidth(doc.getWidth());
//		pane.setPrefHeight(doc.getHeight());
		pane.setScaleX(3);
		pane.setScaleY(3);
		pane.translateXProperty().bind(pane.widthProperty());
		pane.translateYProperty().bind(pane.heightProperty());
		if(simRobot != null)
		{
			pane.getChildren().addAll(new SVGNode(doc), new RobotNode(simRobot), grpMcl);
		}
		else
		{
			pane.getChildren().addAll(new SVGNode(doc), new RobotNode(robot), grpMcl);
		}
		
		mcl = new SimpleObjectProperty<MCL>(this, "mcl", null);
		
		btnStart.disableProperty().bind(mcl.isNull());
		
		mcl.addListener(new ChangeListener<MCL>()
    		{
    			@Override
    			public void changed(ObservableValue<? extends MCL> observable,
    							MCL oldValue, MCL newValue)
    			{
    				mclChanged();
    			}
    		});
	}
	
	private void mclChanged()
	{
		if(mcl.get() == null)
		{
			grpMcl.getChildren().clear();
			return;
		}
		
		List<Node> nodes = new ArrayList<>(mcl.get().getParticles().size());
		for(Particle p : mcl.get().getParticles())
		{
			nodes.add(new ParticleNode(p));
		}
		
		grpMcl.getChildren().setAll(nodes);
	}
	
	@FXML
	private void doStart()
	{
		if(task != null)
		{
			task.cancel();
		}
		
		task = new MoveTask();
		task.exceptionProperty().addListener(new ChangeListener<Throwable>()
    		{
    			@Override
    			public void changed(ObservableValue<? extends Throwable> observable,
    							Throwable oldValue, Throwable newValue)
    			{
    				if(newValue != null)
    				{
    					newValue.printStackTrace();
    				}
    			}
    		});
		Thread t = new Thread(task);
		t.setDaemon(true);
		t.start();
	}
	
	@FXML
	private void doStop()
	{
		Platform.runLater(new Runnable()
		{
			@Override
			public void run()
			{
				if(task != null)
				{
					task.cancel();
				}
			}
		});
	}
	
	@FXML
	private void doGen()
	{
		int cnt;
		try
		{
			cnt = Integer.parseInt(txtNum.getText());
		}
		catch(NumberFormatException e)
		{
			return;
		}
		if(cnt < 1)
		{
			return;
		}
		
		if(mcl.get() != null)
		{
			mcl.get().removeMclListener(this);
		}
		
		mcl.set(new MCL(cnt, map, robot));
		mcl.get().addMclListener(this);
		// TODO $DeH
		if(simRobot != null)
		{
			simRobot.setMap(map);
			simRobot.setDistAngle(-90);
			simRobot.setPos(new Position(10, 80));
			simRobot.setSimPos(new Position(10, 80));
			simRobot.setTheta(0);
		}
		if(robot instanceof SimRobot)
		{
			SimRobot simRobot = (SimRobot)robot;
			simRobot.setMap(map);
			simRobot.setDistAngle(-90);
			simRobot.setPos(new Position(10, 80));
			simRobot.setSimPos(new Position(10, 80));
			simRobot.setTheta(0);
		}
		mcl.get().initializeParticles(0d, 70d, 400d, 20d);
	}
	
	@Override
	public void particlesChanged(MCL mcl)
	{
		mclChanged();
	}
	
	public class MoveTask extends Task<Void>
	{
		public MoveTask()
		{
		}

		@Override
		protected Void call() throws Exception
		{
			if(simRobot != null)
			{
    			while(!isCancelled() && simRobot.getSimPos().getX() < 390)
    			{
    				Platform.runLater(new Runnable()
    				{
    					@Override
    					public void run()
    					{
    						robot.move(10);
    						robot.measure();
    						simRobot.setPos(mcl.get().getBest().getPos());
    					}
    				});
    				
    				try
    				{
    					Thread.sleep(1000);
    				}
    				catch(InterruptedException e)
    				{
    					return null;
    				}
    			}
			}
			else
			{
				while(!isCancelled() && robot.getPos().getX() < 390)
				{
					Platform.runLater(new Runnable()
					{
						@Override
						public void run()
						{
							robot.move(10);
							robot.measure();
						}
					});
					
					try
					{
						Thread.sleep(1000);
					}
					catch(InterruptedException e)
					{
						return null;
					}
				}
			}
			return null;
		}
	}
}