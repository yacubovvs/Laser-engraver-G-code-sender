package ru.cubos.jobSlicers;

import ru.cubos.Settings;
import ru.cubos.Status;
import ru.cubos.customViews.images.BinaryImage_color;

import java.awt.image.BufferedImage;

public abstract class JobSlicer {

    BinaryImage_color image;
    Settings settings;
    Status status;

    public JobSlicer(Settings settings, Status status, BufferedImage bufferedImage){
        this.image      = new BinaryImage_color(bufferedImage);
        this.settings   = settings;
        this.status     = status;
        slice();
    }

    public abstract void slice();


}
