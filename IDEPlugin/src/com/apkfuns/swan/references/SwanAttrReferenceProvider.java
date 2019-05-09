package com.apkfuns.swan.references;

import com.apkfuns.swan.model.SwanAttribute;
import com.apkfuns.swan.model.ValueType;
import com.apkfuns.swan.tag.SwanTag;
import com.apkfuns.swan.utils.SwanFileUtil;
import com.apkfuns.swan.utils.SwanLog;
import com.apkfuns.swan.utils.SwanTagManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

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
        String originText = psiElement.getText();
        String text = originText;
        if (text.startsWith("\"") && text.endsWith("\"")) {
            text = text.substring(1, text.length() - 1);
        }
        if (text.startsWith("{{") && text.endsWith("}}")) {
            text = text.substring(2, text.length() - 2);
        }
        if (psiElement.getParent() != null && psiElement.getParent().getParent() != null) {
            PsiElement tag = psiElement.getParent().getParent();
            String attr = null;
            if (psiElement.getContext() != null) {
                attr = ((XmlAttribute) psiElement.getContext()).getName();
            }
            if (attr != null && tag instanceof XmlTag) {
                String tagName = ((XmlTag) tag).getName();
                SwanLog.debug("unNormal tag:" + tagName);
                SwanTag swanTag = SwanTagManager.getInstance().getTag(tagName);
                if (swanTag != null) {
                    SwanAttribute swanAttribute = swanTag.getAttribute(attr);
                    if (swanAttribute != null) {
                        return new PsiReference[]{new SwanAttrReference(psiElement, swanAttribute, text)};
                    } else if (!invalidAttr(attr)) {
                        SwanLog.debug("unNormal attr:" + text);
                        boolean isFunction = !originText.startsWith("{{");
                        SwanAttribute newAttr = null;
                        if (isFunction) {
                            newAttr = new SwanAttribute();
                            newAttr.setValueType(ValueType.FUNCTION);
                        }
                        return new PsiReference[]{new SwanAttrReference(psiElement, newAttr, text)};
                    }
                }
            }
        }
        return new PsiReference[0];
    }

    private boolean invalidAttr(String attrName) {
        return "class".equals(attrName) || "id".equals(attrName);
    }
}
