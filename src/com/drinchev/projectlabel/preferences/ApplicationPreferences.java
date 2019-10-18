package com.drinchev.projectlabel.preferences;

import com.drinchev.projectlabel.utils.UtilsFont;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.OptionTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

@State(
        name = "ProjectLabelApplicationPreferences",
        storages = {
                @Storage("project-label-global.xml")
        }
)
public class ApplicationPreferences implements PersistentStateComponent<ApplicationPreferences> {

    @OptionTag
    private String fontSize = "8";

    @OptionTag
    private String fontName = "Dialog";

    public static ApplicationPreferences getInstance() {
        return ServiceManager.getService(ApplicationPreferences.class);
    }

    @Nullable
    @Override
    public ApplicationPreferences getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull ApplicationPreferences state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public int getFontSize() {
        return Integer.parseInt(this.fontSize);
    }

    public void setFontSize(int fontSize) {
        this.fontSize = Integer.toString(fontSize);
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String font) {
        this.fontName = font;
    }

    public Font getFont() {
        return UtilsFont.getFontByName(fontName);
    }

}
