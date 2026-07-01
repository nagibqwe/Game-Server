package com.gm.project.gmtool.hefu.tool.tables.game;

import com.gm.project.gmtool.hefu.tool.tables.interfaces.BaseTableHandler;

import java.sql.SQLException;

/**
 * @author gaozhaoguang
 * @desc  服务器活动数据,保留当前服的数据,清掉From
 * @date Created on 2021/1/18 21:57
 **/
public class activitydata  extends BaseTableHandler {

    @Override
    public void doBefore(Object args) throws SQLException {
        WriteLog("开始处理 activitydata 表... ... ");
    }

    @Override
    public void doProcess(Object args) throws SQLException {
        task.clearFromData("activitydata");
        WriteLog("activitydata 表处理完毕！");
    }
}
