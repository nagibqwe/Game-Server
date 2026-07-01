package com.gm.project.gmtool.hefu.tool.tables.game;

import com.gm.project.gmtool.hefu.tool.tables.interfaces.BaseTableHandler;

import java.sql.SQLException;

/**
 * @author gaozhaoguang
 * @desc 个人Boss的死亡记录,暂时逻辑无用,直接合并
 * @date Created on 2021/1/18 21:58
 **/
public class bossdierecord extends BaseTableHandler {

    @Override
    public void doBefore(Object args) throws SQLException {
        WriteLog("开始处理 bossdierecord 表... ... ");
    }

    @Override
    public void doProcess(Object args) throws SQLException {
        WriteLog("bossdierecord 表处理完毕！");
    }
}