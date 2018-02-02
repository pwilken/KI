package de.uni.ki.p1;

public class Command {

    public static final String MOVE = "move";
    public static final String ROTATE= "rotate";
    public static final String MEASURE = "measure";
    public static final String END = "end";
    public static final String SEPARATOR = " ";

    public static String withValue(String commandName, double value)
    {
        return commandName + SEPARATOR + value;
    }
}
