package com.drinchev.projectlabel;

import com.drinchev.projectlabel.preferences.ApplicationPreferences;
import com.drinchev.projectlabel.preferences.ProjectPreferences;
import com.intellij.openapi.project.Project;
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
        return com.drinchev.projectlabel.resources.ui.StatusBarWidget.WIDGET_ID;
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
        System.out.println("createWidget");
        ProjectPreferences projectPreferences = ProjectPreferences.getInstance(project);
        ApplicationPreferences applicationPreferences = ApplicationPreferences.getInstance();
        return new com.drinchev.projectlabel.resources.ui.StatusBarWidget(project, projectPreferences, applicationPreferences);
    }

    @Override
    public void disposeWidget(@NotNull StatusBarWidget statusBarWidget) {
        System.out.println("disposeWidget");
    }

    @Override
    public boolean canBeEnabledOn(@NotNull StatusBar statusBar) {
        return false;
    }
}
