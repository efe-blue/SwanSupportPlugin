package com.apkfuns.swan.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.apkfuns.swan.utils.ValueTypeCodec;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

/**
 * 结点的属性描述
 */
public class SwanAttribute {
    // 属性名称
    private String name;
    // 属性值匹配模式
    private String valuePattern;
    // 属性值类型
    @JSONField(serializeUsing = ValueTypeCodec.class, deserializeUsing = ValueTypeCodec.class)
    private ValueType valueType = ValueType.ANY;
    // 参数描述
    private String desc;
    // 默认值
    private String defaultValue;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValuePattern() {
        return valuePattern;
    }

    public void setValuePattern(String valuePattern) {
        this.valuePattern = valuePattern;
    }

    public ValueType getValueType() {
        return valueType;
    }

    public void setValueType(ValueType valueType) {
        this.valueType = valueType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * 值是否和类型匹配
     *
     * @param value value
     * @return true=匹配
     */
    public boolean match(@NotNull String value) {
        if (valueType == null) {
            return false;
        }
        switch (valueType) {
            case MUSTACHE:
                return Pattern.compile("\\{\\{.*\\}\\}").matcher(value).matches();
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
