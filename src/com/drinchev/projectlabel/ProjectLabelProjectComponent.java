package com.drinchev.projectlabel;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;

public class ProjectLabelProjectComponent implements ProjectComponent {

    private Project project;
    private ProjectLabelSettings settings;
    private StatusBar statusBar;

    public ProjectLabelProjectComponent(Project project) {
        this.project = project;
        settings = ProjectLabelSettings.getInstance(project);
    }

    void onSettingsChanged() {
        StatusBar statusBar = WindowManager.getInstance().getStatusBar(project);
        ProjectLabelWidget labelWidget = (ProjectLabelWidget)statusBar.getWidget(ProjectLabelWidget.WIDGET_ID);

        if (labelWidget != null) {
            labelWidget.rebuildWidget();
            labelWidget.updateUI();
        }
    }

    @Override
    public void projectOpened() {
        statusBar = WindowManager.getInstance().getStatusBar(project);

        if (statusBar != null) {
            statusBar.addWidget(new ProjectLabelWidget(project, settings));
        }
    }

    @Override
    public void projectClosed() {
        if (statusBar != null ) {
            statusBar.removeWidget(ProjectLabelWidget.WIDGET_ID);
        }
    }

}
