package com.terpomo.wavy.rtl;

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public class RTLSDRDevice extends Structure {

    public static List<String> fieldOrder = Arrays.asList(new String[]{
            "libusb_context",
            "libusb_device_handle",
            "xfer_buf_num",
            "xfer_buf_len",
            "xfer",
            "xfer_buf",
            "cb",
            "cb_ctx",
    });

    public long libusb_context;
    public long libusb_device_handle ;
    public int xfer_buf_num;
    public int xfer_buf_len;
    public long xfer;
    public long xfer_buf;
    public long cb;
    public long cb_ctx;

    @Override
    public List<String> getFieldOrder() {
        return fieldOrder;
    }
}
