package com.apkfuns.swan.tag;

import com.apkfuns.swan.attributes.SwanAttrDescriptor;
import com.apkfuns.swan.model.SwanAttribute;
import com.intellij.openapi.util.Condition;
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.xml.XmlAttributeDescriptor;
import com.intellij.xml.XmlElementDescriptor;
import com.intellij.xml.XmlElementsGroup;
import com.intellij.xml.XmlNSDescriptor;
import com.intellij.xml.impl.schema.AnyXmlAttributeDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SwanTagDescriptor implements XmlElementDescriptor {

    private @NotNull SwanTag swanTag;
    private @NotNull XmlTag xmlTag;
    private SwanAttrDescriptor[] attrDescriptors;

    public SwanTagDescriptor(@NotNull SwanTag swanTag, @NotNull XmlTag xmlTag) {
        this.swanTag = swanTag;
        this.xmlTag = xmlTag;
    }

    @Override
    public String getQualifiedName() {
        return getName();
    }

    @Override
    public String getDefaultName() {
        return getName();
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
        if (xmlTag == null) {
            return null;
        }
        if (attrDescriptors != null) {
            return attrDescriptors;
        }
        List<SwanAttribute> swanAttributes = swanTag.getAttrs();
        attrDescriptors = new SwanAttrDescriptor[swanAttributes.size()];
        for (int i = 0; i < attrDescriptors.length; i++) {
            attrDescriptors[i] = new SwanAttrDescriptor(swanAttributes.get(i), xmlTag);
        }
        return attrDescriptors;
    }

    @Override
    public @Nullable XmlAttributeDescriptor getAttributeDescriptor(String attributeName, @Nullable XmlTag xmlTag) {
        XmlAttributeDescriptor descriptor =  ContainerUtil.find(getAttributesDescriptors(xmlTag), new Condition<XmlAttributeDescriptor>() {
            @Override
            public boolean value(XmlAttributeDescriptor descriptor1) {
                return attributeName.equals(descriptor1.getName());
            }
        });
        if (descriptor == null) {
            descriptor = new AnyXmlAttributeDescriptor(attributeName);
        }
        return descriptor;
    }

    @Override
    public @Nullable XmlAttributeDescriptor getAttributeDescriptor(XmlAttribute attribute) {
        return getAttributeDescriptor(attribute.getName(), attribute.getParent());
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
        return CONTENT_TYPE_ANY;
    }

    @Override
    public @Nullable String getDefaultValue() {
        return null;
    }

    @Override
    public PsiElement getDeclaration() {
        return xmlTag;
    }

    @Override
    public String getName(PsiElement psiElement) {
        return getName();
    }

    @Override
    public String getName() {
        return swanTag.getTag();
    }

    @Override
    public void init(PsiElement psiElement) {

    }
}
