package com.terpomo.wavy.core;

import java.beans.PropertyChangeListener;

public interface IObservableObject {

    public void addPropertyChangeListener(PropertyChangeListener listener);
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);
    public void removePropertyChangeListener(PropertyChangeListener listener);
    public void firePropertyChange(String propertyName, Object oldValue, Object newValue);

}
