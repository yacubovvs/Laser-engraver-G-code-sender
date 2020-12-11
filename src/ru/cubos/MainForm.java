package ru.cubos;

import jssc.SerialPortException;
import jssc.SerialPortList;
import ru.cubos.Comanders.Commander;
import ru.cubos.Comanders.GRBL_commander;
import ru.cubos.customViews.CustomJSplitPane;
import ru.cubos.customViews.ImagePanel;
import ru.cubos.customViews.SerialPortReader;
import ru.cubos.jobSlicers.JobSlicer;
import ru.cubos.jobSlicers.LinearJobSlicer;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class MainForm extends JFrame implements SerialPortReader, SlicerCaller {
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
    private JSpinner XYstep_val;
    private JSpinner Zstep_val;
    private JButton resetHomeButton;
    private JButton calibrationButton;
    private JButton toHomeButton;
    private JButton ONLaserButton;
    private JButton OFFLaserButton;
    private JButton startButton;
    private JButton pauseButton;
    private JButton stopButton;
    private JButton runButton;
    private JSpinner sunSinceCommand;
    private JSlider slider1;
    private JSpinner laserWidth;
    private JSpinner laser_travelSpeed_input;
    private JSpinner laser_burnSpeed_input;
    private JScrollPane ImageFormPanelWrapper;
    private JSplitPane menuSplit;
    private JSplitPane splitTerminalImage;
    private JComboBox comboBoxBoundrate;
    private JButton testbtn;
    private JButton resetXYButton;
    private JButton resetZButton;
    private JButton reinitButton;
    private JSpinner LaserMaxPower_input;
    private JSpinner LaserMinPower_input;
    private JSlider LaserPowerPercent_sliderslider;
    private JLabel laserPower_percent_label;
    private JLabel LaserStatusLabel;
    private JButton RecalculateKobButton;
    private JSpinner pixelSizeInput;
    private JLabel spindle_position_x;
    private JLabel spindle_position_y;
    private JLabel spindle_position_z;
    private JLabel imageRealWidth;
    private JLabel imageRealHeight;

    private static int SEND_LASER_POWER_DELAY_MS = 300;

    int menuSplitDividerLocatiom = 460;
    int imageSplitTerminalLocatiom = 240;

    private SerialConnector serialConnector;

    private List<String> commandHistoryList = new ArrayList();
    private int currentHistoryPosition = -1;

    private Settings settings;
    private Status status;
    private Commander commander;
    private JobSlicer jobSlicer;

    public MainForm(){

        settings = new Settings();
        status = new Status();
        commander = new GRBL_commander();

        setContentPane(mainpanel);
        setTitle("Laser G-code sender");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(new Dimension(800,400));

        updateComboBoxCom_Ports();
        updateComboBoxBoundrate();
        createWindowMenu();
        resizeSplitMenu();
        resizeSplitTerminal();

        startCommandsDaemon();
        loadFromSettings(settings);
        setFormStyle();

        setFormEvents();

        recalculateJob();
        setVisible(true);
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
                    updateImageRealWidth();
                    MainForm.this.repaint();
                    recalculateJob();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return file;
    }

    int form_width_last = 0;
    int form_height_last = 0;



    ArrayList<String> jobCommmandsList;
    @Override
    public void onSliceCompleted(ArrayList<String> commmands) {
        jobCommmandsList = commmands;
    }

    class MovingStepSpinnerModel extends SpinnerNumberModel{
        public MovingStepSpinnerModel(Number value, Comparable<?> minimum, Comparable<?> maximum, Number stepSize) {
            super(value, minimum, maximum, stepSize);
        }

        @Override
        public Object getNextValue() {
            double thisValue = Common.hardParseDouble(this.getValue().toString());
            if (thisValue < 0.1){
                this.setStepSize(0.01);
            }else if (thisValue < 1){
                this.setStepSize(0.1);
            }else if (thisValue < 10){
                this.setStepSize(1);
            }else if (thisValue < 100){
                this.setStepSize(10);
            }else if (thisValue < 1000){
                this.setStepSize(100);
            }else{
                this.setStepSize(1);
            }

            return super.getNextValue();
        }

        @Override
        public Object getPreviousValue(){
            double thisValue = Common.hardParseDouble(this.getValue().toString());
            if (thisValue <= 0.1){
                this.setStepSize(0.01);
            }else if (thisValue <= 1){
                this.setStepSize(0.1);
            }else if (thisValue <= 10){
                this.setStepSize(1);
            }else if (thisValue <= 100){
                this.setStepSize(10);
            }else if (thisValue <= 1000){
                this.setStepSize(100);
            }else{
                this.setStepSize(1);
            }

            return super.getPreviousValue();
        }
    }

    private void createUIComponents() {

        pixelSizeInput              = new JSpinner(new SpinnerNumberModel(100.0, 0.0, 1000.0, 0.1));
        laser_burnSpeed_input       = new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1));
        laser_travelSpeed_input     = new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1));
        sunSinceCommand             = new JSpinner(new SpinnerNumberModel(0, 0, null, 1));
        laserWidth                  = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 2.0, 0.01));
        LaserMinPower_input         = new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1));
        LaserMaxPower_input         = new JSpinner(new SpinnerNumberModel(0, 0, 100000, 1));
        Zstep_val                   = new JSpinner(new MovingStepSpinnerModel(5.0, 0.0, 10000.0, 0.01));
        XYstep_val                  = new JSpinner(new MovingStepSpinnerModel(20.0, 0.0, 10000.0, 0.01));

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
            //updateImageRealWidth();
        } catch (IOException e) {
            e.printStackTrace();
        }

        formImagePanel.setBorder(new EmptyBorder(50,50,50,50));

        this.validate();

    }

    private void setFormStyle(){


        Dictionary<Integer, JLabel> labels = new Hashtable<Integer, JLabel>();
        labels.put(0, new JLabel("<html><font color=#00AA00 size=3>0"));
        labels.put(25, new JLabel("<html><font color=gray size=3>25"));
        labels.put(50, new JLabel("<html><font color=gray size=3>50"));
        labels.put(75, new JLabel("<html><font color=gray size=3>75"));
        labels.put(100, new JLabel("<html><font color=#AA0000 size=3>100"));

        LaserPowerPercent_sliderslider.setPaintLabels(true);
        LaserPowerPercent_sliderslider.setPaintTrack(true);
        LaserPowerPercent_sliderslider.setPaintTicks(true);
        LaserPowerPercent_sliderslider.setLabelTable(labels);

    }

    private Thread updateLaserValueThread = null;

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

        LaserPowerPercent_sliderslider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {

                if(updateLaserValueThread==null){
                    updateLaserValueThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(SEND_LASER_POWER_DELAY_MS);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            status.setLaserPower(LaserPowerPercent_sliderslider.getValue());
                            setCurrentLaserPower();

                            updateLaserValueThread = null;
                        }
                    });

                    updateLaserValueThread.start();
                }


                return;
            }
        });

        ChangeListener changeListener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                saveToSettings(settings);
                saveToStatusFromForm(status);
                updateImageRealWidth();
            }
        };

        LaserMaxPower_input.addChangeListener(changeListener);
        LaserMinPower_input.addChangeListener(changeListener);
        laserWidth.addChangeListener(changeListener);
        laser_burnSpeed_input.addChangeListener(changeListener);
        laser_travelSpeed_input.addChangeListener(changeListener);
        Zstep_val.addChangeListener(changeListener);
        XYstep_val.addChangeListener(changeListener);
        laser_burnSpeed_input.addChangeListener(changeListener);
        pixelSizeInput.addChangeListener(changeListener);

        // # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
        // # Serial port connect listener
        // # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #

        connectButton.addActionListener(actionEvent -> {
            if(serialConnector==null) {
                String serialPortName = comboBoxComPorts.getSelectedItem().toString(); //"/dev/ttyUSB0";
                connectToSerialPort(serialPortName, Common.hardParseInt(comboBoxBoundrate.getSelectedItem().toString()));
            }else{
                disconnectFromSerialPort();
            }
        });


        // # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
        // # Laser
        // # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
        ONLaserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                status.setLaserOn(true);
                setCurrentLaserPower();
            }
        });

        OFFLaserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                status.setLaserOn(false);
                prepareCommand(commander.getSpindlePowerCommand(0));
                setCurrentLaserPower();
            }
        });

        // # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
        // # MOVING BUTTON LISTENERS
        // # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #

        RecalculateKobButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                recalculateJob();
            }
        });

        // # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
        // # MOVING BUTTON LISTENERS
        // # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #

        resetHomeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                prepareCommand("G92 X0 Y0 Z0");
                status.x = 0;
                status.y = 0;
                status.z = 0;
                updateSpindlePosition();
            }
        });

        resetXYButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                prepareCommand("G92 X0 Y0");
                status.x = 0;
                status.y = 0;
                updateSpindlePosition();
            }
        });

        resetZButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                prepareCommand("G92 Z0");
                status.z = 0;
                updateSpindlePosition();
            }
        });

        toHomeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                resetPositions();
                prepareCommand(commander.getTravelCommand(0, 0, 0, settings.TRAVEL_SPEED));
            }
        });

        xPlusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                spindelTravel(status.getManual_stepmoving_xy(), 0,0, settings.TRAVEL_SPEED);

            }
        });

        xMinusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                spindelTravel(-status.getManual_stepmoving_xy(), 0,0, settings.TRAVEL_SPEED);
            }
        });

        yPlusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                spindelTravel(0, status.getManual_stepmoving_xy(),0, settings.TRAVEL_SPEED);
            }
        });

        yMinusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                spindelTravel(0, -status.getManual_stepmoving_xy(),0, settings.TRAVEL_SPEED);
            }
        });

        zPlusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                spindelTravel(0, 0,status.getManual_stepmoving_z(), settings.TRAVEL_SPEED);
            }
        });

        zMinusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                spindelTravel(0, 0,-status.getManual_stepmoving_z(), settings.TRAVEL_SPEED);
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

        calibrationButton.setVisible(false);
        calibrationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //prepareCommand("G28");
            }
        });

        reinitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                sendCommand("G21");
                prepareCommand("G92 X0 Y0 Z0");
            }
        });

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(jobCommmandsList!=null) {
                    for (String command : MainForm.this.jobCommmandsList) {
                        prepareCommand(command);
                    }
                }
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                globalCommandsList.clear();
            }
        });

        testbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                prepareCommand(commander.getSpindlePowerCommand(status.getLaserPowerSettings(settings)));
                prepareCommand("G0 X20 Y0 Z0");
                prepareCommand(commander.getSpindlePowerCommand(0));
                prepareCommand("G0 X0 Y0 Z0");
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

    private void updateImageRealWidth(){
        BufferedImage image = ((ImagePanel)formImagePanel).getImage();
        if(image!=null) {
            imageRealWidth.setText("" + (double) (image.getWidth()/settings.PIXELS_IN_MM));
            imageRealHeight.setText("" + (double) (image.getHeight()/settings.PIXELS_IN_MM));
        }
    }

    /*
    # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    #                                        FORM EVENTS AND ACTIONS -                                          #
    # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    */

    /*
    # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    #                                                  JOB +                                                    #
    # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    */

    private void recalculateJob(){
        jobSlicer = new LinearJobSlicer(settings, status, commander, ((ImagePanel)formImagePanel).getImage(), this);
    }

    private void saveToStatusFromForm(Status status) {
        status.setManual_stepmoving_xy(settings.MANUAL_MOVE_XY_STEP_INIT);
        status.setManual_stepmoving_z(settings.MANUAL_MOVE_Z_STEP_INIT);
    }

    /*
    # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    #                                                  JOB +                                                    #
    # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    */

    /*
    # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    #                                      SPINDLE MOVING AND POSITION +                                        #
    # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    */

    private void resetPositions() {
        status.z = 0;
        status.x = 0;
        status.y = 0;
        updateSpindlePosition();
    }

    private void updateSpindlePosition(){
        spindle_position_x.setText("X: " + status.x + " mm");
        spindle_position_y.setText("Y: " + status.y + " mm");
        spindle_position_z.setText("Z: " + status.z + " mm");
    }

    private void spindelTravel(double dx, double dy, double dz, double speed){
        status.x += dx;
        status.y += dy;
        status.z += dz;

        updateSpindlePosition();
        prepareCommand(commander.getTravelCommand(status.x, status.y, status.z, speed));
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
                        }
                    }

                    if(lastSerialAnswer!=null && lastSerialAnswer.trim().equals("Grbl 1.1f ['$' for help]")){
                        lastSerialAnswer = null;
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        globalCommandsList.clear();
                        resetPositions();
                        sendCommand("G21");
                        prepareCommand("G92 X0 Y0 Z0");


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
        System.out.println("Command to prepare " + command);
        globalCommandsList.add(command);
    }

    void sendCommand(String command){
        printToConsoleString(command);
        if(serialConnector==null){
            printToConsoleString("-- Error: Serial port disconnected");
        }else{
            serialConnector.sendToPort(command);
        }

    }


    private void setCurrentLaserPower() {
        laserPower_percent_label.setText("" + status.getCurrentLaserPowerAbsolute(settings));

        if(status.getCurrentLaserPowerAbsolute(settings)==0){
            if(status.isLaserOn()) prepareCommand(commander.getSpindlePowerCommand(0));
            LaserStatusLabel.setText("OFF");
        }else{
            prepareCommand(commander.getSpindlePowerCommand(status.getCurrentLaserPowerAbsolute(settings)));
            LaserStatusLabel.setText("ON");
        }
    }
    /*
    # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    #                                           COMMANDS SENDING -                                              #
    # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    */

    /*
    # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    #                                                SETTINGS  +                                                #
    # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    */

    private void loadFromSettings(Settings settings){
        XYstep_val.setValue(settings.MANUAL_MOVE_XY_STEP_INIT);
        Zstep_val.setValue(settings.MANUAL_MOVE_Z_STEP_INIT);

        laserWidth.setValue(settings.LASER_WIDTH);
        LaserMaxPower_input.setValue(settings.LASER_MAX_POWER);
        LaserMinPower_input.setValue(settings.LASER_MIN_POWER);

        laser_travelSpeed_input.setValue(settings.TRAVEL_SPEED);
        laser_burnSpeed_input.setValue(settings.BURN_SPEED);

        pixelSizeInput.setValue(settings.PIXELS_IN_MM);
    }

    private void saveToSettings(Settings settings){
        settings.MANUAL_MOVE_XY_STEP_INIT = Common.hardParseDouble(XYstep_val.getValue().toString());
        settings.MANUAL_MOVE_Z_STEP_INIT = Common.hardParseDouble(Zstep_val.getValue().toString());

        settings.LASER_WIDTH = Common.hardParseDouble(laserWidth.getValue().toString());
        settings.LASER_MAX_POWER = Common.hardParseInt(LaserMaxPower_input.getValue().toString());
        settings.LASER_MIN_POWER = Common.hardParseInt(LaserMinPower_input.getValue().toString());

        settings.TRAVEL_SPEED = Common.hardParseInt(laser_travelSpeed_input.getValue().toString());
        settings.BURN_SPEED = Common.hardParseInt(laser_burnSpeed_input.getValue().toString());

        settings.PIXELS_IN_MM = Common.hardParseDouble(pixelSizeInput.getValue().toString());

        //setCurrentLaserPower();
    }

    /*
    # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    #                                                SETTINGS  -                                                #
    # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
    */
}
