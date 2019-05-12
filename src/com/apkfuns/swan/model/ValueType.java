package com.apkfuns.swan.model;

import org.jetbrains.annotations.Nullable;

/**
 * 数据类型
 */
public enum ValueType {
    NUMBER("Number"),
    BOOLEAN("Boolean"),
    STRING("String"),
    ENUM("Enum"),
    FUNCTION("EventHandle"),
    ARRAY("Array"),
    COLOR("Color"),
    ANY("Any");

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
                if (name.equalsIgnoreCase(pattern.getName())) {
                    return pattern;
                }
            }
        }
        return ValueType.ANY;
    }
}
