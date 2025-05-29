package com.terpomo.wavy.marshal;

import com.terpomo.wavy.pipes.sources.ConstantWavePipe;
import com.terpomo.wavy.signals.ConstantWave;
import org.json.JSONObject;

public class ConstantWaveMarshaller extends AbstractPipeMarshaller<ConstantWavePipe> {

    private static final String KEY_SAMPLE_RATE = "sample_rate";
    private static final String KEY_AMPLITUDE = "amplitude";
    private static final String KEY_FREQUENCY = "frequency";

    @Override
    public ConstantWavePipe unmarshal(JSONObject json) {
        ConstantWavePipe pipe = super.unmarshal(json);
        float amplitude = json.optFloat(KEY_AMPLITUDE, ConstantWave.DEFAULT_CW_AMPLITUDE);
        pipe.setAmplitude(amplitude);
        float frequency = json.optFloat(KEY_FREQUENCY, ConstantWave.DEFAULT_CW_FREQUENCY);
        pipe.setFrequency(frequency);
        int sampleRate = json.optInt(KEY_SAMPLE_RATE, ConstantWave.DEFAULT_CW_SAMPLE_RATE);
        pipe.setSampleRate(sampleRate);
        return pipe;
    }

    @Override
    public JSONObject marshal(ConstantWavePipe obj) {
        if (obj == null)
            return null;
        JSONObject json = super.marshal(obj);
        json.put("sample_rate", obj.getSignal().getSampleRate());
        json.put("amplitude", obj.getSignal().getAmplitude());
        json.put("frequency", obj.getSignal().getFrequency());
        return json;
    }

    @Override
    public Class<ConstantWavePipe> getPipeClass() {
        return ConstantWavePipe.class;
    }
}
