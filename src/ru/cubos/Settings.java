package ru.cubos;

public class Settings{

    public double MANUAL_MOVE_XY_STEP_INIT = 20;
    public double MANUAL_MOVE_Z_STEP_INIT = 10;

    public int LASER_MAX_POWER = 1024;
    public int LASER_MIN_POWER = 0;
    public double LASER_WIDTH = 1;

    public int TRAVEL_SPEED = 1000;
    public int BURN_SPEED = 500;

    public double PIXELS_IN_MM = 1.0;

    public double getLinesInPixel(){
        return this.PIXELS_IN_MM/this.LASER_WIDTH;
    }
}
