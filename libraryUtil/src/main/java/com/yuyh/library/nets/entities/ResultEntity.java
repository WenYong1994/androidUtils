package com.yuyh.library.nets.entities;

import com.google.gson.JsonElement;

/**
 * created by arvin on 16/10/24 17:25
 * email：1035407623@qq.com
 */
public class ResultEntity {
    /**
     * 状态码
     */
    private int status;
    /**
     * 处理消息
     */
    private String info;
    /**
     * 内容
     */
    private JsonElement data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public JsonElement getData() {
        return data;
    }

    public void setData(JsonElement data) {
        this.data = data;
    }
}
