package com.gm.project.gmtool.hefu.tool.tables.game;

import com.gm.project.gmtool.hefu.tool.tables.interfaces.BaseTableHandler;

import java.sql.SQLException;

/**
 * @author gaozhaoguang
 * @desc 红包信息,全部清理掉
 * @date Created on 2021/1/18 22:03
 **/
public class redpacket extends BaseTableHandler {

    @Override
    public void doBefore(Object args) throws SQLException {
        WriteLog("开始处理 redpacket 表... ... ");
    }

    @Override
    public void doProcess(Object args) throws SQLException {
        task.clearFromData("redpacket");
        task.clearToData("redpacket");
        WriteLog("redpacket 表处理完毕！");
    }
}