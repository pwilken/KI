package de.uni.ki.p3;

import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;

import de.uni.ki.p3.Drawing.MapObject;
import de.uni.ki.p3.Drawing.Particle;
import de.uni.ki.p3.SVG.SVGParsing;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;

public class Main extends Application{
	public static float DrawFactor = 3;
	
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("KIsches Wunder");
        Group root = new Group();
        
        //
        SVGDocument svgDoc = GetSVGDocument();
        MapObject mapObject = new MapObject();
        mapObject.parseSVGDocument(svgDoc);
        Canvas canvas = new Canvas(mapObject.getWidth(), mapObject.getHeight());
        GraphicsContext gc = canvas.getGraphicsContext2D();
        MapObject.drawMapObject(gc, mapObject);
        //
        //TestDraw(gc, 100, 25);
        RobotTest(gc);
        
        
        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
    
    public SVGDocument GetSVGDocument()
    {
    	String filePath = "img/street.svg";
    	return SVGParsing.toSVGDocument(filePath);
    	
    }
    
    public static void RobotTest(GraphicsContext gc)
    {
    	Robot robot = new Robot(100, 25);
    	
    	// ToDo: Wir müssen bis auf den Hintergrund bei jedem Move einmal alles gezeichnetet entfernen
    	// Also nur die Karte da lassen, Partikel und Bot entfernen. Die werden dann ja zwangsläufig neugezeichnet.
    	new Thread(() -> {
    	    for(int i = 0; i < 1000; i++)
    	    {
    	    	robot.move(0, 0, gc);
    	    }
    	}).start();
    	
    	
    }
    
    public static void TestDraw(GraphicsContext gc, float x, float y)
    {
    	Particle.Draw(50,25, 0, 10, gc, false);
    	Particle.Draw(100,25, 90, 10, gc, false);
    	Particle.Draw(150,25, 180, 10, gc, false);
    	Particle.Draw(200,25, 270, 10, gc, false);
    	Particle.Draw(250,25, 360, 10, gc, false);
    }
}