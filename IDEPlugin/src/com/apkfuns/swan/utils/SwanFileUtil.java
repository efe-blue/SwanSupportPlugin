package com.apkfuns.swan.utils;

import com.apkfuns.swan.model.SwanAttribute;
import com.apkfuns.swan.model.ValueType;
import com.apkfuns.swan.tag.SwanTag;
import com.intellij.lang.javascript.psi.*;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.psi.xml.XmlTag;
import com.sun.codemodel.internal.JStringLiteral;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SwanFileUtil {

    /**
     * 是否是swan文件
     *
     * @param element PsiElement
     * @return bool
     */
    public static boolean isSwanFile(PsiElement element) {
        if (element != null && element.getContainingFile() != null) {
            return element.getContainingFile().getName().endsWith(".swan");
        } else {
            return false;
        }
    }

    /**
     * 获取属性的值类型
     *
     * @param value 属性结点
     * @return ValueType
     */
    public static ValueType getValueType(XmlAttributeValue value) {
        if (value != null && value.getContext() instanceof XmlAttribute) {
            XmlAttribute xmlAttribute = ((XmlAttribute) value.getContext());
            String attrName = xmlAttribute.getName();
            if (xmlAttribute.getContext() instanceof XmlTag) {
                String tagName = ((XmlTag) xmlAttribute.getContext()).getName();
                SwanTag tag = SwanTagManager.getInstance().getTag(tagName);
                if (tag != null) {
                    SwanAttribute attribute = tag.getAttribute(attrName);
                    if (attribute != null) {
                        return attribute.getValueType();
                    }
                }
            }
        }
        return ValueType.ANY;
    }

    /**
     * 获取js文件的page结点的所有属性和方法
     *
     * @param element 当前结点
     * @return 属性列表
     */
    private static JSProperty[] getPageProperties(PsiElement element) {
        VirtualFile swanFile = element.getContainingFile().getOriginalFile().getVirtualFile();
        File jsFile = new File(swanFile.getParent().getPath(), swanFile.getNameWithoutExtension() + ".js");
        if (jsFile.exists() && jsFile.isFile()) {
            VirtualFile swanJsFile = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(jsFile);
            if (swanJsFile != null) {
                PsiFile swanPsiFile = PsiManager.getInstance(element.getProject()).findFile(swanJsFile);
                if (swanPsiFile instanceof JSFile) {
                    JSFile matchJsFile = (JSFile) swanPsiFile;
                    JSSourceElement[] jsSourceElements = matchJsFile.getStatements();
                    for (JSSourceElement sourceElement : jsSourceElements) {
                        if (sourceElement instanceof JSExpressionStatement) {
                            JSExpression expression = ((JSExpressionStatement) sourceElement).getExpression();
                            if (expression instanceof JSCallExpression) {
                                JSCallExpression callExpression = (JSCallExpression) expression;
                                // 找到Page方法
                                if (callExpression.getMethodExpression() != null && "Page".equals(
                                        callExpression.getMethodExpression().getText())) {
                                    JSExpression[] argumentExpressions = callExpression.getArguments();
                                    if (argumentExpressions.length == 1 && argumentExpressions[0] instanceof JSObjectLiteralExpression) {
                                        JSObjectLiteralExpression jsObjectLiteralExpression = (JSObjectLiteralExpression) argumentExpressions[0];
                                        return jsObjectLiteralExpression.getProperties();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * 获取对应JS文件的方法
     *
     * @param element 结点
     * @return 方法名称集合
     */
    @NotNull
    public static Set<String> getAllFunctionNames(PsiElement element) {
        JSProperty[] properties = getPageProperties(element);
        if (properties != null) {
            Set<String> functionSet = new HashSet<>();
            for (JSProperty property : properties) {
                if (property instanceof JSFunctionProperty
                        || property.getValue() instanceof JSFunctionExpression) {
                    functionSet.add(property.getName());
                }
            }
            return functionSet;
        }
        return Collections.emptySet();
    }

    public static Set<String> getAllVarNames(PsiElement element) {
        JSProperty[] properties = getPageProperties(element);
        if (properties != null) {
            Set<String> propertySet = new HashSet<>();
            for (JSProperty property : properties) {
                if (property.getValue() instanceof JSLiteralExpression
                        || property.getValue() instanceof JSArrayLiteralExpression
                        || property.getValue() instanceof JSObjectLiteralExpression) {
                    propertySet.add(property.getName());
                }
            }
            return propertySet;
        }
        return Collections.emptySet();
    }
}
