package com.terpomo.wavy.rtl;

import com.sun.jna.Pointer;

public class RTLSDR {

    public static int getDevicesCount() {
        int count = IRTLAPI.INSTANCE.rtlsdr_get_device_count();
        return count;
    }

    public static String[] listDeviceNames() {
        int count = RTLSDR.getDevicesCount();
        String[] deviceNames = new String[count];
        for (int deviceIndex = 0; deviceIndex < count; deviceIndex++) {
            deviceNames[deviceIndex]  = IRTLAPI.INSTANCE.rtlsdr_get_device_name(deviceIndex);
        }
        return deviceNames;
    }

    public static RTLSDRDevice openDevice(int deviceIndex) {
        Pointer[] devicePtr = new Pointer[1];
        int result = IRTLAPI.INSTANCE.rtlsdr_open(devicePtr, deviceIndex);
        if (result >= 0) {
            return new RTLSDRDevice(devicePtr[0]);
        }
        else {
            throw new RuntimeException(String.format("Could not open RTLSDR device. Error code %d", result));
        }
    }

}
