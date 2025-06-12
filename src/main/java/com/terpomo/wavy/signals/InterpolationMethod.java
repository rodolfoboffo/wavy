package com.terpomo.wavy.signals;

public enum InterpolationMethod {
    NEAREST("Nearest"),
    LINEAR("Linear");

    private final String friendlyName;

    InterpolationMethod(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    @Override
    public String toString() {
        return this.getFriendlyName();
    }
}
