package com.sabre.s2_ij_plugin.action;

import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.sabre.s2_ij_plugin.docker.DockerAdapter;
import com.sabre.s2_ij_plugin.docker.DockerAdapterHolder;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
abstract class BaseCliCommandWithOutputAction extends AnAction {

    private static final String S2_PLUGIN_TOOL_WINDOW_ID = "s2-plugin-tool-window";
    protected static final DockerAdapter dockerAdapter = DockerAdapterHolder.getInstance();
    protected final String[] commands;

    @Override
    public final void actionPerformed(@NotNull AnActionEvent action) {
        var project = action.getProject();
        if(project == null) {
            return;
        }
        var s2PluginToolWindow = getS2ToolWindow(project);
        var workingDirectory = getWorkingDirectory(action);
        var consoleView = getConsoleView(project);
        bindConsoleViewToToolWindow(consoleView, s2PluginToolWindow);
        executeInDockerContainerWithOutputInConsole(consoleView, workingDirectory, s2PluginToolWindow);
    }

    private String getWorkingDirectory(@NotNull AnActionEvent action) {
        return CommonDataKeys.VIRTUAL_FILE.getData(action.getDataContext()).getParent().getPath();
    }

    private ToolWindow getS2ToolWindow(@NotNull Project project) {
        return ToolWindowManager.getInstance(project).getToolWindow(S2_PLUGIN_TOOL_WINDOW_ID);
    }

    private ConsoleView getConsoleView(@NotNull Project project) {
        return TextConsoleBuilderFactory.getInstance().createBuilder(project).getConsole();
    }

    private void bindConsoleViewToToolWindow(
            @NotNull ConsoleView console,
            @NotNull ToolWindow toolWindow
    ) {
        var content = toolWindow
                .getContentManager()
                .getFactory()
                .createContent(console.getComponent(), String.join(" ", commands), false);

        toolWindow.getContentManager().addContent(content);
        toolWindow.getContentManager().setSelectedContent(content);
    }

    private void executeInDockerContainerWithOutputInConsole(
            @NotNull ConsoleView console,
            @NotNull String workingDirectory,
            @NotNull ToolWindow toolWindow
    ) {
        toolWindow.show();
        dockerAdapter.executeInDockerContainer(console, commands, workingDirectory);
    }
}
