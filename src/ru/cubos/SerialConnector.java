package ru.cubos;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import ru.cubos.customViews.SerialPortReader;

public class SerialConnector {

    static SerialPort serialPort;
    String serialPortName = "";
    int serialPortSpeed = 115200;
    SerialPortReader serialPortReader;

    public SerialConnector(String serialPortName, int serialPortSpeed, SerialPortReader serialPortReader){
        this.serialPortName = serialPortName;
        this.serialPortSpeed = serialPortSpeed;
        this.serialPortReader = serialPortReader;
        serialPort = new SerialPort(serialPortName);
    }

    private class PortReader implements SerialPortEventListener {

        public void serialEvent(SerialPortEvent event) {
            if(event.isRXCHAR() && event.getEventValue() > 0){
                try {
                    //byte data[] = serialPort.readBytes(event.getEventValue());
                    String string = serialPort.readString();
                    serialPortReader.onSerialPortRead(string);
                }
                catch (SerialPortException ex) {
                    System.out.println(ex);
                }
            }
        }
    }

    public void connect() throws SerialPortException {
        serialPort.openPort();
        serialPort.setParams(serialPortSpeed, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
        //serialPort.setParams(SerialPort.BAUDRATE_115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

        //serialPort.setParams(SerialPort.BAUDRATE_256000, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
        //serialPort.setParams(512000, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
        //serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT);
        //SerialPort.MASK_RXCHAR

        //n103
        serialPort.addEventListener(new PortReader(), SerialPort.MASK_RXCHAR);

    }


    public boolean sendToPort(String string) {
        try {

            serialPort.writeString(string + "\n");
            /*
            byte data_out[] = new byte[data.length];
            for(int i=0; i<data.length; i++){
                data_out[i] = (byte)(data[i] + 128);
            }
            serialPort.writeBytes(data_out);
            */

            /*
            for(int i=0; i<data.length; i++){
                serialPort.writeByte((byte)(data[i]+128));

                if( (sendSerialDelay>0) && (i%64==0 || i==data.length) )
                try {
                    Thread.sleep(sendSerialDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }*/
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
        return false;
    }

}
