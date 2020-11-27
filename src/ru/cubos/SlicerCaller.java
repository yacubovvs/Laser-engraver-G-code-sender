package ru.cubos;

import ru.cubos.jobSlicers.JobElements.JobElement;

import java.util.ArrayList;

public interface SlicerCaller {
    public void onSliceCompleted(ArrayList<JobElement> commmands);
}
