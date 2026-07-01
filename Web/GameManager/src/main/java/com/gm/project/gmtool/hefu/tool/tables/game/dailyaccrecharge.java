package com.gm.project.gmtool.hefu.tool.tables.game;

import com.gm.project.gmtool.hefu.tool.tables.interfaces.BaseTableHandler;

import java.sql.SQLException;

/**
 * @author gaozhaoguang
 * @desc 日常累充数据,暂时逻辑无用,直接合并
 * @date Created on 2021/1/18 21:59
 **/
public class dailyaccrecharge extends BaseTableHandler {

    @Override
    public void doBefore(Object args) throws SQLException {
        WriteLog("开始处理 dailyaccrecharge 表... ... ");
    }

    @Override
    public void doProcess(Object args) throws SQLException {
        WriteLog("dailyaccrecharge 表处理完毕！");
    }

}