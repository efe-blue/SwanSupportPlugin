package com.apkfuns.swan.references;

import com.apkfuns.swan.utils.SwanFileUtil;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
                return getTextReference(text, psiElement);
            }
        }
        return new PsiReference[0];
    }

    /**
     * 获取文本内{{}} 变量的应用数组
     *
     * @param text       文本内容
     * @param psiElement 当前元素
     * @return 引用数组
     */
    public static PsiReference[] getTextReference(@NotNull String text, @Nullable PsiElement psiElement) {
        List<PsiReference> references = new ArrayList<>();
        Map<String, TextRange> vars = SwanFileUtil.getVars(text);
        if (psiElement == null || vars.isEmpty()) {
            return new PsiReference[0];
        }
        Set<String> varSet = SwanFileUtil.getDataVarNames(psiElement);
        for (Map.Entry<String, TextRange> entry : vars.entrySet()) {
            if (varSet.contains(entry.getKey())) {
                references.add(new SwanValueReference(psiElement, entry.getValue(), entry.getKey()));
            }
        }
        return references.toArray(new PsiReference[0]);
    }
}
