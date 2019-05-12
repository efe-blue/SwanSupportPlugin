package com.apkfuns.swan.marker;

import com.apkfuns.swan.utils.SwanFileUtil;
import com.intellij.codeInsight.daemon.GutterIconNavigationHandler;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.icons.AllIcons;
import com.intellij.lang.javascript.psi.JSFile;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlDocument;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.event.MouseEvent;

/**
 * swan 文件跳 js文件
 */
public class SwanFileLineMarkerProvider implements LineMarkerProvider, GutterIconNavigationHandler<PsiElement> {

    @Override
    public void navigate(MouseEvent mouseEvent, PsiElement psiElement) {
        JSFile jsFile = SwanFileUtil.getTargetJsFile(psiElement);
        if (jsFile == null) {
            return;
        }
        OpenFileDescriptor descriptor = new OpenFileDescriptor(psiElement.getProject(),
                jsFile.getVirtualFile(), 0, 0);
        if (descriptor.canNavigate()) {
            descriptor.navigate(true);
        }
    }

    @Override
    public @Nullable LineMarkerInfo getLineMarkerInfo(@NotNull PsiElement psiElement) {
        if (!SwanFileUtil.isSwanFile(psiElement)) {
            return null;
        }
        if (psiElement instanceof XmlDocument) {
            JSFile jsFile = SwanFileUtil.getTargetJsFile(psiElement);
            if (jsFile == null) {
                return null;
            }
            return new LineMarkerInfo<>(psiElement, psiElement.getTextRange(), AllIcons.FileTypes.JavaScript, null, this,
                    GutterIconRenderer.Alignment.LEFT);
        }
        return null;
    }
}
