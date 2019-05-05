package com.apkfuns.swan.utils;

import com.intellij.CommonBundle;
import org.jetbrains.annotations.PropertyKey;

import java.util.ResourceBundle;

/**
 * 加载字符串
 */
public class SwanBundle {

    // bundle name
    private static final String BUNDLE_NAME = "localization.strings";
    // ResourceBundle 对象
    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    private SwanBundle() {
    }

    /**
     * Load a {@link String} from the {@link #BUNDLE} {@link ResourceBundle}.
     *
     * @param key    the key of the resource.
     * @param params the optional parameters for the specific resource.
     * @return the {@link String} value or {@code null} if no resource found for the key.
     */
    public static String message(@PropertyKey(resourceBundle = BUNDLE_NAME) String key, Object... params) {
        return CommonBundle.message(BUNDLE, key, params);
    }
}
