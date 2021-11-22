package com.sabre.s2_ij_plugin.configuration;

import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;

import javax.annotation.Nullable;
import javax.swing.*;
import java.util.Objects;

public class S2PluginSettingsConfigurable implements Configurable {

    private S2PluginSettingsComponent s2PluginSettingsComponent;

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Sabre2 Plugin Configuration";
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return s2PluginSettingsComponent.getPreferredFocusedComponent();
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        s2PluginSettingsComponent = new S2PluginSettingsComponent();
        return s2PluginSettingsComponent.getPanel();
    }

    @Override
    public boolean isModified() {
        var settings = S2PluginSettingsState.getInstance();
        var modified = !s2PluginSettingsComponent.getDockerHostUrl().equals(settings.dockerHostUrl);
        modified |= !Objects.equals(s2PluginSettingsComponent.getS2ContainerName(), settings.s2ContainerName);
        return modified;
    }

    @Override
    public void apply() {
        var settings = S2PluginSettingsState.getInstance();
        settings.dockerHostUrl = s2PluginSettingsComponent.getDockerHostUrl();
        settings.s2ContainerName = s2PluginSettingsComponent.getS2ContainerName();
    }

    @Override
    public void reset() {
        var settings = S2PluginSettingsState.getInstance();
        s2PluginSettingsComponent.setDockerHostUrl(settings.dockerHostUrl);
        s2PluginSettingsComponent.setS2ContainerName(settings.s2ContainerName);
    }

    @Override
    public void disposeUIResources() {
        s2PluginSettingsComponent = null;
    }
}