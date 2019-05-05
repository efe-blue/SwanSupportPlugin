package com.apkfuns.swan.attributes;

import com.apkfuns.swan.model.SwanAttribute;
import com.apkfuns.swan.model.ValueType;

public class DefaultAttrDescriptor {

    // 支持的默认属性
    public static final SwanAttribute[] DEFAULT_ATTRS = {
            new SwanAttribute("class", null, ValueType.ANY, "class", null),
            new SwanAttribute("id", null, ValueType.ANY, "class", null),
            new SwanAttribute("style", null, ValueType.ANY, "style", null),
            new SwanAttribute("s-if", null, ValueType.BOOLEAN, null, null),
            new SwanAttribute("s-elif", null, ValueType.BOOLEAN, null, null),
            new SwanAttribute("s-else", null, ValueType.BOOLEAN, null, null),
            new SwanAttribute("s-for", null, ValueType.ANY, null, null),
            new SwanAttribute("bindtap", null, ValueType.FUNCTION, null, null),
            new SwanAttribute("bindlongtap", null, ValueType.FUNCTION, null, null),
            new SwanAttribute("bindlongpress", null, ValueType.FUNCTION, null, null),
            new SwanAttribute("bindtouchstart", null, ValueType.FUNCTION, null, null),
            new SwanAttribute("bindtouchmove", null, ValueType.FUNCTION, null, null),
            new SwanAttribute("bindtouchcancel", null, ValueType.FUNCTION, null, null),
            new SwanAttribute("bindtouchend", null, ValueType.FUNCTION, null, null),
            new SwanAttribute("bindtouchforcechange", null, ValueType.FUNCTION, null, null),
            new SwanAttribute("catchtap", null, ValueType.FUNCTION, null, null),
            new SwanAttribute("catchlongtap", null, ValueType.FUNCTION, null, null),
            new SwanAttribute("catchlongpress", null, ValueType.FUNCTION, null, null),
            new SwanAttribute("catchtouchstart", null, ValueType.FUNCTION, null, null),
            new SwanAttribute("catchtouchmove", null, ValueType.FUNCTION, null, null),
            new SwanAttribute("catchtouchcancel", null, ValueType.FUNCTION, null, null),
            new SwanAttribute("catchtouchend", null, ValueType.FUNCTION, null, null),
            new SwanAttribute("catchtouchforcechange", null, ValueType.FUNCTION, null, null),
    };
}
