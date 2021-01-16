package ru.cubos;

public class Settings{

    public double MANUAL_MOVE_XY_STEP_INIT = 20;
    public double MANUAL_MOVE_Z_STEP_INIT = 10;

    public int LASER_MAX_POWER = 1024;
    public int LASER_MIN_POWER = 0;
    public double LASER_WIDTH = 0.25;

    public int TRAVEL_SPEED = 10000;
    public int BURN_SPEED = 2000;

    public double PIXELS_IN_MM = 5;

    public double getLinesInPixel(){
        double lines = this.LASER_WIDTH/this.PIXELS_IN_MM;
        if(lines<1) return 1;
        else{
            return (int)lines;
        }
    }
}
