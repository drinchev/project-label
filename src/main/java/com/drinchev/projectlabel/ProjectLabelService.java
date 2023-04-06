package com.drinchev.projectlabel;

import com.drinchev.projectlabel.resources.ui.ProjectLabelStatusBarWidget;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;

@Service
public final class ProjectLabelService {

    private final Project project;

    public ProjectLabelService(Project project) {
        this.project = project;
    }

    void onSettingsChanged() {
        StatusBar statusBar = WindowManager.getInstance().getStatusBar(project);
        ProjectLabelStatusBarWidget statusBarWidget = (ProjectLabelStatusBarWidget) statusBar.getWidget(ProjectLabelStatusBarWidget.WIDGET_ID);
        if (statusBarWidget != null) {
            statusBarWidget.rebuildWidget();
            statusBarWidget.updateUI();
        }
    }

}
