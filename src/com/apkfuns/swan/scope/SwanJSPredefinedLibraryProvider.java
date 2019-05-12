package com.apkfuns.swan.scope;

import com.intellij.lang.javascript.library.JSPredefinedLibraryProvider;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.webcore.libraries.ScriptingLibraryModel;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 预置的swan library
 */
public class SwanJSPredefinedLibraryProvider extends JSPredefinedLibraryProvider {

    private String[] LIBRARIES = {"/libraries/swan.global.ts", "/libraries/swan.d.ts"};

    @NotNull
    @Override
    public ScriptingLibraryModel[] getPredefinedLibraries(@NotNull Project project) {
        List<VirtualFile> virtualFiles = new ArrayList<>();
        for (String libFileName : LIBRARIES) {
            URL fileUrl = SwanJSPredefinedLibraryProvider.class.getResource(libFileName);
            virtualFiles.add(VfsUtil.findFileByURL(fileUrl));
        }
        ScriptingLibraryModel scriptingLibraryModel = ScriptingLibraryModel.createPredefinedLibrary("SwanLibrary", virtualFiles.toArray(new VirtualFile[0]),
                true);
        return new ScriptingLibraryModel[]{scriptingLibraryModel};
    }
}
