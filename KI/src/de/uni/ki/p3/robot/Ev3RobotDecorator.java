package de.uni.ki.p3.robot;

import de.uni.ki.p1.Command;
import de.uni.ki.p3.MCL.Position;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class Ev3RobotDecorator implements Robot {
    private Robot robot;
    private String hostname;
    private int port;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public Ev3RobotDecorator(final Robot robot, final String hostname, final int port) {
        this.robot = robot;
        this.hostname = hostname;
        this.port = port;

        try {
            socket = new Socket(hostname, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void move(final double dist) {
        robot.move(dist);
        out.println(Command.withValue(Command.MOVE, dist));
    }

    @Override
    public void rotate(final double angle) {
        robot.move(angle);
        out.println(Command.withValue(Command.MOVE, angle));
    }

    @Override
    public void measure() {
        out.println(Command.MEASURE);
        String realMeasures = "";
        try {
            realMeasures = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final String[] parts = realMeasures.split(" ");
        int colorId = Integer.parseInt(parts[0]);
        double dist = Double.parseDouble(parts[1]);
        double distAngle = Double.parseDouble(parts[2]);

        RobotMeasurement robotMeasurement = new RobotMeasurement(colorId, dist, distAngle);
        for (RobotListener l : robot.getRobotListener()) {
            l.robotMeasured(this, robotMeasurement);
        }
    }

    @Override
    public Position getPos() {
        return robot.getPos();
    }

    @Override
    public double getTheta() {
        return robot.getTheta();
    }

    @Override
    public double getDistAngle() {
        return robot.getDistAngle();
    }

    @Override
    public void addRobotListener(final RobotListener l) {
        robot.addRobotListener(l);
    }

    @Override
    public void removeRobotListener(final RobotListener l) {
        robot.removeRobotListener(l);
    }

    @Override
    public List<RobotListener> getRobotListener() {
        return robot.getRobotListener();
    }
}