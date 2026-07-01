package com.gm.project.gmtool.hefu.tool.tables.game;

import com.gm.project.gmtool.hefu.tool.tables.interfaces.BaseTableHandler;

import java.sql.SQLException;

/**
 * @author gaozhaoguang
 * @desc 结婚信息 直接合并
 * @date Created on 2021/1/18 22:01
 **/
public class marray extends BaseTableHandler {

    @Override
    public void doBefore(Object args) throws SQLException {
        WriteLog("开始处理 marray 表... ... ");
    }

    @Override
    public void doProcess(Object args) throws SQLException {
        WriteLog("marray 表处理完毕！");
    }
}
