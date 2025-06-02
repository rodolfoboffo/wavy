package com.terpomo.wavy.marshal;

import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;

public class GenericMarshaller<T extends IMarshallable> implements IMarshaller<T>{

    @Override
    public T unmarshal(JSONObject json) {
        String className = json.getString(MarshallingKeys.KEY_CLASS);
        try {
            Class<?> clss = Thread.currentThread().getContextClassLoader().loadClass(className);
            @SuppressWarnings("unchecked") T obj = (T) clss.getConstructor().newInstance();
            for (Method method : clss.getMethods()) {
                MarshalAttr marshalAttrAnnotation = method.getAnnotation(MarshalAttr.class);
                if (marshalAttrAnnotation != null && method.getName().startsWith("set")) {
                    String jsonAttrName = marshalAttrAnnotation.attrName();
                    try {
                        Object value = json.opt(jsonAttrName);
                        if (value != null) {
                            if (BigDecimal.class.isAssignableFrom(value.getClass()))
                                value = ((BigDecimal) value).floatValue();
                            if (JSONObject.class.equals(value.getClass()))
                                value = MarshallerUtil.unmarshal((JSONObject) value);
                            method.invoke(obj, value);
                        }
                    } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
                        throw new RuntimeException(String.format("Could not unmarshall attribute %s for class %s", jsonAttrName, className), e);
                    }
                }
            }
            return obj;
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException |
                 ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public JSONObject marshal(T obj) {
        if (obj == null)
            return null;
        String fqn = obj.getClass().getName();
        JSONObject json = new JSONObject();
        json.put(MarshallingKeys.KEY_CLASS, fqn);
        for (Method method : obj.getClass().getMethods()) {
            MarshalAttr marshalAttrAnnotation = method.getAnnotation(MarshalAttr.class);
            if (marshalAttrAnnotation != null && method.getName().startsWith("get")) {
                try {
                    Object value = method.invoke(obj);
                    if (value != null) {
                        if (IMarshallable.class.isAssignableFrom(value.getClass()))
                            value = MarshallerUtil.marshall((IMarshallable) value);
                        json.put(marshalAttrAnnotation.attrName(), value);
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(String.format("Could not call method %s during marshalling procedure on object of Type %s", method.getName(), obj.getClass().getName()), e);
                }
            }
        }
        return json;
    }
}
