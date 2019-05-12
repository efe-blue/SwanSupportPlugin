package com.apkfuns.swan.action;

import com.apkfuns.swan.utils.SwanBundle;
import com.apkfuns.swan.utils.SwanFileUtil;
import com.apkfuns.swan.utils.SwanIcon;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜单创建 Page
 */
public class NewSwanPageAction extends NewFileActionBase {

    public NewSwanPageAction() {
        super(SwanBundle.message("newPage.dialog.name"),
                SwanBundle.message("newPage.dialog.prompt"), SwanIcon.ICON);
    }

    public NewSwanPageAction(String text, String description, Icon icon) {
        super(text, description, icon);
    }

    @Override
    protected PsiElement[] doCreate(Project project, String fileName, PsiDirectory paramPsiDirectory) {
        String[] templateArray = templateFile();
        List<PsiElement> psiElements = new ArrayList<>();
        for (String template : templateArray) {
            InputStream is = NewSwanPageAction.class.getResourceAsStream("/pageTemplates/" + template + ".ft");
            try {
                CharSequence ext = FileUtil.getExtension(template, "swan");
                String text = FileUtil.loadTextAndClose(is);
                PsiFileFactory factory = PsiFileFactory.getInstance(project);
                PsiFile psiFile = factory.createFileFromText(fileName
                        + "." + ext, SwanFileUtil.getFileType(template), text);
                psiElements.add(paramPsiDirectory.add(psiFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return psiElements.toArray(new PsiElement[0]);
    }

    @Override
    protected String getDialogPrompt() {
        return SwanBundle.message("newPage.dialog.prompt");
    }

    @Override
    protected String getDialogTitle() {
        return SwanBundle.message("newPage.dialog.title");
    }

    /**
     * 需要创建的模板文件
     */
    protected String[] templateFile() {
        return new String[]{"page.js", "page.css", "page.json", "page.swan"};
    }
}
