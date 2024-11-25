package com.terpomo.wavy.core;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public abstract class ObservableObject {

    private PropertyChangeSupport changeSupport;

    public void addPropertyChangeListener(
            PropertyChangeListener listener) {
        synchronized (this) {
            if (listener == null) {
                return;
            }
            if (changeSupport == null) {
                changeSupport = new PropertyChangeSupport(this);
            }
            changeSupport.addPropertyChangeListener(listener);
        }
    }

    public void removePropertyChangeListener(
            PropertyChangeListener listener) {
        synchronized (this) {
            if (listener == null || changeSupport == null) {
                return;
            }
            changeSupport.removePropertyChangeListener(listener);
        }
    }

    protected void firePropertyChange(String propertyName,
                                      Object oldValue, Object newValue) {
        PropertyChangeSupport changeSupport;
        synchronized (this) {
            changeSupport = this.changeSupport;
        }
        if (changeSupport == null ||
                (oldValue != null && newValue != null && oldValue.equals(newValue))) {
            return;
        }
        changeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

}
