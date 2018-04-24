package com.muse.plugin

import com.muse.plugin.exten.AssembleExtension
import com.muse.plugin.util.StringUtil
import org.gradle.api.Plugin
import org.gradle.api.Project

public class AssemblePlugin implements Plugin<Project> {

    String compileModule = "app"

    @Override
    void apply(Project project) {
        project.logger.error("----------------Custom Plugin Success----------------")

        project.extensions.create("AssembleExt", AssembleExtension)
        String taskNames = project.gradle.startParameter.taskNames.toString()
        project.logger.error(">>>>>>tasksNames is " + taskNames)
        String module = project.path.replace(":", "")
        project.logger.error(">>>>>>current module is " + module)
        AssembleTask assembleTask = getTaskInfo(project.gradle.startParameter.taskNames);

        if (assembleTask.isAssemble) {
            fetchMainModuleName(project, assembleTask);
            project.logger.error(">>>>>>compile module is " + compileModule)
        }

        if (!project.hasProperty("isRunAlone")) {
            throw new RuntimeException("You should set isRunAlone in " + module + "'s gradle.properties")
        }

        boolean isRunAlone = Boolean.parseBoolean((project.properties.get("isRunAlone")))
        String mainModuleName = project.rootProject.property("mainModuleName")
        project.logger.error(">>>>>>main module is " + mainModuleName)
        if (isRunAlone && assembleTask.isAssemble) {
            // For the compile module and host app, the value of 'isRunAlone' is changed to true, and all other components are forced to be false.
            // It means that the component can't reference the host app.
            if (module.equals(compileModule) || module.equals(mainModuleName)) {
                isRunAlone = true;
            } else {
                isRunAlone = false;
            }
        }

        project.setProperty("isRunAlone", isRunAlone)

        //Add various component dependencies based on configuration and generate component loading code automatically.
        if (isRunAlone) {
            project.apply plugin: 'com.android.application'
            project.logger.error("-----apply plugin is " + 'com.android.application' + "-----")
            if (!module.equals(mainModuleName)) {
                project.android.sourceSets {
                    main {
                        manifest.srcFile 'src/main/runalone/AndroidManifest.xml'
                        java.srcDirs = ['src/main/java', 'src/main/runalone/java']
                        res.srcDirs = ['src/main/res', 'src/main/runalone/res']
                        assets.srcDirs = ['src/main/assets', 'src/main/runalone/assets']
                        jniLibs.srcDirs = ['src/main/jniLibs', 'src/main/runalone/jniLibs']
                    }
                }
            }

            if (assembleTask.isAssemble && module.equals(compileModule)) {
                compileComponents(assembleTask, project)
                project.android.registerTransform(new InjectCodeTransform(project))
            }
        } else {
            project.apply plugin: 'com.android.library'
            project.logger.error("-----apply plugin is " + 'com.android.library' + "-----")
        }


    }
/**
 * 根据当前的task，获取要运行的组件，规则如下：
 * assembleRelease ---app
 * app:assembleRelease :app:assembleRelease ---app
 * sharecomponent:assembleRelease :sharecomponent:assembleRelease ---sharecomponent
 * @param assembleTask
 */
    private void fetchMainModuleName(Project project, AssembleTask assembleTask) {
        if (!project.rootProject.hasProperty("mainModuleName")) {
            throw new RuntimeException("you should set compile module in rootProject's gradle.properties")
        }
        if (assembleTask.modules.size() > 0 && assembleTask.modules.get(0) != null
                && assembleTask.modules.get(0).trim().length() > 0
                && !assembleTask.modules.get(0).equals("all")) {
            compileModule = assembleTask.modules.get(0);
        } else {
            compileModule = project.rootProject.property("mainModuleName")
        }
        if (compileModule == null || compileModule.trim().length() <= 0) {
            compileModule = "app"
        }
    }

    private AssembleTask getTaskInfo(List<String> taskNames) {
        AssembleTask assembleTask = new AssembleTask()
        for (String task : taskNames) {
            System.out.println("++++++ task name:" + task)
            if (task.toUpperCase().contains("ASSEMBLE") || task.contains("aR") || task.toUpperCase().contains("INSTALL") || task.toUpperCase().contains("RESGUARD")) {
                if (task.toUpperCase().contains("DEBUG")) {
                    assembleTask.isDebug = true
                }
                assembleTask.isAssemble = true
                String[] strs = task.split(":")
                assembleTask.modules.add(strs.length > 1 ? strs[strs.length - 2] : "all")
                break;
            }
        }
        return assembleTask
    }
/**
 * 自动添加依赖，只在运行assemble任务的才会添加依赖，因此在开发期间组件之间是完全感知不到的，这是做到完全隔离的关键
 * 支持两种语法：module或者modulePackage:module,前者之间引用module工程，后者使用componentrelease中已经发布的aar
 * @param assembleTask
 * @param project
 */
    private void compileComponents(AssembleTask assembleTask, Project project) {
        String components;
        if (assembleTask.isDebug) {
            components = (String) project.properties.get("debugComponent")
        } else {
            components = (String) project.properties.get("compileComponent")
        }

        if (components == null || components.length() == 0) {
            return;
        }
        String[] compileComponents = components.split(",")
        if (compileComponents == null || compileComponents.length == 0) {
            return;
        }
        for (String str : compileComponents) {

            project.logger.error(">>>>>>compile components: " + str)
            if (str.trim().startsWith(":")) {
                str = str.substring(1)
            }

            if (StringUtil.isMavenArtifact(str)) {
                project.dependencies.add("compile", str)
                project.logger.error("add dependencies lib: " + str)
            } else {
                project.dependencies.add("compile", project.project(':' + str))
                project.logger.error("add dependencies project: " + str)

            }
        }
    }

    private class AssembleTask {
        boolean isAssemble = false;
        boolean isDebug = false;
        List<String> modules = new ArrayList<>();
    }

}