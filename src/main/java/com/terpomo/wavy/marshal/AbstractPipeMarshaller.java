package com.terpomo.wavy.marshal;

import com.terpomo.wavy.flow.IPipe;
import com.terpomo.wavy.flow.IPort;
import com.terpomo.wavy.pipes.PipeTypeEnum;
import org.json.JSONObject;

public abstract class AbstractPipeMarshaller<T extends IPipe> implements IMarshaller<T> {
    @Override
    public T unmarshal(JSONObject json) {
        return null;
    }

    private JSONObject marshalPort(IPort port, int i) {
        JSONObject portJson = MarshallerFactory.marshall(port);
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
        JSONObject json = new JSONObject();
        json.put("name", obj.getName());
        json.put("location", MarshallerFactory.marshall(obj.getLocation()));
        json.put("dimension", MarshallerFactory.marshall(obj.getDimension()));
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
