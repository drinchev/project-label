package com.drinchev.projectlabel.resources.ui;

import com.drinchev.projectlabel.preferences.PreferencesReader;
import com.drinchev.projectlabel.utils.UtilsFont;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.ImageUtil;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;
import static java.util.Objects.requireNonNull;

public class ProjectLabelAWTRenderer {

    private final static Logger LOG = Logger.getInstance(ProjectLabelAWTRenderer.class);

    public static final RenderingHints RENDERING_HINTS = new RenderingHints(
            ofEntries(
                    entry(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY),
                    entry(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON),
                    entry(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY),
                    entry(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON),
                    entry(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON)
            )
    );

    public static final int HORIZONTAL_PADDING = 12;
    public static final int VERTICAL_PADDING = 2;

    private final PreferencesReader preferences;


    public ProjectLabelAWTRenderer(@NotNull PreferencesReader preferences) {
        this.preferences = requireNonNull(preferences);
    }

    public BufferedImage renderLabel(Dimension targetArea) {
        BufferedImage bufferedImage = ImageUtil.createImage(targetArea.width, targetArea.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();

        g2d.setRenderingHints(RENDERING_HINTS);

        final String label = preferences.label();

        double zoomFactor = calculateZoomFactor(label, targetArea);

        final Font font = getFont(zoomFactor);


        final JBColor backgroundColor = getBackgroundColor();

        final JBColor textColor = getTextColor();

        // calculate text location and size
        int labelWidth = getTextDimensions(font, label).width;
        int labelHeight = getTextDimensions(font, label).height;
        FontMetrics metrics = g2d.getFontMetrics(font);


        int textX = (targetArea.width - labelWidth) / 2;
        int textY = (targetArea.height - metrics.getHeight()) / 2 + metrics.getAscent();

        // bg
        g2d.setColor(backgroundColor);
        final Dimension arcs = new Dimension((int) Math.round(zoomFactor*8), (int) Math.round(zoomFactor*8));

        g2d.fillRoundRect(textX-getHorizontalPadding(zoomFactor), textY-metrics.getAscent()-getVerticalPadding(zoomFactor), labelWidth+(2* getHorizontalPadding(zoomFactor)), labelHeight+(2*getVerticalPadding(zoomFactor)), arcs.width, arcs.height);

        // label
        g2d.setColor(textColor);
        g2d.setFont(font);

        g2d.drawString(
                label,
                textX,
                textY
        );

        g2d.dispose();

        return bufferedImage;
    }

    private double calculateZoomFactor(String label, Dimension targetArea) {
       Dimension defaultLabelBounds = getLabelBounds(1.0);
       double zoomFactorX = targetArea.width / defaultLabelBounds.getWidth();
       double zoomFactorY = targetArea.height / defaultLabelBounds.getHeight();
       return Math.min(zoomFactorX, zoomFactorY);
    }

    private Dimension getStringBounds(Font font, String label) {
        return getTextDimensions(font, label);
    }

    public Dimension getLabelBounds(double zoomFactor) {
        final Font font = getFont(zoomFactor);
        final String label = preferences.label();

        var textDimensions = getTextDimensions(font, label);

        return new Dimension(
                textDimensions.width + (2 * getHorizontalPadding(zoomFactor)),
                textDimensions.height + (2 * getVerticalPadding(zoomFactor))
        );

    }

    public Dimension getPreferredImageRatio() {
        Dimension stringBounds = getStringBounds(getFont(1.0), preferences.label());
        return stringBounds;
    }

    private int getHorizontalPadding(Double zoomFactor) {
        return (int) Math.round(HORIZONTAL_PADDING * Math.min(zoomFactor, 3.0));
    }

    private int getVerticalPadding(double zoomFactor) {
        return (int) Math.round(VERTICAL_PADDING * Math.min(zoomFactor, 3.0));
    }

    private Font getFont(Double zoomFactor) {
        Font font = preferences.font();
        font = UtilsFont.setAttributes(font, TextAttribute.WEIGHT_ULTRABOLD, zoomFactor.floatValue()*font.getSize());
        return font;
    }

    private JBColor getBackgroundColor() {
        return new JBColor(preferences.backgroundColor(), preferences.backgroundColor());
    }

    private JBColor getTextColor() {
        return new JBColor(preferences.textColor(), preferences.textColor());
    }

    public Dimension getTextDimensions() {
        return getTextDimensions(getFont(1.0), preferences.label());
    }

    private Dimension getTextDimensions(Font font, String label) {
            FontRenderContext renderContext = new FontRenderContext(font.getTransform(), true, true);

        Dimension textDimension = new Dimension(
                (int) (font.getStringBounds(label, renderContext).getWidth()),
                (int) (font.getStringBounds(label, renderContext).getHeight())
        );

        return textDimension;
    }

}
