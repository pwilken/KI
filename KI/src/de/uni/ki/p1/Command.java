package de.uni.ki.p1;

public class Command {

    public static final String MOVE = "move";
    public static final String ROTATE= "rotate";
    public static final String ROTATE_US = "rotate-us";
    public static final String MEASUREMENT = "measurement";
    public static final String END = "rotate-us";
    public static final String SEPARATOR = " ";

    public static String createCommand(String commandName, double value)
    {
        return commandName + SEPARATOR + value;
    }
}
