package com.drinchev.projectlabel.resources.ui;

import com.drinchev.projectlabel.preferences.ApplicationPreferences;
import com.drinchev.projectlabel.preferences.ProjectPreferences;
import com.drinchev.projectlabel.utils.UtilsFont;
import com.intellij.openapi.project.Project;
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

    public static final RenderingHints RENDERING_HINTS = new RenderingHints(
            ofEntries(
                    entry(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY),
                    entry(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON),
                    entry(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY),
                    entry(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON),
                    entry(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON)
            )
    );

    public static final int HORIZONTAL_PADDING = 18;
    public static final int VERTICAL_PADDING = 2;

    private final Project project;
    private final ProjectPreferences projectPreferences;

    private final ApplicationPreferences applicationPreferences;


    public ProjectLabelAWTRenderer(@NotNull Project project, @NotNull ProjectPreferences projectPreferences, @NotNull ApplicationPreferences applicationPreferences) {
        this.project = requireNonNull(project);
        this.projectPreferences = requireNonNull(projectPreferences);
        this.applicationPreferences = requireNonNull(applicationPreferences);
    }



    public BufferedImage renderLabel(Rectangle targetArea, double zoomFactor) {

        BufferedImage bufferedImage = ImageUtil.createImage(targetArea.width, targetArea.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();

        g2d.setRenderingHints(RENDERING_HINTS);

        final Font font = getFont(zoomFactor);

        final String label = getLabel();

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
//

        g2d.drawString(
                label,
                textX,
                textY
        );

        g2d.dispose();

        return bufferedImage;
    }

    private int getHorizontalPadding(Double zoomFactor) {
        return (int) Math.round(HORIZONTAL_PADDING * zoomFactor);
    }

    private int getVerticalPadding(double zoomFactor) {
        return (int) Math.round(VERTICAL_PADDING * zoomFactor);
    }

    private Font getFont(Double zoomFactor) {
        Font font = projectPreferences.getFontName().isEmpty() ? applicationPreferences.getFont() : projectPreferences.getFont();
        if (font == null) {
            font = UtilsFont.getStatusBarItemFont();
        }
        font = UtilsFont.setAttributes(font, TextAttribute.WEIGHT_ULTRABOLD, zoomFactor.floatValue()*font.getSize());

        return font;
    }

    private String getLabel() {
        String label = projectPreferences.getLabel().isEmpty() ? project.getName() : projectPreferences.getLabel();
        return label;
    }

    private JBColor getBackgroundColor() {
        return new JBColor(projectPreferences.getBackgroundColor(), projectPreferences.getBackgroundColor());
    }

    private JBColor getTextColor() {
        return new JBColor(projectPreferences.getTextColor(), projectPreferences.getTextColor());
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
