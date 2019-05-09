package com.apkfuns.swan.attributes;

import com.apkfuns.swan.model.SwanAttribute;
import com.apkfuns.swan.model.ValueType;
import com.intellij.psi.PsiElement;
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

    public SwanAttrDescriptor(@NotNull SwanAttribute swanAttribute) {
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
        return swanAttribute.getValueType() == ValueType.ENUM;
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
}
