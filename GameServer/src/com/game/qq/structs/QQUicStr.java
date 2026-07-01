package com.game.qq.structs;

/**
 * @Desc TODO
 * @Date 2022/1/4 18:20
 * @Auth ZUncle
 */
public class QQUicStr {
    String text_;
    int text_scene_;

    public QQUicStr() {
    }

    public QQUicStr(String text_, int text_scene_) {
        this.text_ = text_;
        this.text_scene_ = text_scene_;
    }

    public String getText_() {
        return text_;
    }

    public void setText_(String text_) {
        this.text_ = text_;
    }

    public int getText_scene_() {
        return text_scene_;
    }

    public void setText_scene_(int text_scene_) {
        this.text_scene_ = text_scene_;
    }
}
