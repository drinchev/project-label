package com.drinchev.projectlabel;

import javax.swing.*;

import com.drinchev.projectlabel.preferences.ApplicationPreferences;
import com.drinchev.projectlabel.preferences.BackgroundImagePrefs;
import com.drinchev.projectlabel.preferences.ProjectPreferences;
import com.drinchev.projectlabel.resources.ui.BackgroundImagePosition;
import com.drinchev.projectlabel.resources.ui.PluginConfiguration;

import com.drinchev.projectlabel.utils.UtilsColor;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.project.Project;

import java.util.Objects;

public class ProjectLabelConfigurable implements Configurable {

    private final static Logger LOG = Logger.getInstance(ProjectLabelConfigurable.class);

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
            reset();
        }
        return preferencesPanel.getRootPanel();
    }

    @Override
    public void reset() {
        preferencesPanel.setGlobalFontSize(applicationPreferences.getFontSize());
        preferencesPanel.setGlobalFontName(applicationPreferences.getFontName());
        preferencesPanel.setTextColor(projectPreferences.getTextColor());
        preferencesPanel.setBackgroundColor(projectPreferences.getBackgroundColor());
        preferencesPanel.setFontSize(projectPreferences.getFontSize());
        preferencesPanel.setFontName(projectPreferences.getFontName());
        preferencesPanel.setLabel(projectPreferences.getLabel());
        preferencesPanel.setBackgroundImagePrefs(
                projectPreferences.isBackgroundImageInherited() ? null : new BackgroundImagePrefs(
                        projectPreferences.getBackgroundImageOpacity(),
                        projectPreferences.getBackgroundImagePosition()
                ),
                new BackgroundImagePrefs(
                        applicationPreferences.getBackgroundImageOpacity(),
                        applicationPreferences.getBackgroundImagePosition()
                )
        );
    }

    public boolean isModified() {
        LOG.warn("projPrefs: " + BackgroundImagePrefs.from(projectPreferences.isBackgroundImageInherited(), projectPreferences.getBackgroundImageOpacity(), projectPreferences.getBackgroundImagePosition()) + "\n" + preferencesPanel.getBackgroundImagePrefs());
        LOG.warn("appPrefs: " + BackgroundImagePrefs.from(applicationPreferences.getBackgroundImageOpacity(), applicationPreferences.getBackgroundImagePosition()) + "\n" + preferencesPanel.getGlobalBackgroundImagePrefs());
        return !UtilsColor.isEqual(projectPreferences.getBackgroundColor(), preferencesPanel.getBackgroundColor()) ||
                !UtilsColor.isEqual(projectPreferences.getTextColor(), preferencesPanel.getTextColor()) ||
                projectPreferences.getFontSize() != preferencesPanel.getFontSize() ||
                !projectPreferences.getLabel().equals(preferencesPanel.getLabel()) ||
                !projectPreferences.getFontName().equals(preferencesPanel.getFontName()) ||
                applicationPreferences.getFontSize() != preferencesPanel.getGlobalFontSize() ||
                !applicationPreferences.getFontName().equals(preferencesPanel.getGlobalFontName()) ||
                !Objects.equals(
                        BackgroundImagePrefs.from(projectPreferences.isBackgroundImageInherited(), projectPreferences.getBackgroundImageOpacity(), projectPreferences.getBackgroundImagePosition()),
                        preferencesPanel.getBackgroundImagePrefs()) ||
                !Objects.equals(
                        BackgroundImagePrefs.from(applicationPreferences.getBackgroundImageOpacity(), applicationPreferences.getBackgroundImagePosition()),
                        preferencesPanel.getGlobalBackgroundImagePrefs()
                );
    }

    public void apply() {
        if (null != preferencesPanel) {
            projectPreferences.setTextColor(preferencesPanel.getTextColor());
            projectPreferences.setBackgroundColor(preferencesPanel.getBackgroundColor());
            projectPreferences.setFontSize(preferencesPanel.getFontSize());
            projectPreferences.setFontName(preferencesPanel.getFontName());
            projectPreferences.setLabel(preferencesPanel.getLabel());
            BackgroundImagePrefs projBackgroundImagePrefs = preferencesPanel.getBackgroundImagePrefs();
            if (projBackgroundImagePrefs == null) {
                LOG.warn("PROJ Background image prefs are null");
                projectPreferences.setBackgroundImageInherited(true);
                projectPreferences.setBackgroundImagePosition(BackgroundImagePosition.HIDDEN);
                projectPreferences.setBackgroundImageOpacity(-1);
            } else {
                LOG.warn("PROJ Background image prefs are NOT null");
                projectPreferences.setBackgroundImageInherited(false);
                projectPreferences.setBackgroundImagePosition(projBackgroundImagePrefs.position());
                projectPreferences.setBackgroundImageOpacity(projBackgroundImagePrefs.opacity());
            }
            LOG.warn("App prefs: " + applicationPreferences.getBackgroundImagePosition() + " " + applicationPreferences.getBackgroundImageOpacity());
            BackgroundImagePrefs appBackgroundImagePrefs = preferencesPanel.getGlobalBackgroundImagePrefs();
            applicationPreferences.setBackgroundImagePosition(appBackgroundImagePrefs.position());
            applicationPreferences.setBackgroundImageOpacity(appBackgroundImagePrefs.opacity());

            if (project != null) {
                project.getService(ProjectLabel.class).onSettingsChanged();
            }
        }
    }

    public void disposeUIResources() {
        preferencesPanel = null;
    }
}
