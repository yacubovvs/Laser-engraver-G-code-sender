package ru.cubos;

import jssc.SerialPortException;
import jssc.SerialPortList;
import ru.cubos.customViews.ImagePanel;
import ru.cubos.customViews.SerialPortReader;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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
    private JComboBox comboBoxComPorts;
    private JButton updateButton;
    private JProgressBar progressBar1;
    private JPanel formImagePanel;
    private JSpinner spinner1;
    private JSpinner spinner2;
    private JButton resetHomeButton;
    private JButton calibrationButton;
    private JButton toHomeButton;
    private JButton ONButton;
    private JButton OFFButton;
    private JButton startButton;
    private JButton pauseButton;
    private JButton stopButton;
    private JButton runButton;
    private JSpinner spinner3;
    private JSlider slider1;
    private JSpinner spinner4;
    private JSpinner spinner5;
    private JSpinner spinner6;
    private JSpinner spinner7;
    private JSpinner spinner8;

    private SerialConnector serialConnector;

    public MainForm(){

        setContentPane(mainpanel);
        setTitle("Laser G-code sender");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(new Dimension(800,400));
        setVisible(true);



        connectButton.addActionListener(actionEvent -> {

            if(serialConnector==null) {
                String serialPortName = "/dev/ttyUSB0";
                int serialPortSpeed = 115200;

                printToConsoleString("Connecting to " + serialPortName + " boundrate:" + serialPortSpeed);
                serialConnector = new SerialConnector(serialPortName, serialPortSpeed, MainForm.this);
                try {
                    serialConnector.connect();
                } catch (SerialPortException e) {
                    e.printStackTrace();
                    printToConsoleString("Connecting failed");
                    serialConnector.disconnect();
                    serialConnector = null;
                    connectButton.setText("Connect");
                }
                connectButton.setText("Disconnect");
            }else{
                serialConnector.disconnect();
                serialConnector = null;
                connectButton.setText("Connect");
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

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                updateComboBoxComPorts();
            }
        });

        updateComboBoxComPorts();
        createWindowMenu();
    }

    private void createWindowMenu(){
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        setJMenuBar(menuBar);

        this.validate();
    }

    private JMenu createFileMenu()
    {
        JMenu file = new JMenu("Файл");
        //JMenuItem open = new JMenuItem("Открыть", new ImageIcon("images/open.png"));
        JMenuItem open = new JMenuItem("Open");
        JMenuItem saveGCode = new JMenuItem("Save G-code");
        JMenuItem exit = new JMenuItem("Exit");
        file.add(open);
        file.add(saveGCode);
        file.addSeparator();
        file.add(exit);
        open.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                System.out.println ("ActionListener.actionPerformed : open");
            }
        });
        return file;
    }

    public void printToConsoleString(String string){
        ConsoleField.setText(ConsoleField.getText() + string + "\n");
    }


    @Override
    public void onSerialPortRead(String data) {
        printToConsoleString(data);
    }

    void updateComboBoxComPorts(){
        String[] portNames = SerialPortList.getPortNames();
        comboBoxComPorts.removeAllItems();
        for(String portName: portNames){
            comboBoxComPorts.addItem(portName);
        }
    }

    private void createUIComponents() {
        formImagePanel = new ImagePanel();

        File img = new File("images/test_pcb.png");
        try {
            BufferedImage image = ImageIO.read(img );
            ((ImagePanel)formImagePanel).setImage(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
