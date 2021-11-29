# s2-intellij-plugin

IntelliJ plugin enabling interaction with sabre2 tool via IDE  

## prerequisites

- JDK 11
- Docker
- IntelliJ IDE
- S2 tool container up and running
- S2 tool container volume must reflect your local project dir as s2 commands from IJ are executed in corresponding directory
- Gradle (optional, you can use gradlew wrapper or IntelliJ embedded gradle)

## running plugin in development mode

run gradle runIde task either via executing `gradle runIde` command or 
select `Gradle -> intellij -> runIde` inside your Intellij instance 


## features

- subset of s2 commands added under mouse right click menu in current file view and project tree view
- ability to configure plugin variables via IJ Settings -> Tools -> Sabre2 Plugin Configuration
- communication with s2 tool deployed in local docker container via docker socket

## other hints

### getting started

- install stuff from prerequisites section
- import this repository into your IntelliJ
- read [IJ plugin actions](https://plugins.jetbrains.com/docs/intellij/plugin-actions.html)
- read [IJ plugin extensions](https://plugins.jetbrains.com/docs/intellij/plugin-extensions.html)
- see `s2_ij_plugin.action.BaseCliCommandWithOutputAction` class javadoc
- see `src/main/resources/META-INF/plugin.xml` to understand how to add next actions
- see `s2_ij_plugin.configuration.S2PluginSettingsState` class javadoc to understand how to add your own configurable variables
- see `s2_ij_plugin.docker.DockerAdapter` class javadoc to understand how was communication with docker container implemented
 

### useful links

- [IntelliJ Plugin SDK samples](https://github.com/JetBrains/intellij-sdk-code-samples)
- [Official IntelliJ plugin development documentation](https://plugins.jetbrains.com/docs/intellij/getting-started.html)
- [IntelliJ plugin development tutorial part 1](https://developerlife.com/2020/11/21/idea-plugin-example-intro/)
- [IntelliJ plugin development tutorial part 2](https://developerlife.com/2021/03/13/ij-idea-plugin-advanced/)
- [Make your IJ plugin configurable via IJ settings (used in s2_ij_plugin.configuration.*)](https://plugins.jetbrains.com/docs/intellij/settings-guide.html)
- [docker client java (used in s2_ij_plugin.docker.*)](https://github.com/docker-java/docker-java)
- [tool window sample (used to print s2 command result inside IDE tab)](https://github.com/JetBrains/intellij-sdk-code-samples/tree/main/tool_window)