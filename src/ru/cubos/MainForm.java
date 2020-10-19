package ru.cubos;

import ru.cubos.customViews.ImagePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class MainForm extends JFrame{
    private JPanel mainpanel;
    private JButton yButton;
    private JButton yButton1;
    private JButton xButton;
    private JButton xButton1;
    private JButton zButton;
    private JButton zButton1;


    public MainForm(){

        setContentPane(mainpanel);
        setTitle("Laser G-code sender");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(new Dimension(800,400));
        setVisible(true);

    }

}
