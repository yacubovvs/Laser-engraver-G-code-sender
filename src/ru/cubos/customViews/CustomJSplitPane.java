package ru.cubos.customViews;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public abstract class CustomJSplitPane extends JSplitPane implements PropertyChangeListener//ComponentListener
{

    public CustomJSplitPane(){

        this.addPropertyChangeListener(this);
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
            return;
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                CustomJSplitPane.this.onDeviderMove(CustomJSplitPane.this.getDividerLocation());
            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if(propertyChangeEvent.getPropertyName().equals("dividerLocation")){
           this.onDeviderMove(this.getDividerLocation());
        }
        return;
    }

    protected abstract void onDeviderMove(int deviderLocation);
}
