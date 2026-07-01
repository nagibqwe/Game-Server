package com.gm.project.gmtool.hefu.tool.tables.game;

import com.gm.project.gmtool.hefu.tool.tables.interfaces.BaseTableHandler;

import java.sql.SQLException;

/**
 * @author gaozhaoguang
 * @desc 竞技场信息,全部清理
 * @date Created on 2021/1/18 22:01
 **/
public class jjc extends BaseTableHandler {

    @Override
    public void doBefore(Object args) throws SQLException {
        WriteLog("开始处理 jjc 表... ... ");
    }

    @Override
    public void doProcess(Object args) throws SQLException {
        task.clearFromData("jjc");
        task.clearToData("jjc");
        WriteLog("jjc 表处理完毕！");
    }

}