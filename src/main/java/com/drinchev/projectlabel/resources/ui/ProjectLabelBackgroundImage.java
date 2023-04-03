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
        createImage();
        setImageToIDE();
    }

    private void setImageToIDE() {
        PropertiesComponent prop = projectLevelPropertiesComponent();
        String opacity = String.valueOf(15); // config    OpacitySettingState.loadState());
        String imageProp = String.format("%s,%s", resultingImage, opacity);
        LOG.warn("EDITOR_PROP: " + prop.getValue(IdeBackgroundUtil.EDITOR_PROP));
        LOG.warn("FRAME_PROP: " + prop.getValue(IdeBackgroundUtil.FRAME_PROP));
        prop.setValue(IdeBackgroundUtil.EDITOR_PROP, imageProp+",plain,top_right"); // statt plain: tile (wiederholen), scale (zoomen)
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
        LOG.info("Hiding project label background image.");
        PropertiesComponent prop = projectLevelPropertiesComponent();
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
