package com.drinchev.projectlabel;

import javax.swing.*;

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
    private final ProjectLabelPreferences preferences;

    ProjectLabelConfigurable(@NotNull Project project) {
        this.project = project;
        this.preferences = ProjectLabelPreferences.getInstance(project);
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
            preferencesPanel.setTextColor(preferences.getTextColor());
            preferencesPanel.setBackgroundColor(preferences.getBackgroundColor());
            preferencesPanel.setFontSize(preferences.getFontSize());
            preferencesPanel.setFontName(preferences.getFontName());
            preferencesPanel.setLabel(preferences.getLabel());
        }
        return preferencesPanel.getRootPanel();
    }

    public boolean isModified() {
        return
                !UtilsColor.isEqual(preferences.getBackgroundColor(), preferencesPanel.getBackgroundColor())
                        || !UtilsColor.isEqual(preferences.getTextColor(), preferencesPanel.getTextColor())
                        || preferences.getFontSize() != preferencesPanel.getFontSize()
                        || !preferences.getLabel().equals(preferencesPanel.getLabel())
                        || !preferences.getFontName().equals(preferencesPanel.getFontName());
    }

    public void apply() {
        if (null != preferencesPanel) {
            preferences.setTextColor(preferencesPanel.getTextColor());
            preferences.setBackgroundColor(preferencesPanel.getBackgroundColor());
            preferences.setFontSize(preferencesPanel.getFontSize());
            preferences.setFontName(preferencesPanel.getFontName());
            preferences.setLabel(preferencesPanel.getLabel());
            if (project != null) {
                ProjectLabelProjectComponent component = project.getComponent(ProjectLabelProjectComponent.class);
                if (component != null) {
                    component.onSettingsChanged();
                }
            }
        }
    }

    public void disposeUIResources() {
        preferencesPanel = null;
    }
}
