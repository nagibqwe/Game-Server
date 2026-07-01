package com.gm.project.gmtool.hefu.tool.tables.game;

import com.gm.project.gmtool.hefu.tool.tables.interfaces.BaseTableHandler;

import java.sql.SQLException;

/**
 * @author gaozhaoguang
 * @desc 拍卖行所有数据直接合并
 * @date Created on 2021/1/18 21:58
 **/
public class auction extends BaseTableHandler {

    @Override
    public void doBefore(Object args) throws SQLException {
        WriteLog("开始处理 auction 表... ... ");
    }

    @Override
    public void doProcess(Object args) throws SQLException {
        WriteLog("auction 表处理完毕！");
    }
}