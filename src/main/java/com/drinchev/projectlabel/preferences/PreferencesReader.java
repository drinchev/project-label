package com.drinchev.projectlabel.preferences;

import com.drinchev.projectlabel.resources.ui.BackgroundImagePosition;
import com.drinchev.projectlabel.utils.UtilsFont;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

import static java.util.Objects.requireNonNull;

public class PreferencesReader {

    private final Project project;
    private final ApplicationPreferences appPrefs;

    private final ProjectPreferences projectPrefs;

    public PreferencesReader(@NotNull Project project, @NotNull ProjectPreferences projectPrefs, @NotNull ApplicationPreferences appPrefs) {
        this.project = requireNonNull(project);
        this.appPrefs = requireNonNull(appPrefs);
        this.projectPrefs = requireNonNull(projectPrefs);
    }

    public String label() {
        return projectPrefs.getLabel().isEmpty() ? this.project.getName().toUpperCase() : projectPrefs.getLabel();
    }

    public Font font() {
        Font font = projectPrefs.getFontName().isEmpty() ? appPrefs.getFont() : projectPrefs.getFont();
        return font != null ? font : UtilsFont.getStatusBarItemFont();
    }

    public float fontSize() {
        return projectPrefs.getFontSize() == -1 ? appPrefs.getFontSize() : projectPrefs.getFontSize();
    }

    public Color backgroundColor() {
        return projectPrefs.getBackgroundColor();
    }

    public Color textColor() {
        return projectPrefs.getTextColor();
    }

    public BackgroundImagePosition backgroundImagePosition() {
        return projectPrefs.isBackgroundImageInherited() ? appPrefs.getBackgroundImagePosition() : projectPrefs.getBackgroundImagePosition();
    }

    public int backgroundImageOpacity() {
        return projectPrefs.isBackgroundImageInherited() ? appPrefs.getBackgroundImageOpacity() : projectPrefs.getBackgroundImageOpacity();
    }


}
