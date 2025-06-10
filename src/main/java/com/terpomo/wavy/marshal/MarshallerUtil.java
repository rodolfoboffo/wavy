package com.terpomo.wavy.marshal;

import org.json.JSONObject;

public class MarshallerUtil {

    public static IMarshallable unmarshal(JSONObject json) {
        if (json == null)
            return null;
        String clssString = json.optString(MarshallingKeys.KEY_CLASS);
        try {
            Class<?> modelClass = Class.forName(clssString);
            @SuppressWarnings("unchecked") IMarshaller<IMarshallable> unsmarshaler = (IMarshaller<IMarshallable>) MarshallerFactory.createMarshallerFor(modelClass);
            IMarshallable obj = unsmarshaler.unmarshal(json);
            return obj;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(String.format("Could not find unmarshaller for %s", clssString), e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends IMarshallable> JSONObject marshall(T obj) {
        if (obj == null)
            return null;
        Class<T> clazz = (Class<T>) obj.getClass();
        IMarshaller<T> marshaller = (IMarshaller<T>) MarshallerFactory.createMarshallerFor(clazz);
        return marshaller.marshal(obj);
    }

}
