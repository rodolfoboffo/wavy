package com.terpomo.wavy.rtl;

public class RTLTest {

    public static void main(String[] args) {
        int count = RTLAPI.INSTANCE.rtlsdr_get_device_count();
        for (int i = 0; i < count; i++) {
            System.out.println(RTLAPI.INSTANCE.rtlsdr_get_device_name(i));
            RTLSDRDevice device = new RTLSDRDevice();
            int r = RTLAPI.INSTANCE.rtlsdr_open(device, i);
            System.out.println(String.format("open result: %d", r));
            break;
        }
    }

}
