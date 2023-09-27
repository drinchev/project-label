package com.drinchev.projectlabel;

import static java.util.Objects.requireNonNull;

import com.drinchev.projectlabel.resources.ui.ProjectLabelBackgroundImage;
import com.drinchev.projectlabel.resources.ui.ProjectLabelStatusBarWidget;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.Service.Level;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import org.jetbrains.annotations.NotNull;

@Service(Level.PROJECT)
public final class ProjectLabel {

    private final Project project;

    public ProjectLabel(@NotNull Project project) {
        this.project = requireNonNull(project);
    }

    public void onSettingsChanged() {
        updateStatusBar();
        updateBackgroundImage();
    }

    private void updateStatusBar() {
        StatusBar statusBar = WindowManager.getInstance().getStatusBar(project);
        ProjectLabelStatusBarWidget statusBarWidget =
                (ProjectLabelStatusBarWidget) statusBar.getWidget(ProjectLabelStatusBarWidget.WIDGET_ID);
        if (statusBarWidget != null) {
            statusBarWidget.rebuildWidget();
            statusBarWidget.updateUI();
        }
    }

    private void updateBackgroundImage() {
        project.getService(ProjectLabelBackgroundImage.class).updateImage();
    }
}
