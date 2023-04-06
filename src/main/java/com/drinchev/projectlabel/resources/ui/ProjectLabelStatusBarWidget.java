package com.drinchev.projectlabel.resources.ui;

import com.drinchev.projectlabel.preferences.ApplicationPreferences;
import com.drinchev.projectlabel.preferences.ProjectPreferences;
import com.drinchev.projectlabel.utils.UtilsFont;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.CustomStatusBarWidget;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.ui.JBColor;
import com.intellij.ui.scale.JBUIScale;
import com.intellij.util.ui.ImageUtil;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;

public class ProjectLabelStatusBarWidget extends JButton implements CustomStatusBarWidget {

    private final static Logger LOG = Logger.getInstance(ProjectLabelStatusBarWidget.class);

    @NonNls
    public static final String WIDGET_ID = "ProjectLabelWidget";

    private static final int HORIZONTAL_PADDING = 18;
    private static final int VERTICAL_PADDING = 2;
    private static final int VERTICAL_MARGIN = 5;
    private static final int HEIGHT = 12;

    private final Project project;

    private Dimension textDimension;
    private Image bufferedImage;

    private final ProjectPreferences projectPreferences;
    private final ApplicationPreferences applicationPreferences;

    private String label;
    private Color backgroundColor;
    private Color textColor;
    private Font font;

    private static final RenderingHints HINTS = new RenderingHints(
            ofEntries(
                    entry(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY),
                    entry(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON),
                    entry(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY),
                    entry(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON),
                    entry(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON)
            )
    );

    public ProjectLabelStatusBarWidget(final Project project, ProjectPreferences projectPreferences, ApplicationPreferences applicationPreferences) {
        addActionListener(event -> {
            rebuildWidget();
            updateUI();
            ShowSettingsUtil.getInstance().showSettingsDialog(project, "Project Label");
        });

        this.project = project;
        this.projectPreferences = projectPreferences;
        this.applicationPreferences = applicationPreferences;

        setStateFromSettings();

        setOpaque(false);
        setFocusable(false);
        setBorder(JBUI.CurrentTheme.StatusBar.Widget.border());
        repaint();
        updateUI();
    }

    private void setStateFromSettings() {
        backgroundColor = new JBColor(projectPreferences.getBackgroundColor(), projectPreferences.getBackgroundColor());
        textColor = new JBColor(projectPreferences.getTextColor(), projectPreferences.getTextColor());
        label = projectPreferences.getLabel().isEmpty() ? this.project.getName().toUpperCase() : projectPreferences.getLabel();
        setToolTipText("Project Label: " + label);

        float fontSize = fontSize();

        this.font = fontOfSize(fontSize);
    }

    private Font fontOfSize(float fontSize) {
        Font font = projectPreferences.getFontName().isEmpty() ? applicationPreferences.getFont() : projectPreferences.getFont();
        if (font == null) {
            font = UtilsFont.getStatusBarItemFont();
        }
        return UtilsFont.setAttributes(font, TextAttribute.WEIGHT_ULTRABOLD, fontSize);
    }

    private float fontSize() {
        float projectPreferencesFontSize = projectPreferences.getFontSize();
        float applicationPreferencesFontSize = applicationPreferences.getFontSize();
        float fontSize = JBUIScale.scaleFontSize(projectPreferencesFontSize == -1 ? applicationPreferencesFontSize : projectPreferencesFontSize);
        if (fontSize == 0) {
            fontSize = 8;
        }
        return fontSize;
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
                    (int) (font.getStringBounds(label, renderContext).getHeight())
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

            Dimension size = getSize();
            final Dimension arcs = new Dimension(8, 8);

            FontMetrics metrics = graphics.getFontMetrics(font);

            bufferedImage = ImageUtil.createImage(size.width, size.height - VERTICAL_MARGIN, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics2D = (Graphics2D) bufferedImage.getGraphics().create();

            graphics2D.setRenderingHints(HINTS);

            // background
            graphics2D.setColor(backgroundColor);
            graphics2D.fillRoundRect(0, VERTICAL_MARGIN, size.width, size.height - (2 * VERTICAL_MARGIN), arcs.width, arcs.height);

            // label
            graphics2D.setColor(textColor);
            graphics2D.setFont(font);

            graphics2D.drawString(
                    label,
                    (size.width - labelWidth) / 2,
                    (size.height - metrics.getHeight()) / 2 + metrics.getAscent()
            );
            graphics2D.dispose();
        }

        UIUtil.drawImage(graphics, bufferedImage, 0, 0, null);
    }

    @Override
    public Dimension getPreferredSize() {
        int width = getTextDimensions().width + (HORIZONTAL_PADDING * 2);
        int textHeight = getTextDimensions().height;
        int height = textHeight > HEIGHT ? textHeight + (VERTICAL_PADDING * 2) : HEIGHT;
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
