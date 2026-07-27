package com.gm.project.gmtool.hefu.tool.tables.game;

import com.gm.project.gmtool.hefu.tool.tables.interfaces.BaseTableHandler;

import java.sql.SQLException;

/**
 * @author gaozhaoguang
 * @desc 老的婚姻数据表,逻辑上没有用了,不做处理
 * @date Created on 2021/1/18 22:02
 **/
public class marriage extends BaseTableHandler {

    @Override
    public void doBefore(Object args) throws SQLException {
        WriteLog("开始处理 marriage 表... ... ");
    }

    @Override
    public void doProcess(Object args) throws SQLException {
        WriteLog("marriage 表处理完毕！");
    }

}
