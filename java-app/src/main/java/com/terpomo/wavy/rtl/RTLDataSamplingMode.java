package com.terpomo.wavy.rtl;

public enum RTLDataSamplingMode {
    RTLSDR_DS_IQ(0),
    RTLSDR_DS_I(1),
    RTLSDR_DS_Q(2),
    RTLSDR_DS_I_BELOW(3),
    RTLSDR_DS_Q_BELOW(4);

    private int value;

    RTLDataSamplingMode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
