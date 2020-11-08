package ru.cubos.customViews.images;

import java.awt.*;
import java.awt.image.BufferedImage;

abstract public class BinaryImage {
    public final static short BLACK_AND_WHITE_1BIT     = 1000;
    public final static short GRAYSCALE_8BIT           = 1001;
    public final static short COLOR_24BIT              = 1002;

    private char width;
    private char height;
    private short type;


    abstract public byte[] getColorPixel(char x, char y);
    abstract public byte getGrayscalePixel(char x, char y);
    abstract public boolean getBinaryPixel(char x, char y);
    abstract public void setColorPixel(char x, char y, byte r, byte g, byte b);
    public void setColorPixel(char x, char y, byte rgb[]){setColorPixel(x, y, rgb[0], rgb[1], rgb[2]);}
    abstract public void setGrayscalePixel(char x, char y, byte v);
    abstract public void setBinaryPixel(char x, char y, boolean v);

    public char getWidth() { return width; }
    public void setWidth(char width) {
        this.width = width;
    }
    public char getHeight() {
        return height;
    }
    public void setHeight(char height) { this.height = height; }

    public short getType() { return type; }
    public void setType(short type) {
        this.type = type;
    }

    public void setImage(BufferedImage image){}

    public BufferedImage getBufferedImageImage(){
        //Profiler.start("getBufferedImageImage");
        BufferedImage bufferedImage = new BufferedImage( getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);

        for(char y=0; y<getHeight(); y++) {
            for(char x=0; x<getWidth(); x++){

                byte pixel[] = getColorPixel(x, y);
                int red = pixel[0] + 128;
                int green = pixel[1] + 128;
                int blue = pixel[2] + 128;
                Color color = new Color(red, green, blue);

                /*
                int pixel = getGrayscalePixel(x,y) + 128;
                Color color = new Color(pixel, pixel, pixel);
                */

                /*
                boolean pixel = getBinaryPixel(x,y);
                Color color;
                if (pixel) color = new Color(255, 255, 255);
                else color = new Color(0, 0, 0);
                */

                bufferedImage.setRGB(x, y, color.getRGB());
            }
        }

        return bufferedImage;
    }

    /*
    * # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    * # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    * # #                                                                                             # #
    * # #                                           HELPERS                                           # #
    * # #                                                                                             # #
    * # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    * # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    * */

    byte check_byte(int val){return check_byte((short) val);}
    byte check_byte(short val){
        if(val<-128) val = -128;
        if(val>127) val = 127;
        return (byte)val;
    }

    /*
    * # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    * # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    * # #                                                                                             # #
    * # #                                           FILTERS                                           # #
    * # #                                                                                             # #
    * # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    * # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    * */

    // Преобразуем в оттенки серого
    // TODO: Переопределить функцию filter_toGray для GRAYSCALE изображения, это повысит скорость
    public void filter_toGray(){filter_toGray(true, true, true);}
    public void filter_toGray(boolean r, boolean g, boolean b){
        if((r && g && b) || (!r && !g && !b)){
            for(char x=0; x<getWidth(); x++){
                for(char y=0; y<getHeight(); y++) {
                    byte grayColor = getGrayscalePixel(x,y);
                    setGrayscalePixel(x, y, grayColor);
                }
            }
        }else {
            int colors = (r ? 1 : 0) + (g ? 1 : 0) + (b ? 1 : 0);

            for(char x=0; x<getWidth(); x++){
                for(char y=0; y<getHeight(); y++) {
                    int color_sum = 0;
                    byte grayColor[] = getColorPixel(x,y);

                    if(r){
                        color_sum += grayColor[0];
                    }

                    if(g){
                        color_sum += grayColor[1];
                    }

                    if(b){
                        color_sum += grayColor[2];
                    }

                    setGrayscalePixel(x,y, (byte)(color_sum/colors));
                }
            }
        }
    }

}
