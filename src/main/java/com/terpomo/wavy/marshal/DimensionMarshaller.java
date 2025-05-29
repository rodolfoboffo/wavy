package com.terpomo.wavy.marshal;

import com.terpomo.wavy.util.Dimension;
import org.json.JSONObject;

public class DimensionMarshaller extends AbstractMarshaller<Dimension> {
    @Override
    public Dimension unmarshal(JSONObject json) {
        return null;
    }

    @Override
    public JSONObject marshal(Dimension obj) {
        if (obj == null)
            return null;
        JSONObject json = super.marshal(obj);
        json.put("w", obj.getWidth());
        json.put("h", obj.getHeight());
        return json;
    }
}
