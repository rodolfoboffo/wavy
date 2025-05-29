package com.terpomo.wavy.marshal;

import com.terpomo.wavy.flow.InputPort;
import com.terpomo.wavy.flow.OutputPort;
import com.terpomo.wavy.flow.Project;
import com.terpomo.wavy.pipes.monitors.OscilloscopePipe;
import com.terpomo.wavy.pipes.sources.ConstantWavePipe;
import com.terpomo.wavy.util.Dimension;
import com.terpomo.wavy.util.Point;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class MarshallerFactory {

    private static MarshallerFactory instance;
    private final Map<Class, Class> classToMarshallerMap;

    private MarshallerFactory() {
        this.classToMarshallerMap = new HashMap<>();
        this.classToMarshallerMap.put(Point.class, PointMarshaller.class);
        this.classToMarshallerMap.put(Dimension.class, DimensionMarshaller.class);
        this.classToMarshallerMap.put(InputPort.class, PortMarshaller.class);
        this.classToMarshallerMap.put(OutputPort.class, PortMarshaller.class);
        this.classToMarshallerMap.put(Project.class, ProjectMarshaller.class);
        this.classToMarshallerMap.put(ConstantWavePipe.class, ConstantWaveMarshaller.class);
        this.classToMarshallerMap.put(OscilloscopePipe.class, OscilloscopeMarshaller.class);
    }

    public static MarshallerFactory getInstance() {
        if (instance == null)
            instance = new MarshallerFactory();
        return instance;
    }

    @SuppressWarnings("unchecked")
    private <T extends IMarshallable> IMarshaller<T> createMarshallerInstance(Class<T> marshallerClzz) {
        try {
            return (IMarshaller<T>)marshallerClzz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException("Could not instantiate marshaller class.", e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends IMarshallable> IMarshaller<T> createMarshallerFor(Class<?> clazz) {
        if (this.classToMarshallerMap.containsKey(clazz)) {
            Class<T> marshallerClzz = this.classToMarshallerMap.get(clazz);
            return this.createMarshallerInstance(marshallerClzz);
        }
        throw new RuntimeException(String.format("Unexpected class to be marshalled %s.", clazz.toGenericString()));
    }
}
