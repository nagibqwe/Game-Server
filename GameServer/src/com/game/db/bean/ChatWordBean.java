package com.game.db.bean;

import game.core.db.BaseBean;

/**
 * 聊天词句替换
 */
public class ChatWordBean extends BaseBean {

    private int id;
    private int serverId;
    private String word;//关键词或句子
    private String replace;//替换的词句
    private int type;//0:词 1:句

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getReplace() {
        return replace;
    }

    public void setReplace(String replace) {
        this.replace = replace;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
