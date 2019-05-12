package com.apkfuns.swan.references;

import com.apkfuns.swan.utils.SwanLog;
import com.intellij.lang.html.HTMLLanguage;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.patterns.XmlPatterns;
import com.intellij.psi.*;
import com.intellij.psi.xml.XmlAttributeValue;
import org.jetbrains.annotations.NotNull;

/**
 * 打开swan文件的引用
 */
public class SwanReferenceContributor extends PsiReferenceContributor {
    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar psiReferenceRegistrar) {
        SwanLog.debug("registerReferenceProviders");
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
