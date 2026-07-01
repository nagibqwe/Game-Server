package com.backend.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

@Table("t_function")
public class Function {

    @Column
    @Comment("功能Id")
    private int funcId;

    @Column
    @Comment("功能名")
    private String funcName;

    @Column
    @Comment("父Id")
    private int parentId;

    @Column
    @Comment("开启状态")
    private int openState;

    public int getFuncId() {
        return funcId;
    }

    public void setFuncId(int funcId) {
        this.funcId = funcId;
    }

    public String getFuncName() {
        return funcName;
    }

    public void setFuncName(String funcName) {
        this.funcName = funcName;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getOpenState() {
        return openState;
    }

    public void setOpenState(int openState) {
        this.openState = openState;
    }
}
