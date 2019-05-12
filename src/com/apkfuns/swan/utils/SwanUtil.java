package com.apkfuns.swan.utils;

import com.intellij.openapi.util.SystemInfo;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class SwanUtil {

    /**
     * 字符串是否为空
     * @param msg string
     * @return bool
     */
    public static boolean isEmpty(String msg) {
        return msg == null || msg.trim().length() == 0;
    }

    /**
     * 打开浏览器
     *
     * @param url
     */
    public static void openUrl(String url) {
        if (SystemInfo.isWindows) {
            try {
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                URI uri = new URI(url);
                Desktop desktop = null;
                if (Desktop.isDesktopSupported()) {
                    desktop = Desktop.getDesktop();
                }
                if (desktop != null)
                    desktop.browse(uri);
            } catch (IOException | URISyntaxException ioe) {
                ioe.printStackTrace();
            }
        }
    }
}
