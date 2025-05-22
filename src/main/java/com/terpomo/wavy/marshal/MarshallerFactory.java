package com.terpomo.wavy.marshal;

import com.terpomo.wavy.flow.InputPort;
import com.terpomo.wavy.flow.OutputPort;
import com.terpomo.wavy.flow.Project;
import com.terpomo.wavy.pipes.monitors.OscilloscopePipe;
import com.terpomo.wavy.pipes.sources.ConstantWavePipe;
import com.terpomo.wavy.util.Dimension;
import com.terpomo.wavy.util.Point;
import org.json.JSONObject;

public class MarshallerFactory {

    @SuppressWarnings("unchecked")
    public static <T extends IMarshallable> IMarshaller<T> createMarshallerFor(T obj) {
        Class<? extends IMarshallable> clazz = obj.getClass();
        if (clazz.equals(Point.class))
            return (IMarshaller<T>)new PointMarshaller();
        if (clazz.equals(Dimension.class))
            return (IMarshaller<T>)new DimensionMarshaller();
        if (clazz.equals(OutputPort.class) || clazz.equals(InputPort.class))
            return (IMarshaller<T>)new PortMarshaller();
        if (clazz.equals(Project.class))
            return (IMarshaller<T>)new ProjectMarshaller();
        if (clazz.equals(ConstantWavePipe.class))
            return (IMarshaller<T>)new ConstantWaveMarshaller();
        if (clazz.equals(OscilloscopePipe.class))
            return (IMarshaller<T>)new OscilloscopeMarshaller();
        throw new RuntimeException(String.format("Unexpected Marshallable class %s.", clazz.toGenericString()));
    }

    public static <T extends IMarshallable> JSONObject marshall(T obj) {
        if (obj == null)
            return null;
        IMarshaller<T> marshaller = createMarshallerFor(obj);
        return marshaller.marshal(obj);
    }

}
