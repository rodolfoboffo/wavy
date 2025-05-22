package com.terpomo.wavy.marshal;

import org.json.JSONObject;

public interface IMarshaller<T extends IMarshallable>{
    public T unmarshal(JSONObject json);
    public JSONObject marshal(T obj);
}
