package com.kits.project.serverList.domain;

public class NoticeList {
    private long auto;
    private String content;
    private String title;
    private long type;

    public long getAuto() {
        return auto;
    }

    public void setAuto(long auto) {
        this.auto = auto;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }
}
