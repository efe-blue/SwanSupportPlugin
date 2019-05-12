package com.apkfuns.swan.inspection;

import com.apkfuns.swan.utils.SwanFileUtil;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlFileNSInfoProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 自定义 namespace
 */
public class SwanFileNSInfoProvider implements XmlFileNSInfoProvider {

    private static final String[][] NAMESPACES = {
            {"bind", "http://www.w3.org/2001/XMLSchema"},
            {"catch", "http://www.w3.org/2001/XMLSchema"},
    };

    @Nullable
    @Override
    public String[][] getDefaultNamespaces(@NotNull XmlFile xmlFile) {
        if (SwanFileUtil.isSwanFile(xmlFile)) {
            return NAMESPACES;
        }
        return null;
    }

    @Override
    public boolean overrideNamespaceFromDocType(@NotNull XmlFile xmlFile) {
        return false;
    }
}
