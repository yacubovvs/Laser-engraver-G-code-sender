package ru.cubos;

import jssc.SerialPortException;
import jssc.SerialPortList;
import ru.cubos.customViews.ImagePanel;
import ru.cubos.customViews.SerialPortReader;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
    private JScrollPane ImageFormPanelWrapper;
    private JSplitPane menuSplit;
    private JSplitPane splitTerminalImage;
    private JComboBox comboBoxBoundrate;


    private SerialConnector serialConnector;

    public MainForm(){

        setContentPane(mainpanel);
        setTitle("Laser G-code sender");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(new Dimension(800,400));


        setFormEvents();
        updateComboBoxCom_Ports();
        updateComboBoxBoundrate();
        createWindowMenu();
        resizeSplitMenu();
        resizeSplitTerminal();

        setVisible(true);
    }

    void resizeSplitMenu(){
        menuSplit.setDividerLocation(MainForm.this.getWidth() - 440);
    }

    void resizeSplitTerminal(){
        splitTerminalImage.setDividerLocation(MainForm.this.getHeight() - 240);
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

    @Override
    public void onSerialPortRead(String data) {
        printToConsoleString(data);
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

        formImagePanel.setBorder(new EmptyBorder(50,50,50,50));

        this.validate();

    }

    /*
    # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    #                                         SETTING ELEMENTS VIEW  +                                          #
    # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    */

    void updateComboBoxCom_Ports(){
        String[] portNames = SerialPortList.getPortNames();
        comboBoxComPorts.removeAllItems();
        for(String portName: portNames){
            comboBoxComPorts.addItem(portName);
        }
    }

    void updateComboBoxBoundrate(){
        comboBoxBoundrate.removeAllItems();
        //comboBoxBoundrate.addItem("1000000");
        comboBoxBoundrate.addItem("500000");
        //comboBoxBoundrate.addItem("250000");
        comboBoxBoundrate.addItem("230400");
        comboBoxBoundrate.addItem("115200");
        //comboBoxBoundrate.addItem("74880");
        comboBoxBoundrate.addItem("57600");
        //comboBoxBoundrate.addItem("38400");
        //comboBoxBoundrate.addItem("19200");
        comboBoxBoundrate.addItem("9600");
        comboBoxBoundrate.addItem("4800");
        //comboBoxBoundrate.addItem("2400");
        //comboBoxBoundrate.addItem("1200");
        comboBoxBoundrate.addItem("300");
        comboBoxBoundrate.setSelectedIndex(2);
    }

    /*
    # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    #                                         SETTING ELEMENTS VIEW  -                                          #
    # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    */


    /*
    # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    #                                                CONSOLE  +                                                 #
    # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    */

    public void printToConsoleString(String string){
        ConsoleField.setText(ConsoleField.getText() + string + "\n");
    }


    /*
    # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    #                                                CONSOLE  -                                                 #
    # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    */

    /*
    # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    #                                              SERIAL PORT  +                                               #
    # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    */

    private void connectToSerialPort(String serialPort, int boundrate){
        printToConsoleString("Connecting to " + serialPort + " boundrate:" + boundrate);
        serialConnector = new SerialConnector(serialPort, boundrate, MainForm.this);
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
    }

    private void disconnectFromSerialPort(){
        serialConnector.disconnect();
        serialConnector = null;
        connectButton.setText("Connect");
    }

    /*
    # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    #                                              SERIAL PORT  -                                               #
    # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    */

    /*
    # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    #                                              FORM EVENTS  +                                               #
    # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    */

    private void setFormEvents(){
        // # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
        // # Form listeners
        // # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
        this.getRootPane().addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                resizeSplitMenu();
                resizeSplitTerminal();
            }
        });

        // # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
        // # Serial port connect listener
        // # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
        connectButton.addActionListener(actionEvent -> {
            if(serialConnector==null) {
                String serialPortName = comboBoxComPorts.getSelectedItem().toString(); //"/dev/ttyUSB0";
                connectToSerialPort(serialPortName, 115200);
            }else{
                disconnectFromSerialPort();
            }
        });


        // # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
        // # MOVING BUTTON LISTENERS
        // # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
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
                updateComboBoxCom_Ports();
            }
        });
    }

    /*
    # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    #                                              FORM EVENTS  -                                               #
    # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    */

    /*
    # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    #                                      SPINDLE MOVING AND POSITION +                                        #
    # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    */

    private double x_laser_position = 0;
    private double y_laser_position = 0;
    private double z_laser_position = 0;

    private double xy_step_moving = 20;
    private double z_step_moving = 20;

    private void moveFromSpindlePosition(double x, double y, double z){

    }

    private void moveSpindleAbsolute(double x, double y, double z){

    }

    /*
    # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    #                                      SPINDLE MOVING AND POSITION -                                        #
    # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    */
}
