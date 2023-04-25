package com.drinchev.projectlabel.preferences;

import com.drinchev.projectlabel.resources.ui.BackgroundImagePosition;
import com.drinchev.projectlabel.utils.UtilsFont;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.OptionTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Objects;

@State(
        name = "ProjectLabelApplicationPreferences",
        storages = {
                @Storage("project-label-global.xml")
        }
)
public class ApplicationPreferences implements PersistentStateComponent<ApplicationPreferences> {

    @OptionTag
    private String fontSize = String.valueOf(UtilsFont.getStatusBarItemFont().getSize()); // https://jetbrains.design/intellij/principles/typography/#03

    @OptionTag
    private String fontName = UtilsFont.getStatusBarItemFont().getFontName();

    @OptionTag
    private String backgroundImagePosition = BackgroundImagePosition.HIDDEN.name();

    @OptionTag
    private int backgroundImageOpacity = 15;

    public static ApplicationPreferences getInstance() {
        return ApplicationManager.getApplication().getService(ApplicationPreferences.class);
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

    public void setBackgroundImagePosition(@NotNull BackgroundImagePosition backgroundImagePosition) {
        this.backgroundImagePosition = Objects.requireNonNull(backgroundImagePosition).name();
    }

    public BackgroundImagePosition getBackgroundImagePosition() {
        return BackgroundImagePosition.valueOf(this.backgroundImagePosition);
    }

    public int getBackgroundImageOpacity() {
        return backgroundImageOpacity;
    }

    public void setBackgroundImageOpacity(int backgroundImageOpacity) {
        this.backgroundImageOpacity = backgroundImageOpacity;
    }

}
