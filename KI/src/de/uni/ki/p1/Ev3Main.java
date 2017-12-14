/*
 * Copyright © 2017 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.uni.ki.p1;

import lejos.hardware.Button;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.*;
import lejos.robotics.*;
import lejos.robotics.chassis.*;
import lejos.robotics.filter.PublishFilter;
import lejos.robotics.navigation.MovePilot;

public class Ev3Main
{
	public static void main(String[] args) throws Exception
	{
		Wheel wheel1 = WheeledChassis.modelWheel(Motor.A, 43.2).offset(-72);
		Wheel wheel2 = WheeledChassis.modelWheel(Motor.D, 43.2).offset(72);
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
		
		Button.waitForAnyPress();
		
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
		Thread.sleep(3000);
	}
}
