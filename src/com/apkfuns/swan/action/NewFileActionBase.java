package com.apkfuns.swan.action;

import com.apkfuns.swan.utils.SwanIcon;
import com.intellij.CommonBundle;
import com.intellij.ide.actions.CreateElementActionBase;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * 创建新文件 action
 */
public abstract class NewFileActionBase extends CreateElementActionBase {

    public NewFileActionBase(String text, String description, Icon icon) {
        super(text, description, icon);
    }

    @NotNull
    @Override
    protected PsiElement[] invokeDialog(Project project, PsiDirectory psiDirectory) {
        CreateElementActionBase.MyInputValidator inputValidator = new CreateElementActionBase.MyInputValidator(project, psiDirectory);
        Messages.showInputDialog(project, getDialogPrompt(), getDialogTitle(), SwanIcon.ICON, "", inputValidator);
        return inputValidator.getCreatedElements();
    }

    @NotNull
    @Override
    protected PsiElement[] create(@NotNull String s, PsiDirectory psiDirectory) throws Exception {
        return doCreate(psiDirectory.getProject(), s, psiDirectory);
    }

    @Override
    protected String getCommandName() {
        return getClass().getSimpleName();
    }

    @Override
    protected String getActionName(PsiDirectory psiDirectory, String s) {
        return getClass().getSimpleName();
    }

    @Override
    protected String getErrorTitle() {
        return CommonBundle.getErrorTitle();
    }

    /**
     * 创建文件
     *
     * @param project           project
     * @param fileName          文件名
     * @param paramPsiDirectory 所在文件夹
     * @return PsiElement[]
     */
    protected abstract PsiElement[] doCreate(Project project, String fileName, PsiDirectory paramPsiDirectory);

    /**
     * 弹窗提示的描述文本
     */
    protected abstract String getDialogPrompt();

    /**
     * 弹窗提示的标题
     */
    protected abstract String getDialogTitle();
}
