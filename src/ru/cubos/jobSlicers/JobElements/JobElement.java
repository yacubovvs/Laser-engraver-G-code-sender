package ru.cubos.jobSlicers.JobElements;

import ru.cubos.jobSlicers.JobElementsCoordinate;

import java.util.ArrayList;

public abstract class JobElement {

    static public enum Type{
        LASER_DRAWING_LINE,
        DRILLING_HOLE,
        CUTTING_LINE
    }

    public ArrayList<JobElementsCoordinate> coordinates = new ArrayList<>();

    private Type type;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

}
