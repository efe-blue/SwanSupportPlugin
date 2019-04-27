package com.apkfuns.swan.attributes;

import com.apkfuns.swan.model.SwanAttribute;
import com.apkfuns.swan.tag.SwanTag;
import com.apkfuns.swan.utils.SwanTagManager;
import com.intellij.psi.xml.XmlTag;
import com.intellij.xml.XmlAttributeDescriptor;
import com.intellij.xml.XmlAttributeDescriptorsProvider;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SwanAttrDescriptorProvider implements XmlAttributeDescriptorsProvider {
    @Override
    public SwanAttrDescriptor[] getAttributeDescriptors(XmlTag xmlTag) {
        SwanTag swanTag = SwanTagManager.getInstance().getTag(xmlTag.getName());
        if (swanTag != null) {
            System.out.println(swanTag);
            List<SwanAttrDescriptor> descriptorList = new ArrayList<>();
            for (SwanAttribute attribute : swanTag.getAttrs()) {
                descriptorList.add(new SwanAttrDescriptor(attribute));
            }
            return descriptorList.toArray(new SwanAttrDescriptor[0]);
        }
        return new SwanAttrDescriptor[0];
    }

    @Override
    public @Nullable XmlAttributeDescriptor getAttributeDescriptor(String attr, XmlTag xmlTag) {
        SwanTag swanTag = SwanTagManager.getInstance().getTag(xmlTag.getName());
        if (swanTag != null) {
            System.out.println("2: " + swanTag + ", attr=" + attr);
            SwanAttribute attribute = swanTag.getAttribute(attr);
            if (attribute != null) {
                return new SwanAttrDescriptor(attribute);
            }
        }
        return null;
    }
}
