package com.drinchev.projectlabel.resources.ui;

import com.intellij.ui.FontComboBox;

import javax.swing.*;
import java.awt.*;

public class PluginConfiguration {

    private JPanel rootPanel;
    private JTextField textFieldBackgroundColor;
    private JSpinner spinnerFontSize;
    private JPanel panelBackgroundColor;
    private JPanel panelTextColor;
    private JTextField textFieldTextColor;
    private FontComboBox fontComboBoxFont;
    private JTextField textFieldLabel;

    private ColorField colorFieldTextColor;
    private ColorField colorFieldBackgroundColor;

    private SpinnerNumberModel spinnerFontSizeModel;

    /**
     * Constructor
     */
    public PluginConfiguration() {
        this.colorFieldTextColor = new ColorField(
                textFieldTextColor,
                panelTextColor,
                "Text Color"
        );
        this.colorFieldBackgroundColor = new ColorField(
                textFieldBackgroundColor,
                panelBackgroundColor,
                "Background Color"
        );
        this.spinnerFontSizeModel = new SpinnerNumberModel(0, 0, 36, 1);
        this.spinnerFontSize.setModel(this.spinnerFontSizeModel);
    }

    public void setTextColor(Color color) {
        this.colorFieldTextColor.setColor(color);
    }

    public Color getTextColor() {
        return this.colorFieldTextColor.getColor();
    }

    public void setBackgroundColor(Color color) {
        this.colorFieldBackgroundColor.setColor(color);
    }

    public Color getBackgroundColor() {
        return this.colorFieldBackgroundColor.getColor();
    }

    public int getFontSize() {
        return this.spinnerFontSizeModel.getNumber().intValue();
    }

    public void setFontSize(int fontSize) {
        this.spinnerFontSizeModel.setValue(fontSize);
    }

    public String getFontName() {
        String name = this.fontComboBoxFont.getFontName();
        return name == null ? "Serif" : name;
    }

    public void setFontName(String font) {
        this.fontComboBoxFont.setFontName(font);
    }

    public String getLabel() {
        return this.textFieldLabel.getText();
    }

    public void setLabel(String label) {
        this.textFieldLabel.setText(label);
    }

    /**
     * @return JPanel
     */
    public JPanel getRootPanel() {
        return rootPanel;
    }

}
