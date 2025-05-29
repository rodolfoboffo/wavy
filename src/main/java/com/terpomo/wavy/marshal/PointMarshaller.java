package com.terpomo.wavy.marshal;

import com.terpomo.wavy.util.Point;
import org.json.JSONObject;

public class PointMarshaller extends AbstractMarshaller<Point> {

    private static final String KEY_X = "x";
    private static final String KEY_Y = "y";

    @Override
    public Point unmarshal(JSONObject json) {
        if (json == null)
            return null;
        float x = json.getFloat(KEY_X);
        float y = json.getFloat(KEY_Y);
        Point p = new Point(x, y);
        return p;
    }

    @Override
    public JSONObject marshal(Point obj) {
        if (obj == null)
            return null;
        JSONObject json = super.marshal(obj);
        json.put(KEY_X, obj.getX());
        json.put(KEY_Y, obj.getY());
        return json;
    }
}
