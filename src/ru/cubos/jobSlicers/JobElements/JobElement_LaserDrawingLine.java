package ru.cubos.jobSlicers.JobElements;

import ru.cubos.jobSlicers.JobElementsCoordinate;

public class JobElement_LaserDrawingLine extends JobElement {
    public JobElement_LaserDrawingLine(double x, double y, double x1, double y1){
        this.setType(Type.LASER_DRAWING_LINE);
        coordinates.add(new JobElementsCoordinate(x,y));
        coordinates.add(new JobElementsCoordinate(x1,y1));
    }
}
