package com.gm.project.gmtool.hefu.tool.tables.game;

import com.gm.project.gmtool.hefu.tool.tables.interfaces.BaseTableHandler;

import java.sql.SQLException;

/**
 * @author gaozhaoguang
 * @desc 商品表 清理掉From
 * @date Created on 2021/1/18 22:04
 **/
public class shop  extends BaseTableHandler {

    @Override
    public void doBefore(Object args) throws SQLException {
        WriteLog("开始处理 shop 表... ... ");
    }

    @Override
    public void doProcess(Object args) throws SQLException {
        task.clearFromData("shop");
        WriteLog("shop 表处理完毕！");
    }

}