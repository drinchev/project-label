package com.drinchev.projectlabel;

import com.drinchev.projectlabel.preferences.ApplicationPreferences;
import com.drinchev.projectlabel.preferences.ProjectPreferences;
import com.drinchev.projectlabel.resources.ui.ProjectLabelStatusBarWidget;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.openapi.wm.StatusBarWidgetFactory;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class ProjectLabelWidgetFactory implements StatusBarWidgetFactory {
    @Override
    public @NotNull
    @NonNls String getId() {
        return ProjectLabelStatusBarWidget.WIDGET_ID;
    }

    @Override
    public @NotNull
    @NlsContexts.ConfigurableName String getDisplayName() {
        return "Project Label";
    }

    @Override
    public boolean isAvailable(@NotNull Project project) {
        return true;
    }

    @Override
    public @NotNull StatusBarWidget createWidget(@NotNull Project project) {
        ProjectPreferences projectPreferences = ProjectPreferences.getInstance(project);
        ApplicationPreferences applicationPreferences = ApplicationPreferences.getInstance();
        return new ProjectLabelStatusBarWidget(project, projectPreferences, applicationPreferences);
    }

    @Override
    public void disposeWidget(@NotNull StatusBarWidget statusBarWidget) {
        Disposer.dispose(statusBarWidget);
    }

    @Override
    public boolean canBeEnabledOn(@NotNull StatusBar statusBar) {
        return false;
    }
}
