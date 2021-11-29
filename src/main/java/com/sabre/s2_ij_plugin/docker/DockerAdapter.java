package com.sabre.s2_ij_plugin.docker;

import com.github.dockerjava.api.DockerClient;
import com.intellij.execution.ui.ConsoleView;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

/**
 * <p>This class communicates plugin with docker container containing s2 tool</p>
 * <p>
 *     your local s2 docker container is resolved via its name configured in <b>S2PluginSettingsState.s2ContainerName</b>,
 *     see how its happening in DockerAdapterFactory.findS2ContainerId()
 * </p>
 * <p>Usage:</p>
 * <ol>
 *     <li>
 *         use DockerAdapterHolder.getInstance() to retrieve DockerAdapter instance
 *     </li>
 * </ol>
 */

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class DockerAdapter {
    private final DockerClient client;
    private final String s2ContainerId;

    @SneakyThrows
    public void executeInDockerContainer(ConsoleView consoleView, String[] commands, String workingDir) {
       var execResponse = client.execCreateCmd(s2ContainerId)
               .withAttachStdout(true)
               .withAttachStderr(true)
               .withCmd(commands)
               .withWorkingDir(workingDir)
               .exec();

       var resultCallback = new BlockingExecResultCallback(consoleView);
       client.execStartCmd(execResponse.getId()).exec(resultCallback);
       resultCallback.await();
    }
}
