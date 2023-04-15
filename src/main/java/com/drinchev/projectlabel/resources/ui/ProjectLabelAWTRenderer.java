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

    public static final int HORIZONTAL_PADDING = 8;
    public static final int VERTICAL_PADDING = 2;

    private static final int HEIGHT = 12;

    private final PreferencesReader preferences;


    public ProjectLabelAWTRenderer(@NotNull PreferencesReader preferences) {
        this.preferences = requireNonNull(preferences);
    }

    public BufferedImage renderIntoImage(BufferedImage source, Insets border) {
        final Dimension targetArea = new Dimension(source.getWidth() + border.left + border.right, source.getHeight() + border.top + border.bottom);

        // make sure to transfer image configuration (to account for high DPI)
        var origGraphics = source.createGraphics();
        var gfxConfig = origGraphics.getDeviceConfiguration();
        BufferedImage bufferedImage = gfxConfig.createCompatibleImage(targetArea.width, targetArea.height, source.getTransparency());

        origGraphics.dispose();
        Graphics2D g2d = bufferedImage.createGraphics();
//        g2d.setRenderingHints(RENDERING_HINTS);

        g2d.drawImage(source, border.left, border.top, null);

        g2d.setColor(Color.RED);
        g2d.drawRect(0, 0, targetArea.width , targetArea.height );

        g2d.dispose();

        return bufferedImage;
    }

    public BufferedImage renderLabel(Dimension targetArea) {
        LOG.warn("targetArea: " + targetArea.width + " " + targetArea.height);
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

        LOG.warn("textX: " + textX + " textY: " + textY + " labelWidth: " + labelWidth + " labelHeight: " + labelHeight);

        g2d.setColor(Color.RED);
        g2d.drawRect(0, 0, targetArea.width , targetArea.height );

        // bg
        g2d.setColor(backgroundColor);
        final Dimension arcs = new Dimension((int) Math.round(zoomFactor*8), (int) Math.round(zoomFactor*8));

        g2d.fillRoundRect(textX-getHorizontalPadding(zoomFactor), textY-metrics.getAscent()-getVerticalPadding(zoomFactor), labelWidth+(2* getHorizontalPadding(zoomFactor)), labelHeight+(2*getVerticalPadding(zoomFactor)), arcs.width, arcs.height);
        LOG.warn("roundRect:" + (textX-getHorizontalPadding(zoomFactor)) + " " + (textY-metrics.getAscent()-getVerticalPadding(zoomFactor)) + " " + (labelWidth+(2* getHorizontalPadding(zoomFactor))) + " " + (labelHeight+(2*getVerticalPadding(zoomFactor))) + " " + arcs.width + " " + arcs.height);

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

    public BufferedImage renderLabel2(Dimension size, Dimension margins) {

        int height = size.height - (2 * margins.height);
        int width = size.width - (2 * margins.width);

        var zoomFactor = calculateZoomFactor(preferences.label(), new Dimension(width, height));
        final Dimension arcs = zoomed(zoomFactor, new Dimension(8, 8));

        var bufferedImage = ImageUtil.createImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
        var graphics = bufferedImage.createGraphics();
        graphics.setRenderingHints(RENDERING_HINTS);

        var font = getFont(zoomFactor);
        FontMetrics metrics = graphics.getFontMetrics(font); // check

        int labelWidth = getTextDimensions(font, preferences.label()).width;

        // debug

        graphics.setColor(Color.RED);
        graphics.drawRect(margins.width, margins.height, width , height );

        graphics.setColor(Color.GREEN);
        graphics.drawRect(0, 0, size.width , size.height );
        // background
        graphics.setColor(preferences.backgroundColor());
        graphics.fillRoundRect(0, 0,  width, height, arcs.width, arcs.height);

        // label
        graphics.setColor(preferences.textColor());
        graphics.setFont(font);

        graphics.drawString(
                preferences.label(),
                (width - labelWidth) / 2,
                (height - metrics.getHeight()) / 2 + metrics.getAscent()
        );
        graphics.dispose();
        return bufferedImage;
    }

    private Dimension zoomed(double zoomFactor, Dimension dimension) {
        return new Dimension((int) Math.round(zoomFactor * dimension.getWidth()), (int) Math.round(zoomFactor * dimension.getHeight()));
    }

    public Dimension getPreferredSize2() {
        int width = getTextDimensions().width + (HORIZONTAL_PADDING * 2);
        int textHeight = getTextDimensions().height;
        int height = textHeight > HEIGHT ? textHeight + (VERTICAL_PADDING * 2) : HEIGHT;
        return new Dimension(width, height);
    }

    private double calculateZoomFactor(String label, Dimension targetArea) {
        Dimension preferredSize = getPreferredSize2();
//       Dimension defaultLabelBounds = getLabelBounds(1.0); // is this the right area to check? should we use without padding?
       double zoomFactorX = targetArea.width / preferredSize.getWidth();
       double zoomFactorY = targetArea.height / preferredSize.getHeight();
       LOG.warn("zoomFactorX: " + zoomFactorX + " zoomFactorY: " + zoomFactorY);
       return Math.min(zoomFactorX, zoomFactorY);
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
