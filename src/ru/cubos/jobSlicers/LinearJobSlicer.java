package ru.cubos.jobSlicers;

import ru.cubos.Settings;
import ru.cubos.Status;
import ru.cubos.customViews.images.BinaryImage_color;

import java.awt.image.BufferedImage;

public class LinearJobSlicer extends JobSlicer {

    public LinearJobSlicer(Settings settings, Status status, BufferedImage bufferedImage) {
        super(settings, status, bufferedImage);
    }

    @Override
    public void slice() {

        for (int y=0; y<image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if(image.IsBlackPixel(x,y)){
                    // Black
                    System.out.println("black");
                }else{
                    // White
                    System.out.println("white");
                }
            }
        }


        return;
    }
}
