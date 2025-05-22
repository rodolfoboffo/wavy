package com.terpomo.wavy.marshal;

import com.terpomo.wavy.util.Point;
import org.json.JSONArray;
import org.json.JSONObject;

public class PointMarshaller implements IMarshaller<Point>{

    @Override
    public Point unmarshal(JSONObject json) {
        return null;
    }

    @Override
    public JSONObject marshal(Point obj) {
        if (obj == null)
            return null;
        JSONObject json = new JSONObject();
        json.put("x", obj.getX());
        json.put("y", obj.getY());
        return json;
    }
}
