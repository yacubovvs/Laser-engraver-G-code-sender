package ru.cubos;

import jssc.SerialPortException;
import ru.cubos.customViews.ImagePanel;
import ru.cubos.customViews.SerialPortReader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class MainForm extends JFrame implements SerialPortReader {
    private JPanel mainpanel;
    private JButton yPlusButton;
    private JButton yMinusButton;
    private JButton xPlusButton;
    private JButton xMinusButton;
    private JButton zPlusButton;
    private JButton zMinusButton1;
    private JTextArea ConsoleField;
    private JTextField InputCommandField;
    private JButton sendButton;
    private JButton clearButton;
    private JButton connectButton;
    private JComboBox comboBox1;

    private SerialConnector serialConnector;

    public MainForm(){

        setContentPane(mainpanel);
        setTitle("Laser G-code sender");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(new Dimension(800,400));
        setVisible(true);



        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                serialConnector = new SerialConnector("/dev/ttyUSB0", 115200, MainForm.this);
                try {
                    serialConnector.connect();
                } catch (SerialPortException e) {
                    e.printStackTrace();
                }
            }
        });

        xPlusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
            }
        });

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                printToConsoleString(InputCommandField.getText());
                serialConnector.sendToPort(InputCommandField.getText());
                InputCommandField.setText("");
            }
        });

    }

    public void printToConsoleString(String string){
        ConsoleField.setText(ConsoleField.getText() + string + "\n");
    }


    @Override
    public void onSerialPortRead(String data) {
        printToConsoleString(data);
    }
}
