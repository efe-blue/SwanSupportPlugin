package com.apkfuns.swan.model;

import org.jetbrains.annotations.Nullable;

/**
 * 数据类型
 */
public enum ValueType {
    MUSTACHE("mustache"),
    NUMBER("number"),
    BOOLEAN("boolean"),
    ENUM("enum"),
    FUNCTION("function"),
    ANY("any");

    // 值类型
    private String type;

    ValueType(String type) {
        this.type = type;
    }

    public String getName() {
        return type;
    }

    public static ValueType convert(@Nullable String name) {
        if (name != null) {
            for (ValueType pattern : ValueType.values()) {
                if (name.equals(pattern.getName())) {
                    return pattern;
                }
            }
        }
        return ValueType.ANY;
    }
}
