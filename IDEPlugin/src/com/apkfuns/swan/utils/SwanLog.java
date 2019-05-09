package com.apkfuns.swan.utils;

import com.intellij.notification.*;
import com.intellij.openapi.diagnostic.Logger;


/**
 * 日志管理类
 */
public class SwanLog {
    private static final String NAME = "SwanCheckTagEmptyBodyInspection";
    private static final boolean IS_DEBUG = true;
    private static final Logger LOG = Logger.getInstance(SwanLog.class);

    static {
        NotificationsConfiguration.getNotificationsConfiguration().register(NAME, NotificationDisplayType.NONE);
    }

    public static void debug(String text) {
        if (IS_DEBUG) {
            print(NotificationType.INFORMATION, text);
            LOG.debug(text);
        }
    }

    public static void warn(String text) {
        if (IS_DEBUG) {
            print(NotificationType.WARNING, text);
            LOG.warn(text);
        }
    }

    public static void error(String text) {
        print(NotificationType.ERROR, text);
        LOG.error(text);
    }

    private static void print(NotificationType type, String msg) {
        StackTraceElement ste = new Throwable().getStackTrace()[2];
        String prefix = ste.getFileName();
        int lineNum = ste.getLineNumber();
        String text = "[" + prefix + ":" + lineNum + "] " + msg;
        Notifications.Bus.notify(
                new Notification(NAME, NAME, text, type));
    }
}
