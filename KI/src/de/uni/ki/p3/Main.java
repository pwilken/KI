package de.uni.ki.p3;

import java.util.*;

import de.uni.ki.p3.Drawing.*;
import de.uni.ki.p3.MCL.*;
import de.uni.ki.p3.SVG.SvgDocument;
import de.uni.ki.p3.robot.*;
import de.uni.ki.p3.robot.Robot;
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
		robot = new SimRobot();
		grpMcl = new Group();
//		pane.setPrefWidth(doc.getWidth());
//		pane.setPrefHeight(doc.getHeight());
		pane.setScaleX(3);
		pane.setScaleY(3);
		pane.translateXProperty().bind(pane.widthProperty());
		pane.translateYProperty().bind(pane.heightProperty());
		pane.getChildren().addAll(new SVGNode(doc), grpMcl, new RobotNode(robot));
		
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
		
		task = new MoveTask(robot);
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
		if(robot instanceof SimRobot)
		{
			SimRobot sim = (SimRobot)robot;
			sim.setDistAngle(90);
			sim.setMap(map);
			sim.setPos(new Position(10, 80));
			sim.setTheta(0);
		}
		mcl.get().initializeParticles(0d, 70d, 400d, 20d);
	}
	
	@Override
	public void particlesChanged(MCL mcl)
	{
		mclChanged();
	}
	
	public static class MoveTask extends Task<Void>
	{
		private Robot robot;
		
		public MoveTask(Robot robot)
		{
			this.robot = robot;
		}

		@Override
		protected Void call() throws Exception
		{
			while(!isCancelled() && robot.getPos().getX() < 390)
			{
				Platform.runLater(new Runnable()
				{
					@Override
					public void run()
					{
						robot.move(10);
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
			return null;
		}
	}
}