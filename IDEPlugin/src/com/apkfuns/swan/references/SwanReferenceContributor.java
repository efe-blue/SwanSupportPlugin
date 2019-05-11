package com.apkfuns.swan.references;

import com.intellij.lang.html.HTMLLanguage;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.patterns.XmlPatterns;
import com.intellij.psi.*;
import com.intellij.psi.xml.XmlAttributeValue;
import org.jetbrains.annotations.NotNull;


public class SwanReferenceContributor extends PsiReferenceContributor {
    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar psiReferenceRegistrar) {
        psiReferenceRegistrar.registerReferenceProvider(
                PlatformPatterns.psiElement(XmlAttributeValue.class)
                        .withLanguage(HTMLLanguage.INSTANCE),
                new SwanAttrReferenceProvider());
        psiReferenceRegistrar.registerReferenceProvider(
                XmlPatterns.xmlTag().withLanguage(HTMLLanguage.INSTANCE)
                        .andNot(XmlPatterns.xmlTag().withLocalName("script"))
                        .andNot(XmlPatterns.xmlTag().withLocalName("style")),
                new SwanValueReferenceProvider()
        );
    }
}
