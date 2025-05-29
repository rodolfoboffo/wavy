package com.terpomo.wavy.marshal;

import com.terpomo.wavy.flow.IPipe;
import com.terpomo.wavy.flow.IPort;
import com.terpomo.wavy.pipes.PipeFactory;
import com.terpomo.wavy.pipes.PipeTypeEnum;
import com.terpomo.wavy.util.Dimension;
import com.terpomo.wavy.util.Point;
import org.json.JSONObject;

public abstract class AbstractPipeMarshaller<T extends IPipe> extends AbstractMarshaller<T> implements IPipeMarshaller<T> {

    private static final String KEY_PIPE_NAME = "name";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_DIMENSION = "dimension";

    @SuppressWarnings("unchecked")
    @Override
    public T unmarshal(JSONObject json) {
        String pipeName = json.getString(KEY_PIPE_NAME);
        T pipe = (T) PipeFactory.createPipe(PipeTypeEnum.valueOf(this.getPipeClass()), pipeName);
        Point location = (Point) MarshallerUtil.unmarshal(json.optJSONObject(KEY_LOCATION));
        pipe.setLocation(location);
        Dimension dimension = (Dimension) MarshallerUtil.unmarshal(json.optJSONObject(KEY_DIMENSION));
        pipe.setDimension(dimension);
        return pipe;
    }

    private JSONObject marshalPort(IPort port, int i) {
        JSONObject portJson = MarshallerUtil.marshall(port);
        portJson.put("index", i);
        IPort linkedPort = port.getLinkedPort();
        if (linkedPort != null) {
            JSONObject linkedPortJson = new JSONObject();
            IPipe linkedPipe = linkedPort.getPipe();
            int linkedPortIndex = linkedPipe.getInputPorts().contains(linkedPort) ? linkedPipe.getInputPorts().indexOf(linkedPort) : linkedPipe.getOutputPorts().indexOf(linkedPort);
            linkedPortJson.put("pipe_name", linkedPipe.getName());
            linkedPortJson.put("port_index", linkedPortIndex);
            portJson.put("linked_port", linkedPortJson);
        }
        return portJson;
    }

    @Override
    public JSONObject marshal(T obj) {
        JSONObject json = super.marshal(obj);
        json.put(KEY_PIPE_NAME, obj.getName());
        json.put(KEY_LOCATION, MarshallerUtil.marshall(obj.getLocation()));
        json.put(KEY_DIMENSION, MarshallerUtil.marshall(obj.getDimension()));
        for (int i = 0; i < obj.getInputPorts().size(); i++) {
            IPort port = obj.getInputPorts().get(i);
            JSONObject portJson = this.marshalPort(port, i);
            json.append("input_ports", portJson);
        }
        for (int i = 0; i < obj.getOutputPorts().size(); i++) {
            IPort port = obj.getOutputPorts().get(i);
            JSONObject portJson = this.marshalPort(port, i);
            json.append("output_ports", portJson);
        }
        return json;
    }
}
