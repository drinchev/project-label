package com.drinchev.projectlabel;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.CustomStatusBarWidget;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.ui.JBColor;
import com.intellij.ui.scale.JBUIScale;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProjectLabelWidget extends JButton implements CustomStatusBarWidget {

    @NonNls
    static final String WIDGET_ID = "ProjectLabelWidget";
    private static final String FONTNAME = "Dialog.bold";

    private static final int PADDING = 18;
    private static final int HEIGHT = 12;
    private Font font;
    private Dimension textDimension;
    private String projectName;
    private String label;
    private Image bufferedImage;
    private Color backgroundColor;
    private Color foregroundColor;
    private ProjectLabelSettings settings;

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

    private String myProjectName;
    private Image myBufferedImage;
    private Color myBackgroundColor;
    private ProjectLabelSettings mySettings = null;

    ProjectLabelWidget(final Project project, final ProjectLabelSettings settings) {
        addActionListener(event -> {
            ProjectLabelSettingsDialogWrapper wrapper = new ProjectLabelSettingsDialogWrapper(project);
            if(wrapper.showAndGet()){
                setStateFromSettings();
            }
        });

        projectName = project.getName().toUpperCase();
        this.settings = settings;

        setStateFromSettings();

        setOpaque(false);
        setFocusable(false);
        setBorder(StatusBarWidget.WidgetBorder.INSTANCE);
        repaint();
        updateUI();
    }

    private void setStateFromSettings() {
        backgroundColor = new JBColor(
                Color.decode(settings.backgroundColor),
                Color.decode(settings.backgroundColor)
        );
        foregroundColor = new JBColor(
                Color.decode(settings.foregroundColor),
                Color.decode(settings.foregroundColor)
        );

        label = settings.label.isEmpty() ? projectName : settings.label;
    }

    void rebuildWidget() {
        try {
            setStateFromSettings();
            bufferedImage = null;
            textDimension = null;
            repaint();
        } catch (Throwable e) {
            // Do nothing
        }
    }

    @Override
    public void dispose() {}

    @Override
    public void install(@NotNull StatusBar statusBar) { }

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
        setFont(getWidgetFont());
    }

    private Font getWidgetFont() {
        if(font == null) {
            Font[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
            for (Font f : fonts) {
                if (Objects.equals(f.getFontName(), FONTNAME)) {

                    Map<TextAttribute, Object> attributes = new HashMap<>();

                    attributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_ULTRABOLD);
                    attributes.put(TextAttribute.SIZE, JBUIScale.scale(8));

                    font = f.deriveFont( attributes );
                    return font;
                }
            }
            font = JBUI.Fonts.label(JBUIScale.scale(8));
        }

        return font;
    }

    private Dimension getTextDimensions() {
        if(textDimension == null) {
            FontRenderContext renderContext = new FontRenderContext(font.getTransform(),true,true);

            textDimension = new Dimension(
                    (int)(font.getStringBounds(label, renderContext).getWidth()),
                    (int)(font.getStringBounds(label, renderContext).getHeight()) - 2
            );
        }

        return textDimension;
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
            graphics2D.setColor(foregroundColor);
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
