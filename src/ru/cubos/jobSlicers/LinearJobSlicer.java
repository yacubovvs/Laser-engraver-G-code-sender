package ru.cubos.jobSlicers;

import ru.cubos.Comanders.Commander;
import ru.cubos.Settings;
import ru.cubos.SlicerCaller;
import ru.cubos.Status;
import ru.cubos.jobSlicers.JobElements.JobElement;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class LinearJobSlicer extends JobSlicer {

    public LinearJobSlicer(Settings settings, Status status, Commander commander, BufferedImage bufferedImage, SlicerCaller slicerCaller) {
        super(settings, status, commander, bufferedImage, slicerCaller);
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

        prepareStringCommands();
        slicerCaller.onSliceCompleted(this.stringCommmands);
        return;
    }

    void prepareStringCommands(){

        double linesInPixel = settings.getLinesInPixel();
        this.stringCommmands = new ArrayList<>();

        for(JobElement jobElement: this.commmands){
            if(jobElement.getType() == JobElement.Type.LASER_DRAWING_LINE){
                JobElementsCoordinate coordinate1;
                JobElementsCoordinate coordinate2;

                for(int line=0; line<linesInPixel; line++){
                    if(line%2==0){
                        coordinate1 = jobElement.coordinates.get(0);
                        coordinate2 = jobElement.coordinates.get(1);
                    }else{
                        coordinate2 = jobElement.coordinates.get(0);
                        coordinate1 = jobElement.coordinates.get(1);
                    }

                    this.stringCommmands.add(commander.getSpindlePowerCommand(0));
                    this.stringCommmands.add(commander.getTravelCommand(coordinate1.getX()+1, coordinate1.getY() + (1/linesInPixel)*line, 0, settings.BURN_SPEED));
                    this.stringCommmands.add(commander.getSpindlePowerCommand(status.getLaserPowerSettings(settings)));
                    this.stringCommmands.add(commander.getTravelCommand(coordinate2.getX()+1, coordinate2.getY() + (1/linesInPixel)*line, 0, settings.BURN_SPEED));
                    this.stringCommmands.add(commander.getSpindlePowerCommand(0));
                }


            }
        }
    }
}
