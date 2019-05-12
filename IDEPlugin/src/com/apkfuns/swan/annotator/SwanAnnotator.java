package com.apkfuns.swan.annotator;

import com.apkfuns.swan.model.SwanAttribute;
import com.apkfuns.swan.model.ValueType;
import com.apkfuns.swan.tag.SwanTag;
import com.apkfuns.swan.utils.SwanFileUtil;
import com.apkfuns.swan.utils.SwanLog;
import com.apkfuns.swan.utils.SwanTagManager;
import com.apkfuns.swan.utils.SwanUtil;
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
import java.util.Objects;
import java.util.Set;

public class SwanAnnotator implements Annotator {

    private Set<String> functionCache = null;
    private Set<String> variableCache = null;
    private Set<String> variableInXmlCache = null;

    @Override
    public void annotate(@NotNull PsiElement psiElement, @NotNull AnnotationHolder annotationHolder) {
        if (!SwanFileUtil.isSwanFile(psiElement)) {
            return;
        }
        functionCache = new HashSet<>();
        variableCache = new HashSet<>();
        if (psiElement instanceof XmlDocument) {
            // 缓存数据集合和方法
            functionCache.addAll(SwanFileUtil.getAllFunctionNames(psiElement));
            variableCache.addAll(SwanFileUtil.getDataVarNames(psiElement));
            variableInXmlCache = new HashSet<>();
            SwanLog.debug("checkStructure  functionCache=" + functionCache + ", variableCache="
            + variableCache + ", variableInXmlCache=" + variableInXmlCache);
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
     * @param xmlTag           xmlTag
     * @param annotationHolder annotationHolder
     */
    private void checkAttributes(@NotNull XmlTag xmlTag, @NotNull AnnotationHolder annotationHolder) {
        SwanTag swanTag = SwanTagManager.getInstance().getTag(xmlTag.getName());
        if (swanTag != null) {
            for (XmlAttribute attr : xmlTag.getAttributes()) {
                String attrName = attr.getName();
                String attrValue = attr.getValue();
                // 统计 s-for 里面的变量
                if ("s-for".equals(attrName) && !SwanUtil.isEmpty(attrValue)) {
                    String[] forVars = attrValue.split(",|\\s");
                    for (String var : forVars) {
                        if ("in".equals(var)) {
                            break;
                        }
                        if (!SwanUtil.isEmpty(var)) {
                            variableInXmlCache.add(var);
                        }
                    }
                }
                SwanAttribute swanAttribute = swanTag.getAttribute(attrName);
                if (swanAttribute != null && swanAttribute.getValueType() == ValueType.FUNCTION) {
                    if (!SwanUtil.isEmpty(attrValue) && !functionCache.contains(attrValue)) {
                        Annotation annotation = annotationHolder
                                .createWarningAnnotation(Objects.requireNonNull(attr.getValueElement()), attrValue + " Not Found");
                        annotation.registerFix(new QuickFixAction(attrValue, ValueType.FUNCTION));
                    }
                } else {
                    String bindVar = SwanFileUtil.getMustacheValue(attrValue);
                    if (bindVar != null) {
                        if (bindVar.trim().length() == 0) {
                            annotationHolder.createErrorAnnotation(Objects.requireNonNull(attr.getValueElement()),
                                    "Value Must Not empty");
                        } else if (!variableCache.contains(bindVar) && !variableInXmlCache.contains(bindVar)
                                && !bindVar.contains(".")) {
                            Annotation annotation = annotationHolder
                                    .createWarningAnnotation(Objects.requireNonNull(attr.getValueElement()), bindVar + " Not Found");
                            annotation.registerFix(new QuickFixAction(bindVar, ValueType.ANY));
                        }
                    }
                }
            }
        } else {
            annotationHolder.createErrorAnnotation(xmlTag, "Unknown Swan components <" + xmlTag.getName() + ">");
        }
    }
}
