package com.apkfuns.swan.annotator;

import com.apkfuns.swan.model.ValueType;
import com.apkfuns.swan.utils.MessageUtil;
import com.apkfuns.swan.utils.SwanFileUtil;
import com.intellij.codeInsight.intention.impl.BaseIntentionAction;
import com.intellij.lang.ASTNode;
import com.intellij.lang.javascript.psi.JSFile;
import com.intellij.lang.javascript.psi.JSObjectLiteralExpression;
import com.intellij.lang.javascript.psi.impl.JSChangeUtil;
import com.intellij.lang.javascript.psi.impl.JSFunctionExpressionImpl;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

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
        JSObjectLiteralExpression expression = SwanFileUtil.getPageExpression(jsFile);
        if (expression == null) {
            MessageUtil.showTopic(project, "QuickFix failed", "解析JS文件异常", NotificationType.WARNING);
            return;
        }
        ASTNode newStatementNode = JSChangeUtil.createStatementFromTextWithContext(fixValue + ":function(e){},", expression.getContext());
        JSFunctionExpressionImpl expression1 = new JSFunctionExpressionImpl(newStatementNode);
        System.out.println(expression.getFirstProperty());
        expression.getLastChild().addAfter(expression1, expression.getLastChild());
//        switch (valueType) {
//            case FUNCTION:
//                JSElementFactory.createExpressionCodeFragment();
//                JSFunctionExpressionImpl  t = null;
//                expression.add(t);
//                JSPropertyImpl
//                break;
//            default:
//                break;
//        }
    }
}
