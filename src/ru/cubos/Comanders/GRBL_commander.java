package ru.cubos.Comanders;

public class GRBL_commander extends Commander {
    @Override
    public String getTravelCommand(double dx, double dy, double dz, double speed) {
        //G1 Z" + status.getManual_stepmoving_z() + " F10000
        String command = "G1";
        command += " X" + dx;
        command += " Y" + dy;
        command += " Z" + dz;
        command+= " F" + speed;

        return command;
    }

    @Override
    public String getSpindlePowerCommand(double power) {
        return "M3 S" + power;
    }

    @Override
    public String getInitCommand() {
        return null;
    }
}
