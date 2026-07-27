package com.gm.project.gmtool.hefu.tool.tables.game;

import com.gm.project.gmtool.hefu.tool.tables.interfaces.BaseTableHandler;

import java.sql.SQLException;

/**
 * @author gaozhaoguang
 * @desc 角色活动数据,清理From
 * @date Created on 2021/1/18 22:04
 **/
public class roleactivitydata  extends BaseTableHandler {

    @Override
    public void doBefore(Object args) throws SQLException {
        WriteLog("开始处理 roleactivitydata 表... ... ");
    }

    @Override
    public void doProcess(Object args) throws SQLException {
        task.clearFromData("roleactivitydata");
        WriteLog("roleactivitydata 表处理完毕！");
    }
}
