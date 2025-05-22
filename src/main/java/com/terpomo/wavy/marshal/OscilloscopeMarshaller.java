package com.terpomo.wavy.marshal;

import com.terpomo.wavy.pipes.PipeTypeEnum;
import com.terpomo.wavy.pipes.monitors.OscilloscopePipe;
import org.json.JSONObject;

public class OscilloscopeMarshaller extends AbstractPipeMarshaller<OscilloscopePipe>{
    @Override
    public OscilloscopePipe unmarshal(JSONObject json) {
        return null;
    }

    @Override
    public JSONObject marshal(OscilloscopePipe obj) {
        JSONObject json = super.marshal(obj);
        json.put("pipe_type", PipeTypeEnum.OSCILLOSCOPE_PIPE_ENUM.toString());
        json.put("sample_rate", obj.getSampleRate());
        json.put("num_channels", obj.getNumberOfChannels());
        json.put("quality", obj.getQuality());
        json.put("scale", obj.getScale());
        return json;
    }
}
