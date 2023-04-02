package com.drinchev.projectlabel;

import com.drinchev.projectlabel.resources.ui.ProjectLabelStatusBarWidget;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.ProjectPostStartupActivity;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.drinchev.projectlabel.resources.ui.ProjectLabelBackgroundImage;

public class ProjectLabelStartupActivity implements ProjectPostStartupActivity {

    @Nullable
    @Override
    public Object execute(@NotNull Project project, @NotNull Continuation<? super Unit> continuation) {
        try {
            ProjectLabelBackgroundImage.showImage(project);
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}