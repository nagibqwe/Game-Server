package com.gm.project.gmtool.hefu.tool.tables.game;

import com.gm.project.gmtool.hefu.tool.tables.interfaces.BaseTableHandler;

import java.sql.SQLException;

/**
 * @author gaozhaoguang
 * @desc 活动配置数据,保留当前服的数据,清掉From
 * @date Created on 2021/1/18 21:56
 **/
public class activityconfig extends BaseTableHandler {

    @Override
    public void doBefore(Object args) throws SQLException {
        WriteLog("开始处理 activityconfig 表... ... ");

    }

    @Override
    public void doProcess(Object args) throws SQLException {
        task.clearFromData("activityconfig");
        WriteLog("activityconfig 表处理完毕！");
    }

}
