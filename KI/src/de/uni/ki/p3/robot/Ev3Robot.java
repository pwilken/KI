package de.uni.ki.p3.robot;

import de.uni.ki.p1.Command;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

public class Ev3Robot implements Robot
{
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	
	private List<RobotListener> listener;

	public Ev3Robot(final String hostname, final int port) throws IOException
	{
		listener = new ArrayList<>();

		socket = new Socket(hostname, port);
		out = new PrintWriter(socket.getOutputStream(), true);
		in = new BufferedReader(
				new InputStreamReader(
					socket.getInputStream()));
	}

	@Override
	public void move(final double dist)
	{
		sendCommand(Command.MOVE, dist);
		
		for(RobotListener l : listener)
		{
			l.robotMoved(this, dist);
		}
	}

	@Override
	public void rotate(final double angle)
	{
		sendCommand(Command.ROTATE, angle);
		
		for(RobotListener l : listener)
		{
			l.robotRotated(this, angle);
		}
	}

	@Override
	public void measure()
	{
		String realMeasures = sendCommand(Command.MEASURE);

		RobotMeasurement rm = parseMeasurement(realMeasures);
		
		for(RobotListener l : listener)
		{
			l.robotMeasured(this, rm);
		}
	}
	
	private RobotMeasurement parseMeasurement(String realMeasures)
	{
		final String[] parts = realMeasures.split(Command.SEPARATOR);
		int colorId = (int)Double.parseDouble(parts[0]);
		List<RobotDistance> distances = new ArrayList<>();
		for(int i = 1; i < parts.length; i += 2)
		{
			double dist = Double.parseDouble(parts[i]);
			double distAngle = Double.parseDouble(parts[i + 1]);
			
			distances.add(new RobotDistance(dist, distAngle));
		}

		RobotMeasurement robotMeasurement = new RobotMeasurement(colorId, distances);
		return robotMeasurement;
	}

	protected String sendCommand(String command, double value)
	{
		return sendCommand(Command.withValue(command, value));
	}
	
	protected String sendCommand(String command)
	{
		out.println(command);
		
		try
		{
			return in.readLine();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			terminate();
		}
		
		return null;
	}

	@Override
	public void terminate()
	{
		// do not use sendCommand as it could lead to an endless loop
		out.println(Command.END);
		
		for(RobotListener l : listener)
		{
			l.robotTerminated(this);
		}
	}

	@Override
	public void addRobotListener(final RobotListener l)
	{
		listener.add(l);
	}

	@Override
	public void removeRobotListener(final RobotListener l)
	{
		listener.remove(l);
	}
}