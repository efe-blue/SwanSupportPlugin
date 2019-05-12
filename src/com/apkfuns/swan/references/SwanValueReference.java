package com.apkfuns.swan.references;

import com.apkfuns.swan.utils.SwanFileUtil;
import com.apkfuns.swan.utils.SwanIcon;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SwanValueReference extends PsiReferenceBase<XmlTag> {

    private String varName;
    private PsiElement element;
    private TextRange rangeInValue;
    private int offset;

    public SwanValueReference(XmlTag element, TextRange range, String varName) {
        super(element);
        this.rangeInValue = range;
        this.varName = varName;
        this.element = element;
        offset = getRangeInElement().getStartOffset();
    }

    @Override
    public @Nullable PsiElement resolve() {
        setRangeInElement(new TextRange(offset + rangeInValue.getStartOffset(),
                offset + rangeInValue.getEndOffset()));
        return SwanFileUtil.getJSStatementByName(element, null, varName);
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        List<LookupElement> variants = new ArrayList<>();
        for (String var : SwanFileUtil.getDataVarNames(element)) {
            variants.add(LookupElementBuilder.create("{{" + var + "}}").
                    withIcon(SwanIcon.ICON).
                    withTypeText(var)
            );
        }
        return variants.toArray();
    }
}
