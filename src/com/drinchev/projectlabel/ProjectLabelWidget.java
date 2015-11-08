package com.drinchev.projectlabel;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.CustomStatusBarWidget;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.ui.Gray;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.text.AttributedString;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProjectLabelWidget extends JButton implements CustomStatusBarWidget {

    /**
     * Configuration
     */
    private static final int PADDING = 18;
    private static final int HEIGHT = 12;

    @NonNls
    public static final String WIDGET_ID = "NameLabelWidget";

    static RenderingHints HINTS;

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

    public ProjectLabelWidget(final Project project, final ProjectLabelSettings settings) {

        addActionListener(e -> setBackgroundColor());

        myProjectName = project.getName().toUpperCase();
        mySettings = settings;
        myBackgroundColor = new JBColor(
                Color.decode(mySettings.backgroundColor),
                Color.decode(mySettings.backgroundColor)
        );

        setOpaque(false);
        setFocusable(false);
        setBorder(StatusBarWidget.WidgetBorder.INSTANCE);
        repaint();
        updateUI();

    }

    private void setBackgroundColor() {
        int r = myBackgroundColor.getRed();
        int g = myBackgroundColor.getGreen();
        int b = myBackgroundColor.getBlue();
        String colorStr = JOptionPane.showInputDialog(null, "Set project color", String.format("#%02x%02x%02x",r, g, b));
        try {
            myBackgroundColor = new JBColor(Color.decode(colorStr), Color.decode(colorStr));
            myBufferedImage = null;
            repaint();
            mySettings.backgroundColor = colorStr;
        } catch (Throwable e) {
            // Do nothing
        }
    }

    @Override
    public void dispose() {}

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
        return WIDGET_ID + myProjectName;
    }

    @Override
    public void updateUI() {
        super.updateUI();
        myBufferedImage = null;
        setFont(getWidgetFont());
    }

    private static Font getWidgetFont() {
        GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font[] fonts = e.getAllFonts();
        for (Font f : fonts) {
            if (Objects.equals(f.getFontName(), "Verdana")) {

                Map<TextAttribute, Object> attributes = new HashMap<>();

                attributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_ULTRABOLD);
                attributes.put(TextAttribute.SIZE, 8);

                return f.deriveFont( attributes );

            }
        }
        return JBUI.Fonts.label(11);
    }

    @Override
    public JComponent getComponent() {
        return this;
    }

    @Override
    public void paintComponent(final Graphics g) {

        if (myBufferedImage == null) {
            final Dimension size = getSize();
            final Dimension arcs = new Dimension(8, 8);

            myBufferedImage = UIUtil.createImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
            final Graphics2D g2 = (Graphics2D) myBufferedImage.getGraphics().create();
            final FontMetrics fontMetrics = g.getFontMetrics();

            g2.setRenderingHints(HINTS);

            final int nameWidth = fontMetrics.charsWidth(myProjectName.toCharArray(), 0, myProjectName.length());
            final int nameHeight = fontMetrics.getAscent();

            final AttributedString as = new AttributedString(myProjectName);

            as.addAttribute(TextAttribute.FAMILY, getFont().getFamily());
            as.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_ULTRABOLD);
            as.addAttribute(TextAttribute.SIZE, 8);

            // background
            g2.setColor(myBackgroundColor);
            g2.fillRoundRect(0, 0, size.width, HEIGHT, arcs.width, arcs.height);

            // label
            g2.setColor(new JBColor(Gray._255, Gray._255));
            g2.setFont(getFont());
            g2.drawString(as.getIterator(), (size.width - nameWidth) / 2, nameHeight + (size.height - nameHeight) / 2 - JBUI.scale(1));
            g2.dispose();

        }

        UIUtil.drawImage(g, myBufferedImage, 0, 0, null);

        if (UIUtil.isRetina() && !UIUtil.isUnderDarcula()) {
            Graphics2D g2 = (Graphics2D) g.create(0, 0, getWidth(), getHeight());
            g2.scale(0.5, 0.5);
            g2.setColor(Gray.x91);
            g2.drawLine(0, 0, 2 * getWidth(), 0);
            g2.scale(1, 1);
            g2.dispose();
        }

    }

    @Override
    public Dimension getPreferredSize() {
        int width = getFontMetrics(getWidgetFont()).charsWidth(myProjectName.toCharArray(), 0, myProjectName.length()) + PADDING + PADDING;
        int height = HEIGHT;
        return new Dimension(width, height);
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
