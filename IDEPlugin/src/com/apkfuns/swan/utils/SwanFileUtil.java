package com.apkfuns.swan.utils;

import com.apkfuns.swan.lang.SwanFileType;
import com.apkfuns.swan.model.SwanAttribute;
import com.apkfuns.swan.model.ValueType;
import com.apkfuns.swan.tag.SwanTag;
import com.intellij.json.JsonFileType;
import com.intellij.lang.javascript.JavaScriptFileType;
import com.intellij.lang.javascript.psi.*;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.css.CssFileType;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SwanFileUtil {

    /**
     * 是否是swan文件
     *
     * @param element PsiElement
     * @return bool
     */
    public static boolean isSwanFile(PsiElement element) {
        if (element != null) {
            return isSwanFile(element.getContainingFile());
        } else {
            return false;
        }
    }

    /**
     * 是否是swan文件
     *
     * @param psiFile PsiFile
     * @return bool
     */
    public static boolean isSwanFile(PsiFile psiFile) {
        return psiFile != null && psiFile.getName().endsWith(".swan");
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

    @Nullable
    public static VirtualFile getTargetSwanFile(PsiElement psiElement) {
        VirtualFile jsFile = psiElement.getContainingFile().getOriginalFile().getVirtualFile();
        File swanFile = new File(jsFile.getParent().getPath(), jsFile.getNameWithoutExtension() + ".swan");
        if (swanFile.exists() && swanFile.isFile()) {
            return LocalFileSystem.getInstance().refreshAndFindFileByIoFile(swanFile);
        }
        return null;
    }

    /**
     * 获取swan 文件对象的js文件对象
     *
     * @param element swan元素
     * @return JSFile
     */
    @Nullable
    public static JSFile getTargetJsFile(PsiElement element) {
        VirtualFile swanFile = element.getContainingFile().getOriginalFile().getVirtualFile();
        File jsFile = new File(swanFile.getParent().getPath(), swanFile.getNameWithoutExtension() + ".js");
        if (jsFile.exists() && jsFile.isFile()) {
            VirtualFile swanJsFile = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(jsFile);
            if (swanJsFile != null) {
                PsiFile swanPsiFile = PsiManager.getInstance(element.getProject()).findFile(swanJsFile);
                if (swanPsiFile instanceof JSFile) {
                    return (JSFile) swanPsiFile;
                }
            }
        }
        return null;
    }

    /**
     * 获取js文件里面的Page方法参数对象
     *
     * @param matchJsFile js文件
     * @return Page参数
     */
    @Nullable
    public static JSObjectLiteralExpression getPageExpression(@NotNull JSFile matchJsFile) {
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
                            return (JSObjectLiteralExpression) argumentExpressions[0];
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * 获取js文件的page结点的所有属性和方法
     *
     * @param element 当前结点
     * @return 属性列表
     */
    private static JSProperty[] getPageProperties(PsiElement element) {
        JSFile matchJsFile = getTargetJsFile(element);
        if (matchJsFile != null) {
            JSObjectLiteralExpression pageExpression = getPageExpression(matchJsFile);
            if (pageExpression != null) {
                return pageExpression.getProperties();
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

    /**
     * 获取data结点下属性
     *
     * @param element 结点
     * @return data结点下属性名称
     */
    public static Set<String> getDataVarNames(PsiElement element) {
        JSProperty[] properties = getPageProperties(element);
        if (properties != null) {
            Set<String> propertySet = new HashSet<>();
            for (JSProperty property : properties) {
                if (property.getValue() instanceof JSObjectLiteralExpression
                        && "data".equals(property.getName())) {
                    JSProperty[] childProperties = ((JSObjectLiteralExpression) property.getValue()).getProperties();
                    for (JSProperty childProperty : childProperties) {
                        if (childProperty.getValue() instanceof JSLiteralExpression
                                || childProperty.getValue() instanceof JSArrayLiteralExpression
                                || childProperty.getValue() instanceof JSObjectLiteralExpression) {
                            propertySet.add(childProperty.getName());
                        }
                    }
                    break;
                }
            }
            return propertySet;
        }
        return Collections.emptySet();
    }

    /**
     * 查找属性对应的JS表达式
     *
     * @param element       xml结点
     * @param swanAttribute swan 属性
     * @param varName       变量或者方法名称
     * @return JS表达式
     */
    @Nullable
    public static JSElement getJSStatementByName(PsiElement element, @Nullable SwanAttribute swanAttribute,
                                                 @NotNull String varName) {
        JSProperty[] properties = getPageProperties(element);
        if (properties == null) {
            return null;
        }
        if (swanAttribute != null && swanAttribute.getValueType() == ValueType.FUNCTION) {
            for (JSProperty property : properties) {
                if (varName.equals(property.getName())) {
                    return property;
                }
            }
        } else {
            for (JSProperty property : properties) {
                if (property.getValue() instanceof JSObjectLiteralExpression
                        && "data".equals(property.getName())) {
                    SwanLog.debug("varName=" + varName);
                    JSProperty[] childProperties = ((JSObjectLiteralExpression) property.getValue()).getProperties();
                    for (JSProperty childProperty : childProperties) {
                        if (varName.equals(childProperty.getName())) {
                            return childProperty;
                        }
                    }
                    break;
                }
            }
        }
        return null;
    }

    /**
     * 获取文件类型
     *
     * @param fileName 文件名
     * @return LanguageFileType
     */
    public static LanguageFileType getFileType(@NotNull String fileName) {
        CharSequence ext = FileUtil.getExtension(fileName, SwanFileType.EXTENSION);
        if ("js".contentEquals(ext)) {
            return JavaScriptFileType.INSTANCE;
        } else if ("json".contentEquals(ext)) {
            return JsonFileType.INSTANCE;
        } else if ("css".contentEquals(ext)) {
            return CssFileType.INSTANCE;
        } else {
            return SwanFileType.INSTANCE;
        }
    }

    public static Map<String, TextRange> getVars(String src) {
        Map<String, TextRange> results = new IdentityHashMap<>();
        Pattern p = Pattern.compile("\\{\\{.+?}}");
        Matcher m = p.matcher(src);
        while (m.find()) {
            String g = m.group().replaceAll("\\{+", "").replaceAll("\\}+", "").trim();
            TextRange textRange = new TextRange(m.start(), m.end());
            results.put(g, textRange);
        }
        return results;
    }

    /**
     * 获取模板内字符串，非该模式返回空
     *
     * @param value 待检测字符串
     * @return null | 变量内容
     */
    @Nullable
    public static String getMustacheValue(String value) {
        if (value == null) {
            return null;
        }
        Matcher matcher = Pattern.compile("\\{\\{(.*?)}}").matcher(value);
        if (matcher.matches()) {
            return matcher.group(1);
        }
        return null;
    }
}
