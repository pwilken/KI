package de.uni.ki.p3;

import de.uni.ki.p3.Drawing.MapObject;
import de.uni.ki.p3.MCL.MCL;
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
        primaryStage.setTitle("KIsches Wunder");
        VBox vBox = new VBox();
        //
        SVGDocument svgDoc = GetSVGDocument();
        MapObject mapObject = new MapObject();
        mapObject.parseSVGDocument(svgDoc);
        Canvas background = new Canvas(mapObject.getWidth(), mapObject.getHeight());
        Canvas foreground = new Canvas(mapObject.getWidth(), mapObject.getHeight());
        GraphicsContext backgroundGC = background.getGraphicsContext2D();
        GraphicsContext foregroundGC = foreground.getGraphicsContext2D();
        MapObject.drawMapObject(backgroundGC, mapObject);
        //
        //TestDraw(gc, 100, 25);

        vBox.setPadding(new Insets(10));
        vBox.setSpacing(10);

        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER_LEFT);
        final Label lbl = new Label("Anzahl Partikel:");

        TextField txtField = new TextField();
        Button generateParticleBtn = new Button("Generieren");
        generateParticleBtn.setOnAction(event -> {
            try {
                final int value = Integer.parseInt(txtField.getText());
                MCL mcl = new MCL(value);
                mcl.generateParticles();
            } catch (final NumberFormatException e) {
                txtField.setText("Muss eine Ganzzahl sein!");
            }
        });
        Button startBtn = new Button("Start");
        startBtn.setOnAction(event -> {
            RobotTest(foregroundGC, (float)mapObject.getWidth(), (float)mapObject.getHeight());
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
        primaryStage.setScene(new Scene(vBox));
        primaryStage.show();
    }
    
    private SVGDocument GetSVGDocument()
    {
    	String filePath = "img/street.svg";
    	return SVGParsing.toSVGDocument(filePath);
    }
    
    private void RobotTest(GraphicsContext gc, float mapWidth, float mapHeight)
    {
    	Robot robot = new Robot(0, mapHeight / 4 + 7);

    	// ToDo: Wir müssen bis auf den Hintergrund bei jedem Move einmal alles gezeichnetet entfernen
    	// Also nur die Karte da lassen, Partikel und Bot entfernen. Die werden dann ja zwangsläufig neugezeichnet.
    	new Thread(() -> {
    	    for(int i = 0; i < mapWidth; i++)
    	    {
                Platform.runLater(() -> robot.move(180, 0, gc));
    	    	try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
    	    }
    	}).start();
    }
}