package com.gm.project.gmtool.hefu.tool.tables.game;

import com.gm.project.gmtool.hefu.entiry.HefuServer;
import com.gm.project.gmtool.hefu.tool.tables.interfaces.BaseTableHandler;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author gaozhaoguang
 * @desc 玩家的简要信息,修改该角色名字之后,直接合并
 * @date Created on 2021/1/18 21:56
 **/
public class playerworldinfo extends BaseTableHandler {

    @Override
    public void doBefore(Object args) throws SQLException {
        WriteLog("开始处理 playerworldinfo 表... ... ");
    }

    @Override
    public void doProcess(Object args) throws SQLException {
        for(HefuServer server : task.getFromServer()){
            processOne(server, task.getRoles().get(server.getServerId()));
        }

        WriteLog("playerworldinfo 表处理完毕！");
    }

    private void processOne(HefuServer server,HashMap<Long,String[]> map)  throws SQLException {
        PreparedStatement update = server.getDb().createPreStat("update playerworldinfo set rolename = ? where roleid= ?");
        Iterator<Map.Entry<Long, String[]>> iter = map.entrySet().iterator();
        int count = 0;
        while(iter.hasNext()){
            Map.Entry<Long,String[]> entry = (iter.next());
            update.setLong(2,entry.getKey());
            update.setString(1,entry.getValue()[1]);
            update.addBatch();
            count++;
            if(count % 7200==0){
                update.executeBatch();
                server.getDb().commit();
                update.clearBatch();

            }
        }
        update.executeBatch();
        server.getDb().commit();
        update.clearBatch();
    }
}