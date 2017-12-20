/*
 * Copyright © 2017 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.uni.ki.p1;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.*;
import lejos.hardware.motor.*;
import lejos.hardware.port.*;
import lejos.hardware.sensor.*;
import lejos.robotics.*;
import lejos.robotics.chassis.*;
import lejos.robotics.filter.PublishFilter;
import lejos.robotics.navigation.MovePilot;

public class Ev3Main
{
	public static void main(String[] args) throws Exception
	{
		Wheel wheel1 = WheeledChassis.modelWheel(new EV3LargeRegulatedMotor(MotorPort.A), 43.2).offset(-72);
		Wheel wheel2 = WheeledChassis.modelWheel(new EV3LargeRegulatedMotor(MotorPort.D), 43.2).offset(72);
		Chassis chassis = new WheeledChassis(new Wheel[]{wheel1, wheel2}, 2); 
		MovePilot pilot = new MovePilot(chassis);
		
		EV3ColorSensor col = new EV3ColorSensor(SensorPort.S1);
		EV3UltrasonicSensor us = new EV3UltrasonicSensor(SensorPort.S2);
		
		SampleProvider sp = new PublishFilter(new SampleProvider()
    		{
    			
    			@Override
    			public int sampleSize()
    			{
    				return 4;
    			}
    			
    			@Override
    			public void fetchSample(float[] sample, int offset)
    			{
    				sample[0] = wheel1.getMotor().getTachoCount();
    				sample[1] = wheel2.getMotor().getTachoCount();
    				sample[2] = col.getColorID();
    				us.getDistanceMode().fetchSample(sample, 3);
    			}
    		}, "data", 1);
		
		float[] sample = new float[sp.sampleSize()];
		pilot.setLinearSpeed(30);  // cm per second
		
		introMessage();
		
		sendData(sp, sample);
		pilot.travel(-50);
		sendData(sp, sample);
		pilot.rotate(-90);
		pilot.rotate(270);
		pilot.travel(-50);
		sendData(sp, sample);
		pilot.rotate(180);
		sendData(sp, sample);
		
		col.close();
		us.close();
		pilot.stop();
	}
	
	private static void sendData(SampleProvider sp, float[] sample) throws InterruptedException
	{
		sp.fetchSample(sample, 0);
		
		GraphicsLCD g = LocalEV3.get().getGraphicsLCD();
		g.drawString("Praktikum 3-1", 5, 0, 0);
		g.setFont(Font.getSmallFont());
		
		g.drawString("Wheel1: " + sample[0], 5, 20, 0);
		g.drawString("Wheel2: " + sample[1], 5, 30, 0);
		g.drawString("Color: " + sample[2] + " - " + ColorToString((int)sample[2]), 5, 30, 0);
		g.drawString("distance: " + sample[3], 5, 30, 0);
		
		Thread.sleep(3000);
	}

	private static String ColorToString(int i)
	{
		switch(i)
		{
			case Color.RED:
				return "RED";
			case Color.GREEN:
				return "GREEN";
			case Color.BLUE:
				return "BLUE";
			case Color.YELLOW:
				return "YELLOW";
			case Color.MAGENTA:
				return "MAGENTA";
			case Color.ORANGE:
				return "ORANGE";
			case Color.WHITE:
				return "WHITE";
		    case Color.BLACK:
		    	return "BLACK";
		    case Color.PINK:
		    	return "PINK";
		    case Color.GRAY:
		    	return "GRAY";
		    case Color.LIGHT_GRAY:
		    	return "LIGHT_GRAY";
		    case Color.DARK_GRAY:
		    	return "DARK_GRAY";
		    case Color.CYAN:
		    	return "CYAN";
		    case Color.BROWN:
		    	return "BROWN";
	    	default:
	    		return "None";
		}
	}

	public static void introMessage() {
		
		GraphicsLCD g = LocalEV3.get().getGraphicsLCD();
		g.drawString("Praktikum 3-1", 5, 0, 0);
		g.setFont(Font.getSmallFont());
		 
		g.drawString("Press any Button to continue", 2, 20, 0);
		  
		// Quit GUI button:
		g.setFont(Font.getSmallFont()); // can also get specific size using Font.getFont()
		int y_quit = 100;
		int width_quit = 45;
		int height_quit = width_quit/2;
		int arc_diam = 6;
		g.drawString("QUIT", 9, y_quit+7, 0);
		g.drawLine(0, y_quit,  45, y_quit); // top line
		g.drawLine(0, y_quit,  0, y_quit+height_quit-arc_diam/2); // left line
		g.drawLine(width_quit, y_quit,  width_quit, y_quit+height_quit/2); // right line
		g.drawLine(0+arc_diam/2, y_quit+height_quit,  width_quit-10, y_quit+height_quit); // bottom line
		g.drawLine(width_quit-10, y_quit+height_quit, width_quit, y_quit+height_quit/2); // diagonal
		g.drawArc(0, y_quit+height_quit-arc_diam, arc_diam, arc_diam, 180, 90);
		
		// Enter GUI button:
		g.fillRect(width_quit+10, y_quit, height_quit, height_quit);
		g.drawString("GO", width_quit+15, y_quit+7, 0,true);
		
		Button.waitForAnyPress();
		if(Button.ESCAPE.isDown()) System.exit(0);
		g.clear();
	}
}
