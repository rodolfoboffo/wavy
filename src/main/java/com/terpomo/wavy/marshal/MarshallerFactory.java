package com.terpomo.wavy.marshal;

import com.terpomo.wavy.flow.IPipe;
import com.terpomo.wavy.flow.IPort;
import com.terpomo.wavy.flow.Project;

import java.lang.reflect.InvocationTargetException;

public class MarshallerFactory {

    private static IMarshaller<?> createMarshallerInstance(Class<?> marshallerClzz) {
        try {
            return (IMarshaller<?>)marshallerClzz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException("Could not instantiate marshaller class.", e);
        }
    }

    public  static IMarshaller<?> createMarshallerFor(Class<?> clazz) {
        Class<?> marshallerClass = MarshallerFactory.getMarshallerClassFor(clazz);
        if (marshallerClass == null)
            throw new RuntimeException(String.format("Unexpected class to be marshalled %s.", clazz.toGenericString()));
        return MarshallerFactory.createMarshallerInstance(marshallerClass);
    }

    public  static Class<?> getMarshallerClassFor(Class<?> clazz) {
        if (IPipe.class.isAssignableFrom(clazz)) {
            return PipeMarshaller.class;
        }
        else if (IPort.class.isAssignableFrom(clazz)) {
            return PortMarshaller.class;
        }
        else if (clazz.equals(Project.class)) {
            return ProjectMarshaller.class;
        }
        return GenericMarshaller.class;
    }
}
