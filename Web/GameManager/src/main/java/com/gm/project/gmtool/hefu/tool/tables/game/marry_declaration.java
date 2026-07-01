package com.gm.project.gmtool.hefu.tool.tables.game;

import com.gm.project.gmtool.hefu.tool.tables.interfaces.BaseTableHandler;

import java.sql.SQLException;

/**
 * @author gaozhaoguang
 * @desc 订婚信息,直接合并
 * @date Created on 2021/1/18 22:02
 **/
public class marry_declaration extends BaseTableHandler {

    @Override
    public void doBefore(Object args) throws SQLException {
        WriteLog("开始处理 marry_declaration 表... ... ");
    }

    @Override
    public void doProcess(Object args) throws SQLException {
        WriteLog("marry_declaration 表处理完毕！");
    }

}
