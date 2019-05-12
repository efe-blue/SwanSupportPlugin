package com.apkfuns.swan.lang;

import com.intellij.lang.html.HTMLLanguage;
import com.intellij.lang.xml.XMLLanguage;

public class SwanLanguage extends XMLLanguage {

    public static final SwanLanguage INSTANCE = new SwanLanguage();

    protected SwanLanguage() {
        super(HTMLLanguage.INSTANCE, "swan-xml", "application/xml", "text/xml");
    }
}
