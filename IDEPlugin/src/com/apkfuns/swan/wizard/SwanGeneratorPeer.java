package com.apkfuns.swan.wizard;

import com.apkfuns.swan.model.ProjectConfigurationData;
import com.apkfuns.swan.utils.SwanUtil;
import com.apkfuns.swan.widget.ClickAbleLabel;
import com.intellij.ide.util.projectWizard.SettingsStep;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.platform.ProjectGeneratorPeer;
import com.intellij.platform.WebProjectGenerator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * 创建项目的UI管理
 */
public class SwanGeneratorPeer implements ProjectGeneratorPeer<ProjectConfigurationData>, DocumentListener,
        ItemListener {

    // appId 输入框
    private JTextField appIdText;
    // 空项目 Radio
    private final JRadioButton[] radioButtons;
    // 参数变动回调
    private SettingsListener settingsListener;

    public SwanGeneratorPeer() {
        radioButtons = new JRadioButton[]{
                new JRadioButton("空白模板"), new JRadioButton("官方示例")
        };
    }

    @NotNull
    @Override
    public JComponent getComponent() {
        return new JPanel();
    }

    @Override
    public void buildUI(@NotNull SettingsStep settingsStep) {
        // appID
        appIdText = new JTextField();
        appIdText.getDocument().addDocumentListener(this);
        settingsStep.addSettingsField("AppID", appIdText);
        settingsStep.addSettingsField("", createAppIDPrompt());
        // 项目模板
        JPanel radioPanel = new JPanel(new GridLayout(2, 1));
        ButtonGroup radioGroup = new ButtonGroup();
        for (JRadioButton radioButton : radioButtons) {
            radioGroup.add(radioButton);
            radioPanel.add(radioButton);
        }
        radioButtons[0].setSelected(true);
        radioButtons[0].addItemListener(this);
        settingsStep.addSettingsField("项目模板", radioPanel);
    }

    @NotNull
    @Override
    public ProjectConfigurationData getSettings() {
        return new ProjectConfigurationData();
    }

    @Nullable
    @Override
    public ValidationInfo validate() {
        if (SwanUtil.isEmpty(appIdText.getText())) {
            return new ValidationInfo("AppID 不能为空");
        }
        return null;
    }

    @Override
    public boolean isBackgroundJobRunning() {
        return false;
    }

    @Override
    public void addSettingsListener(@NotNull SettingsListener listener) {
        listener.stateChanged(true);
        this.settingsListener = listener;
    }

    @Override
    public void addSettingsStateListener(WebProjectGenerator.@NotNull SettingsStateListener settingsStateListener) {
    }

    /**
     * 创建 appID 提醒
     *
     * @return JComponent
     */
    private JComponent createAppIDPrompt() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new ClickAbleLabel("若无 AppID 可点击去"));
        panel.add(new ClickAbleLabel("注册", "https://smartprogram.baidu.com").setColor("#6d77b8"));
        panel.add(new ClickAbleLabel("或"));
        panel.add(new ClickAbleLabel("体验", new Runnable() {
            @Override
            public void run() {
                appIdText.setText("体验AppID");
            }
        }).setColor("#6d77b8"));
        panel.add(new ClickAbleLabel("智能小程序"));
        return panel;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        if (settingsListener != null) {
            settingsListener.stateChanged(true);
            getSettings().setAppId(appIdText.getText());
        }
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        if (settingsListener != null) {
            settingsListener.stateChanged(true);
            getSettings().setAppId(appIdText.getText());
        }
    }

    @Override
    public void changedUpdate(DocumentEvent e) {

    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        getSettings().setWizardType(radioButtons[0].isSelected() ?
                ProjectConfigurationData.ProjectWizardType.EMPTY : ProjectConfigurationData.ProjectWizardType.OFFICIAL);
    }
}
