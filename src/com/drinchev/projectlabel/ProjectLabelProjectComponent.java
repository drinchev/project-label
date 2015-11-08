package com.drinchev.projectlabel;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import org.jetbrains.annotations.NotNull;

public class ProjectLabelProjectComponent implements ProjectComponent {

    private Project myProject;
    private ProjectLabelSettings mySettings = null;

    public ProjectLabelProjectComponent(Project project) {
        myProject = project;
        mySettings = ProjectLabelSettings.getInstance(project);
    }

    @Override
    public void initComponent() {
    }

    @Override
    public void disposeComponent() {
    }

    @Override
    @NotNull
    public String getComponentName() {
        return "ProjectLabelProjectComponent";
    }

    @Override
    public void projectOpened() {
        StatusBar statusBar = WindowManager.getInstance().getStatusBar(myProject);
        if (statusBar != null) {
            statusBar.addWidget(new ProjectLabelWidget(myProject, mySettings));
        }
    }

    @Override
    public void projectClosed() {
        StatusBar statusBar = WindowManager.getInstance().getStatusBar(myProject);
        if ( statusBar != null ) {
            statusBar.removeWidget("NameLabelWidget" + myProject.getName().toUpperCase());
        }
    }

}
