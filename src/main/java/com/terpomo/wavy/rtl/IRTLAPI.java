package com.terpomo.wavy.rtl;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

public interface IRTLAPI extends Library {

    IRTLAPI INSTANCE = Native.load("librtlsdr", IRTLAPI.class);

    interface IReadAsyncCallback extends Callback {
        void invoke(Pointer buffer, int length, Pointer contextPointer);
    }

    int rtlsdr_get_device_count();
    String rtlsdr_get_device_name(int index);
    int rtlsdr_open(Pointer[] p, int index);
    int rtlsdr_close(Pointer devicePointer);
    int rtlsdr_get_sample_rate(Pointer devicePointer);
    int rtlsdr_set_sample_rate(Pointer devicePointer, int sampleRate);
    int rtlsdr_get_tuner_gains(Pointer devicePointer, int[] gains);
    int rtlsdr_get_tuner_gain(Pointer devicePointer);
    int rtlsdr_set_tuner_gain(Pointer devicePointer, int gain);
    int rtlsdr_get_center_freq(Pointer devicePointer);
    long rtlsdr_get_center_freq64(Pointer devicePointer);
    int rtlsdr_set_center_freq(Pointer devicePointer, int freq);
    int rtlsdr_set_center_freq64(Pointer devicePointer, long freq);
    int rtlsdr_get_direct_sampling(Pointer devicePointer);
    int rtlsdr_set_direct_sampling(Pointer devicePointer, boolean on);
    int rtlsdr_read_sync(Pointer devicePointer, byte[] buffer, int bufferSize, int[] bytesRead);
    int rtlsdr_reset_buffer(Pointer devicePointer);
    int rtlsdr_set_testmode(Pointer devicePointer, boolean on);
    int rtlsdr_set_ds_mode(Pointer devicePointer, int mode, int freqThreshold);
    int rtlsdr_wait_async(Pointer devicePointer, IReadAsyncCallback cb, Pointer contextPointer);
    int rtlsdr_read_async(Pointer devicePointer, IReadAsyncCallback cb, Pointer contextPointer, int bufNum, int bufLen);
    int rtlsdr_cancel_async(Pointer devicePointer);

}
