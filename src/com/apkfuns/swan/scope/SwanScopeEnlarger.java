package com.apkfuns.swan.scope;

import com.apkfuns.swan.lang.SwanFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.ResolveScopeEnlarger;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.search.SearchScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 全局搜索
 */
public class SwanScopeEnlarger extends ResolveScopeEnlarger {
    @Override
    public @Nullable SearchScope getAdditionalResolveScope(@NotNull VirtualFile virtualFile, Project project) {
        if (virtualFile.getFileType() instanceof SwanFileType) {
            SwanJSPredefinedLibraryProvider provider = new SwanJSPredefinedLibraryProvider();
            Iterator<VirtualFile> iterator = provider.getPredefinedLibraryFiles().iterator();
            Set<PsiFile> psiFiles = new HashSet<>();
            while (iterator.hasNext()) {
                PsiFile psiFile = PsiManager.getInstance(project).findFile(iterator.next());
                if (psiFile != null) {
                    psiFiles.add(psiFile);
                }
            }
            if (psiFiles.size() > 0) {
                return new LocalSearchScope(psiFiles.toArray(new PsiFile[0]));
            }
        }
        return null;
    }
}
