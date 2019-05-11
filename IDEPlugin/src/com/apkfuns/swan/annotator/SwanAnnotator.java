package com.apkfuns.swan.annotator;

import com.apkfuns.swan.model.SwanAttribute;
import com.apkfuns.swan.model.ValueType;
import com.apkfuns.swan.tag.SwanTag;
import com.apkfuns.swan.utils.SwanFileUtil;
import com.apkfuns.swan.utils.SwanLog;
import com.apkfuns.swan.utils.SwanTagManager;
import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import com.intellij.psi.html.HtmlTag;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlDocument;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class SwanAnnotator implements Annotator {

    private @NotNull Set<String> functionCache = null;
    private @NotNull Set<String> variableCache = null;

    @Override
    public void annotate(@NotNull PsiElement psiElement, @NotNull AnnotationHolder annotationHolder) {
        if (!SwanFileUtil.isSwanFile(psiElement)) {
            return;
        }
        functionCache = new HashSet<>();
        variableCache = new HashSet<>();
        if (psiElement instanceof XmlDocument) {
            functionCache.addAll(SwanFileUtil.getAllFunctionNames(psiElement));
            variableCache.addAll(SwanFileUtil.getDataVarNames(psiElement));
            checkStructure(psiElement, annotationHolder);
        }
    }

    /**
     * 递归解析 xml结构
     */
    private void checkStructure(@NotNull PsiElement document, @NotNull AnnotationHolder annotationHolder) {
        PsiElement[] children = document.getChildren();
        for (PsiElement element : children) {
            if (element instanceof HtmlTag) {
                checkAttributes((XmlTag) element, annotationHolder);
                checkStructure(element, annotationHolder);
            }
        }
    }

    /**
     * 检查属性
     *
     * @param xmlTag
     * @param annotationHolder
     */
    private void checkAttributes(@NotNull XmlTag xmlTag, @NotNull AnnotationHolder annotationHolder) {
        SwanTag swanTag = SwanTagManager.getInstance().getTag(xmlTag.getName());
        SwanLog.debug("swanTag=" + swanTag + ", xmlTag=" + xmlTag);
        if (swanTag != null) {
            for (XmlAttribute attr : xmlTag.getAttributes()) {
                String attrName = attr.getName();
                String attrValue = attr.getValue();
                SwanAttribute swanAttribute = swanTag.getAttribute(attrName);
                String bindVar = SwanFileUtil.getMustacheValue(attrValue);
                if (bindVar != null) {
                    if (bindVar.length() == 1) {
//                        LintResult result = new LintResult(LintResultType.PASSED, "Skip repeat tag");
//                        annotationHolder.createErrorAnnotation(xmlTag, "Value must not empty");
                        Annotation annotation = annotationHolder
                                .createErrorAnnotation(attr.getValueElement(), "哈哈哈哈哈");
                        annotation.registerFix(new QuickFixAction(bindVar, ValueType.ANY));
                    }
                }
            }
        } else {
            annotationHolder.createErrorAnnotation(xmlTag, "Unknown Swan components <" + xmlTag.getName() + ">");
        }
    }
}
