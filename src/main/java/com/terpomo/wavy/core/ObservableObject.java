package com.terpomo.wavy.core;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public abstract class ObservableObject implements IObservableObject {

    private PropertyChangeSupport changeSupport;

    public void addPropertyChangeListener(PropertyChangeListener listener) {
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

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        synchronized (this) {
            if (listener == null) {
                return;
            }
            if (changeSupport == null) {
                changeSupport = new PropertyChangeSupport(this);
            }
            changeSupport.addPropertyChangeListener(propertyName, listener);
        }
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        synchronized (this) {
            if (listener == null || changeSupport == null) {
                return;
            }
            changeSupport.removePropertyChangeListener(listener);
        }
    }

    public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        PropertyChangeSupport changeSupport;
        synchronized (this) {
            changeSupport = this.changeSupport;
        }
        if (changeSupport == null || (oldValue != null && newValue != null && oldValue.equals(newValue))) {
            return;
        }
        changeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

}
