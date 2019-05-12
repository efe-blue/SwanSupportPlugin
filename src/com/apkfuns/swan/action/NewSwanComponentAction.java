package com.apkfuns.swan.action;

import com.apkfuns.swan.utils.SwanBundle;
import com.apkfuns.swan.utils.SwanIcon;

/**
 * 菜单创建 Component
 */
public class NewSwanComponentAction extends NewSwanPageAction {

    public NewSwanComponentAction() {
        super(SwanBundle.message("newComponent.dialog.name"),
                SwanBundle.message("newComponent.dialog.prompt"), SwanIcon.ICON);
    }

    @Override
    protected String[] templateFile() {
        return new String[]{"component.js", "page.css", "component.json", "page.swan"};
    }

    @Override
    protected String getDialogPrompt() {
        return SwanBundle.message("newComponent.dialog.prompt");
    }

    @Override
    protected String getDialogTitle() {
        return SwanBundle.message("newComponent.dialog.title");
    }
}
