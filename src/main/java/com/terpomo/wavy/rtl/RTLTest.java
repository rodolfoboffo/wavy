package com.terpomo.wavy.rtl;

import com.sun.jna.Pointer;

import java.util.Arrays;

public class RTLTest {

    public static void main(String[] args) throws InterruptedException {
        int count = IRTLAPI.INSTANCE.rtlsdr_get_device_count();
        for (int deviceIndex = 0; deviceIndex < count; deviceIndex++) {
            System.out.println(IRTLAPI.INSTANCE.rtlsdr_get_device_name(deviceIndex));
            Pointer[] devicePtr = new Pointer[1];
            int result = IRTLAPI.INSTANCE.rtlsdr_open(devicePtr, deviceIndex);
            System.out.println(String.format("Open result: %d", result));
            if (result >= 0) {
                result = IRTLAPI.INSTANCE.rtlsdr_reset_buffer(devicePtr[0]);
                System.out.println(String.format("Reset buffer result: %d", result));
                result = IRTLAPI.INSTANCE.rtlsdr_set_testmode(devicePtr[0], false);
                System.out.println(String.format("Enable test mode result: %d", result));
                int[] gains = new int[100];
                int nGains = IRTLAPI.INSTANCE.rtlsdr_get_tuner_gains(devicePtr[0], gains);
                gains = Arrays.stream(gains).filter(g->g!=0).toArray();
                System.out.println(String.format("Number of gains: %d, %s", nGains, Arrays.toString(gains)));
                result = IRTLAPI.INSTANCE.rtlsdr_set_tuner_gain(devicePtr[0], gains[gains.length-1]);
                System.out.println(String.format("Set gain result: %d", result));
                int tunerGain = IRTLAPI.INSTANCE.rtlsdr_get_tuner_gain(devicePtr[0]);
                System.out.println(String.format("Tuner gain: %d", tunerGain));
//                result = IRTLAPI.INSTANCE.rtlsdr_set_sample_rate(devicePtr[0], 2048000);
                result = IRTLAPI.INSTANCE.rtlsdr_set_sample_rate(devicePtr[0], 256000);
                System.out.println(String.format("Set sample rate result: %d", result));
                int sampleRate = IRTLAPI.INSTANCE.rtlsdr_get_sample_rate(devicePtr[0]);
                System.out.println(String.format("Sample Rate: %d", sampleRate));
                result = IRTLAPI.INSTANCE.rtlsdr_set_center_freq(devicePtr[0], 106300000);
                System.out.println(String.format("Set frequency result: %d", result));
                long centerFreq = IRTLAPI.INSTANCE.rtlsdr_get_center_freq64(devicePtr[0]);
                System.out.println(String.format("Center Frequency: %d", centerFreq));
                result = IRTLAPI.INSTANCE.rtlsdr_set_direct_sampling(devicePtr[0], false);
                System.out.println(String.format("Set Direct Sample result: %d", result));
                int dsMode = IRTLAPI.INSTANCE.rtlsdr_get_direct_sampling(devicePtr[0]);
                System.out.println(String.format("Direct sample mode: %d", dsMode));
                result = IRTLAPI.INSTANCE.rtlsdr_set_ds_mode(devicePtr[0], RTLDataSamplingMode.RTLSDR_DS_IQ.getValue(), 28000000);
                System.out.println(String.format("Set DS Mode: %d", result));
                int BUFFER_SIZE = 262144;
                byte[] buffer = new byte[BUFFER_SIZE];
                result = 0;
                for (int i = 0; i < 5; i++) {
                    int[] readResult = new int[1];
                    result = IRTLAPI.INSTANCE.rtlsdr_read_sync(devicePtr[0], buffer, BUFFER_SIZE, readResult);
                    System.out.println(String.format("Read sync result: %d", result));
                    if (result < 0) break;
                }

                IRTLAPI.IReadAsyncCallback cb = new IRTLAPI.IReadAsyncCallback() {
                    @Override
                    public void invoke(Pointer buffer, int length, Pointer contextPointer) {
                        System.out.println(String.format("Callback invoked. Length %d", length));
                        byte[] buff2 = new byte[length];
                        buffer.read(0, buff2, 0, length);
                        System.out.println(String.format("Buffer %d, %d, %d", buff2[0], buff2[1], buff2[2]));
                    }
                };
                Pointer ctx[] = new Pointer[1];
                Runnable startAsyncRunnable = new Runnable() {
                    @Override
                    public void run() {
                        IRTLAPI.INSTANCE.rtlsdr_read_async(devicePtr[0], cb, ctx[0], 0, 0);
                    }
                };
                Thread startAsynThread = new Thread(startAsyncRunnable);
                startAsynThread.start();
                Thread.sleep(5000);
                result = IRTLAPI.INSTANCE.rtlsdr_cancel_async(devicePtr[0]);
                System.out.println(String.format("Cancel async result: %d", result));
                result = IRTLAPI.INSTANCE.rtlsdr_close(devicePtr[0]);
                System.out.println(String.format("Close result: %d", result));
            }
            break;
        }
    }

}
