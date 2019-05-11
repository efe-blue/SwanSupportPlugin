package com.apkfuns.swan.intention;

import com.apkfuns.swan.tag.SwanTag;
import com.apkfuns.swan.utils.SwanBundle;
import com.apkfuns.swan.utils.SwanTagManager;
import com.apkfuns.swan.utils.SwanUtil;
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlToken;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

/**
 * 打开官方文档
 */
public class SwanDocumentIntention extends PsiElementBaseIntentionAction {

    @Override
    public @NotNull String getText() {
        return SwanBundle.message("swan.document");
    }

    @Override
    public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement psiElement) throws IncorrectOperationException {
        String tagName = psiElement.getText();
        SwanTag swanTag = SwanTagManager.getInstance().getTag(tagName);
        if (swanTag != null) {
            SwanUtil.openUrl(swanTag.getLink());
        }
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, @NotNull PsiElement psiElement) {
        if (psiElement instanceof XmlToken) {
            String tokenType = ((XmlToken) psiElement).getTokenType().toString();
            if ("XML_NAME".equals(tokenType)) {
                String tagName = psiElement.getText();
                return SwanTagManager.getInstance().getTag(tagName) != null;
            }
        }
        return false;
    }

    @Nls(capitalization = Nls.Capitalization.Sentence)
    @Override
    public @NotNull String getFamilyName() {
        return "Swan";
    }
}
