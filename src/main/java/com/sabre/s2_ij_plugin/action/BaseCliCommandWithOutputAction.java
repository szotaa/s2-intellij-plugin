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

/**
 * <p>Base class for command executed inside local s2 docker container with output printed in IJ S2 Tool Window</p>
 * <p>To add new action to the s2 dropdown menu:</p>
 * <ol>
 *     <li>
 *         extend this class, pass your custom command via <b>String[] commands</b> constructor property
 *     </li>
 *     <li>
 *         register new action in main/resources/META-INF/plugin.xml
 *     </li>
 * </ol>
 */

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

    /**
     * <p>if you execute s2 action on {project-dir}/src/main/com/sabre/Test.java in project tree,
     * this method will return {project-dir}/src/main/com/sabre/ as a working dir</p>
     *
     * @return command working directory
     */

    private String getWorkingDirectory(@NotNull AnActionEvent action) {
        return CommonDataKeys.VIRTUAL_FILE.getData(action.getDataContext()).getParent().getPath();
    }

    /**
     * @return IJ S2 Tool Window which is a container for console views showing command output
     */

    private ToolWindow getS2ToolWindow(@NotNull Project project) {
        return ToolWindowManager.getInstance(project).getToolWindow(S2_PLUGIN_TOOL_WINDOW_ID);
    }

    /**
     * @return console window in IJ S2 Tool Window wrapper
     */

    private ConsoleView getConsoleView(@NotNull Project project) {
        return TextConsoleBuilderFactory.getInstance().createBuilder(project).getConsole();
    }

    /**
     * binds console view to tool window, sets console view as tool window tab with tab header set to command variable
     */

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

    /**
     * executes command inside docker container, makes sure output is printed to bind console view
     */

    private void executeInDockerContainerWithOutputInConsole(
            @NotNull ConsoleView console,
            @NotNull String workingDirectory,
            @NotNull ToolWindow toolWindow
    ) {
        toolWindow.show();
        dockerAdapter.executeInDockerContainer(console, commands, workingDirectory);
    }
}
