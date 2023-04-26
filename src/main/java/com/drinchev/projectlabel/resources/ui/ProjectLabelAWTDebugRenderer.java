package com.drinchev.projectlabel.resources.ui;

import java.awt.*;

public class ProjectLabelAWTDebugRenderer {

    public static final boolean DEBUG = false;

    private ProjectLabelAWTDebugRenderer() {}

    public static void drawDebugRect(Graphics2D g2d, int x, int y, int width, int height, Color color) {
        if (!DEBUG) {
            return;
        }
        g2d.setColor(color);
        g2d.drawRect(x, y, width, height);
    }
}
