package com.gm.project.gmtool.hefu.tool.tables.interfaces;

import com.gm.project.gmtool.hefu.HefuTask;

import java.sql.SQLException;

public interface ITableHandler {
    /**
     * 获取当前处理的优先级
     * @return
     */
    int getPriority();

    /**
     * 当前处理的前期处理
     * @param args
     * @throws SQLException
     */
    void doBefore(Object args) throws SQLException;

    /**
     * 开始处理
     * @param args
     * @throws SQLException
     */
    void doProcess(Object args) throws SQLException;

    void setTask(HefuTask task);
}
