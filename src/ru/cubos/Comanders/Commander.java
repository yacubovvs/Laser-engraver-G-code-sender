package ru.cubos.Comanders;

public abstract class Commander {

    abstract public String getTravelCommand(double dx, double dy, double dz, String speed);
    abstract public String getSpindlePowerCommand(String power);
    abstract public String getInitCommand();
}
