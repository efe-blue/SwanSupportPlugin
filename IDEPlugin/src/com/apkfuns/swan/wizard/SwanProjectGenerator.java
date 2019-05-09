package com.apkfuns.swan.wizard;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.apkfuns.swan.model.ProjectConfigurationData;
import com.apkfuns.swan.utils.SwanBundle;
import com.apkfuns.swan.utils.SwanIcon;
import com.intellij.ide.util.projectWizard.WebProjectTemplate;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.platform.ProjectGeneratorPeer;
import com.intellij.util.io.ZipUtil;
import org.jetbrains.annotations.NotNull;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class SwanProjectGenerator extends WebProjectTemplate<ProjectConfigurationData> {
    @Override
    public String getDescription() {
        return SwanBundle.message("project.create.desc");
    }

    @NotNull
    @Override
    public String getName() {
        return SwanBundle.message("project.create.title");
    }

    @Override
    public Icon getIcon() {
        return SwanIcon.ICON;
    }

    @Override
    public void generateProject(@NotNull Project project, @NotNull VirtualFile virtualFile,
                                @NotNull ProjectConfigurationData data, @NotNull Module module) {
        ProgressManager.getInstance().run(new Task.Backgroundable(project, "Initializing") {
            @Override
            public void run(@NotNull ProgressIndicator progressIndicator) {
                progressIndicator.setIndeterminate(true);
                copyTemp2Project(data, project.getBasePath());
                notifyFinished();
            }

            @Override
            public void onSuccess() {
                super.onSuccess();
                ApplicationManager.getApplication().runWriteAction(() -> {
                    VirtualFileManager.getInstance().refreshWithoutFileWatcher(false);
                });
            }
        });
    }

    /**
     * 拷贝示例文件到项目文件夹
     *
     * @param data     项目初始化配置参数
     * @param descPath 新建项目目录
     */
    private void copyTemp2Project(@NotNull ProjectConfigurationData data, String descPath) {
        try {
            String fileName = data.getWizardType() == ProjectConfigurationData.ProjectWizardType.OFFICIAL ?
                    "/assets/sample.zip" : "/assets/preset.zip";
            InputStream inputStream = SwanProjectGenerator.class.getResourceAsStream(fileName);
            File swanPreset = Files.createTempFile("swan_preset", ".zip").toFile();
            FileUtils.copyInputStreamToFile(inputStream, swanPreset);
            inputStream.close();
            ZipUtil.extract(swanPreset, new File(descPath), null, true);
            FileUtil.asyncDelete(swanPreset);
            replaceSwanJsonFile(data, descPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 替换 project.swan.json
     * @param data  ProjectConfigurationData
     * @param descPath 新建项目目录
     * @throws IOException
     */
    private void replaceSwanJsonFile(@NotNull ProjectConfigurationData data, String descPath) throws IOException {
        InputStream is = SwanProjectGenerator.class.getResourceAsStream("/pageTemplates/project.swan.json.ft");
        String templateContent = FileUtil.loadTextAndClose(is);
        File projectSwanFile = new File(descPath, "project.swan.json");
        JSONObject projectSwanJson = JSON.parseObject(templateContent);
        projectSwanJson.put("appid", data.getAppId());
        FileUtil.writeToFile(projectSwanFile, projectSwanJson.toJSONString(), false);
    }

    @Override
    @NotNull
    public ProjectGeneratorPeer<ProjectConfigurationData> createPeer() {
        return new SwanGeneratorPeer();
    }
}
