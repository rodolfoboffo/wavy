package com.terpomo.wavy.rtl;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

public interface RTLAPI extends Library {

    RTLAPI INSTANCE = Native.load("librtlsdr", RTLAPI.class);

    int rtlsdr_get_device_count();
    String rtlsdr_get_device_name(int index);
    int rtlsdr_open(Pointer[] p, int index);
    int rtlsdr_get_tuner_gains(Pointer p, int[] gains);
}
