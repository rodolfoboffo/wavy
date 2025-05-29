package com.terpomo.wavy.marshal;

import org.json.JSONObject;

import static com.terpomo.wavy.marshal.AbstractMarshaller.KEY_CLASS;

public class MarshallerUtil {

    public static IMarshallable unmarshal(JSONObject json) {
        if (json == null)
            return null;
        String clssString = json.optString(KEY_CLASS);
        try {
            Class<?> modelClass = Class.forName(clssString);
            IMarshaller<IMarshallable> unsmarshaler = MarshallerFactory.getInstance().createMarshallerFor(modelClass);
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
        IMarshaller<T> marshaller = MarshallerFactory.getInstance().createMarshallerFor(clazz);
        return marshaller.marshal(obj);
    }

}
