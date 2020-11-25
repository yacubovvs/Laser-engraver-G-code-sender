package ru.cubos.Comanders;

public abstract class Commander {

    abstract public String getTravelCommand(double dx, double dy, double dz, double speed);
    abstract public String getSpindlePowerCommand(double power);
    abstract public String getInitCommand();
}
