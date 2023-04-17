package com.drinchev.projectlabel.preferences;

import com.drinchev.projectlabel.resources.ui.BackgroundImagePosition;
import com.drinchev.projectlabel.utils.UtilsColor;
import com.drinchev.projectlabel.utils.UtilsFont;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;

import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.OptionTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

@State(
        name = "ProjectLabel",
        storages = {
                @Storage("project-label.xml"),
        }
)
public class ProjectPreferences implements PersistentStateComponent<ProjectPreferences> {

    @OptionTag
    private String label = "";

    @OptionTag
    private String backgroundColor = "#b12f2f";

    @OptionTag
    private String textColor = "#ffffff";

    @OptionTag
    private String fontSize = null;

    @OptionTag
    private String fontName = null;

    @OptionTag
    private String backgroundImagePosition = BackgroundImagePosition.HIDDEN.name();

    @OptionTag
    private int backgroundImageOpacity = 15;

    @OptionTag
    private boolean backgroundImageInherited = true;

    public static ProjectPreferences getInstance(Project project) {
        return project.getService(ProjectPreferences.class);
    }

    @Nullable
    @Override
    public ProjectPreferences getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull ProjectPreferences state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public void setBackgroundColor(Color color) {
        this.backgroundColor = UtilsColor.toHex(color);
    }

    public Color getBackgroundColor() {
        return Color.decode(this.backgroundColor);
    }

    public void setTextColor(Color color) {
        this.textColor = UtilsColor.toHex(color);
    }

    public Color getTextColor() {
        return Color.decode(this.textColor);
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getFontSize() {
        return this.fontSize == null ? -1 : Integer.parseInt(this.fontSize);
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize == -1 ? null : Integer.toString(fontSize);
    }

    public String getFontName() {
        return fontName == null ? "" : fontName;
    }

    public Font getFont() {
        return UtilsFont.getFontByName(fontName);
    }

    public void setFontName(String font) {
        this.fontName = font.isEmpty() ? null : font;
    }

    public void setBackgroundImagePosition(BackgroundImagePosition backgroundImagePosition) {
        this.backgroundImagePosition = backgroundImagePosition != null ? backgroundImagePosition.name() : null;
    }

    public BackgroundImagePosition getBackgroundImagePosition() {
        if (this.backgroundImagePosition == null) {
            return null;
        }
        return BackgroundImagePosition.valueOf(this.backgroundImagePosition);
    }

    public int getBackgroundImageOpacity() {
        return backgroundImageOpacity;
    }

    public void setBackgroundImageOpacity(int backgroundImageOpacity) {
        this.backgroundImageOpacity = backgroundImageOpacity;
    }

    public boolean isBackgroundImageInherited() {
        return backgroundImageInherited;
    }

    public void setBackgroundImageInherited(boolean backgroundImageInherited) {
        this.backgroundImageInherited = backgroundImageInherited;
    }
}
