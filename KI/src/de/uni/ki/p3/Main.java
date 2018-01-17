package de.uni.ki.p3;

import de.uni.ki.p3.Drawing.MapObject;
import de.uni.ki.p3.Drawing.Rect;
import de.uni.ki.p3.MCL.MCL;
import de.uni.ki.p3.MCL.Particle;
import de.uni.ki.p3.SVG.SVGParsing;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.w3c.dom.svg.SVGDocument;

public class Main extends Application{
	public static float DrawFactor = 3;
	
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        SVGDocument svgDoc = GetSVGDocument();
        MapObject mapObject = new MapObject();
        mapObject.parseSVGDocument(svgDoc);
        Canvas background = new Canvas(mapObject.getWidth(), mapObject.getHeight());
        Canvas foreground = new Canvas(mapObject.getWidth(), mapObject.getHeight());
        System.out.println("mapObject.height: " + mapObject.getHeight());
        System.out.println("Canvas-Height: " + background.getHeight());
        GraphicsContext backgroundGC = background.getGraphicsContext2D();
        GraphicsContext foregroundGC = foreground.getGraphicsContext2D();
        mapObject.setGc(backgroundGC);
        mapObject.draw();

        MCL mcl = new MCL(foregroundGC);
        Robot robot = new Robot(0, (float) mapObject.getHeight() / 4 + 7, mapObject, foregroundGC, mcl);
        robot.draw();
        robot.rotateSensor(-90);

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10));
        vBox.setSpacing(10);

        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER_LEFT);

        final Label lbl = new Label("Anzahl Partikel:");
        TextField txtField = new TextField();
        Button generateParticleBtn = new Button("Generieren");
        Button startBtn = new Button("Start");
        generateParticleBtn.setOnAction(event -> {
            try {
                Rect rect = mapObject.getRects().get(0);
                System.out.println("rect.getY: " + rect.getY());

                int particleAmount = Integer.parseInt(txtField.getText());
                mcl.setParticleAmount(particleAmount);
                mcl.initializeParticles(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
                mcl.getParticles().forEach(Particle::draw);
                startBtn.setDisable(false);
            } catch (final NumberFormatException e) {
                txtField.setText("Muss eine Ganzzahl sein!");
            }
        });
        startBtn.setDisable(true);
        startBtn.setOnAction(event -> {
        	RobotTest((float)mapObject.getWidth(), mapObject, robot);
        });
        hBox.getChildren().add(lbl);
        hBox.getChildren().add(txtField);
        hBox.getChildren().add(generateParticleBtn);
        hBox.getChildren().add(startBtn);
        vBox.getChildren().add(hBox);

        Group canvasGroup = new Group();
        canvasGroup.getChildren().add(background);
        canvasGroup.getChildren().add(foreground);
        vBox.getChildren().add(canvasGroup);

        primaryStage.setTitle("KIsches Wunder");
        primaryStage.setScene(new Scene(vBox));
        primaryStage.show();
    }
    
    private SVGDocument GetSVGDocument()
    {
    	String filePath = "img/street.svg";
    	return SVGParsing.toSVGDocument(filePath);
    }
    

    private void RobotTest(float mapWidth, MapObject map, Robot robot)
    {
    	new Thread(() -> {
    	    for(int i = 0; i < mapWidth; i++)
    	    {
                Platform.runLater(() -> robot.move(180, 0));
    	    	try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
    	    }
    	}).start();
    }
}