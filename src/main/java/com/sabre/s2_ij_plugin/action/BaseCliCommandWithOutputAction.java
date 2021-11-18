package com.sabre.s2_ij_plugin.action;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
abstract class BaseCliCommandWithOutputAction extends AnAction {

    private static final String S2_PLUGIN_TOOL_WINDOW_ID = "Sabre2";
    protected final String[] commands;

    @Override
    public final void actionPerformed(@NotNull AnActionEvent action) {
        var project = action.getProject();
        if(project == null) {
            return;
        }
        var s2PluginToolWindow = getS2ToolWindow(project);
        var workingDirectory = getWorkingDirectory(action);
        var command = buildGeneralCommandLine(workingDirectory);
        var consoleView = getConsoleView(project);
        bindConsoleViewToToolWindow(consoleView, s2PluginToolWindow, command.getCommandLineString());
        executeCommandWithOutputInConsole(consoleView, s2PluginToolWindow, command);
    }

    private String getWorkingDirectory(@NotNull AnActionEvent action) {
        return CommonDataKeys.VIRTUAL_FILE.getData(action.getDataContext()).getParent().getPath();
    }

    private ToolWindow getS2ToolWindow(@NotNull Project project) {
        return ToolWindowManager.getInstance(project).getToolWindow(S2_PLUGIN_TOOL_WINDOW_ID);
    }

    private GeneralCommandLine buildGeneralCommandLine(@NotNull String workingDirectory) {
        var command = new GeneralCommandLine(commands);
        command.setCharset(StandardCharsets.UTF_8);
        command.setWorkDirectory(workingDirectory);
        return command;
    }

    private ConsoleView getConsoleView(@NotNull Project project) {
        return TextConsoleBuilderFactory.getInstance().createBuilder(project).getConsole();
    }

    private void bindConsoleViewToToolWindow(
            @NotNull ConsoleView console,
            @NotNull ToolWindow toolWindow,
            @NotNull String header
    ) {
        var content = toolWindow
                .getContentManager()
                .getFactory()
                .createContent(console.getComponent(), header, false);

        toolWindow.getContentManager().addContent(content);
        toolWindow.getContentManager().setSelectedContent(content);
    }

    private void executeCommandWithOutputInConsole(
            @NotNull ConsoleView console,
            @NotNull ToolWindow toolWindow,
            @NotNull GeneralCommandLine command
    ) {
        try {
            var osProcessHandler = new OSProcessHandler(command);
            toolWindow.show();
            console.attachToProcess(osProcessHandler);
            osProcessHandler.startNotify();
            log.info("executed: {}", command);
        } catch (ExecutionException e) {
            log.error("failed to execute {}: {}", command, e);
        }
    }
}
