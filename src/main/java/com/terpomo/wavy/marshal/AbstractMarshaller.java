package com.terpomo.wavy.marshal;

import org.json.JSONObject;

public abstract class AbstractMarshaller<T extends IMarshallable> implements IMarshaller<T>{

    public static final String KEY_CLASS = "class";

    @Override
    public JSONObject marshal(T obj) {
        if (obj == null)
            return null;
        String fqn = obj.getClass().getName();
        JSONObject json = new JSONObject();
        json.put(KEY_CLASS, fqn);
        return json;
    }
}
