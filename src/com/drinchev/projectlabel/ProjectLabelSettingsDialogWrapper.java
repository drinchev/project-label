package com.drinchev.projectlabel;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBLabel;
import com.intellij.uiDesigner.core.AbstractLayout;
import com.intellij.util.ui.GridBag;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;

import javax.swing.*;
import java.awt.*;

public class ProjectLabelSettingsDialogWrapper extends DialogWrapper {

    private JPanel panel = new JPanel(new GridBagLayout());
    private JTextField txtLabel = new JTextField();
    private JTextField txtBackgroundColor = new JTextField();
    private JTextField txtForegroundColor = new JTextField();
    private Project project;

    ProjectLabelSettingsDialogWrapper(Project project) {
        super(project, true); // use current window as parent
        init();

        this.project = project;

        setTitle("Project Label Settings");
        ProjectLabelSettings state = ProjectLabelSettings.getInstance(this.project).getState();
        if(state != null) {
            txtBackgroundColor.setText(state.backgroundColor);
            txtForegroundColor.setText(state.foregroundColor);
            txtLabel.setText(state.label);
        } else {
            txtBackgroundColor.setText("red");
            txtForegroundColor.setText("blue");
            txtLabel.setText(null);
        }
    }

    @Override
    public JComponent createCenterPanel() {
        GridBag gb = new GridBag();
        gb.setDefaultInsets(JBUI.insets(0, 0, AbstractLayout.DEFAULT_VGAP, AbstractLayout.DEFAULT_HGAP));
        gb.setDefaultFill(GridBagConstraints.HORIZONTAL);

        panel.add(label("Background"), gb.nextLine().next().weightx(0.2));
        panel.add(txtBackgroundColor, gb.next().weightx(0.8));
        panel.add(label("Foreground"), gb.nextLine().next().weightx(0.2));
        panel.add(txtForegroundColor, gb.next().weightx(0.8));
        panel.add(label("Label"), gb.nextLine().next().weightx(0.2));
        panel.add(txtLabel, gb.next().weightx(0.8));

        return panel;
    }

    @Override
    protected void doOKAction() {
        String txtBg = txtBackgroundColor.getText();
        String txtFg = txtForegroundColor.getText();
        String txtLbl = txtLabel.getText();

        ProjectLabelSettings state = ProjectLabelSettings.getInstance(this.project).getState();
        assert state != null;
        state.backgroundColor = txtBg;
        state.foregroundColor = txtFg;
        state.label = txtLbl;

        ProjectLabelProjectComponent prC = project.getComponent(ProjectLabelProjectComponent.class);
        prC.onSettingsChanged();

        super.doOKAction();
    }

    private JComponent label(String text) {
        JBLabel label = new JBLabel(text);
        label.setComponentStyle(UIUtil.ComponentStyle.SMALL);
        label.setFontColor(UIUtil.FontColor.BRIGHTER);
        label.setBorder(JBUI.Borders.empty(0, 5, 2, 0));
        return label;
    }
}
