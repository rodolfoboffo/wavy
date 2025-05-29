package com.terpomo.wavy.marshal;

import com.terpomo.wavy.flow.IPort;
import org.json.JSONObject;

public class PortMarshaller extends AbstractMarshaller<IPort> {
    @Override
    public IPort unmarshal(JSONObject json) {
        return null;
    }

    @Override
    public JSONObject marshal(IPort obj) {
        if (obj == null)
            return null;
        JSONObject json = super.marshal(obj);
        return json;
    }
}
