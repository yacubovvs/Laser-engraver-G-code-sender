package ru.cubos.jobSlicers;

import ru.cubos.Settings;
import ru.cubos.SlicerCaller;
import ru.cubos.Status;
import ru.cubos.customViews.images.BinaryImage_color;
import ru.cubos.jobSlicers.JobElements.JobElement;
import ru.cubos.jobSlicers.JobElements.JobElement_LaserDrawingLine;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class JobSlicer {

    ArrayList<JobElement> commmands = new ArrayList<>();

    BinaryImage_color image;
    Settings settings;
    Status status;
    SlicerCaller slicerCaller;

    public JobSlicer(Settings settings, Status status, BufferedImage bufferedImage, SlicerCaller slicerCaller){
        this.image          = new BinaryImage_color(bufferedImage);
        this.settings       = settings;
        this.status         = status;
        this.slicerCaller   = slicerCaller;
        slice();
    }

    public abstract void slice();

    public void addLineCommand(int x1, int y1, int x2, int y2){
        commmands.add(new JobElement_LaserDrawingLine(x1, y1, x2, y2));
    }
}
