package com.apkfuns.swan.tag;

import com.apkfuns.swan.model.SwanAttribute;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class SwanTag {
    // Tag名称
    private String tag;
    // 文档地址
    private String link;
    // 描述信息
    private String desc;
    // 属性列表
    private List<SwanAttribute> attrs;
    // 父节点
    private List<String> parent;
    // 子节点
    private List<String> child;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @NotNull
    public List<SwanAttribute> getAttrs() {
        if (attrs == null) {
            attrs = new ArrayList<>();
        }
        return attrs;
    }

    public void setAttrs(List<SwanAttribute> attrs) {
        this.attrs = attrs;
    }

    public List<String> getParent() {
        return parent;
    }

    public void setParent(List<String> parent) {
        this.parent = parent;
    }

    public List<String> getChild() {
        return child;
    }

    public void setChild(List<String> child) {
        this.child = child;
    }

    /**
     * 获取参数名称列表
     *
     * @return Set<String>
     */
    @NotNull
    public Set<String> getAttrNames() {
        Set<String> ret = new HashSet<>();
        if (attrs != null) {
            for (SwanAttribute attr : attrs) {
                ret.add(attr.getName());
            }
        }
        return ret;
    }

    /**
     * 根据参数名称获取参数
     *
     * @param name 参数名称
     * @return SwanAttribute
     */
    @Nullable
    public SwanAttribute getAttribute(String name) {
        if (attrs != null) {
            for (SwanAttribute attr : attrs) {
                if (name != null && name.equals(attr.getName())) {
                    return attr;
                }
            }
        }
        return null;
    }

    /**
     * 合并属性
     * @param attrs List<SwanAttribute>
     */
    public void mergeAttr(SwanAttribute... attrs) {
        if (this.attrs == null) {
            this.attrs = Arrays.asList(attrs);
        } else {
            this.attrs.addAll(Arrays.asList(attrs));
        }
    }

    @Override
    public String toString() {
        return "SwanTag{" +
                "tag='" + tag + '\'' +
                ", desc='" + desc + '\'' +
                ", attrs=" + getAttrNames() +
                '}';
    }
}
