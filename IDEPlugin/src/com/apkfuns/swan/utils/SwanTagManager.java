package com.apkfuns.swan.utils;

import com.alibaba.fastjson.JSON;
import com.apkfuns.swan.tag.SwanTag;
import com.intellij.openapi.util.io.FileUtil;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SwanTagManager {
    // 单例对象
    private volatile static SwanTagManager singleton;
    // 结点列表
    private final List<SwanTag> swanTagList = new ArrayList<>();
    // SwanTag 缓存
    private final HashMap<String, SwanTag> swanTagCache = new HashMap<>();

    private SwanTagManager() {
        this.parseNodeList();
    }

    public static SwanTagManager getInstance() {
        if (singleton == null) {
            synchronized (SwanTagManager.class) {
                if (singleton == null) {
                    singleton = new SwanTagManager();
                }
            }
        }
        return singleton;
    }

    /**
     * 解析结点列表
     */
    private void parseNodeList() {
        InputStream is = SwanTagManager.class.getResourceAsStream("/directives/component_list.json");
        try {
            String content = FileUtil.loadTextAndClose(is);
            List<SwanTag> parseList = JSON.parseArray(content, SwanTag.class);
            if (parseList != null) {
                swanTagList.addAll(parseList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取 swanTag 列表
     *
     * @return List<SwanTag>
     */
    public List<SwanTag> getSwanTagList() {
        return swanTagList;
    }

    /**
     * 根据名称获取 SwanTag
     *
     * @param tagName name
     * @return SwanTag
     */
    @Nullable
    public SwanTag getTag(String tagName) {
        if (swanTagCache.containsKey(tagName)) {
            return swanTagCache.get(tagName);
        }
        for (SwanTag swanTag : swanTagList) {
            if (swanTag.getTag().equals(tagName)) {
                swanTagCache.put(tagName, swanTag);
                return swanTag;
            }
        }
        return null;
    }
}
