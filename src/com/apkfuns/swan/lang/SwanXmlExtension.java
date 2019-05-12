package com.apkfuns.swan.lang;

import com.intellij.psi.xml.XmlTag;
import com.intellij.xml.HtmlXmlExtension;

public class SwanXmlExtension extends HtmlXmlExtension {
    @Override
    public boolean isCustomTagAllowed(XmlTag tag) {
        return true;
    }
}
