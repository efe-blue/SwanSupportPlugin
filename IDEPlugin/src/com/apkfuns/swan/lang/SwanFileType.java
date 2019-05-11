package com.apkfuns.swan.lang;

import com.apkfuns.swan.utils.SwanIcon;
import com.intellij.ide.highlighter.DomSupportEnabled;
import com.intellij.ide.highlighter.XmlLikeFileType;
import com.intellij.lang.html.HTMLLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class SwanFileType extends XmlLikeFileType implements DomSupportEnabled {

    public static final String EXTENSION = "swan";
    public static final SwanFileType INSTANCE = new SwanFileType();

    private SwanFileType() {
        super(HTMLLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "Swan Harmony";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "智能小程序页面展现模板";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return EXTENSION;
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return SwanIcon.ICON;
    }
}
