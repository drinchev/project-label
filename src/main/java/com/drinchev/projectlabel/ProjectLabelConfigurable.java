package com.drinchev.projectlabel;

import javax.swing.*;

import com.drinchev.projectlabel.preferences.ApplicationPreferences;
import com.drinchev.projectlabel.preferences.ProjectPreferences;
import com.drinchev.projectlabel.resources.ui.PluginConfiguration;

import com.drinchev.projectlabel.utils.UtilsColor;
import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.project.Project;

public class ProjectLabelConfigurable implements Configurable {

    private PluginConfiguration preferencesPanel;

    private final Project project;
    private final ProjectPreferences projectPreferences;
    private final ApplicationPreferences applicationPreferences;

    ProjectLabelConfigurable(@NotNull Project project) {
        this.project = project;
        this.projectPreferences = ProjectPreferences.getInstance(project);
        this.applicationPreferences = ApplicationPreferences.getInstance();
    }

    public String getDisplayName() {
        return "Project Label";
    }

    @Nullable
    @NonNls
    public String getHelpTopic() {
        return null;
    }

    public JComponent createComponent() {
        if (null == preferencesPanel) {
            preferencesPanel = new PluginConfiguration();
            preferencesPanel.setGlobalFontSize(applicationPreferences.getFontSize());
            preferencesPanel.setGlobalFontName(applicationPreferences.getFontName());
            preferencesPanel.setTextColor(projectPreferences.getTextColor());
            preferencesPanel.setBackgroundColor(projectPreferences.getBackgroundColor());
            preferencesPanel.setFontSize(projectPreferences.getFontSize());
            preferencesPanel.setFontName(projectPreferences.getFontName());
            preferencesPanel.setLabel(projectPreferences.getLabel());
        }
        return preferencesPanel.getRootPanel();
    }

    public boolean isModified() {
        return !UtilsColor.isEqual(projectPreferences.getBackgroundColor(), preferencesPanel.getBackgroundColor()) ||
                !UtilsColor.isEqual(projectPreferences.getTextColor(), preferencesPanel.getTextColor()) ||
                projectPreferences.getFontSize() != preferencesPanel.getFontSize() ||
                !projectPreferences.getLabel().equals(preferencesPanel.getLabel()) ||
                !projectPreferences.getFontName().equals(preferencesPanel.getFontName()) ||
                applicationPreferences.getFontSize() != preferencesPanel.getGlobalFontSize() ||
                !applicationPreferences.getFontName().equals(preferencesPanel.getGlobalFontName());
    }

    public void apply() {
        if (null != preferencesPanel) {
            projectPreferences.setTextColor(preferencesPanel.getTextColor());
            projectPreferences.setBackgroundColor(preferencesPanel.getBackgroundColor());
            projectPreferences.setFontSize(preferencesPanel.getFontSize());
            projectPreferences.setFontName(preferencesPanel.getFontName());
            projectPreferences.setLabel(preferencesPanel.getLabel());
            applicationPreferences.setFontSize(preferencesPanel.getGlobalFontSize());
            applicationPreferences.setFontName(preferencesPanel.getGlobalFontName());

            if (project != null) {
                project.getService(ProjectLabel.class).onSettingsChanged();
            }
        }
    }

    public void disposeUIResources() {
        preferencesPanel = null;
    }
}
