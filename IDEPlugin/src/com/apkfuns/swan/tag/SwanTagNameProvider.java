package com.apkfuns.swan.tag;

import com.apkfuns.swan.utils.SwanFileUtil;
import com.apkfuns.swan.utils.SwanIcon;
import com.apkfuns.swan.utils.SwanTagManager;
import com.intellij.codeInsight.completion.XmlTagInsertHandler;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.impl.source.xml.XmlElementDescriptorProvider;
import com.intellij.psi.xml.XmlTag;
import com.intellij.xml.XmlElementDescriptor;
import com.intellij.xml.XmlTagNameProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SwanTagNameProvider implements XmlTagNameProvider, XmlElementDescriptorProvider {
    @Nullable
    @Override
    public XmlElementDescriptor getDescriptor(XmlTag xmlTag) {
        if (!SwanFileUtil.isSwanFile(xmlTag)) {
            return null;
        }
        SwanTag swanTag = SwanTagManager.getInstance().getTag(xmlTag.getName());
        if (swanTag != null) {
            return new SwanTagDescriptor(swanTag, xmlTag);
        }
        return null;
    }

    @Override
    public void addTagNameVariants(List<LookupElement> list, @NotNull XmlTag xmlTag, String prefix) {
        for (SwanTag tag : SwanTagManager.getInstance().getSwanTagList()) {
            LookupElement element = LookupElementBuilder
                    .create(tag.getTag())
                    .withInsertHandler(XmlTagInsertHandler.INSTANCE)
                    .withBoldness(true)
                    .withIcon(SwanIcon.ICON)
                    .withTypeText(tag.getDesc());
            list.add(element);
        }
    }
}
