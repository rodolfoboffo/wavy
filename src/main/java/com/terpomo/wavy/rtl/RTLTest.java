package com.terpomo.wavy.rtl;

import com.sun.jna.Pointer;

import java.util.Arrays;

public class RTLTest {

    public static void main(String[] args) {
        int count = RTLAPI.INSTANCE.rtlsdr_get_device_count();
        for (int i = 0; i < count; i++) {
            System.out.println(RTLAPI.INSTANCE.rtlsdr_get_device_name(i));
            Pointer[] devicePtr = new Pointer[1];
            int result = RTLAPI.INSTANCE.rtlsdr_open(devicePtr, i);
            System.out.println(String.format("Open result: %d", result));
            if (result >= 0) {
                int[] gains = new int[100];
                int nGains = RTLAPI.INSTANCE.rtlsdr_get_tuner_gains(devicePtr[0], gains);
                System.out.println(String.format("Number of gains: %d, %s", nGains, Arrays.toString(Arrays.stream(gains).filter(g->g!=0).toArray())));
            }
            break;
        }
    }

}
