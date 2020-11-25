package ru.cubos.customViews;

import ru.cubos.Settings;

public class Status {
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
}
