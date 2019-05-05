package com.apkfuns.swan.model;

/**
 * 项目初始化配置参数
 */
public class ProjectConfigurationData {
    // 创建项目的 appID
    private String appId;
    // 创建项目的类型
    private ProjectWizardType wizardType = ProjectWizardType.EMPTY;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public ProjectWizardType getWizardType() {
        return wizardType;
    }

    public void setWizardType(ProjectWizardType wizardType) {
        this.wizardType = wizardType;
    }

    /**
     * 创建项目类型
     */
    public enum ProjectWizardType {
        EMPTY,  // 空项目
        OFFICIAL // 官方示例
    }


}
