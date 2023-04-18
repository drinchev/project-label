package com.drinchev.projectlabel.resources.ui;

import com.drinchev.projectlabel.preferences.BackgroundImagePrefs;
import com.drinchev.projectlabel.utils.UtilsIcon;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.ui.FontComboBox;
import com.intellij.ui.TitledSeparator;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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
    private JCheckBox checkBoxInheritFont;
    private FontComboBox fontComboBoxGlobalFont;
    private JLabel editorBackgroundOpacityLabel;
    private JSpinner editorImageBackgroundOpacity;
    private JCheckBox editorImageEnabledCheckbox;
    private JLabel editorImageBackgroundOpacityUnitLabel;
    private JLabel editorImagePositionLabel;
    private JComboBox<String> editorImagePositionComboBox;
    private JComboBox<String> editorImagePositionComboBoxGlobal;
    private JSpinner editorImageBackgroundOpacityGlobal;
    private JCheckBox editorImageEnabledCheckboxGlobal;
    private JCheckBox editorImageInheritCheckbox;
    private TitledSeparator globalPreferencesSectionTitle;
    private TitledSeparator projectPreferencesSectionTitle;
    private final SpinnerNumberModel editorImageBackgroundOpacityModel;
    private final SpinnerNumberModel editorImageBackgroundOpacityModelGlobal;
    private ColorField colorFieldTextColor;
    private ColorField colorFieldBackgroundColor;
    private SpinnerNumberModel spinnerFontSizeModel;
    private SpinnerNumberModel spinnerGlobalFontSizeModel;

    private final static Map<BackgroundImagePosition, Icon> LABEL_POSITIONS = Collections.unmodifiableMap(new LinkedHashMap<>() {{ // linked hash map to preserve order
        put(BackgroundImagePosition.CENTER, UtilsIcon.loadRasterizedIcon("icons/bg_image_pos_center.svg"));
        put(BackgroundImagePosition.TOP_LEFT, UtilsIcon.loadRasterizedIcon("icons/bg_image_pos_top_left.svg"));
        put(BackgroundImagePosition.TOP_RIGHT, UtilsIcon.loadRasterizedIcon("icons/bg_image_pos_top_right.svg"));
        put(BackgroundImagePosition.BOTTOM_RIGHT, UtilsIcon.loadRasterizedIcon("icons/bg_image_pos_bottom_right.svg"));
        put(BackgroundImagePosition.BOTTOM_LEFT, UtilsIcon.loadRasterizedIcon("icons/bg_image_pos_bottom_left.svg"));
    }});

    private final static Map<BackgroundImagePosition, Icon> DISABLED_LABEL_POSITIONS = Collections.unmodifiableMap(new LinkedHashMap<>() {{ // linked hash map to preserve order
        put(BackgroundImagePosition.CENTER, UtilsIcon.disabledIcon(LABEL_POSITIONS.get(BackgroundImagePosition.CENTER)));
        put(BackgroundImagePosition.TOP_LEFT, UtilsIcon.disabledIcon(LABEL_POSITIONS.get(BackgroundImagePosition.TOP_LEFT)));
        put(BackgroundImagePosition.TOP_RIGHT, UtilsIcon.disabledIcon(LABEL_POSITIONS.get(BackgroundImagePosition.TOP_RIGHT)));
        put(BackgroundImagePosition.BOTTOM_RIGHT, UtilsIcon.disabledIcon(LABEL_POSITIONS.get(BackgroundImagePosition.BOTTOM_RIGHT)));
        put(BackgroundImagePosition.BOTTOM_LEFT, UtilsIcon.disabledIcon(LABEL_POSITIONS.get(BackgroundImagePosition.BOTTOM_LEFT)));
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

        checkBoxInheritFont.addActionListener(this::updateBackgroundImageCheckboxDependingStatesAndValues);

        this.editorImageEnabledCheckbox.addActionListener(this::updateBackgroundImageCheckboxDependingStatesAndValues);

        this.editorImageEnabledCheckboxGlobal.addActionListener(this::updateBackgroundImageCheckboxDependingStatesAndValues);

        this.editorImageBackgroundOpacityModel = new SpinnerNumberModel(15, 0, 100, 1);
        this.editorImageBackgroundOpacity.setModel(this.editorImageBackgroundOpacityModel);

        this.editorImageBackgroundOpacityModelGlobal = new SpinnerNumberModel(15, 0, 100, 1);
        this.editorImageBackgroundOpacityGlobal.setModel(this.editorImageBackgroundOpacityModelGlobal);

        Stream.of(this.editorImagePositionComboBox, this.editorImagePositionComboBoxGlobal)
                .forEach(comboBox -> {
                    comboBox.setRenderer(new LabelWithIconRenderer(comboBox));
                    comboBox.setModel(new DefaultComboBoxModel<>(LABEL_POSITIONS.keySet().stream().map(BackgroundImagePosition::displayName).toArray(String[]::new)));
                });

        editorImageInheritCheckbox.addActionListener(this::updateBackgroundImageCheckboxDependingStatesAndValues);

        rootPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateSectionTitleWidths();
            }

            private void updateSectionTitleWidths() {
                Insets insets = rootPanel.getInsets();
                Border border = rootPanel.getBorder();
                Insets borderInsets = border.getBorderInsets(rootPanel);
                Dimension preferredSize = new Dimension(rootPanel.getWidth() - (insets.left + insets.right + borderInsets.left + borderInsets.right), -1);

                globalPreferencesSectionTitle.setPreferredSize(preferredSize);
                projectPreferencesSectionTitle.setPreferredSize(preferredSize);
            }
        });
    }

    private void updateBackgroundImageCheckboxDependingStatesAndValues(Object ignore) {
        // global
        final boolean globallyEnabled = this.editorImageEnabledCheckboxGlobal.isSelected();
        Stream.of(
                this.editorImagePositionComboBoxGlobal,
                this.editorImageBackgroundOpacityGlobal
        ).forEach(component -> component.setEnabled(globallyEnabled));

        // project
        final boolean inherited = this.editorImageInheritCheckbox.isSelected();
        if (inherited) {
            copyBackgroundImageValuesFromGlobalToProject();
        }
        this.editorImageEnabledCheckbox.setEnabled(!inherited);

        final boolean projectEnabled = this.editorImageEnabledCheckbox.isSelected() && !inherited;
        Stream.of(
                this.editorImagePositionComboBox,
                this.editorImageBackgroundOpacity
        ).forEach(component -> component.setEnabled(projectEnabled));
    }

    private void copyBackgroundImageValuesFromGlobalToProject() {
        if (this.editorImageInheritCheckbox.isSelected()) {
            this.editorImageEnabledCheckbox.setSelected(this.editorImageEnabledCheckboxGlobal.isSelected());
            this.editorImageBackgroundOpacityModel.setValue(this.editorImageBackgroundOpacityGlobal.getValue());
            this.editorImagePositionComboBox.setSelectedItem(this.editorImagePositionComboBoxGlobal.getSelectedItem());
        }
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
        return this.checkBoxInheritFont.isSelected() ? -1 : this.spinnerFontSizeModel.getNumber().intValue();
    }

    public void setFontSize(int fontSize) {
        if (fontSize == -1) {
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

    public void setBackgroundImagePrefs(BackgroundImagePrefs projPrefs, @NotNull BackgroundImagePrefs globalPrefs) {
        Objects.requireNonNull(globalPrefs);
        if (globalPrefs.position() == BackgroundImagePosition.HIDDEN) {
            editorImageEnabledCheckboxGlobal.setSelected(false);
            Stream.of(this.editorImagePositionComboBoxGlobal, this.editorImageBackgroundOpacityGlobal, this.editorImageEnabledCheckboxGlobal)
                    .forEach(component -> component.setEnabled(false));
        } else {
            editorImageEnabledCheckboxGlobal.setSelected(true);
            Stream.of(this.editorImagePositionComboBoxGlobal, this.editorImageBackgroundOpacityGlobal, this.editorImageEnabledCheckboxGlobal)
                    .forEach(component -> component.setEnabled(true));
            editorImagePositionComboBoxGlobal.getModel().setSelectedItem(globalPrefs.position().displayName());
            editorImageBackgroundOpacityModelGlobal.setValue(globalPrefs.opacity());
        }
        if (projPrefs == null) {
            editorImageInheritCheckbox.setSelected(true);
        } else {
            editorImageInheritCheckbox.setSelected(false);
            editorImageEnabledCheckbox.setSelected(projPrefs.position() != BackgroundImagePosition.HIDDEN);
            editorImagePositionComboBox.getModel().setSelectedItem(projPrefs.position().displayName());
            editorImageBackgroundOpacityModel.setValue(projPrefs.opacity());
        }
        updateBackgroundImageCheckboxDependingStatesAndValues(null);
    }

    public BackgroundImagePrefs getBackgroundImagePrefs() {
        if (editorImageInheritCheckbox.isSelected()) {
            return null;
        }
        int opacity = editorImageBackgroundOpacityModel.getNumber().intValue();
        String positionString = (String) editorImagePositionComboBox.getSelectedItem();
        BackgroundImagePosition position = BackgroundImagePosition.findByDisplayName(positionString);
        return new BackgroundImagePrefs(opacity, editorImageEnabledCheckbox.isSelected() ? position : BackgroundImagePosition.HIDDEN);
    }

    public BackgroundImagePrefs getGlobalBackgroundImagePrefs() {
        int opacity = editorImageBackgroundOpacityModelGlobal.getNumber().intValue();
        String positionString = (String) editorImagePositionComboBoxGlobal.getSelectedItem();
        BackgroundImagePosition position = BackgroundImagePosition.findByDisplayName(positionString);
        return new BackgroundImagePrefs(opacity, editorImageEnabledCheckboxGlobal.isSelected() ? position : BackgroundImagePosition.HIDDEN);
    }

    /**
     * @return JPanel
     */
    public JPanel getRootPanel() {
        return rootPanel;
    }

    private static class LabelWithIconRenderer extends DefaultListCellRenderer {

        private final JComboBox<String> comboBox;

        public LabelWithIconRenderer(@NotNull JComboBox<String> comboBox) {
            this.comboBox = Objects.requireNonNull(comboBox);
        }

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            String positionDisplayName = (String) value;
            BackgroundImagePosition position = BackgroundImagePosition.findByDisplayName(positionDisplayName);
            if (comboBox.isEnabled()) {
                label.setIcon(LABEL_POSITIONS.get(position));
            } else {
                label.setIcon(DISABLED_LABEL_POSITIONS.get(position));
            }
            return label;
        }
    }
}
