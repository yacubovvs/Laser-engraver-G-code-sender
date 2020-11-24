package ru.cubos;

public class Common {
    public static int hardParseInt(String string){
        try {
            int value = Integer.parseInt(string);
            return value;
        }catch (Exception ex){
            return -1;
        }
    }

    public static double hardParseDouble(String string){
        try {
            double value = Double.parseDouble(string);
            return value;
        }catch (Exception ex){
            return -1.0;
        }
    }
}
