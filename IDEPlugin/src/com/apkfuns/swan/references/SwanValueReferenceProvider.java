package com.apkfuns.swan.references;

import com.apkfuns.swan.utils.SwanFileUtil;
import com.apkfuns.swan.utils.SwanLog;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 支持结点内容跳转, 如<text>{{vars}}</text>
 */
public class SwanValueReferenceProvider extends PsiReferenceProvider {
    @NotNull
    @Override
    public PsiReference[] getReferencesByElement(@NotNull PsiElement psiElement, @NotNull ProcessingContext processingContext) {
        if (psiElement instanceof XmlTag) {
            if (((XmlTag) psiElement).getSubTags().length == 0) {
                String text = ((XmlTag) psiElement).getValue().getText();
                List<PsiReference> references = new ArrayList<>();
                Map<String, TextRange> vars = SwanFileUtil.getVars(text);
                Set<String> varSet = SwanFileUtil.getDataVarNames(psiElement);
                SwanLog.debug("var=" + vars + ", varSet=" + varSet);
                for (Map.Entry<String, TextRange> entry : vars.entrySet()) {
                    if (varSet.contains(entry.getKey())) {
                        references.add(new SwanValueReference((XmlTag) psiElement, entry.getValue(), entry.getKey()));
                    }
                }
                return references.toArray(new PsiReference[0]);
            }
        }
        return new PsiReference[0];
    }
}
