package ru.cubos.jobSlicers;

import ru.cubos.Settings;
import ru.cubos.Status;
import ru.cubos.customViews.images.BinaryImage_color;

import java.awt.image.BufferedImage;

public abstract class jobSlicer {

    BinaryImage_color image;
    Settings settings;
    Status status;

    public jobSlicer(Settings settings, Status status, BufferedImage bufferedImage){
        this.image      = new BinaryImage_color(bufferedImage);
        this.settings   = settings;
        this.status     = status;
    }

    public void slice(){

    }


}
