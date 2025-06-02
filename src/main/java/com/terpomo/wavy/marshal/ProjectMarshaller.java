package com.terpomo.wavy.marshal;

import com.terpomo.wavy.flow.IPipe;
import com.terpomo.wavy.flow.InputPort;
import com.terpomo.wavy.flow.OutputPort;
import com.terpomo.wavy.flow.Project;
import org.json.JSONArray;
import org.json.JSONObject;

public class ProjectMarshaller extends GenericMarshaller<Project> {

    @Override
    public Project unmarshal(JSONObject json) {
        Project project = super.unmarshal(json);
        JSONArray jsonPipes = json.getJSONArray(MarshallingKeys.KEY_PIPES);
        for (Object _jsonPipe : jsonPipes) {
            JSONObject jsonPipe = (JSONObject) _jsonPipe;
            IPipe pipe = (IPipe) MarshallerUtil.unmarshal(jsonPipe);
            project.addPipe(pipe);
        }
        for (Object o : jsonPipes) {
            JSONObject jsonPipe = (JSONObject) o;
            String pipeName = jsonPipe.getString(MarshallingKeys.KEY_NAME);
            IPipe pipe = project.getPipeByName(pipeName);
            JSONArray jsonInputPorts = jsonPipe.optJSONArray(MarshallingKeys.KEY_INPUT_PORTS, new JSONArray());
            for (int i = 0; i < jsonInputPorts.length(); i++) {
                JSONObject jsonInputPort = (JSONObject) jsonInputPorts.get(i);
                InputPort inputPort = pipe.getInputPorts().get(i);
                JSONObject jsonLinkedPort = jsonInputPort.optJSONObject(MarshallingKeys.KEY_LINKED_PORT);
                if (jsonLinkedPort != null) {
                    String linkedPipeName = jsonLinkedPort.getString(MarshallingKeys.KEY_PIPE_NAME);
                    int linkedPortIndex = jsonLinkedPort.getInt(MarshallingKeys.KEY_PORT_INDEX);
                    IPipe linkedPipe = project.getPipeByName(linkedPipeName);
                    OutputPort linkedPort = linkedPipe.getOutputPorts().get(linkedPortIndex);
                    inputPort.setLinkedPort(linkedPort);
                }
            }
        }
        return project;
    }

    @Override
    public JSONObject marshal(Project obj) {
        if (obj == null)
            return null;
        JSONObject json = super.marshal(obj);
        for (IPipe pipe : obj.getPipes()) {
            JSONObject pipeJson = MarshallerUtil.marshall(pipe);
            json.append(MarshallingKeys.KEY_PIPES, pipeJson);
        }
        return json;
    }
}
