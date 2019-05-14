package com.apkfuns.swan.inspection;

import com.intellij.codeInsight.highlighting.HighlightErrorFilter;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.xml.XmlAttribute;
import org.jetbrains.annotations.NotNull;

/**
 * 过滤错误的高亮
 */
public class SwanHighlightErrorFilter extends HighlightErrorFilter {
    @Override
    public boolean shouldHighlightErrorElement(@NotNull PsiErrorElement psiErrorElement) {
        if ("a term expected".equals(psiErrorElement.getErrorDescription())) {
            XmlAttribute attribute = getStyleXmlAttr(psiErrorElement);
            if (attribute != null && "style".equals(attribute.getName())) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取 style标签的 XmlAttribute
     */
    private XmlAttribute getStyleXmlAttr(@NotNull PsiErrorElement psiErrorElement) {
        try {
            assert psiErrorElement.getContext() != null;
            assert psiErrorElement.getContext().getContext() != null;
            assert psiErrorElement.getContext().getContext().getContext() != null;
            assert psiErrorElement.getContext().getContext().getContext().getContext() != null;
            return ((XmlAttribute) psiErrorElement.getContext().getContext().getContext().getContext().getContext());
        } catch (Throwable e) {
            return null;
        }
    }
}
