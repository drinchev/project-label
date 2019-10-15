package com.drinchev.projectlabel.resources.ui;

import com.drinchev.projectlabel.ProjectLabelPreferences;
import com.drinchev.projectlabel.utils.UtilsFont;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.CustomStatusBarWidget;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.ui.JBColor;
import com.intellij.ui.scale.JBUIScale;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;

public class StatusBarWidget extends JButton implements CustomStatusBarWidget {

    private final static Logger LOG = Logger.getInstance(StatusBarWidget.class);

    @NonNls
    public static final String WIDGET_ID = "ProjectLabelWidget";

    private static final int PADDING = 18;
    private static final int HEIGHT = 12;

    private Project project;

    private Dimension textDimension;
    private Image bufferedImage;

    private ProjectLabelPreferences preferences;

    private String label;
    private Color backgroundColor;
    private Color textColor;
    private Font font;

    private static RenderingHints HINTS;

    static {
        HINTS = new RenderingHints(RenderingHints.KEY_ALPHA_INTERPOLATION,
                RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        HINTS.put(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        HINTS.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        HINTS.put(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        HINTS.put(RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    }

    public StatusBarWidget(final Project project, ProjectLabelPreferences preferences) {
        addActionListener(event -> {
            ShowSettingsUtil.getInstance().showSettingsDialog(project, "Project Label");
        });

        this.project = project;
        this.preferences = preferences;

        setStateFromSettings();

        setOpaque(false);
        setFocusable(false);
        setBorder(StatusBarWidget.WidgetBorder.INSTANCE);
        repaint();
        updateUI();
    }

    private void setStateFromSettings() {
        backgroundColor = new JBColor(preferences.getBackgroundColor(), preferences.getBackgroundColor());
        textColor = new JBColor(preferences.getTextColor(), preferences.getTextColor());
        label = preferences.getLabel().isEmpty() ? this.project.getName().toUpperCase() : preferences.getLabel();
        float fontSize = JBUIScale.scaleFontSize(preferences.getFontSize());
        font = preferences.getFont();
        if (font == null) {
            font = UtilsFont.getFontByName("Serif");
        }
        if (fontSize == 0) {
            fontSize = 8;
        }
        font = UtilsFont.setAttributes(font, TextAttribute.WEIGHT_ULTRABOLD, fontSize);
    }

    public void rebuildWidget() {
        try {
            setStateFromSettings();
            bufferedImage = null;
            textDimension = null;
            repaint();
        } catch (Throwable e) {
            LOG.error(e);
        }
    }

    private Dimension getTextDimensions() {
        if (textDimension == null) {
            FontRenderContext renderContext = new FontRenderContext(font.getTransform(), true, true);

            textDimension = new Dimension(
                    (int) (font.getStringBounds(label, renderContext).getWidth()),
                    (int) (font.getStringBounds(label, renderContext).getHeight()) - 2
            );
        }

        return textDimension;
    }

    @Override
    public void dispose() {
    }

    @Override
    public void install(@NotNull StatusBar statusBar) {
    }

    @Override
    @Nullable
    public WidgetPresentation getPresentation(@NotNull PlatformType type) {
        return null;
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
            int labelWidth = getTextDimensions().width;
            int labelHeight = getTextDimensions().height;

            Dimension size = getSize();
            final Dimension arcs = new Dimension(8, 8);

            // image
            bufferedImage = UIUtil.createImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics2D = (Graphics2D) bufferedImage.getGraphics().create();

            graphics2D.setRenderingHints(HINTS);

            // background
            graphics2D.setColor(backgroundColor);
            graphics2D.fillRoundRect(0, 0, size.width, size.height, arcs.width, arcs.height);

            // label
            graphics2D.setColor(textColor);
            graphics2D.setFont(font);

            graphics2D.drawString(
                    label,
                    (size.width - labelWidth) / 2,
                    ((size.height - labelHeight) / 2) + labelHeight
            );
            graphics2D.dispose();
        }

        UIUtil.drawImage(graphics, bufferedImage, 0, 0, null);
    }

    @Override
    public Dimension getPreferredSize() {
        int width = getTextDimensions().width + PADDING + PADDING;

        return new Dimension(JBUIScale.scale(width), JBUIScale.scale(HEIGHT));
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
