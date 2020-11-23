package ru.cubos;

import jssc.SerialPortException;
import jssc.SerialPortList;
import ru.cubos.customViews.CustomJSplitPane;
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
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MainForm extends JFrame implements SerialPortReader {
    private JPanel mainpanel;
    private JButton yPlusButton;
    private JButton yMinusButton;
    private JButton xPlusButton;
    private JButton xMinusButton;
    private JButton zPlusButton;
    private JButton zMinusButton;
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
    private JButton ONLaserButton;
    private JButton OFFLaserButton;
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
    private JButton steppersOffButton;
    private JButton resetXYButton;
    private JButton resetZButton;
    private JButton reinitButton;
    private JTextField textField1;
    private JTextField textField2;
    private JSlider slider2;


    private SerialConnector serialConnector;

    private List<String> commandHistoryList = new ArrayList();
    private int currentHistoryPosition = -1;

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
        startCommandsDaemon();
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
    #                                                DIALOGS  +                                                 #
    # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    */
    private BufferedImage chooseImage() throws IOException {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.showOpenDialog(this);
        Path file = fileChooser.getSelectedFile().toPath();
        //Icon imageIcon = new ImageIcon(file.toUri().toURL());
        //setSize(imageIcon.getIconWidth(), imageIcon.getIconHeight() + 100);

        BufferedImage image = null;
        image = ImageIO.read(file.toFile());
        return image;

        //String decodeText = getDecodeText(file);
        //textArea.setText(decodeText);
    }
    /*
    # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    #                                                DIALOGS  +                                                 #
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

    private void addCommandToCommandHistory(String command) {
        commandHistoryList.add(command);
        if(commandHistoryList.size()>=100){
            commandHistoryList.remove(0);
        }

        currentHistoryPosition = -1;
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

    @Override
    public void onSerialPortRead(String data) {
        printToConsoleString(data);
        lastSerialAnswer = data;
    }

    /*
    # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    #                                              SERIAL PORT  -                                               #
    # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    */

    /*
    # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    #                                        FORM EVENTS AND ACTION  +                                          #
    # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    */

    int menuSplitDividerLocatiom = 380;
    int imageSplitTerminalLocatiom = 240;

    void resizeSplitMenu(){
        menuSplit.setDividerLocation(MainForm.this.getWidth() - menuSplitDividerLocatiom);
    }

    void resizeSplitTerminal(){
        splitTerminalImage.setDividerLocation(MainForm.this.getHeight() - imageSplitTerminalLocatiom);
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
                try {
                    ((ImagePanel)formImagePanel).setImage(chooseImage());
                    MainForm.this.repaint();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return file;
    }

    int form_width_last = 0;
    int form_height_last = 0;
    private void createUIComponents() {

        splitTerminalImage = new CustomJSplitPane() {
            public void onDeviderMove(int deviderLocation){
                int form_height = MainForm.this.getHeight();
                if(form_height!=0 && form_height_last == form_height){
                    MainForm.this.imageSplitTerminalLocatiom = form_height - deviderLocation;
                }

                form_height_last = form_height;
            }
        };
        menuSplit = new CustomJSplitPane() {
            public void onDeviderMove(int deviderLocation){
                int form_width = MainForm.this.getWidth();
                if(form_width!=0 && form_width_last == form_width){
                    MainForm.this.menuSplitDividerLocatiom = form_width - deviderLocation;
                }

                form_width_last = form_width;
            }
        };

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

    private void setFormEvents(){
        // # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
        // # Form listeners
        // # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
        this.getRootPane().addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                resizeSplitMenu();
                resizeSplitTerminal();
                //System.out.println("Resize " + MainForm.this.getWidth());
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
                prepareCommand("G1 X" + xy_step_moving + " F10000");
            }
        });

        xMinusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                prepareCommand("G1 X-" + xy_step_moving + " F10000");
            }
        });

        yPlusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                prepareCommand("G1 Y" + xy_step_moving + " F10000");
            }
        });

        yMinusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                prepareCommand("G1 Y-" + xy_step_moving + " F10000");
            }
        });

        zPlusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                prepareCommand("G1 Z" + z_step_moving + " F10000");
            }
        });

        zMinusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                prepareCommand("G1 Z-" + z_step_moving + " F10000");
            }
        });

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                sendCommand_Form();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                updateComboBoxCom_Ports();
            }
        });

        calibrationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                prepareCommand("G28");
            }
        });

        reinitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                sendCommand("G91");
            }
        });

        ONLaserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                sendCommand("M104 S200");
            }
        });

        OFFLaserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                sendCommand("M104 S0");
            }
        });

        // # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
        // # TERMINAL LISTENERS
        // # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ConsoleField.setText("");
            }
        });

        InputCommandField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
                if(keyEvent.getKeyChar() == '\n'){
                    sendCommand_Form();
                }
                return;
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if(keyEvent.getKeyText(keyEvent.getKeyCode()).equals("Up")){
                    setCommandFromHistory_From(currentHistoryPosition+1);
                }else if(keyEvent.getKeyText(keyEvent.getKeyCode()).equals("Down")){
                    setCommandFromHistory_From(currentHistoryPosition-1);
                }else if(keyEvent.getKeyText(keyEvent.getKeyCode()).equals("Backspace")){
                    if(InputCommandField.getText().length()==1){
                        currentHistoryPosition = -1;
                    }
                }
                return;
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                return;
            }
        });
    }

    /*
    # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    #                                        FORM EVENTS AND ACTIONS -                                          #
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
    private double z_step_moving = 5;

    private void moveFromSpindlePosition(double x, double y, double z){

    }

    private void moveSpindleAbsolute(double x, double y, double z){

    }

    /*
    # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    #                                      SPINDLE MOVING AND POSITION -                                        #
    # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    */

    /*
    # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    #                                           COMMANDS SENDING +                                              #
    # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    */

    void setCommandFromHistory_From(int history_num){
        this.currentHistoryPosition = history_num;
        if(commandHistoryList.size()==0){
            currentHistoryPosition = 1;
            return;
        }else if(history_num>=commandHistoryList.size()) {
            currentHistoryPosition = commandHistoryList.size()-1;
        }else if(currentHistoryPosition<=-1){
            InputCommandField.setText("");
            currentHistoryPosition=-1;
            return;
        }

        InputCommandField.setText(commandHistoryList.get(commandHistoryList.size()- 1 - currentHistoryPosition));
    }

    private void sendCommand_Form(){
        String command = InputCommandField.getText();
        InputCommandField.setText("");

        printToConsoleString(command);
        if(!command.trim().equals("")) addCommandToCommandHistory(command);

        try{
            serialConnector.sendToPort(command);
        }catch(Exception e){
            printToConsoleString("--- ERROR: sending command error");
            if(serialConnector==null) printToConsoleString("--- serial port is not connected");

        }

    }

    private List<String> globalCommandsList = new ArrayList<>();
    private String lastSerialAnswer = "";

    private void startCommandsDaemon(){
        Thread daemonThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    if(lastSerialAnswer!=null && globalCommandsList.size()>0){
                        //has device answer
                        if(lastSerialAnswer.trim().equals("ok")){
                            // run next command
                            lastSerialAnswer = null;
                            sendCommand(globalCommandsList.get(0));
                            globalCommandsList.remove(0);
                        }else{
                        }
                    }

                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        daemonThread.start();
    }

    private void prepareCommands(List<String> commandsList){
        for(String command: commandsList) globalCommandsList.add(command);
    }

    private void prepareCommand(String command){
        globalCommandsList.add(command);
    }

    void sendCommand(String command){
        printToConsoleString(command);
        serialConnector.sendToPort(command);
    }


    /*
    # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    #                                           COMMANDS SENDING -                                              #
    # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    */

}
