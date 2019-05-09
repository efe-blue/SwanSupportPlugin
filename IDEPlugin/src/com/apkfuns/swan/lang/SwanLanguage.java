package com.apkfuns.swan.lang;

import com.intellij.ide.highlighter.XmlFileHighlighter;
import com.intellij.lang.html.HTMLLanguage;
import com.intellij.lang.xml.XMLLanguage;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SwanLanguage extends XMLLanguage {

    public static final SwanLanguage INSTANCE = new SwanLanguage();

    protected SwanLanguage() {
        super(HTMLLanguage.INSTANCE, "swan-xml", "application/xml", "text/xml");
        SyntaxHighlighterFactory.LANGUAGE_FACTORY.addExplicitExtension(this, new SyntaxHighlighterFactory() {
            @Override
            public @NotNull SyntaxHighlighter getSyntaxHighlighter(@Nullable Project project, @Nullable VirtualFile virtualFile) {
                    return new XmlFileHighlighter();
            }
        });
    }
}
