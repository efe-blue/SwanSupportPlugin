package com.apkfuns.swan.references;

import com.apkfuns.swan.model.SwanAttribute;
import com.apkfuns.swan.model.ValueType;
import com.apkfuns.swan.tag.SwanTag;
import com.apkfuns.swan.utils.SwanFileUtil;
import com.apkfuns.swan.utils.SwanTagManager;
import com.apkfuns.swan.utils.SwanUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 支持结点属性跳转, 如<image src='{{src}}'></image>
 */
public class SwanAttrReferenceProvider extends PsiReferenceProvider {
    @NotNull
    @Override
    public PsiReference[] getReferencesByElement(@NotNull PsiElement psiElement, @NotNull ProcessingContext processingContext) {
        if (!SwanFileUtil.isSwanFile(psiElement)) {
            return new PsiReference[0];
        }
        if (psiElement.getParent() != null && psiElement.getParent().getParent() != null) {
            String originText = psiElement.getText();
            String text = originText;
            if (text.startsWith("\"") && text.endsWith("\"")) {
                text = text.substring(1, text.length() - 1);
                originText = text;
            }
            String mustacheValue = SwanFileUtil.getMustacheValue(text);
            if (mustacheValue != null) {
                text = mustacheValue;
            }
            PsiElement tag = psiElement.getParent().getParent();
            String attr = null;
            if (psiElement.getContext() != null) {
                attr = ((XmlAttribute) psiElement.getContext()).getName();
            }
            if (attr != null && tag instanceof XmlTag) {
                if (isHtmlAttr(attr)) {
                    return new PsiReference[0];
                }
                String tagName = ((XmlTag) tag).getName();
                SwanTag swanTag = SwanTagManager.getInstance().getTag(tagName);
                if (swanTag != null) {
                    SwanAttribute swanAttribute = swanTag.getAttribute(attr);
                    if (swanAttribute != null) {
                        return new PsiReference[]{new SwanAttrReference(psiElement, swanAttribute, text)};
                    } else {
                        boolean isFunction = !originText.startsWith("{{") || isFunction4NS(attr);
                        SwanAttribute newAttr = null;
                        if (isFunction) {
                            newAttr = new SwanAttribute(attr, null, ValueType.FUNCTION, null, null);
                        }
                        return new PsiReference[]{new SwanAttrReference(psiElement, newAttr, text)};
                    }
                }
            }
        }
        return new PsiReference[0];
    }

    /**
     * 检查命名空间是否是方法
     *
     * @param attrName 属性名称
     * @return bool
     */
    private boolean isFunction4NS(@Nullable String attrName) {
        if (SwanUtil.isEmpty(attrName)) {
            return false;
        }
        return attrName.startsWith("bind:") || attrName.startsWith("catch:");
    }


    /**
     * 是否是html 的属性
     *
     * @param attrName 属性名称
     * @return bool
     */
    private boolean isHtmlAttr(@Nullable String attrName) {
        return "class".equals(attrName) || "id".equals(attrName) || "style".equals(attrName);
    }
}
