package com.apkfuns.swan.lang;

import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.xml.XmlFileTypeFactory;
import org.jetbrains.annotations.NotNull;

public class SwanFileTypeFactory extends XmlFileTypeFactory {
    @Override
    public void createFileTypes(@NotNull FileTypeConsumer fileTypeConsumer) {
        fileTypeConsumer.consume(SwanFileType.INSTANCE, SwanFileType.EXTENSION);
    }
}
