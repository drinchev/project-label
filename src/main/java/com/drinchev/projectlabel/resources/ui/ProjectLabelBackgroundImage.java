package com.drinchev.projectlabel.resources.ui;

import com.drinchev.projectlabel.preferences.ApplicationPreferences;
import com.drinchev.projectlabel.preferences.PreferencesReader;
import com.drinchev.projectlabel.preferences.ProjectPreferences;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.Service.Level;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.openapi.wm.impl.IdeBackgroundUtil;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.util.Objects.requireNonNull;


@Service(Level.PROJECT)
public class ProjectLabelBackgroundImage {

    public static final int MIN_HEIGHT = 30;

    public static final int DEFAULT_HEIGHT = 100;

    public static final double TARGET_HEIGHT_PERCENTAGE = 0.07;

    private final static Logger LOG = Logger.getInstance(ProjectLabelStatusBarWidget.class);

    private BufferedImage bufferedImage;

    private String resultingImage;

    private final Project project;

    private final PreferencesReader preferences;

    public ProjectLabelBackgroundImage(@NotNull Project project) {
        this.project = requireNonNull(project);
        this.preferences = new PreferencesReader(project, ProjectPreferences.getInstance(project), ApplicationPreferences.getInstance());

        var window = WindowManager.getInstance().getFrame(project);
        if (window == null) {
            LOG.error("Could not get window for project " + project.getName());
        } else {
            window.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    updateImage();
                }
            });
        }
    }

    public void showImage() {
        if (!shouldShowBackgroundImage()) {
            hideImage();
            return;
        }
        createImage();
        setImageToIDE();
    }

    private boolean shouldShowBackgroundImage() {
        return preferences.backgroundImagePosition() != BackgroundImagePosition.HIDDEN;
    }

    private void setImageToIDE() {
        PropertiesComponent prop = projectLevelPropertiesComponent();
        String opacity = String.valueOf(preferences.backgroundImageOpacity());
        String imageProp = String.format("%s,%s,plain,%s", resultingImage, opacity, preferences.backgroundImagePosition().name().toLowerCase());
        String prev_editor = prop.getValue(IdeBackgroundUtil.EDITOR_PROP);
        prop.setValue(IdeBackgroundUtil.EDITOR_PROP, imageProp);
        String prev_frame = prop.getValue(IdeBackgroundUtil.FRAME_PROP);
        prop.setValue(IdeBackgroundUtil.FRAME_PROP, imageProp);
        LOG.debug("Previous editor image: " + prev_editor);
        LOG.debug("Previous frame image: " + prev_frame);
    }

    private PropertiesComponent projectLevelPropertiesComponent() {
        PropertiesComponent prop = project.getService(PropertiesComponent.class);
        return prop;
    }

    public void updateImage() {
        LOG.info("Updating project label background image.");
        bufferedImage = null;
        resultingImage = null;
        showImage();
    }

    public void hideImage() {
        PropertiesComponent prop = projectLevelPropertiesComponent();
        if (!prop.getValue(IdeBackgroundUtil.EDITOR_PROP, "").contains("project-label") && !prop.getValue(IdeBackgroundUtil.FRAME_PROP, "").contains("project-label")) {
            LOG.info("Not touching background image since not a project label image.");
            return;
        }
        LOG.info("Hiding project label background image.");
        prop.setValue(IdeBackgroundUtil.EDITOR_PROP, null);
        prop.setValue(IdeBackgroundUtil.FRAME_PROP, null);
    }


    private void createImage() {
        if (bufferedImage == null) {
            try {
                ProjectLabelAWTRenderer renderer = new ProjectLabelAWTRenderer(preferences);
                Dimension preferredImageDimension = getPreferredImageDimension();
                bufferedImage = renderer.renderLabel(preferredImageDimension);

                Path filePath = Files.createTempFile("project-label", ".png");
                filePath.toFile().deleteOnExit();
                ImageIO.write(bufferedImage, "png", filePath.toFile());
                resultingImage = filePath.toFile().getAbsolutePath();
            } catch (IOException e) {
                LOG.error("Exception while creating project label background image", e);
            }
        }
    }

    private Dimension getPreferredImageDimension() {
        ProjectLabelAWTRenderer renderer = new ProjectLabelAWTRenderer(preferences);
        Dimension preferredImageRatio = renderer.getPreferredImageRatio();
        LOG.debug("Preferred image ratio: " + preferredImageRatio);

        final int targetHeight = targetHeight();
        final int targetWidth = (int) Math.round(targetHeight / preferredImageRatio.getHeight() * preferredImageRatio.getWidth());

        LOG.debug("Target image size: " + targetWidth + "x" + targetHeight);

        return new Dimension(targetWidth, targetHeight);
    }

    private int targetHeight() {
        final JFrame projectFrame = WindowManager.getInstance().getFrame(project);
        if (projectFrame == null) {
            LOG.warn("Could not get project frame for project " + project.getName() + ". Using default height.");
            return DEFAULT_HEIGHT;
        }
        LOG.info("Project frame size: " + projectFrame.getWidth() + "x" + projectFrame.getHeight());
        int partialHeight = (int) Math.round(projectFrame.getHeight() * TARGET_HEIGHT_PERCENTAGE);
        int resultingHeight = Math.max(partialHeight, MIN_HEIGHT);
        LOG.info("Background image height: " + resultingHeight);
        return resultingHeight;
    }
}
