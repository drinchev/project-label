package com.drinchev.projectlabel.resources.ui;

import com.drinchev.projectlabel.preferences.PreferencesReader;
import com.drinchev.projectlabel.utils.UtilsUI;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.CustomStatusBarWidget;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.ui.scale.JBUIScale;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

import static java.util.Objects.requireNonNull;


public class ProjectLabelStatusBarWidget extends JButton implements CustomStatusBarWidget {

    private final static Logger LOG = Logger.getInstance(ProjectLabelStatusBarWidget.class);

    @NonNls
    public static final String WIDGET_ID = "ProjectLabelWidget";

    private static final int HORIZONTAL_PADDING = ProjectLabelAWTRenderer.HORIZONTAL_PADDING;
    private static final int VERTICAL_PADDING = ProjectLabelAWTRenderer.VERTICAL_PADDING;
    private static final int VERTICAL_MARGIN = UtilsUI.isNewUI() ? 5 : 3;
    private static final int HEIGHT = 12;

    private Image bufferedImage;

    private final PreferencesReader preferences;

    public ProjectLabelStatusBarWidget(@NotNull Project project, @NotNull PreferencesReader preferences) {
        requireNonNull(project);
        this.preferences = requireNonNull(preferences);

        addActionListener(event -> {
            rebuildWidget();
            updateUI();
            ShowSettingsUtil.getInstance().showSettingsDialog(project, "Project Label");
        });

        setStateFromSettings();

        setOpaque(false);
        setFocusable(false);
        setBorder(JBUI.CurrentTheme.StatusBar.Widget.border());
        repaint();
        updateUI();
    }

    private void setStateFromSettings() {
        String label = preferences.label();
        setToolTipText("Project Label: " + label);
    }

    public void rebuildWidget() {
        try {
            setStateFromSettings();
            bufferedImage = null;
            repaint();
        } catch (Throwable e) {
            LOG.error(e);
        }
    }

    @Override
    public void dispose() {
    }

    @Override
    public void install(@NotNull StatusBar statusBar) {
    }

    @Override
    @NotNull
    public String ID() {
        return WIDGET_ID;
    }

    @Override
    public void updateUI() {
        super.updateUI();
        bufferedImage = null;
    }

    @Override
    public JComponent getComponent() {
        return this;
    }

    @Override
    public void paintComponent(final Graphics graphics) {
        if (bufferedImage == null) {
            Dimension size = getSize();
            LOG.debug("Status Bar Widget Size: " + size.width + "x" + size.height);
            ProjectLabelAWTRenderer renderer = new ProjectLabelAWTRenderer(preferences);
            bufferedImage = renderer.renderLabelAsImage(size, new Dimension(0, VERTICAL_MARGIN));
        }
        UIUtil.drawImage(graphics, bufferedImage, 0, VERTICAL_MARGIN, null);
    }

    @Override
    public Dimension getPreferredSize() {
        ProjectLabelAWTRenderer renderer = new ProjectLabelAWTRenderer(preferences);
        Dimension textDimensions = renderer.getTextDimensions();
        int width = textDimensions.width + (HORIZONTAL_PADDING * 2);
        int textHeight = textDimensions.height;
        int height = textHeight > HEIGHT ? textHeight + (VERTICAL_PADDING * 2) + (VERTICAL_MARGIN * 2) : HEIGHT;
        return new Dimension(JBUIScale.scale(width), JBUIScale.scale(height));
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    @Override
    public Dimension getMaximumSize() {
        return getPreferredSize();
    }


}
