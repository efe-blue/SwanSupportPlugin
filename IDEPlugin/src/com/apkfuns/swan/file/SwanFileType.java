package com.apkfuns.swan.file;

import com.intellij.lang.html.HTMLLanguage;
import com.intellij.openapi.fileTypes.LanguageFileType;
import icons.JavaScriptPsiIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class SwanFileType extends LanguageFileType {

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
        return "swan";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return JavaScriptPsiIcons.FileTypes.TypeScriptFile;
    }
}
