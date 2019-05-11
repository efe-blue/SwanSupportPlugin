package com.apkfuns.swan.marker;

import com.apkfuns.swan.utils.SwanFileUtil;
import com.apkfuns.swan.utils.SwanIcon;
import com.intellij.codeInsight.daemon.GutterIconNavigationHandler;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.lang.javascript.psi.JSFile;
import com.intellij.lang.javascript.psi.JSObjectLiteralExpression;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.event.MouseEvent;

/**
 * js文件跳swan文件
 */
public class SwanJSLineMarkerProvider implements LineMarkerProvider, GutterIconNavigationHandler<PsiElement> {

    @Override
    public void navigate(MouseEvent mouseEvent, PsiElement psiElement) {
        VirtualFile swanFile = SwanFileUtil.getTargetSwanFile(psiElement);
        if (swanFile == null) {
            return;
        }
        OpenFileDescriptor descriptor = new OpenFileDescriptor(psiElement.getProject(),
                swanFile, 0, 0);
        if (descriptor.canNavigate()) {
            descriptor.navigate(true);
        }
    }

    @Override
    public @Nullable LineMarkerInfo getLineMarkerInfo(@NotNull PsiElement psiElement) {
        PsiFile psiFile = psiElement.getContainingFile();
        if (!(psiFile instanceof JSFile)) {
            return null;
        }
        JSObjectLiteralExpression page = SwanFileUtil.getPageExpression((JSFile) psiFile);
        if (psiElement == page) {
            VirtualFile swanFile = SwanFileUtil.getTargetSwanFile(psiElement);
            if (swanFile == null) {
                return null;
            }
            return new LineMarkerInfo<>(page, page.getTextRange(), SwanIcon.ICON, null, this,
                    GutterIconRenderer.Alignment.LEFT);
        }
        return null;
    }
}
