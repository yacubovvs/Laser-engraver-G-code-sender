package ru.cubos.jobSlicers;

import ru.cubos.Comanders.Commander;
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
    ArrayList<String> stringCommmands = new ArrayList<>();

    BinaryImage_color image;
    Settings settings;
    Status status;
    SlicerCaller slicerCaller;
    Commander commander;

    public JobSlicer(Settings settings, Status status, Commander commander, BufferedImage bufferedImage, SlicerCaller slicerCaller){
        this.image          = new BinaryImage_color(bufferedImage);
        this.settings       = settings;
        this.status         = status;
        this.slicerCaller   = slicerCaller;
        this.commander      = commander;
        slice();
    }

    public abstract void slice();

    public void addLineCommand(int x1, int y1, int x2, int y2){
        commmands.add(new JobElement_LaserDrawingLine(x1, y1, x2, y2));
    }
}
