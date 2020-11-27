package ru.cubos.jobSlicers;

import ru.cubos.Settings;
import ru.cubos.SlicerCaller;
import ru.cubos.Status;
import ru.cubos.customViews.images.BinaryImage_color;

import java.awt.image.BufferedImage;

public class LinearJobSlicer extends JobSlicer {

    public LinearJobSlicer(Settings settings, Status status, BufferedImage bufferedImage, SlicerCaller slicerCaller) {
        super(settings, status, bufferedImage, slicerCaller);
    }

    @Override
    public void slice() {

        for (int y=0; y<image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if(image.IsBlackPixel(x,y)){

                    int nx;
                    for(nx = x; nx < image.getWidth(); nx++){
                        if(!image.IsBlackPixel(nx,y)){
                            nx--;
                            break;
                        }
                    }

                    // Black
                    //System.out.println("black");
                    addLineCommand(x, y, nx, y);
                    x=nx;

                }else{
                    // White
                    //System.out.println("white");
                }
            }
        }

        slicerCaller.onSliceCompleted(this.commmands);
        return;
    }
}
