package com.apkfuns.swan.tag;

import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;
import com.intellij.xml.XmlAttributeDescriptor;
import com.intellij.xml.XmlElementDescriptor;
import com.intellij.xml.XmlElementsGroup;
import com.intellij.xml.XmlNSDescriptor;
import org.jetbrains.annotations.Nullable;

public class SwanTagDescriptor implements XmlElementDescriptor {
    @Override
    public String getQualifiedName() {
        return null;
    }

    @Override
    public String getDefaultName() {
        return null;
    }

    @Override
    public XmlElementDescriptor[] getElementsDescriptors(XmlTag xmlTag) {
        return new XmlElementDescriptor[0];
    }

    @Override
    public @Nullable XmlElementDescriptor getElementDescriptor(XmlTag xmlTag, XmlTag xmlTag1) {
        return null;
    }

    @Override
    public XmlAttributeDescriptor[] getAttributesDescriptors(@Nullable XmlTag xmlTag) {
        return new XmlAttributeDescriptor[0];
    }

    @Override
    public @Nullable XmlAttributeDescriptor getAttributeDescriptor(String s, @Nullable XmlTag xmlTag) {
        return null;
    }

    @Override
    public @Nullable XmlAttributeDescriptor getAttributeDescriptor(XmlAttribute xmlAttribute) {
        return null;
    }

    @Override
    public @Nullable XmlNSDescriptor getNSDescriptor() {
        return null;
    }

    @Override
    public @Nullable XmlElementsGroup getTopGroup() {
        return null;
    }

    @Override
    public int getContentType() {
        return 0;
    }

    @Override
    public @Nullable String getDefaultValue() {
        return null;
    }

    @Override
    public PsiElement getDeclaration() {
        return null;
    }

    @Override
    public String getName(PsiElement psiElement) {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void init(PsiElement psiElement) {

    }
}
