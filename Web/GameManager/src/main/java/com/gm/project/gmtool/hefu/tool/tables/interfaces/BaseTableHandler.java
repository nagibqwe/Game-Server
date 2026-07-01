package com.gm.project.gmtool.hefu.tool.tables.interfaces;

import com.gm.project.gmtool.hefu.HefuTask;

/**
 * @author gaozhaoguang
 * @desc BaseTableHandler
 * @date Created on 2021/1/14 10:07
 **/
public abstract class BaseTableHandler implements ITableHandler {

    protected HefuTask task;

    @Override
    public int getPriority() {
        return 1000;
    }

    protected void WriteLog(String msg){
        task.writeLog(msg);
    }

    public HefuTask getTask() {
        return task;
    }

    public void setTask(HefuTask task) {
        this.task = task;
    }
}
