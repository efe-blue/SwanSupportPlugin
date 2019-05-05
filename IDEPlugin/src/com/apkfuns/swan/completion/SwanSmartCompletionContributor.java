package com.apkfuns.swan.completion;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.lang.javascript.completion.JSSmartCompletionContributor;
import com.intellij.lang.javascript.psi.JSReferenceExpression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SwanSmartCompletionContributor extends JSSmartCompletionContributor {
    @Override
    public @Nullable List<LookupElement> getSmartCompletionVariants(@NotNull JSReferenceExpression location) {
        return super.getSmartCompletionVariants(location);
    }
}
