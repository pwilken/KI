package de.uni.ki.p1;

import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.navigation.MovePilot;

public interface MoveStrategy {
    void move(final MovePilot pilot,
              final EV3UltrasonicSensor ultrasonicSensor,
              final RegulatedMotor ultraSonicMotor,
              final EV3ColorSensor colorSensor);
}