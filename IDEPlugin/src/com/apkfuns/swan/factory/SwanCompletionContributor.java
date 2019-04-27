package com.apkfuns.swan.factory;

import com.apkfuns.swan.model.ValueType;
import com.apkfuns.swan.utils.SwanFileUtil;
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
 * 代码自动提醒
 */
public class SwanCompletionContributor extends CompletionContributor {
    public SwanCompletionContributor() {
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(PsiElement.class),
                new CompletionProvider<CompletionParameters>() {
                    @Override
                    protected void addCompletions(@NotNull CompletionParameters completionParameters,
                                                  @NotNull ProcessingContext processingContext,
                                                  @NotNull CompletionResultSet resultSet) {
                        if (!SwanFileUtil.isSwanFile(completionParameters.getPosition())) {
                            return;
                        }
                        PsiElement psiElement = completionParameters.getPosition().getContext();
                        if (psiElement instanceof XmlAttributeValue) {
                            final XmlAttributeValue value = (XmlAttributeValue) psiElement;
                            if (SwanFileUtil.getValueType(value) == ValueType.FUNCTION) {
                                Set<String> functionSet = SwanFileUtil.getAllFunctionNames(value);
                                for (String functionName : functionSet) {
                                    resultSet.addElement(LookupElementBuilder.create(functionName)
                                            .withLookupString(functionName)
                                            .withInsertHandler(new InsertHandler<LookupElement>() {
                                                @Override
                                                public void handleInsert(@NotNull InsertionContext insertionContext,
                                                                         @NotNull LookupElement lookupElement) {
                                                    performInsert(value, insertionContext, lookupElement);
                                                }
                                            })
                                            .withBoldness(true)
                                            .withTypeText("Function"));
                                }
                            } else {

                            }
                        }
                    }
                });
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
