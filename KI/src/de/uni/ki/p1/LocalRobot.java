/*
 * Copyright © 2018 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.uni.ki.p1;

import java.io.*;
import java.net.*;
import java.util.*;

import de.uni.ki.p3.robot.RobotDistance;
import lejos.hardware.device.NXTCam;
import lejos.hardware.motor.*;
import lejos.hardware.port.*;
import lejos.hardware.sensor.*;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.chassis.*;
import lejos.robotics.navigation.MovePilot;

public class LocalRobot
{
	public final int PORT = 8000;
	public final int[] measureAngles = new int[] {-90, -45, 0, 45, 90};
	
	private RegulatedMotor usMotor;
	private MovePilot pilot;
	private NXTCam cam;
	private EV3ColorSensor col;
	private EV3UltrasonicSensor us;
	
	private int usAngle;
	
	public LocalRobot()
	{
		final Wheel wheel1 = WheeledChassis.modelWheel(
            	new EV3LargeRegulatedMotor(MotorPort.B), 6.5
			).offset(-6.8);
		final Wheel wheel2 = WheeledChassis.modelWheel(
            	new EV3LargeRegulatedMotor(MotorPort.C), 6.5
			).offset(6.8);
		usMotor = new EV3MediumRegulatedMotor(MotorPort.D);
		usAngle = 0;
		
		Chassis chassis = new WheeledChassis(new Wheel[]{wheel1, wheel2}, 2); 
		pilot = new MovePilot(chassis);

		cam = new NXTCam(SensorPort.S1);
		col = new EV3ColorSensor(SensorPort.S2);
		us = new EV3UltrasonicSensor(SensorPort.S4);
	}
	
	public void connect()
	{
		try(ServerSocket serverSocket = new ServerSocket(PORT))
		{
			while(true)
			{
				try(Socket clientSocket = serverSocket.accept())
				{
					System.out.println("connected to<" + clientSocket.getInetAddress().getHostName() + ">");
					listen(clientSocket);
					System.out.println("disconnected from host");
				}
				catch(IOException e)
				{
					System.out.println("Connection lost");
				}
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	private void listen(Socket clientSocket) throws IOException
	{
		try(PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())))
		{
			boolean run = true;
			float[] samples = new float[1];
            while(run)
            {
            	final String line = in.readLine();
            	
            	System.out.println("received <" + line + ">");
            	
            	String[] parts = line.split(Command.SEPARATOR);
            	
            	switch(parts[0])
            	{
            		case Command.MOVE:
            		{
            			pilot.travel(Double.parseDouble(parts[1]));
            			break;
            		}
            		case Command.ROTATE:
            		{
            			pilot.rotate(Double.parseDouble(parts[1]));
            			break;
            		}
            		case Command.MEASURE:
            		{
            			out.println(measure(samples));
            			break;
            		}
            		case Command.END:
            		{
            			run = false;
            			break;
            		}
            	}
            }
		}
	}

	private String measure(float[] samples)
	{
		final float color = getColor(samples);
		final List<RobotDistance> distances = getDistances(samples);
		
        StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf(color));
        sb.append(Command.SEPARATOR).append(distances.size());
        for(RobotDistance d : distances)
        {
        	sb.append(Command.SEPARATOR).append(d.getDist())
        		.append(Command.SEPARATOR).append(d.getDistAngle());
        }
        
        return sb.toString();
	}

	private List<RobotDistance> getDistances(float[] samples)
	{
		List<RobotDistance> distances = new ArrayList<>();
		
		for(int angle : measureAngles)
		{
			rotateUsTo(angle);
			float dist = getDist(samples);
			
			distances.add(new RobotDistance(dist, angle));
		}
		return distances;
	}
	
	private float getDist(float[] samples)
	{
		us.getDistanceMode().fetchSample(samples, 0);
		// convert meters to centimeters
		return samples[0] * 100;
	}

	private void rotateUsTo(int angle)
	{
		angle = -angle;
		usMotor.rotate(angle - usAngle);
		usAngle = angle;
	}

	private float getColor(float[] samples)
	{
		col.fetchSample(samples, 0);
		return samples[0];
	}
}
