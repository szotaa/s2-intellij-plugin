package com.sabre.s2_ij_plugin.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.sabre.s2_ij_plugin.configuration.S2PluginSettingsState;

import java.util.Collections;

class DockerAdapterFactory {
    private static final S2PluginSettingsState config = S2PluginSettingsState.getInstance();

    static DockerAdapter build() {
        var dockerConfig = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost(config.dockerHostUrl)
                .build();
        var dockerClient = DockerClientBuilder.getInstance(dockerConfig).build();
        var s2ContainerId = findS2ContainerId(dockerClient, config.s2ContainerName);
        return new DockerAdapter(dockerClient, s2ContainerId);
    }

    private static String findS2ContainerId(DockerClient dockerClient, String containerName) {
        return dockerClient.listContainersCmd()
                .withStatusFilter(Collections.singleton("running"))
                .withNameFilter(Collections.singleton(containerName))
                .exec()
                .stream()
                .map(Container::getId)
                .findAny()
                .orElseThrow(() -> new IllegalStateException("s2 container not found"));
    }
}
