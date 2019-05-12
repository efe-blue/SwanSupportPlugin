package com.apkfuns.swan.lang;

import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlTag;
import com.intellij.xml.HtmlXmlExtension;
import com.intellij.xml.XmlAttributeDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SwanXmlExtension extends HtmlXmlExtension {
    @Override
    public boolean isCustomTagAllowed(XmlTag tag) {
        return true;
    }

    @Override
    public @NotNull AttributeValuePresentation getAttributeValuePresentation(@Nullable XmlAttributeDescriptor descriptor, @NotNull String defaultAttributeQuote, @NotNull PsiElement context) {
        return super.getAttributeValuePresentation(descriptor, defaultAttributeQuote, context);
    }
}
