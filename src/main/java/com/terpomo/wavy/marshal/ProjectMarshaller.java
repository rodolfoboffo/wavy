package com.terpomo.wavy.marshal;

import com.terpomo.wavy.flow.IPipe;
import com.terpomo.wavy.flow.Project;
import org.json.JSONArray;
import org.json.JSONObject;

public class ProjectMarshaller extends AbstractMarshaller<Project> {

    private static final String KEY_NAME = "name";
    private static final String KEY_PIPES = "pipes";

    @Override
    public Project unmarshal(JSONObject json) {
        String projectName = json.getString(KEY_NAME);
        Project p = new Project(projectName);
        JSONArray jsonPipes = json.getJSONArray(KEY_PIPES);
        for (Object o : jsonPipes) {
            JSONObject jsonPipe = (JSONObject) o;
            IPipe pipe = (IPipe) MarshallerUtil.unmarshal(jsonPipe);
            p.getPipes().add(pipe);
        }
        return p;
    }

    @Override
    public JSONObject marshal(Project obj) {
        if (obj == null)
            return null;
        JSONObject json = super.marshal(obj);
        for (IPipe pipe : obj.getPipes()) {
            JSONObject pipeJson = MarshallerUtil.marshall(pipe);
            json.put(KEY_NAME, obj.getName());
            json.append(KEY_PIPES, pipeJson);
        }
        return json;
    }
}
