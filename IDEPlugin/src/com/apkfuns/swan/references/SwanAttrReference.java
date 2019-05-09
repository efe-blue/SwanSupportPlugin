package com.apkfuns.swan.references;

import com.apkfuns.swan.model.SwanAttribute;
import com.apkfuns.swan.utils.SwanFileUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.xml.XmlAttributeValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class SwanAttrReference extends PsiReferenceBase<PsiElement> {

    @NotNull
    private final PsiElement element;
    @Nullable
    private final SwanAttribute attribute;
    @NotNull
    private final String varName;

    public SwanAttrReference(@NotNull PsiElement element, @Nullable SwanAttribute attribute,
                             @NotNull String varName) {
        super(element);
        this.element = element;
        this.attribute = attribute;
        this.varName = varName;
    }

    @Override
    public @Nullable PsiElement resolve() {
        if (!(element instanceof XmlAttributeValue)) {
            return null;
        }
        return SwanFileUtil.getJSStatementByName(element, attribute, varName);
    }
}
