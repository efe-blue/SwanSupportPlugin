package com.apkfuns.swan.intention;

import com.apkfuns.swan.utils.SwanFileUtil;
import com.apkfuns.swan.utils.SwanIcon;
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.lang.javascript.psi.JSBlockStatement;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListPopup;
import com.intellij.openapi.ui.popup.PopupStep;
import com.intellij.openapi.ui.popup.util.BaseListPopupStep;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.css.CssDeclaration;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.psi.xml.XmlText;
import com.intellij.psi.xml.XmlToken;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

/**
 * 标签内变量智能提醒
 */
public class SwanTagTextIntention extends PsiElementBaseIntentionAction {
    @Override
    public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement element) throws IncorrectOperationException {
        Set<String> vars = SwanFileUtil.getDataVarNames(element);
        ListPopup listPopup = JBPopupFactory.getInstance().createListPopup(
                new BaseListPopupStep<String>(null, vars.toArray(new String[0])) {
                    @Override
                    public Icon getIconFor(String value) {
                        return SwanIcon.ICON;
                    }

                    @Override
                    public PopupStep onChosen(final String selectedValue, final boolean finalChoice) {
                        WriteCommandAction.runWriteCommandAction(project, () -> {
                            editor.getDocument().insertString(editor.getCaretModel().getOffset(), "{{" + selectedValue + "}}");
                            int start = editor.getSelectionModel().getSelectionStart();
                            int end = editor.getSelectionModel().getSelectionEnd();
                            editor.getDocument().replaceString(start, end, "");
                        });
                        return super.onChosen(selectedValue, finalChoice);
                    }
                });
        listPopup.setMinimumSize(new Dimension(240, -1));
        listPopup.showInBestPositionFor(editor);
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, @NotNull PsiElement element) {
        if (!SwanFileUtil.isSwanFile(element)) {
            return false;
        }
        int offset = editor.getCaretModel().getOffset();
        Document document = editor.getDocument();
        if (!element.isWritable() || element.getContext() == null || !element.getContext().isWritable()) {
            return false;
        }
        if (element instanceof XmlToken && ((XmlToken) element).getTokenType().toString().equals("XML_END_TAG_START")) {
            String next = document.getText(new TextRange(offset, offset + 1));
            if (next.equals("<")) {
                return true;
            }
        }
        return available(element);
    }

    private boolean available(PsiElement element) {
        PsiElement context = element.getContext();
        return context instanceof JSBlockStatement
                || context instanceof CssDeclaration
                || context instanceof XmlAttributeValue
                || context instanceof XmlText;
    }

    @Override
    public boolean startInWriteAction() {
        return true;
    }

    @Nls(capitalization = Nls.Capitalization.Sentence)
    @Override
    public @NotNull String getFamilyName() {
        return "Swan";
    }

    @Override
    public @NotNull String getText() {
        return "Insert swan variables";
    }

    @Override
    public boolean checkFile(@Nullable PsiFile file) {
        return file != null && file.getName().endsWith(".swan");
    }
}
