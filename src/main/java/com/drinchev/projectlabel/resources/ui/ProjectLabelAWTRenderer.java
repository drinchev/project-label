package com.drinchev.projectlabel.resources.ui;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;
import static java.util.Objects.requireNonNull;

import com.drinchev.projectlabel.preferences.PreferencesReader;
import com.drinchev.projectlabel.utils.UtilsFont;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.ui.JBColor;
import com.intellij.ui.scale.JBUIScale;
import com.intellij.util.ui.ImageUtil;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import org.jetbrains.annotations.NotNull;

public class ProjectLabelAWTRenderer {

    private static final Logger LOG = Logger.getInstance(ProjectLabelAWTRenderer.class);

    public static final RenderingHints RENDERING_HINTS = new RenderingHints(ofEntries(
            entry(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY),
            entry(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON),
            entry(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY),
            entry(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON),
            entry(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON)));

    public static final int HORIZONTAL_PADDING = 8;
    public static final int VERTICAL_PADDING = 2;

    private static final int HEIGHT = 12;

    private final PreferencesReader preferences;

    public ProjectLabelAWTRenderer(@NotNull PreferencesReader preferences) {
        this.preferences = requireNonNull(preferences);
    }

    public static BufferedImage renderImageWithInsets(BufferedImage source, Insets border) {
        final Dimension targetArea = new Dimension(
                source.getWidth() + border.left + border.right, source.getHeight() + border.top + border.bottom);

        // make sure to transfer image configuration (to account for high DPI)
        Graphics2D origGraphics = source.createGraphics();
        GraphicsConfiguration gfxConfig = origGraphics.getDeviceConfiguration();
        BufferedImage bufferedImage =
                gfxConfig.createCompatibleImage(targetArea.width, targetArea.height, source.getTransparency());
        origGraphics.dispose();

        Graphics2D g2d = bufferedImage.createGraphics();

        g2d.drawImage(source, border.left, border.top, null);

        ProjectLabelAWTDebugRenderer.drawDebugRect(g2d, 0, 0, targetArea.width, targetArea.height, JBColor.cyan);

        g2d.dispose();

        return bufferedImage;
    }

    public BufferedImage renderLabelAsImage(Dimension size, Dimension margins) {

        int height = size.height - (2 * margins.height);
        int width = size.width - (2 * margins.width);

        double zoomFactor = calculateZoomFactor(preferences.label(), new Dimension(width, height));
        final Dimension arcs = zoomed(zoomFactor, new Dimension(8, 8));

        BufferedImage bufferedImage = ImageUtil.createImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.setRenderingHints(RENDERING_HINTS);

        Font font = getFont(zoomFactor);
        FontMetrics metrics = graphics.getFontMetrics(font); // check

        int labelWidth = getTextDimensions(font, preferences.label()).width;

        // debug
        ProjectLabelAWTDebugRenderer.drawDebugRect(graphics, margins.width, margins.height, width, height, JBColor.red);
        ProjectLabelAWTDebugRenderer.drawDebugRect(graphics, 0, 0, size.width, size.height, JBColor.green);

        // background
        graphics.setColor(preferences.backgroundColor());
        graphics.fillRoundRect(0, 0, width, height, arcs.width, arcs.height);

        // label
        graphics.setColor(preferences.textColor());
        graphics.setFont(font);

        graphics.drawString(
                preferences.label(),
                (width - labelWidth) / 2,
                (height - metrics.getHeight()) / 2 + metrics.getAscent());
        graphics.dispose();
        return bufferedImage;
    }

    private Dimension zoomed(double zoomFactor, Dimension dimension) {
        return new Dimension((int) Math.round(zoomFactor * dimension.getWidth()), (int)
                Math.round(zoomFactor * dimension.getHeight()));
    }

    public Dimension getPreferredSize() {
        int width = getTextDimensions().width + (HORIZONTAL_PADDING * 2);
        int textHeight = getTextDimensions().height;
        int height = textHeight > HEIGHT ? textHeight + (VERTICAL_PADDING * 2) : HEIGHT;
        return new Dimension(width, height);
    }

    private double calculateZoomFactor(String label, Dimension targetArea) {
        Dimension preferredSize = getPreferredSize();
        double zoomFactorX = targetArea.width / preferredSize.getWidth();
        double zoomFactorY = targetArea.height / preferredSize.getHeight();
        return Math.min(zoomFactorX, zoomFactorY);
    }

    private Font getFont(Double zoomFactor) {
        Font font = preferences.font();
        float fontSize = preferences.fontSize();
        float fontSizeUnscaled = zoomFactor.floatValue() * fontSize;
        float fontSizeScaled = JBUIScale.scaleFontSize(fontSizeUnscaled);
        font = UtilsFont.setAttributes(font, TextAttribute.WEIGHT_ULTRABOLD, fontSizeScaled);
        return font;
    }

    public Dimension getTextDimensions() {
        return getTextDimensions(getFont(1.0), preferences.label());
    }

    private Dimension getTextDimensions(Font font, String label) {
        FontRenderContext renderContext = new FontRenderContext(font.getTransform(), true, true);

        Dimension textDimension =
                new Dimension((int) (font.getStringBounds(label, renderContext).getWidth()), (int)
                        (font.getStringBounds(label, renderContext).getHeight()));

        return textDimension;
    }
}
