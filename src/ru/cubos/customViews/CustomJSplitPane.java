package ru.cubos.customViews;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public abstract class CustomJSplitPane extends JSplitPane implements PropertyChangeListener//ComponentListener
{

    public CustomJSplitPane(){
        this.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if(propertyChangeEvent.getPropertyName().equals("dividerLocation")){
            this.onDeviderMove(this.getDividerLocation());
        }
    }

    protected abstract void onDeviderMove(int deviderLocation);
}
