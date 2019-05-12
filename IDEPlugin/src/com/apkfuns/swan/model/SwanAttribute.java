package com.apkfuns.swan.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.apkfuns.swan.utils.ValueTypeCodec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

/**
 * 结点的属性描述
 */
public class SwanAttribute {
    @NotNull
    // 属性名称
    private String name = "#";
    @Nullable
    // 属性值匹配模式
    private String valuePattern;
    // 属性值类型
    @NotNull
    @JSONField(serializeUsing = ValueTypeCodec.class, deserializeUsing = ValueTypeCodec.class)
    private ValueType valueType = ValueType.ANY;
    // 参数描述
    @Nullable
    private String desc;
    // 默认值
    @Nullable
    private String defaultValue;

    public SwanAttribute() {
    }

    public SwanAttribute(@NotNull String name, @Nullable String valuePattern, @NotNull ValueType valueType,
                         @Nullable String desc, @Nullable String defaultValue) {
        this.name = name;
        this.valuePattern = valuePattern;
        this.valueType = valueType;
        this.desc = desc;
        this.defaultValue = defaultValue;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    @Nullable
    public String getValuePattern() {
        return valuePattern;
    }

    public void setValuePattern(@Nullable String valuePattern) {
        this.valuePattern = valuePattern;
    }

    @NotNull
    public ValueType getValueType() {
        return valueType;
    }

    public void setValueType(@NotNull ValueType valueType) {
        this.valueType = valueType;
    }

    @Nullable
    public String getDesc() {
        return desc;
    }

    public void setDesc(@Nullable String desc) {
        this.desc = desc;
    }

    @Nullable
    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(@Nullable String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * 值是否和类型匹配
     *
     * @param value value
     * @return true=匹配
     */
    public boolean match(@NotNull String value) {
        switch (valueType) {
            case NUMBER:
                return Pattern.compile("[0-9]+([.][0-9]+)?$").matcher(value).matches();
            case BOOLEAN:
                return Pattern.compile("(true|false)$").matcher(value).matches();
            case ENUM:
            case ANY:
            default:
                if (valuePattern == null) {
                    return false;
                }
                try {
                    return Pattern.compile(valuePattern).matcher(value).matches();
                } catch (Exception e) {
                    return false;
                }
        }
    }
}
