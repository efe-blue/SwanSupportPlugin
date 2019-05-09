package com.apkfuns.swan.inspection;

import com.intellij.codeInsight.daemon.impl.analysis.XmlUnboundNsPrefixInspection;

public class SwanXmlUnboundNsPrefixInspection extends XmlUnboundNsPrefixInspection {
    @Override
    public boolean isEnabledByDefault() {
        return false;
    }
}
