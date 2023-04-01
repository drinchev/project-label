package com.drinchev.projectlabel.resources.ui;

import com.drinchev.projectlabel.preferences.ApplicationPreferences;
import com.drinchev.projectlabel.preferences.ProjectPreferences;
import com.drinchev.projectlabel.utils.UtilsFont;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.impl.IdeBackgroundUtil;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.ImageUtil;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;

public class ProjectLabelBackgroundImage {

    private final static Logger LOG = Logger.getInstance(ProjectLabelStatusBarWidget.class);


    private static final RenderingHints HINTS = new RenderingHints(
            ofEntries(
                    entry(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY),
                    entry(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON),
                    entry(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY),
                    entry(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON),
                    entry(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON)
            )
    );
    private static Dimension textDimension;
    private static Font font;
    private static String label;
    private static BufferedImage bufferedImage;
    private static JBColor backgroundColor;
    private static JBColor textColor;

    private static String resultingImage;

    public static void showImage(@NotNull Project project) throws IOException {
        ProjectPreferences projectPreferences = ProjectPreferences.getInstance(project);
        ApplicationPreferences applicationPreferences = ApplicationPreferences.getInstance();

        PropertiesComponent prop = PropertiesComponent.getInstance();
        String image = createImage(project, projectPreferences, applicationPreferences);
        String opacity = String.valueOf(10); // config    OpacitySettingState.loadState());
        String imageProp = String.format("%s,%s", image, opacity);
        prop.setValue(IdeBackgroundUtil.EDITOR_PROP, imageProp);
        prop.setValue(IdeBackgroundUtil.FRAME_PROP, imageProp);

//        NotificationCenter.notice("Background switched successfully");
    }


    private static String createImage(@NotNull Project project, ProjectPreferences projectPreferences, ApplicationPreferences applicationPreferences) throws IOException {


//        return "/Users/simschla/Downloads/ilya-pavlov-OqtafYT5kTw-unsplash.jpg";


        if (bufferedImage == null) {
            font = projectPreferences.getFontName().isEmpty() ? applicationPreferences.getFont() : projectPreferences.getFont();
            if (font == null) {
                font = UtilsFont.getStatusBarItemFont();
            }
            font = UtilsFont.setAttributes(font, TextAttribute.WEIGHT_ULTRABOLD, 5*font.getSize()*1.0f);


            label = projectPreferences.getLabel().isEmpty() ? project.getName() : projectPreferences.getLabel();
            backgroundColor = new JBColor(projectPreferences.getBackgroundColor(), projectPreferences.getBackgroundColor());
            textColor = new JBColor(projectPreferences.getTextColor(), projectPreferences.getTextColor());

            JButton button = new JButton(label);
            button.setFont(font);
            button.setForeground(textColor);
            button.setBackground(backgroundColor);

            bufferedImage = createImageFromButton(button);

            BufferedImage drawImage = new BufferedImage(1024, 768, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = drawImage.createGraphics();
            g2.drawImage(bufferedImage, 0, 0, null);//drawImage.getWidth()/2-bufferedImage.getWidth()/2, drawImage.getHeight()- bufferedImage.getHeight()/2, null);
            g2.dispose();
            Path filePath = Files.createTempFile("project-label", ".png");
            filePath.toFile().deleteOnExit();
            ImageIO.write(drawImage, "png", filePath.toFile());
            resultingImage = filePath.toFile().getAbsolutePath();
            LOG.warn("resulting image path: " + resultingImage);
        }
        return resultingImage;


    }

    private static Dimension getTextDimensions() {
        if (textDimension == null) {
            FontRenderContext renderContext = new FontRenderContext(font.getTransform(), true, true);

            textDimension = new Dimension(
                    (int) (font.getStringBounds(label, renderContext).getWidth()),
                    (int) (font.getStringBounds(label, renderContext).getHeight())
            );
        }

        return textDimension;
    }

    public static BufferedImage createImageFromButton(JButton button) {
        // Create a JPanel and add the button to it
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
//        panel.setBorder(new LineBorder(Color.white, 2, true));
//        panel.setForeground(Color.white);
        panel.add(button, BorderLayout.CENTER);

        // Make sure the panel has a non-zero size
        panel.setSize(1024, 768);

        // Force the panel to lay out its children
        panel.doLayout();

        // Create a BufferedImage and get its Graphics2D object
        BufferedImage image = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        // Paint the panel onto the Graphics2D object
        panel.print(g2d);

        // Dispose of the Graphics2D object
        g2d.dispose();

        // Return the resulting image
        return image;
    }
}
