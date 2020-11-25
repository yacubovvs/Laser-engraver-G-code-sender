package ru.cubos;

import ru.cubos.Settings;

public class Status {
    public double x;
    public double y;
    public double z;

    private double manual_stepmoving_xy = 20;
    private double manual_stepmoving_z = 5;

    private boolean laserOn = false;
    private int laserPower = 0;

    public boolean isLaserOn() {
        return laserOn;
    }

    public void setLaserOn(boolean laserOn) {
        this.laserOn = laserOn;
    }

    public int getLaserPower(Settings settings) {
        return laserPower;
    }

    public int getCurrentLaserPowerAbsolute(Settings settings) {
        if(laserOn) return settings.LASER_MIN_POWER + laserPower*(settings.LASER_MAX_POWER - settings.LASER_MIN_POWER)/100;
        else return 0;
    }

    public void setLaserPower(int laserPower) {
        this.laserPower = laserPower;
    }

    public double getManual_stepmoving_xy() {
        return manual_stepmoving_xy;
    }

    public void setManual_stepmoving_xy(double manual_stepmoving_xy) {
        this.manual_stepmoving_xy = manual_stepmoving_xy;
    }

    public double getManual_stepmoving_z() {
        return manual_stepmoving_z;
    }

    public void setManual_stepmoving_z(double manual_stepmoving_z) {
        this.manual_stepmoving_z = manual_stepmoving_z;
    }
}
