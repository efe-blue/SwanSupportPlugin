package com.apkfuns.swan.annotator;

import com.apkfuns.swan.model.ValueType;
import com.apkfuns.swan.utils.MessageUtil;
import com.apkfuns.swan.utils.SwanFileUtil;
import com.intellij.codeInsight.intention.impl.BaseIntentionAction;
import com.intellij.lang.javascript.JSTokenTypes;
import com.intellij.lang.javascript.psi.JSFile;
import com.intellij.lang.javascript.psi.JSObjectLiteralExpression;
import com.intellij.lang.javascript.psi.impl.JSChangeUtil;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 通过 QuickFix 创建变量和方法
 */
public class QuickFixAction extends BaseIntentionAction {

    @NotNull
    private final String fixValue;
    @NotNull
    private final ValueType valueType;

    public QuickFixAction(@NotNull String fixValue, @NotNull ValueType valueType) {
        this.fixValue = fixValue;
        this.valueType = valueType;
    }

    @Nls(capitalization = Nls.Capitalization.Sentence)
    @Override
    public @NotNull String getFamilyName() {
        return "Swan";
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile psiFile) {
        return SwanFileUtil.isSwanFile(psiFile);
    }

    @Override
    public @NotNull String getText() {
        String t = valueType == ValueType.FUNCTION ? "function" : "variable";
        return "Swan create " + t + " '" + fixValue + "'";
    }

    @Override
    public void invoke(@NotNull Project project, Editor editor, PsiFile psiFile) throws IncorrectOperationException {
        JSFile jsFile = SwanFileUtil.getTargetJsFile(psiFile);
        if (jsFile == null || !jsFile.isValid()) {
            MessageUtil.showTopic(project, "QuickFix failed", "未找到对应的JS文件", NotificationType.WARNING);
            return;
        }
        JSObjectLiteralExpression pageExpression = SwanFileUtil.getPageExpression(jsFile);
        if (pageExpression == null) {
            MessageUtil.showTopic(project, "QuickFix failed", "解析JS文件异常", NotificationType.WARNING);
            return;
        }
        try {
            if (valueType == ValueType.FUNCTION) {
                PsiElement lastChild = pageExpression.getChildren()[pageExpression.getChildren().length - 1];
                PsiElement funElement = pageExpression.addAfter(JSChangeUtil.createStatementFromTextWithContext(fixValue + ":function(e){}", pageExpression).getPsi(), lastChild);
                pageExpression.getNode().addLeaf(JSTokenTypes.COMMA, ",", funElement.getNode());
                pageExpression.getNode().addLeaf(JSTokenTypes.WHITE_SPACE, "\n", funElement.getNode());
            } else {
                PsiElement dataProperty = getJsExpression(pageExpression.findProperty("data"));
                if (dataProperty != null) {
                    PsiElement varLastChild = dataProperty.getChildren()[dataProperty.getChildren().length - 1];
                    PsiElement varElement = dataProperty.addAfter(JSChangeUtil.createStatementFromTextWithContext(fixValue + ":null", dataProperty).getPsi(), varLastChild);
                    dataProperty.getNode().addLeaf(JSTokenTypes.COMMA, ",", varElement.getNode());
                    dataProperty.getNode().addLeaf(JSTokenTypes.WHITE_SPACE, "\n", varElement.getNode());
                }
            }
            PsiDocumentManager.getInstance(project).commitDocument(editor.getDocument());
            CodeStyleManager.getInstance(project).reformat(psiFile);
        } catch (Exception e) {
            MessageUtil.error(project, e.getMessage());
        }
    }

    /**
     * 获取JS对象的表达式
     */
    @Nullable
    private JSObjectLiteralExpression getJsExpression(@Nullable PsiElement node) {
        if (node == null) {
            return null;
        }
        for (PsiElement child : node.getChildren()) {
            if (child instanceof JSObjectLiteralExpression) {
                return (JSObjectLiteralExpression) child;
            }
        }
        return null;
    }
}
