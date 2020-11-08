package ru.cubos.customViews.images;

import java.awt.image.BufferedImage;

public class BinaryImage_color extends BinaryImage {
    protected byte data[];

    public BinaryImage_color(BufferedImage image) {
        this.setType(COLOR_24BIT);
        setImage(image);
    }

    public BinaryImage_color() {
        this.setType(COLOR_24BIT);
    }

    public BinaryImage_color(char width, char height){
        this.setType(COLOR_24BIT);

        data = new byte[width * height * 3];
        this.setHeight(height);
        this.setWidth(width);
    }

    @Override
    public void setImage(BufferedImage image){
        data = new byte[image.getHeight() * image.getWidth() * 3];
        this.setHeight((char)image.getHeight());
        this.setWidth((char)image.getWidth());

        for(char y=0; y<image.getHeight(); y++){
            for(char x=0; x<image.getWidth(); x++){
                int color = image.getRGB(x, y);

                int blue = color & 0xff;
                int green = (color & 0xff00) >> 8;
                int red = (color & 0xff0000) >> 16;

                this.setColorPixel(x,y, (byte)(red-128), (byte)(green-128), (byte)(blue-128));

                continue;
            }
        }
    }

    @Override
    public BinaryImage clone() {
        BinaryImage_color binaryImage_color = new BinaryImage_color();
        binaryImage_color.setWidth((char)(getWidth()));
        binaryImage_color.setHeight((char)(getHeight()));

        binaryImage_color.data = new byte[data.length];

        System.arraycopy(data, 0, binaryImage_color.data, 0, data.length);
        /*
        for(int i=0; i<data.length; i++){
            binaryImage_color.data[i] = data[i];
        }
        */

        return binaryImage_color;
    }

    @Override
    public byte[] getColorPixel(char x, char y){
        int position = (x + (getWidth())*y)*3;
        byte pixel[] = {
                data[position],
                data[position + 1],
                data[position + 2]
        };
        return pixel;
    }

    @Override
    public byte getGrayscalePixel(char x, char y){
        int position = (x + (getWidth())*y)*3;
        return (byte)((data[position] + data[position + 1] + data[position + 2])/3);
    }

    @Override
    public boolean getBinaryPixel(char x, char y){
        int position = (x + (getWidth())*y)*3;
        return (data[position] + data[position + 1] + data[position + 2])>=0;
    }

    @Override
    public void setColorPixel(char x, char y, byte r, byte g, byte b){
        int position = (x + (getWidth())*y)*3;
        data[position]      = r;
        data[position + 1]  = g;
        data[position + 2]  = b;
    }

    @Override
    public void setGrayscalePixel(char x, char y, byte v){
        int position = (x + (getWidth())*y)*3;
        data[position]      = v;
        data[position + 1]  = v;
        data[position + 2]  = v;
    }

    @Override
    public void setBinaryPixel(char x, char y, boolean v){
        int position = (x + (getWidth())*y)*3;
        if(v) {
            data[position] = 127;
            data[position + 1] = 127;
            data[position + 2] = 127;
        }else{
            data[position] = -128;
            data[position + 1] = -128;
            data[position + 2] = -128;
        }
    }



}