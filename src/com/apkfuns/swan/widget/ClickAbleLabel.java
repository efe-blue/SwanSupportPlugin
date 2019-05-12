package com.apkfuns.swan.widget;

import com.apkfuns.swan.utils.SwanUtil;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 支持点击跳转的 JLabel
 */
public class ClickAbleLabel extends JLabel {

    public ClickAbleLabel(String text) {
        this(text, (String) null);
    }

    public ClickAbleLabel(String text, @Nullable String linkUrl) {
        this(text, new Runnable() {
            @Override
            public void run() {
                if (linkUrl != null && linkUrl.trim().length() > 0) {
                    SwanUtil.openUrl(linkUrl);
                }
            }
        });

    }

    public ClickAbleLabel(String text, @Nullable Runnable runnable) {
        super(text);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (runnable != null) {
                    runnable.run();
                }
            }
        });
    }

    /**
     * 设置字体颜色
     *
     * @param color 16进制颜色
     * @return ClickAbleLabel
     */
    public ClickAbleLabel setColor(String color) {
        setForeground(Color.decode(color));
        return this;
    }
}
