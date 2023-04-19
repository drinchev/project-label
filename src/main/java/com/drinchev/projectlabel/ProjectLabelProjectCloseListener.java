package com.drinchev.projectlabel;

import com.drinchev.projectlabel.resources.ui.ProjectLabelBackgroundImage;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManagerListener;
import org.jetbrains.annotations.NotNull;

public class ProjectLabelProjectCloseListener implements ProjectManagerListener {

    @Override
    public void projectClosingBeforeSave(@NotNull Project project) {
        // when the project is closing, we remove the background image (since it is going to be deleted)
        ProjectLabelBackgroundImage backgroundImage = project.getService(ProjectLabelBackgroundImage.class);
        backgroundImage.hideImage();
    }
}
