package com.drinchev.projectlabel;

import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;

@State(
        name = "ProjectLabel",
        storages = {
                @Storage(id = "default", file = StoragePathMacros.PROJECT_FILE),
                @Storage(id = "dir", file = StoragePathMacros.PROJECT_CONFIG_DIR + "/ProjectLabelPlugin.xml", scheme = StorageScheme.DIRECTORY_BASED)
        }
)
public class ProjectLabelSettings implements PersistentStateComponent<ProjectLabelSettings> {

    public String backgroundColor = "#b12f2f";

    protected Project project;

    public static ProjectLabelSettings getInstance(Project project) {
        ProjectLabelSettings settings = ServiceManager.getService(project, ProjectLabelSettings.class);
        settings.project = project;
        return settings;
    }

    @Nullable
    @Override
    public ProjectLabelSettings getState() {
        return this;
    }

    @Override
    public void loadState(ProjectLabelSettings state) {
        XmlSerializerUtil.copyBean(state, this);
    }

}
