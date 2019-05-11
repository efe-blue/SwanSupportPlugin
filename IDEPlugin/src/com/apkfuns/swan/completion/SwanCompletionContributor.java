package com.apkfuns.swan.completion;

import com.apkfuns.swan.model.ValueType;
import com.apkfuns.swan.utils.SwanFileUtil;
import com.apkfuns.swan.utils.SwanIcon;
import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * 自动提醒对应JS文件的变量
 */
public class SwanCompletionContributor extends CompletionContributor {

    public SwanCompletionContributor() {
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(PsiElement.class),
                new CompletionProvider<CompletionParameters>() {
                    @Override
                    protected void addCompletions(@NotNull CompletionParameters completionParameters,
                                                  @NotNull ProcessingContext processingContext,
                                                  @NotNull CompletionResultSet resultSet) {
                        SwanCompletionContributor.this.addCompletions(completionParameters, processingContext, resultSet);
                    }
                });
    }

    private void addCompletions(@NotNull CompletionParameters completionParameters, @NotNull ProcessingContext processingContext,
                                @NotNull CompletionResultSet resultSet) {
        if (!SwanFileUtil.isSwanFile(completionParameters.getPosition())) {
            return;
        }
        PsiElement psiElement = completionParameters.getPosition().getContext();
        if (psiElement instanceof XmlAttributeValue) {
            final XmlAttributeValue value = (XmlAttributeValue) psiElement;
            ValueType valueType = SwanFileUtil.getValueType(value);
            if (valueType == ValueType.FUNCTION) {
                Set<String> functionSet = SwanFileUtil.getAllFunctionNames(value);
                for (String functionName : functionSet) {
                    resultSet.addElement(LookupElementBuilder.create(functionName)
                            .withLookupString(functionName)
                            .withInsertHandler((insertionContext, lookupElement) -> performInsert(value, insertionContext, lookupElement))
                            .withIcon(SwanIcon.ICON)
                            .withBoldness(true)
                            .withTypeText("Function"));
                }
            } else {
                Set<String> varNames = SwanFileUtil.getDataVarNames(value);
                for (String name : varNames) {
                    resultSet.addElement(LookupElementBuilder.create("{{" + name + "}}")
                            .withLookupString(name)
                            .withInsertHandler((insertionContext, lookupElement) -> performInsert(value, insertionContext, lookupElement))
                            .withIcon(SwanIcon.ICON)
                            .withBoldness(true)
                            .withTypeText("Variable"));
                }
            }
        }
    }

    private void performInsert(XmlAttributeValue value, InsertionContext insertionContext, LookupElement lookupElement) {
        if (value.getText().startsWith("\"")) {
            insertionContext.getDocument().replaceString(
                    value.getTextOffset(),
                    value.getTextOffset() + getTailLength(value) + lookupElement.getLookupString().length(),
                    lookupElement.getLookupString());
        } else {
            insertionContext.getDocument().replaceString(
                    value.getTextOffset() - 1,
                    value.getTextOffset() + getTailLength(value) + lookupElement.getLookupString().length() - 1,
                    "\"" + lookupElement.getLookupString() + "\"");
        }
    }

    private int getTailLength(XmlAttributeValue value) {
        String[] temp = value.getValue().split(CompletionInitializationContext.DUMMY_IDENTIFIER);
        if (temp.length == 2) {
            return temp[1].length();
        }
        return 0;
    }
}
