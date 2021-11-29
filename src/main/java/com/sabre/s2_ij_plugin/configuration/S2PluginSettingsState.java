package com.sabre.s2_ij_plugin.configuration;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

/**
 * <p>This class stores configurable properties for S2-IJ-Plugin. All properties are persisted between IDE restarts.</p>
 *
 * <br>
 * <p>Accessing properties:</p>
 *
 *
 * <p>Use S2PluginSettingsState.getInstance() static method, access its public fields</p>
 *
 *
 * <br>
 * <p>Extending configuration with new properties:</p>
 * <ol>
 *      <li>
 *          Add new public field in S2PluginSettingsState class.
 *          Assigning value in class will only serve as property default since all
 *          variables are later loaded from local .xml file via S2PluginSettingsState.loadState method
 *      </li>
 *      <li>
 *        Add new Swing component in S2PluginSettingsComponent and bind it to myMainPanel in constructor.
 *        Remember to add necessary accessors which will update/read value inside Swing components
 *      </li>
 *      <li>
 *          Add your new properties in methods isModified, apply, reset from S2PluginSettingsConfigurable class,
 *          it should be self explanatory if you look into its source
 *      </li>
 * </ol>
 *
 *
 */

@State(
        name = "com.sabre.s2_ij_plugin.configuration.S2PluginSettingState",
        storages = @Storage("S2PluginSettings.xml")
)
public class S2PluginSettingsState implements PersistentStateComponent<S2PluginSettingsState> {

    public String dockerHostUrl = "unix:///var/run/docker.sock";
    public String s2ContainerName = "s2:latest";

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
