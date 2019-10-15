package com.drinchev.projectlabel;

import com.drinchev.projectlabel.utils.UtilsColor;
import com.drinchev.projectlabel.utils.UtilsFont;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;

import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.OptionTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

@State(
        name = "ProjectLabel",
        storages = {
                @Storage("project-label.xml"),
        }
)
public class ProjectLabelPreferences implements PersistentStateComponent<ProjectLabelPreferences> {

    @OptionTag
    private String label = "";

    @OptionTag
    private String backgroundColor = "#B12F2F";

    @OptionTag
    private String textColor = "#FFFFFF";

    @OptionTag
    private String fontSize = "8";

    @OptionTag
    private String fontName = "Verdana";

    static ProjectLabelPreferences getInstance(Project project) {
        return ServiceManager.getService(project, ProjectLabelPreferences.class);
    }

    @Nullable
    @Override
    public ProjectLabelPreferences getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull ProjectLabelPreferences state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    void setBackgroundColor(Color color) {
        this.backgroundColor = UtilsColor.toHex(color);
    }

    public Color getBackgroundColor() {
        return Color.decode(this.backgroundColor);
    }

    void setTextColor(Color color) {
        this.textColor = UtilsColor.toHex(color);
    }

    public Color getTextColor() {
        return Color.decode(this.textColor);
    }

    public String getLabel() {
        return this.label;
    }

    void setLabel(String label) {
        this.label = label;
    }

    public int getFontSize() {
        return Integer.parseInt(this.fontSize);
    }

    void setFontSize(int fontSize) {
        this.fontSize = Integer.toString(fontSize);
    }

    public String getFontName() {
        return fontName;
    }

    public Font getFont() {
        return UtilsFont.getFontByName(fontName);
    }

    void setFontName(String font) {
        this.fontName = font;
    }


}
