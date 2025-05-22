package com.terpomo.wavy.marshal;

import com.terpomo.wavy.flow.IPipe;
import com.terpomo.wavy.flow.Project;
import org.json.JSONObject;

public class ProjectMarshaller implements IMarshaller<Project> {

    @Override
    public Project unmarshal(JSONObject json) {
        return null;
    }

    @Override
    public JSONObject marshal(Project project) {
        JSONObject json = new JSONObject();
        for (IPipe pipe : project.getPipes()) {
            IMarshaller<IPipe> pipeMarshaller = MarshallerFactory.createMarshallerFor(pipe);
            JSONObject pipeJson = pipeMarshaller.marshal(pipe);
            json.append("pipes", pipeJson);
        }
        return json;
    }
}
