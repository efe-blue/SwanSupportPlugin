package com.apkfuns.swan.attributes;

import com.apkfuns.swan.model.SwanAttribute;
import com.apkfuns.swan.model.ValueType;
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlTag;
import com.intellij.xml.impl.BasicXmlAttributeDescriptor;
import com.intellij.xml.impl.XmlAttributeDescriptorEx;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 属性的描述文件
 */
public class SwanAttrDescriptor extends BasicXmlAttributeDescriptor implements XmlAttributeDescriptorEx {

    @NotNull
    private final SwanAttribute swanAttribute;

    public SwanAttrDescriptor(@NotNull SwanAttribute swanAttribute, @NotNull XmlTag xmlTag) {
        this.swanAttribute = swanAttribute;
    }

    @Override
    public @Nullable String handleTargetRename(@NotNull String newTargetName) {
        return newTargetName;
    }

    @Override
    public boolean isRequired() {
        return false;
    }

    @Override
    public boolean hasIdType() {
        return false;
    }

    @Override
    public boolean hasIdRefType() {
        return false;
    }

    @Override
    public boolean isEnumerated() {
        return swanAttribute.getValueType() == ValueType.ENUM || swanAttribute.getValueType() == ValueType.BOOLEAN;
    }

    @Override
    public PsiElement getDeclaration() {
        return null;
    }

    @Override
    public String getName() {
        return swanAttribute.getName();
    }

    @Override
    public void init(PsiElement psiElement) {

    }

    @Override
    public boolean isFixed() {
        return false;
    }

    @Override
    public String getDefaultValue() {
        return swanAttribute.getDefaultValue();
    }

    @Override
    public String[] getEnumeratedValues() {
        if (swanAttribute.getValueType() == ValueType.BOOLEAN) {
            return new String[]{"true", "false"};
        }
        String pattern = swanAttribute.getValuePattern();
        if (pattern != null) {
            return pattern.split("\\|");
        }
        return new String[0];
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SwanAttrDescriptor)) {
            return false;
        }
        return getName().equals(((SwanAttrDescriptor) obj).getName());
    }

    @NotNull
    @Override
    public Object[] getDependencies() {
        return new Object[0];
    }

    @NotNull
    @Override
    public Object[] getDependences() {
        return new Object[0];
    }
}
