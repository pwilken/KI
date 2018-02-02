package de.uni.ki.p3.gui;

import java.io.*;
import java.util.Random;
import java.util.concurrent.Callable;

import de.uni.ki.p3.MCL.*;
import de.uni.ki.p3.SVG.SvgDocument;
import de.uni.ki.p3.gui.nodes.GraphicNode;
import de.uni.ki.p3.pilot.Pilot;
import de.uni.ki.p3.robot.*;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.event.EventHandler;
import javafx.fxml.*;
import javafx.geometry.Point2D;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.*;
import javafx.stage.FileChooser.ExtensionFilter;

public class Main extends Application
{
	public static void main(String[] args)
	{
		launch(args);
	}
	
	@FXML
	private RadioButton radSimu;
	@FXML
	private RadioButton radEv3;
	@FXML
	private Parent parentEv3;
	@FXML
	private TextField txtIp;
	@FXML
	private TextField txtPort;
	@FXML
	private Button btnStart;
	@FXML
	private Button btnStop;
	@FXML
	private Button btnNextStep;
	@FXML
	private Label lblState;
	@FXML
	private Label lblRobot;
	@FXML
	private Parent parentConfig;
	@FXML
	private Parent parentSimu;
	@FXML
	private Parent parentPilot;
	@FXML
	private TextField txtSeed;
	@FXML
	private Pane pane;
	@FXML
	private TextField txtNum;
	@FXML
	private TextField txtMinAngle;
	@FXML
	private TextField txtMaxAngle;
	@FXML
	private TextField txtTolX;
	@FXML
	private TextField txtTolY;
	@FXML
	private TextField txtTolAngle;
	@FXML
	private TextField txtSimuMoveTol;
	@FXML
	private TextField txtSimuRotTol;
	@FXML
	private TextField txtSimuDistTol;
	@FXML
	private TextField txtSimuAngleTol;
	@FXML
	private TextField txtSimuAngles;
	@FXML
	private TextField txtPilotMove;
	@FXML
	private TextField txtPilotRot;
	@FXML
	private TextField txtPilotMinDist;
	private GraphicNode graphicNode;
	
	private ObjectProperty<SvgDocument> svgDocument;
	private ObjectProperty<RangeMap> rangeMap;
	private ObjectProperty<Robot> robot;
	private ObjectProperty<Pilot> pilot;
	
	private MCLConfiguration config;
	
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
		initFields();
		initListener();
		
		setTxtValuesCfg();
		
		robot.set(new SimRobot());
	}
	
	private void initFields()
	{
		config = new MCLConfiguration();
		
		svgDocument = new SimpleObjectProperty<>(this, "svgDocument", null);
		rangeMap = new SimpleObjectProperty<>(this, "svgMap", null);
		robot = new SimpleObjectProperty<Robot>(this, "robot", null);
		pilot = new SimpleObjectProperty<>(this, "pilot", null);
		
		graphicNode = new GraphicNode(pane, svgDocument, robot, pilot);
	}
	
	private void initListener()
	{
		ToggleGroup group = new ToggleGroup();
		group.getToggles().addAll(radSimu, radEv3);
		
		parentEv3.disableProperty().bind(radSimu.selectedProperty());
		
		group.selectedToggleProperty().addListener(new ChangeListener<Toggle>()
    		{
    			@Override
    			public void changed(ObservableValue<? extends Toggle> o,
    							Toggle oldVal, Toggle newVal)
    			{
    				if(newVal == radSimu)
    				{
    					robot.set(new SimRobot());
    					((SimRobot)robot.get()).setMap(rangeMap.get());
    					setTxtValuesSimu((SimRobot)robot.get());
    				}
    				else
    				{
    					doConnect();
    				}
    			}
    		});
		
		lblRobot.textProperty().bind(Bindings.createStringBinding(
			new Callable<String>()
			{
				@Override
				public String call() throws Exception
				{
					if(robot.get() == null)
					{
						return "Kein Roboter";
					}
					if(robot.get() instanceof SimRobot)
					{
						return "Simu";
					}
					if(robot.get() instanceof Ev3Robot)
					{
						return "EV3";
					}
					
					throw new IllegalStateException();
				}
			},
			robot));
		
		lblState.styleProperty().bind(Bindings.createStringBinding(
			new Callable<String>()
			{
				@Override
				public String call() throws Exception
				{
					if(robot.get() == null)
					{
						return "-fx-background-color: red;";
					}
					return "-fx-background-color: green;";
				}
			},
			robot));
		
		rangeMap.bind(Bindings.createObjectBinding(
			new Callable<RangeMap>()
			{
				@Override
				public RangeMap call() throws Exception
				{
					if(svgDocument.get() == null)
					{
						return null;
					}
//					return new SvgRangeMap(svgDocument.get());
					return new SvgLejosRangeMap(svgDocument.get());
				}
			},
			svgDocument));
		
		rangeMap.addListener(new ChangeListener<RangeMap>()
    		{
    			@Override
    			public void changed(
    							ObservableValue<? extends RangeMap> observable,
    							RangeMap oldValue, RangeMap newValue)
    			{
    				if(newValue == null)
    				{
    					config.initialParticlePosX = Double.MIN_VALUE;
    					config.initialParticlePosY = Double.MIN_VALUE;
    					config.initialParticlePosWidth = Double.MAX_VALUE;
    					config.initialParticlePosHeight = Double.MAX_VALUE;
    					if(robot.get() instanceof SimRobot)
    					{
    						((SimRobot)robot.get()).setMap(null);
    					}
    				}
    				else
    				{
        				config.initialParticlePosX = 0d;
        				config.initialParticlePosY = 0d;
        				config.initialParticlePosWidth = newValue.getWidth();
        				config.initialParticlePosHeight = newValue.getHeight();
        				if(robot.get() instanceof SimRobot)
        				{
        					((SimRobot)robot.get()).setMap(newValue);
        				}
    				}
    			}
    		});
		
		btnStart.disableProperty().bind(
			robot.isNull().or(rangeMap.isNull()));
		btnNextStep.disableProperty().bind(btnStart.disableProperty());
		btnStop.disableProperty().bind(btnStart.disableProperty());
		
		pane.setOnMouseClicked(new EventHandler<MouseEvent>()
    		{
    			@Override
    			public void handle(MouseEvent event)
    			{
    				if(robot.get() instanceof SimRobot
						&& rangeMap.get() != null)
    				{
    					Point2D p = new Point2D(event.getX(), event.getY());
    					p = pane.getChildren().get(0).parentToLocal(p);
    					((SimRobot)robot.get()).setPos(new Position(p.getX(), p.getY()));
    				}
    			}
    		});
		
		ChangeListener<String> configCl = new ChangeListener<String>()
    		{
    			@Override
    			public void changed(ObservableValue<? extends String> observable,
    							String oldValue, String newValue)
    			{
    				setTxtValuesCfg();
    			}
    		};
		txtMinAngle.textProperty().addListener(configCl);
		txtMaxAngle.textProperty().addListener(configCl);
		txtTolX.textProperty().addListener(configCl);
		txtTolY.textProperty().addListener(configCl);
		txtTolAngle.textProperty().addListener(configCl);
		
		ChangeListener<String> simuCl = new ChangeListener<String>()
    		{
    			@Override
    			public void changed(ObservableValue<? extends String> observable,
    							String oldValue, String newValue)
    			{
    				if(robot.get() instanceof SimRobot)
    				{
    					setTxtValuesSimu((SimRobot) robot.get());
    				}
    			}
    		};
		txtSimuMoveTol.textProperty().addListener(simuCl);
		txtSimuRotTol.textProperty().addListener(simuCl);
		txtSimuDistTol.textProperty().addListener(simuCl);
		txtSimuAngleTol.textProperty().addListener(simuCl);
		txtSimuAngles.textProperty().addListener(simuCl);
	}
	
	private void setTxtValuesCfg()
	{
		try
		{
			config.minAngle = Integer.parseInt(txtMinAngle.getText());
		}
		catch(RuntimeException e)
		{
			config.minAngle = 0d;
		}
		
		try
		{
			config.maxAngle = Integer.parseInt(txtMaxAngle.getText());
		}
		catch(RuntimeException e)
		{
			config.maxAngle = 360d;
		}
		
		try
		{
			config.xTolerance = Integer.parseInt(txtTolX.getText());
		}
		catch(RuntimeException e)
		{
			config.xTolerance = 0d;
		}
		
		try
		{
			config.yTolerance = Integer.parseInt(txtTolY.getText());
		}
		catch(RuntimeException e)
		{
			config.yTolerance = 0d;
		}
		
		try
		{
			config.angleTolerance = Integer.parseInt(txtTolAngle.getText());
		}
		catch(RuntimeException e)
		{
			config.angleTolerance = 0d;
		}
		
		try
		{
			config.random = new Random(Long.parseLong(txtSeed.getText()));
		}
		catch(RuntimeException e)
		{
			config.random = new Random();
		}
		
		try
		{
			config.initialParticleCount = Integer.parseInt(txtNum.getText());
		}
		catch(RuntimeException e)
		{
			config.initialParticleCount = 200;
		}
	}
	
	private void setTxtValuesSimu(SimRobot simRobot)
	{
		try
		{
			simRobot.setMoveTolerance(Double.parseDouble(txtSimuMoveTol.getText()));
		}
		catch(RuntimeException e)
		{
			simRobot.setMoveTolerance(0d);
		}

		try
		{
			simRobot.setRotateTolerance(Double.parseDouble(txtSimuRotTol.getText()));
		}
		catch(RuntimeException e)
		{
			simRobot.setRotateTolerance(0d);
		}
		
		try
		{
			simRobot.setMeasureDistTolerance(Double.parseDouble(txtSimuDistTol.getText()));
		}
		catch(RuntimeException e)
		{
			simRobot.setMeasureDistTolerance(0d);
		}
		
		try
		{
			simRobot.setMeasureAngleTolerance(Double.parseDouble(txtSimuAngleTol.getText()));
		}
		catch(RuntimeException e)
		{
			simRobot.setMeasureAngleTolerance(0d);
		}
		
		try
		{
			String[] parts = txtSimuAngles.getText().split(",");
			int[] angles = new int[parts.length];
			for(int i = 0; i < parts.length; ++i)
			{
				angles[i] = Integer.parseInt(parts[i].trim());
			}
			
			if(angles.length == 0)
			{
				simRobot.setMeasureAngles(0);
			}
			else
			{
				simRobot.setMeasureAngles(angles);
			}
		}
		catch(RuntimeException e)
		{
			simRobot.setMeasureAngles(0);
		}
	}
	
	private void crePilot()
	{
		pilot.set(new Pilot(robot.get(), rangeMap.get(), config));
		
		try
		{
			pilot.get().setMoveDist(Double.parseDouble(txtPilotMove.getText()));
		}
		catch(RuntimeException e)
		{
			pilot.get().setMoveDist(10d);
		}
		
		try
		{
			pilot.get().setRotateAngle(Double.parseDouble(txtPilotRot.getText()));
		}
		catch(RuntimeException e)
		{
			pilot.get().setRotateAngle(30d);
		}
		
		try
		{
			pilot.get().setMinDistFromWall(Double.parseDouble(txtPilotMinDist.getText()));
		}
		catch(RuntimeException e)
		{
			pilot.get().setMinDistFromWall(10d);
		}
	}

	@FXML
	private void loadMap()
	{
		FileChooser fc = new FileChooser();
		fc.setInitialDirectory(new File("img"));
		fc.getExtensionFilters().add(new ExtensionFilter("Svg-Maps", "*.svg"));
		File f = fc.showOpenDialog(null);
		
		if(f != null)
		{
			svgDocument.set(new SvgDocument(f.getAbsolutePath()));
		}
	}
	
	@FXML
	private void doConnect()
	{
		try
		{
			robot.set(new Ev3Robot(txtIp.getText(), Integer.parseInt(txtPort.getText())));
		}
		catch(NumberFormatException | IOException e)
		{
			e.printStackTrace();
			robot.set(null);
		}
	}
	
	@FXML
	private void doStart()
	{
		if(pilot.get() == null)
		{
			crePilot();
		}
		
		pilot.get().start();
	}

	@FXML
	private void doStop()
	{
		if(pilot.get() != null)
		{
			if(pilot.get().isRunning())
			{
				pilot.get().stop();
			}
			else
			{
				pilot.get().terminate();
				pilot.set(null);
			}
		}
	}
	
	@FXML
	private void doNextStep()
	{
		if(pilot.get() == null)
		{
			crePilot();
		}
		else
		{
			pilot.get().nextStep();
		}
	}
}