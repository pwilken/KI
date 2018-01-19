/*
 * Copyright © 2017 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.uni.ki.p1;

import lejos.hardware.Button;
import lejos.hardware.device.NXTCam;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.Color;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.filter.PublishFilter;
import lejos.robotics.navigation.MovePilot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Ev3Main
{
	public static void main(String[] args) throws Exception
	{
		final Wheel wheel1 = WheeledChassis.modelWheel(
            new EV3LargeRegulatedMotor(MotorPort.B), 6.5
        ).offset(-6.8);
		final Wheel wheel2 = WheeledChassis.modelWheel(
            new EV3LargeRegulatedMotor(MotorPort.C), 6.5
        ).offset(6.8);
		RegulatedMotor usMotor = new EV3MediumRegulatedMotor(MotorPort.D);
        final int usMotorAngle = 90;
        usMotor.rotate(usMotorAngle);
		
		Chassis chassis = new WheeledChassis(new Wheel[]{wheel1, wheel2}, 2); 
		MovePilot pilot = new MovePilot(chassis);

		final NXTCam cam = new NXTCam(SensorPort.S1);
		final EV3ColorSensor col = new EV3ColorSensor(SensorPort.S2);
		final EV3UltrasonicSensor us = new EV3UltrasonicSensor(SensorPort.S4);

        int port = 8000;
        try (
            ServerSocket serverSocket = new ServerSocket(port);
            Socket clientSocket = serverSocket.accept();
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
        ) {
        	System.out.println("connected to<" + clientSocket.getInetAddress().getHostName() + ">");
            float[] samples = new float[us.sampleSize() + col.sampleSize()];

            for (;;) {
                String command = in.readLine();

                System.out.println("received command<" + command + ">");
                
                if (command.equalsIgnoreCase(Command.END)) {
                    break;
                }

                double value = getValue(command);
                switch (command) {
                    case Command.MOVE:
                        pilot.travel(value);
                        break;
                    case Command.ROTATE:
                        pilot.rotate(value);
                        break;
                    case Command.ROTATE_US:
                        // the motor of the ultrasonic sensor is mounted the other way around
                        // therefore the angle to rotate has to be multiplied with -1 to ensure the expected behaviour
                        usMotor.rotate((int) normalizeUsMotorAngle(value));
                        break;
                    case Command.MEASURE:
                        col.fetchSample(samples, 0);
                        us.getDistanceMode().fetchSample(samples, col.sampleSize());
                        // convert meters to centimeters
                        samples[col.sampleSize()] = samples[col.sampleSize()] * 100;
                        out.println(String.format("%s %s %s", samples[0], samples[1], normalizeUsMotorAngle(usMotorAngle)));
                        break;
                }
            }
        }

		SampleProvider sp = new PublishFilter(new SampleProvider()
    		{
    			
    			@Override
    			public int sampleSize()
    			{
    				return 3 + us.sampleSize() + cam.sampleSize();
    			}
    			
    			@Override
    			public void fetchSample(float[] sample, int offset)
    			{
    				sample[0] = wheel1.getMotor().getTachoCount();
    				sample[1] = wheel2.getMotor().getTachoCount();
    				sample[2] = col.getColorID();
    				us.getDistanceMode().fetchSample(sample, 3);
    				cam.fetchSample(sample, 3 + us.sampleSize());
    			}
    		}, "data", 1);
		
		float[] sample = new float[sp.sampleSize()];
		pilot.setLinearSpeed(6);  // cm per second
		pilot.setAngularSpeed(90);
		
		introMessage();

		final MoveStrategy moveStrategy = new SmartMoveStrategy();
		moveStrategy.move(pilot, us, usMotor, col);

		col.close();
		us.close();
		pilot.stop();
		cam.close();
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

    private static double getValue(String command)
    {
        return Double.parseDouble(command.split(Command.SEPARATOR)[1]);
    }

    private static double normalizeUsMotorAngle(double angle) {
	    return angle * -1;
    }
}
