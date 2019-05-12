package com.apkfuns.swan.utils;

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

public class TypeHandleListener extends TypedHandlerDelegate {
    @Override
    public @NotNull Result beforeCharTyped(char c, @NotNull Project project,
           @NotNull Editor editor, @NotNull PsiFile file, @NotNull FileType fileType) {
        return super.beforeCharTyped(c, project, editor, file, fileType);
    }
}
