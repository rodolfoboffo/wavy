package com.terpomo.wavy.marshal;

import com.terpomo.wavy.pipes.monitors.OscilloscopePipe;
import org.json.JSONObject;

public class OscilloscopeMarshaller extends AbstractPipeMarshaller<OscilloscopePipe>{

    private static final String KEY_SAMPLE_RATE = "sample_rate";
    private static final String KEY_NUM_CHANNELS = "num_channels";
    private static final String KEY_QUALITY = "quality";
    private static final String KEY_SCALE = "scale";

    @Override
    public OscilloscopePipe unmarshal(JSONObject json) {
        OscilloscopePipe pipe = super.unmarshal(json);
        pipe.setSampleRate(json.optInt(KEY_SAMPLE_RATE, OscilloscopePipe.DEFAULT_SAMPLE_RATE));
        pipe.setNumberOfChannels(json.optInt(KEY_NUM_CHANNELS, OscilloscopePipe.DEFAULT_NUMBER_OF_CHANNELS));
        pipe.setQuality(json.optFloat(KEY_QUALITY, OscilloscopePipe.DEFAULT_QUALITY));
        pipe.setScale(json.optFloat(KEY_SCALE, OscilloscopePipe.DEFAULT_SCALE));
        return pipe;
    }

    @Override
    public JSONObject marshal(OscilloscopePipe obj) {
        if (obj == null)
            return null;
        JSONObject json = super.marshal(obj);
        json.put(KEY_SAMPLE_RATE, obj.getSampleRate());
        json.put(KEY_NUM_CHANNELS, obj.getNumberOfChannels());
        json.put(KEY_QUALITY, obj.getQuality());
        json.put(KEY_SCALE, obj.getScale());
        return json;
    }

    @Override
    public Class<OscilloscopePipe> getPipeClass() {
        return OscilloscopePipe.class;
    }
}
