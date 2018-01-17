package de.uni.ki.p1;

import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.navigation.MovePilot;

public class SmartMoveStrategy implements MoveStrategy {

    public static final double THRESHOLD_TURNING = 0.1;

    enum Direction {
        FORWARD,
        RIGHT,
        BACKWARD,
        LEFT,
    }

    @Override
    public void move(final MovePilot pilot,
                     final EV3UltrasonicSensor ultrasonicSensor,
                     final RegulatedMotor ultraSonicMotor,
                     final EV3ColorSensor colorSensor) {

        int previousColor;
        int currentColor = colorSensor.getColorID();

        while (true) {
            final Direction direction = determineNextDirectionToGo(ultrasonicSensor, ultraSonicMotor);

            if (!pilot.isMoving()) {
                switch (direction) {
                    case FORWARD:
                        pilot.forward();
                        break;
                    case RIGHT:
                        pilot.rotate(90);
                        break;
                    case BACKWARD:
                        pilot.rotate(180);
                        pilot.forward();
                        break;
                    case LEFT:
                        pilot.rotate(-90);
                        break;
                }
            }

            previousColor = currentColor;
            currentColor = colorSensor.getColorID();

            while (previousColor != currentColor) {
                pilot.stop();
                currentColor = colorSensor.getColorID();
            }

            ultraSonicMotor.rotate(0);
            break;
        }

        pilot.stop();
    }

    private Direction determineNextDirectionToGo(final EV3UltrasonicSensor ultrasonicSensor,
                                                 final RegulatedMotor ultraSonicMotor) {
        final float[] sample = new float[3];

        ultrasonicSensor.getDistanceMode().fetchSample(sample, 0);
        if (sample[0] > THRESHOLD_TURNING) {
            return Direction.FORWARD;
        }

        // turn the ultrasonic motor left and measure the distance
        ultraSonicMotor.rotate(90);
        ultrasonicSensor.getDistanceMode().fetchSample(sample, 1);

        // turn the ultrasonic motor right and measure the distance
        ultraSonicMotor.rotate(-90);
        ultrasonicSensor.getDistanceMode().fetchSample(sample, 2);

        if (sample[1] > THRESHOLD_TURNING) {
            return Direction.LEFT;
        } else if (sample[2] > THRESHOLD_TURNING) {
            return Direction.RIGHT;
        }

        return Direction.BACKWARD;
    }
}