package ru.cubos.jobSlicers;

import ru.cubos.Comanders.Commander;
import ru.cubos.Settings;
import ru.cubos.SlicerCaller;
import ru.cubos.Status;
import ru.cubos.jobSlicers.JobElements.JobElement;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class LinearToAndBackJobSlicer extends JobSlicer {

    public LinearToAndBackJobSlicer(Settings settings, Status status, Commander commander, BufferedImage bufferedImage, SlicerCaller slicerCaller) {
        super(settings, status, commander, bufferedImage, slicerCaller);
    }

    @Override
    public void slice() {

        for (int y=0; y<image.getHeight(); y++) {
            System.out.println("y");
            for (int x = 0; x < image.getWidth(); x++) {
                if(y%2==0) {
                    if (image.IsBlackPixel(x, y)) {
                        // Black
                        int nx;
                        for (nx = x; nx < image.getWidth(); nx++) {
                            if (!image.IsBlackPixel(nx, y)) {
                                nx--;
                                break;
                            }
                        }
                        addLineCommand(x, y, nx, y);
                        x = nx;
                    }
                }else{
                    if (image.IsBlackPixel(x, y)) {
                        // Black
                        int nx;
                        for (nx = x; nx < image.getWidth(); nx++) {
                            if (!image.IsBlackPixel(image.getWidth() - nx, y)) {
                                nx--;
                                break;
                            }
                        }
                        addLineCommand(x, y, image.getWidth()-nx, y);
                        x = nx + 1;
                    }
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

        int total_commands = 0;
        double total_length = 0;
        double total_burn_length = 0;

        double last_x=0;
        double last_y=0;

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

                    double x1 = coordinate1.getX()/settings.PIXELS_IN_MM;
                    double y1 = coordinate1.getY()/settings.PIXELS_IN_MM;
                    double x2 = (coordinate2.getX()+1)/settings.PIXELS_IN_MM;
                    double y2 = coordinate2.getY()/settings.PIXELS_IN_MM;

                    total_commands ++;
                    total_length += Math.sqrt(Math.pow(x1-last_x, 2) + Math.pow(y1-last_y, 2));
                    total_length += Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2));
                    total_burn_length += Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2));
                    last_x = x2;
                    last_y = y2;

                    this.stringCommmands.add(commander.getSpindlePowerCommand("0"));
                    this.stringCommmands.add(commander.getTravelCommand(x1, y1 + (1/linesInPixel)*line, 0, "---TRAVEL_SPEED---"));
                    this.stringCommmands.add(commander.getSpindlePowerCommand("---LASER_POWER_BURN---")); // status.getLaserPowerSettings(settings)
                    this.stringCommmands.add(commander.getTravelCommand(x2, y2 + (1/linesInPixel)*line, 0, "---BURN_SPEED---"));
                    this.stringCommmands.add(commander.getSpindlePowerCommand("0"));
                }
            }
        }

        System.out.println("Slicing finished. Length: " + total_length + ", commands: " + total_commands + ", burn length: " + total_burn_length);
    }
}
