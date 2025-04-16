package com.terpomo.wavy.rtl;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

public interface RTLAPI extends Library {

    RTLAPI INSTANCE = Native.load("librtlsdr", RTLAPI.class);

    int rtlsdr_get_device_count();
    String rtlsdr_get_device_name(int index);
    int rtlsdr_open(Pointer[] p, int index);
    int rtlsdr_close(Pointer p);
    int rtlsdr_get_sample_rate(Pointer p);
    int rtlsdr_set_sample_rate(Pointer p, int sampleRate);
    int rtlsdr_get_tuner_gains(Pointer p, int[] gains);
    int rtlsdr_get_tuner_gain(Pointer p);
    int rtlsdr_set_tuner_gain(Pointer p, int gain);
    int rtlsdr_get_center_freq(Pointer p);
    long rtlsdr_get_center_freq64(Pointer p);
    int rtlsdr_set_center_freq(Pointer p, int freq);
    int rtlsdr_set_center_freq64(Pointer p, long freq);
    int rtlsdr_get_direct_sampling(Pointer p);
    int rtlsdr_set_direct_sampling(Pointer p, boolean on);
    int rtlsdr_read_sync(Pointer p, byte[] buffer, int bufferSize, int[] bytesRead);
    int rtlsdr_reset_buffer(Pointer p);
    int rtlsdr_set_testmode(Pointer p, boolean on);
    int rtlsdr_set_ds_mode(Pointer p, int mode, int freqThreshold);

}
