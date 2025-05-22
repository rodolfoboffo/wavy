package com.terpomo.wavy.marshal;

import com.terpomo.wavy.pipes.PipeTypeEnum;
import com.terpomo.wavy.pipes.sources.ConstantWavePipe;
import org.json.JSONObject;

public class ConstantWaveMarshaller extends AbstractPipeMarshaller<ConstantWavePipe> {
    @Override
    public ConstantWavePipe unmarshal(JSONObject json) {
        return null;
    }

    @Override
    public JSONObject marshal(ConstantWavePipe obj) {
        JSONObject json = super.marshal(obj);
        json.put("pipe_type", PipeTypeEnum.CONSTANT_WAVE_SIGNAL_PIPE_ENUM.toString());
        json.put("sample_rate", obj.getSignal().getSampleRate());
        json.put("amplitude", obj.getSignal().getAmplitude());
        json.put("frequency", obj.getSignal().getFrequency());
        return json;
    }
}
