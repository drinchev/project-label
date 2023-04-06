package com.drinchev.projectlabel.resources.ui;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.ui.FontComboBox;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.stream.Stream;

public class PluginConfiguration {

    private final static Logger LOG = Logger.getInstance(PluginConfiguration.class);

    private JPanel rootPanel;
    private JTextField textFieldBackgroundColor;
    private JSpinner spinnerFontSize;
    private JPanel panelBackgroundColor;
    private JPanel panelTextColor;
    private JTextField textFieldTextColor;
    private FontComboBox fontComboBoxFont;
    private JTextField textFieldLabel;
    private JSpinner spinnerGlobalFontSize;
    private JCheckBox checkBoxInheritFontSize;
    private JCheckBox checkBoxInheritFont;
    private FontComboBox fontComboBoxGlobalFont;
    private JRadioButton editorImageHiddenRadioButton;
    private JRadioButton editorImageTopRightRadioButton;
    private JRadioButton editorImageBottomRightRadioButton;
    private JRadioButton editorImageTopLeftRadioButton;
    private JRadioButton editorImageBottomLeftRadioButton;
    private JRadioButton editorImageCenterRadioButton;
    private JLabel editorBackgroundOpacityLabel;
    private JSpinner editorImageBackgroundOpacity;
    private SpinnerNumberModel editorImageBackgroundOpacityModel;
    private ColorField colorFieldTextColor;
    private ColorField colorFieldBackgroundColor;

    private SpinnerNumberModel spinnerFontSizeModel;
    private SpinnerNumberModel spinnerGlobalFontSizeModel;

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

        this.spinnerGlobalFontSizeModel = new SpinnerNumberModel(0, 0, 36, 1);
        this.spinnerGlobalFontSize.setModel(this.spinnerGlobalFontSizeModel);

        checkBoxInheritFont.addActionListener(event -> {
            if (checkBoxInheritFont.isSelected()) {
                fontComboBoxFont.setEnabled(false);
                fontComboBoxFont.setFontName(fontComboBoxGlobalFont.getFontName());
            } else {
                fontComboBoxFont.setEnabled(true);
            }
        });

        checkBoxInheritFontSize.addActionListener(event -> {
            if (checkBoxInheritFontSize.isSelected()) {
                spinnerFontSize.setEnabled(false);
                spinnerFontSizeModel.setValue(spinnerGlobalFontSizeModel.getValue());
            } else {
                spinnerFontSize.setEnabled(true);
            }
        });

        this.editorImageBackgroundOpacityModel = new SpinnerNumberModel(15, 0, 100, 1);
        this.editorImageBackgroundOpacity.setModel(this.editorImageBackgroundOpacityModel);

        ButtonGroup buttonGroup = new ButtonGroup();
        Stream.of(editorImageHiddenRadioButton,
                        editorImageTopLeftRadioButton,
                        editorImageTopRightRadioButton,
                        editorImageBottomRightRadioButton,
                        editorImageBottomLeftRadioButton,
                        editorImageCenterRadioButton)
                .forEach(buttonGroup::add);

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
        return this.checkBoxInheritFontSize.isSelected() ? -1 : this.spinnerFontSizeModel.getNumber().intValue();
    }

    public void setFontSize(int fontSize) {
        if (fontSize == -1) {
            this.checkBoxInheritFontSize.setSelected(true);
            this.spinnerFontSizeModel.setValue(this.spinnerGlobalFontSizeModel.getValue());
            this.spinnerFontSize.setEnabled(false);
        } else {
            this.spinnerFontSizeModel.setValue(fontSize);
        }
    }

    public String getFontName() {
        if (this.checkBoxInheritFont.isSelected()) {
            return "";
        }
        String name = this.fontComboBoxFont.getFontName();
        return name == null ? "Dialog" : name;
    }

    public void setFontName(String font) {
        if (font.isEmpty()) {
            this.checkBoxInheritFont.setSelected(true);
            this.fontComboBoxFont.setEnabled(false);
            this.fontComboBoxFont.setFontName(this.fontComboBoxGlobalFont.getFontName());
        } else {
            this.fontComboBoxFont.setFontName(font);
        }
    }

    public String getLabel() {
        return this.textFieldLabel.getText();
    }

    public void setLabel(String label) {
        this.textFieldLabel.setText(label);
    }

    public int getGlobalFontSize() {
        return this.spinnerGlobalFontSizeModel.getNumber().intValue();
    }

    public void setGlobalFontSize(int fontSize) {
        this.spinnerGlobalFontSizeModel.setValue(fontSize);
    }

    public void setGlobalFontName(String font) {
        this.fontComboBoxGlobalFont.setFontName(font);
    }

    public String getGlobalFontName() {
        String name = this.fontComboBoxGlobalFont.getFontName();
        return name == null ? "Dialog" : name;
    }

    // backgroundImage
    public BackgroundImagePosition getBackgroundImagePosition() {
        if (editorImageTopLeftRadioButton.isSelected()) {
            return BackgroundImagePosition.TOP_LEFT;
        } else if (editorImageTopRightRadioButton.isSelected()) {
            return BackgroundImagePosition.TOP_RIGHT;
        } else if (editorImageBottomRightRadioButton.isSelected()) {
            return BackgroundImagePosition.BOTTOM_RIGHT;
        } else if (editorImageBottomLeftRadioButton.isSelected()) {
            return BackgroundImagePosition.BOTTOM_LEFT;
        } else if (editorImageCenterRadioButton.isSelected()) {
            return BackgroundImagePosition.CENTER;
        } else {
            return BackgroundImagePosition.HIDDEN;
        }
    }

    public void setBackgroundImagePosition(@NotNull BackgroundImagePosition position) {
        Objects.requireNonNull(position);
        switch (position) {
            case TOP_LEFT -> editorImageTopLeftRadioButton.setSelected(true);
            case TOP_RIGHT -> editorImageTopRightRadioButton.setSelected(true);
            case BOTTOM_RIGHT -> editorImageBottomRightRadioButton.setSelected(true);
            case BOTTOM_LEFT -> editorImageBottomLeftRadioButton.setSelected(true);
            case CENTER -> editorImageCenterRadioButton.setSelected(true);
            default -> editorImageHiddenRadioButton.setSelected(true);
        }
    }

    public void setBackgroundImageOpacity(int opacity) {
        this.editorImageBackgroundOpacityModel.setValue(opacity);
    }

    public int getBackgroundImageOpacity() {
        return this.editorImageBackgroundOpacityModel.getNumber().intValue();
    }

    /**
     * @return JPanel
     */
    public JPanel getRootPanel() {
        return rootPanel;
    }

}
