package com.drinchev.projectlabel.resources.ui;

import com.drinchev.projectlabel.preferences.ApplicationPreferences;
import com.drinchev.projectlabel.preferences.ProjectPreferences;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.Service.Level;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.impl.IdeBackgroundUtil;
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
    private final ProjectPreferences projectPreferences;
    private final ApplicationPreferences applicationPreferences;

    private BufferedImage bufferedImage;

    private String resultingImage;

    private final Project project;

    public ProjectLabelBackgroundImage(@NotNull Project project) {
        this.project = requireNonNull(project);
        projectPreferences = ProjectPreferences.getInstance(project);
        applicationPreferences = ApplicationPreferences.getInstance();

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
        return projectPreferences.getBackgroundImagePosition() != BackgroundImagePosition.HIDDEN;
    }

    private void setImageToIDE() {
        PropertiesComponent prop = projectLevelPropertiesComponent();
        String opacity = String.valueOf(15); // config    OpacitySettingState.loadState());
        String imageProp = String.format("%s,%s,plain,%s", resultingImage, opacity, projectPreferences.getBackgroundImagePosition().name().toLowerCase());
        prop.setValue(IdeBackgroundUtil.EDITOR_PROP, imageProp); // statt plain: tile (wiederholen), scale (zoomen)
        prop.setValue(IdeBackgroundUtil.FRAME_PROP, imageProp);
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
                ProjectLabelAWTRenderer renderer = new ProjectLabelAWTRenderer(project, projectPreferences, applicationPreferences);
                bufferedImage = renderer.renderLabel(new Rectangle(0, 0, 1024, 768), 5);

                Path filePath = Files.createTempFile("project-label", ".png");
                filePath.toFile().deleteOnExit();
                ImageIO.write(bufferedImage, "png", filePath.toFile());
                resultingImage = filePath.toFile().getAbsolutePath();
                LOG.warn("resulting image path: " + resultingImage);
                Runtime.getRuntime().exec(new String[]{"open", resultingImage});
            } catch (IOException e) {
                LOG.error("Exception while creating project label background image", e);
            }
        }
    }

}
