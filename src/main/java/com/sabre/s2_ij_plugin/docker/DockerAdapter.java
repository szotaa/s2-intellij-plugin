package com.sabre.s2_ij_plugin.docker;

import com.github.dockerjava.api.DockerClient;
import com.intellij.execution.ui.ConsoleView;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

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
