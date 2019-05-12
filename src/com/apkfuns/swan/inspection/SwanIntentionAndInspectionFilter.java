package com.apkfuns.swan.inspection;

import com.intellij.lang.javascript.highlighting.IntentionAndInspectionFilter;
import org.jetbrains.annotations.NotNull;

public class SwanIntentionAndInspectionFilter extends IntentionAndInspectionFilter {
    public SwanIntentionAndInspectionFilter() {
        super();
    }

    @Override
    public boolean isSupportedIntention(@NotNull Class clazz) {
        return super.isSupportedIntention(clazz);
    }

    @Override
    public boolean isSupportedInspection(String inspectionToolId) {
        return super.isSupportedInspection(inspectionToolId);
    }
}
