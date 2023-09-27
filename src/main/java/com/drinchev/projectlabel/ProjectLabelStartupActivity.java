package com.drinchev.projectlabel;

import com.drinchev.projectlabel.resources.ui.ProjectLabelBackgroundImage;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import org.jetbrains.annotations.NotNull;

public class ProjectLabelStartupActivity implements StartupActivity {
    @Override
    public void runActivity(@NotNull Project project) {
        try {
            project.getService(ProjectLabelBackgroundImage.class).showImage();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
