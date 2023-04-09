package com.drinchev.projectlabel.resources.ui;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.ui.FontComboBox;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
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
    private JLabel editorBackgroundOpacityLabel;
    private JSpinner editorImageBackgroundOpacity;
    private JCheckBox editorImageEnabledCheckbox;
    private JLabel editorImageBackgroundOpacityUnitLabel;
    private JLabel editorImagePositionLabel;
    private JComboBox<String> editorImagePositionComboBox;
    private final SpinnerNumberModel editorImageBackgroundOpacityModel;
    private ColorField colorFieldTextColor;
    private ColorField colorFieldBackgroundColor;
    private SpinnerNumberModel spinnerFontSizeModel;
    private SpinnerNumberModel spinnerGlobalFontSizeModel;

    private static final String BACKGROUND_IMAGE_POSITION_TOP_LEFT = "Top Left";
    private static final String BACKGROUND_IMAGE_POSITION_TOP_RIGHT = "Top Right";
    private static final String BACKGROUND_IMAGE_POSITION_BOTTOM_RIGHT = "Bottom Right";
    private static final String BACKGROUND_IMAGE_POSITION_BOTTOM_LEFT = "Bottom Left";
    private static final String BACKGROUND_IMAGE_POSITION_CENTER = "Center";

    private final static Map<String, Icon> LABEL_POSITIONS = Collections.unmodifiableMap(new LinkedHashMap<>() {{ // linked hash map to preserve order
        put(BACKGROUND_IMAGE_POSITION_CENTER, AllIcons.General.FitContent);
        put(BACKGROUND_IMAGE_POSITION_TOP_LEFT, AllIcons.Actions.MoveToTopLeft);
        put(BACKGROUND_IMAGE_POSITION_TOP_RIGHT, AllIcons.Actions.MoveToTopRight);
        put(BACKGROUND_IMAGE_POSITION_BOTTOM_RIGHT, AllIcons.Actions.MoveToBottomRight);
        put(BACKGROUND_IMAGE_POSITION_BOTTOM_LEFT, AllIcons.Actions.MoveToBottomLeft);
    }});

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

        this.editorImageEnabledCheckbox.addActionListener(event -> {
            boolean enabled = this.editorImageEnabledCheckbox.isSelected();
            updateBackgroundImageEnabledStates(enabled);
        });

        this.editorImageBackgroundOpacityModel = new SpinnerNumberModel(15, 0, 100, 1);
        this.editorImageBackgroundOpacity.setModel(this.editorImageBackgroundOpacityModel);

        this.editorImagePositionComboBox.setRenderer(new LabelWithIconRenderer());
        this.editorImagePositionComboBox.setModel(new DefaultComboBoxModel<>(LABEL_POSITIONS.keySet().toArray(new String[0])));
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

    private void updateBackgroundImageEnabledStates(boolean enabled) {
        Stream.of(this.editorImagePositionLabel,
                        this.editorImagePositionComboBox,
                        this.editorBackgroundOpacityLabel,
                        this.editorImageBackgroundOpacity,
                        this.editorImageBackgroundOpacityUnitLabel)
                .forEach(component -> component.setEnabled(enabled));
    }

    // backgroundImage
    public BackgroundImagePosition getBackgroundImagePosition() {
        if (!editorImageEnabledCheckbox.isSelected()) {
            return BackgroundImagePosition.HIDDEN;
        }
        String position = (String) editorImagePositionComboBox.getSelectedItem();
        if (position == null) {
            // should never happen
            return BackgroundImagePosition.HIDDEN;
        }
        if (position.equals(BACKGROUND_IMAGE_POSITION_TOP_LEFT)) {
            return BackgroundImagePosition.TOP_LEFT;
        }
        if (position.equals(BACKGROUND_IMAGE_POSITION_TOP_RIGHT)) {
            return BackgroundImagePosition.TOP_RIGHT;
        }
        if (position.equals(BACKGROUND_IMAGE_POSITION_BOTTOM_RIGHT)) {
            return BackgroundImagePosition.BOTTOM_RIGHT;
        }
        if (position.equals(BACKGROUND_IMAGE_POSITION_BOTTOM_LEFT)) {
            return BackgroundImagePosition.BOTTOM_LEFT;
        }
        if (position.equals(BACKGROUND_IMAGE_POSITION_CENTER)) {
            return BackgroundImagePosition.CENTER;
        }
        throw new IllegalStateException("Unknown position: " + position);
    }

    public void setBackgroundImagePosition(@NotNull BackgroundImagePosition position) {
        Objects.requireNonNull(position);
        if (position == BackgroundImagePosition.HIDDEN) {
            editorImageEnabledCheckbox.setSelected(false);
            updateBackgroundImageEnabledStates(false);
            return;
        }
        editorImageEnabledCheckbox.setSelected(true);
        updateBackgroundImageEnabledStates(true);
        switch (position) {
            case TOP_LEFT -> editorImagePositionComboBox.setSelectedItem(BACKGROUND_IMAGE_POSITION_TOP_LEFT);
            case TOP_RIGHT -> editorImagePositionComboBox.setSelectedItem(BACKGROUND_IMAGE_POSITION_TOP_RIGHT);
            case BOTTOM_RIGHT -> editorImagePositionComboBox.setSelectedItem(BACKGROUND_IMAGE_POSITION_BOTTOM_RIGHT);
            case BOTTOM_LEFT -> editorImagePositionComboBox.setSelectedItem(BACKGROUND_IMAGE_POSITION_BOTTOM_LEFT);
            case CENTER -> editorImagePositionComboBox.setSelectedItem(BACKGROUND_IMAGE_POSITION_CENTER);
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

    private static class LabelWithIconRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            label.setIcon(LABEL_POSITIONS.get((String) value));
            return label;
        }
    }
}
