package com.terpomo.wavy.marshal;

import com.terpomo.wavy.flow.IPipe;
import com.terpomo.wavy.flow.IPort;
import org.json.JSONObject;

public class PipeMarshaller<T extends IPipe> extends GenericMarshaller<T> implements IPipeMarshaller<T> {

    private JSONObject marshalPort(IPort port, int i) {
        JSONObject portJson = MarshallerUtil.marshall(port);
        IPort linkedPort = port.getLinkedPort();
        if (linkedPort != null) {
            JSONObject linkedPortJson = new JSONObject();
            IPipe linkedPipe = linkedPort.getPipe();
            int linkedPortIndex = linkedPipe.getInputPorts().contains(linkedPort) ? linkedPipe.getInputPorts().indexOf(linkedPort) : linkedPipe.getOutputPorts().indexOf(linkedPort);
            linkedPortJson.put(MarshallingKeys.KEY_PIPE_NAME, linkedPipe.getName());
            linkedPortJson.put(MarshallingKeys.KEY_PORT_INDEX, linkedPortIndex);
            portJson.put(MarshallingKeys.KEY_LINKED_PORT, linkedPortJson);
        }
        return portJson;
    }

    @Override
    public JSONObject marshal(T obj) {
        JSONObject json = super.marshal(obj);
        for (int i = 0; i < obj.getInputPorts().size(); i++) {
            IPort port = obj.getInputPorts().get(i);
            JSONObject portJson = this.marshalPort(port, i);
            json.append(MarshallingKeys.KEY_INPUT_PORTS, portJson);
        }
        for (int i = 0; i < obj.getOutputPorts().size(); i++) {
            IPort port = obj.getOutputPorts().get(i);
            JSONObject portJson = this.marshalPort(port, i);
            json.append(MarshallingKeys.KEY_OUTPUT_PORTS, portJson);
        }
        return json;
    }
}
