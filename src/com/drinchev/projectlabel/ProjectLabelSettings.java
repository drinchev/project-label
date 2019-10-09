package com.drinchev.projectlabel;

import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
        name = "ProjectLabel",
        storages = {
                @Storage("project-label.xml"),
        }
)
public class ProjectLabelSettings implements PersistentStateComponent<ProjectLabelSettings> {

    public String label = "";
    public String backgroundColor = "#B12F2F";
    public String foregroundColor = "#FFFFFF";

    public static ProjectLabelSettings getInstance(Project project) {
        return ServiceManager.getService(project, ProjectLabelSettings.class);
    }

    @Nullable
    @Override
    public ProjectLabelSettings getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull ProjectLabelSettings state) {
        XmlSerializerUtil.copyBean(state, this);
    }

}
