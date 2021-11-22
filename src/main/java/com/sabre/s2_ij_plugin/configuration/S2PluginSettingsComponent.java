package com.sabre.s2_ij_plugin.configuration;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class S2PluginSettingsComponent {
    private final JPanel myMainPanel;
    private final JBTextField dockerHostUrl = new JBTextField();


    public S2PluginSettingsComponent() {
        myMainPanel = FormBuilder.createFormBuilder()
                .addLabeledComponent(new JBLabel("Docker host: "), dockerHostUrl, 1, false)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
    }

    public JPanel getPanel() {
        return myMainPanel;
    }

    public JComponent getPreferredFocusedComponent() {
        return dockerHostUrl;
    }

    @NotNull
    public String getDockerHostUrl() {
        return dockerHostUrl.getText();
    }

    public void setDockerHostUrl(@NotNull String newText) {
        dockerHostUrl.setText(newText);
    }
}
