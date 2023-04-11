package com.drinchev.projectlabel.resources.ui;

import com.drinchev.projectlabel.preferences.ApplicationPreferences;
import com.drinchev.projectlabel.preferences.PreferencesReader;
import com.drinchev.projectlabel.preferences.ProjectPreferences;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.Service.Level;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.impl.IdeBackgroundUtil;
import com.intellij.ui.scale.JBUIScale;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.util.Objects.requireNonNull;


@Service(Level.PROJECT)
public class ProjectLabelBackgroundImage {

    private final static Logger LOG = Logger.getInstance(ProjectLabelStatusBarWidget.class);

    private BufferedImage bufferedImage;

    private String resultingImage;

    private final Project project;

    private final PreferencesReader preferences;

    public ProjectLabelBackgroundImage(@NotNull Project project) {
        this.project = requireNonNull(project);
        this.preferences = new PreferencesReader(project, ProjectPreferences.getInstance(project), ApplicationPreferences.getInstance());
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
        LOG.warn("Previous editor image: " + prev_editor);
        LOG.warn("Previous frame image: " + prev_frame);
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
                Dimension preferredImageRation = renderer.getPreferredImageRatio();
                final int paddingX = JBUIScale.scale(100);
                final int paddingY = JBUIScale.scale(100);
                // we want to have a 800x600 image, but prefer it to have the same ratio as the project label, so we scale it accordingly
                final int targetWidth = 800;
                final int targetHeight = (int) Math.round(targetWidth / preferredImageRation.getWidth() * preferredImageRation.getHeight());
                bufferedImage = renderer.renderLabel(
                        new Dimension(targetWidth+2*paddingX, targetHeight+2*paddingY),
                        new Dimension(paddingX, paddingY));

                Path filePath = Files.createTempFile("project-label", ".png");
                filePath.toFile().deleteOnExit();
                ImageIO.write(bufferedImage, "png", filePath.toFile());
                resultingImage = filePath.toFile().getAbsolutePath();
            } catch (IOException e) {
                LOG.error("Exception while creating project label background image", e);
            }
        }
    }

}
