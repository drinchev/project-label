package com.drinchev.projectlabel;

import com.drinchev.projectlabel.preferences.ApplicationPreferences;
import com.drinchev.projectlabel.preferences.ProjectPreferences;
import com.drinchev.projectlabel.resources.ui.StatusBarWidget;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;

public class ProjectLabelProjectComponent implements ProjectComponent {

    private Project project;
    private ProjectPreferences projectPreferences;
    private ApplicationPreferences applicationPreferences;
    private StatusBar statusBar;

    public ProjectLabelProjectComponent(Project project) {
        this.project = project;
        projectPreferences = ProjectPreferences.getInstance(project);
        applicationPreferences = ApplicationPreferences.getInstance();
    }

    void onSettingsChanged() {
        StatusBar statusBar = WindowManager.getInstance().getStatusBar(project);
        StatusBarWidget statusBarWidget = (StatusBarWidget) statusBar.getWidget(StatusBarWidget.WIDGET_ID);
        if (statusBarWidget != null) {
            statusBarWidget.rebuildWidget();
            statusBarWidget.updateUI();
        }
    }

    @Override
    public void projectOpened() {
        statusBar = WindowManager.getInstance().getStatusBar(project);

        if (statusBar != null) {
            statusBar.addWidget(new StatusBarWidget(project, projectPreferences, applicationPreferences));
        }
    }

    @Override
    public void projectClosed() {
        if (statusBar != null) {
            statusBar.removeWidget(StatusBarWidget.WIDGET_ID);
        }
    }

}
