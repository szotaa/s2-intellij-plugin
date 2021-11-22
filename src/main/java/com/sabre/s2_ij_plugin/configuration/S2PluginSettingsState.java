package com.sabre.s2_ij_plugin.configuration;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@State(
        name = "com.sabre.s2_ij_plugin.configuration.S2PluginSettingState",
        storages = @Storage("S2PluginSettings.xml")
)
public class S2PluginSettingsState implements PersistentStateComponent<S2PluginSettingsState> {

    public String dockerHostUrl = "tcp://127.0.0.1:2376";

    public static S2PluginSettingsState getInstance() {
        return ApplicationManager.getApplication().getService(S2PluginSettingsState.class);
    }

    @Nullable
    @Override
    public S2PluginSettingsState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull S2PluginSettingsState state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}
