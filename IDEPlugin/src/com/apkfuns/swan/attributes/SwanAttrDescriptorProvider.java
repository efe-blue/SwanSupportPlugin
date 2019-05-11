package com.apkfuns.swan.attributes;

import com.apkfuns.swan.model.SwanAttribute;
import com.apkfuns.swan.tag.SwanTag;
import com.apkfuns.swan.utils.SwanTagManager;
import com.intellij.psi.xml.XmlTag;
import com.intellij.xml.XmlAttributeDescriptor;
import com.intellij.xml.XmlAttributeDescriptorsProvider;
import com.intellij.xml.impl.schema.AnyXmlAttributeDescriptor;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class SwanAttrDescriptorProvider implements XmlAttributeDescriptorsProvider {
    @Override
    public SwanAttrDescriptor[] getAttributeDescriptors(XmlTag xmlTag) {
        SwanTag swanTag = SwanTagManager.getInstance().getTag(xmlTag.getName());
        if (swanTag != null) {
            Set<SwanAttrDescriptor> descriptorList = new HashSet<>();
            for (SwanAttribute attribute : swanTag.getAttrs()) {
                descriptorList.add(new SwanAttrDescriptor(attribute, xmlTag));
            }
            return descriptorList.toArray(new SwanAttrDescriptor[0]);
        }
        return new SwanAttrDescriptor[0];
    }

    @Override
    public @Nullable XmlAttributeDescriptor getAttributeDescriptor(String attr, XmlTag xmlTag) {
        SwanTag swanTag = SwanTagManager.getInstance().getTag(xmlTag.getName());
        if (swanTag != null) {
            SwanAttribute attribute = swanTag.getAttribute(attr);
            if (attribute != null) {
                return new SwanAttrDescriptor(attribute, xmlTag);
            }
        }
        return new AnyXmlAttributeDescriptor(attr);
    }
}
