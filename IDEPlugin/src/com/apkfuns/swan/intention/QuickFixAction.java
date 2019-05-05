package com.apkfuns.swan.intention;

import com.apkfuns.swan.model.SwanAttribute;
import com.apkfuns.swan.model.ValueType;
import com.apkfuns.swan.utils.SwanFileUtil;
import com.intellij.codeInsight.intention.impl.BaseIntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

public class QuickFixAction extends BaseIntentionAction {

    @NotNull
    private final SwanAttribute swanAttribute;

    public QuickFixAction(@NotNull SwanAttribute swanAttribute) {
        this.swanAttribute = swanAttribute;
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
        String t = swanAttribute.getValueType() == ValueType.FUNCTION ? "function" : "variable";
        return "Create " + t + " '" + swanAttribute.getName() + "'";
    }

    @Override
    public void invoke(@NotNull Project project, Editor editor, PsiFile psiFile) throws IncorrectOperationException {
        switch (swanAttribute.getValueType()) {
            case FUNCTION:
                break;
            default:
                break;
        }
    }
}
