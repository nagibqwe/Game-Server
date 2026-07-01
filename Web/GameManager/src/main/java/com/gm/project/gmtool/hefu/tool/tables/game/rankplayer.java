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
 * @desc 玩家的排行榜信息,修改名字后直接合并
 * @date Created on 2021/1/18 22:03
 **/
public class rankplayer extends BaseTableHandler {

    @Override
    public void doBefore(Object args) throws SQLException {
        WriteLog("开始处理 rankplayer 表... ... ");
    }

    @Override
    public void doProcess(Object args) throws SQLException {
        //把排行中关于竞技场的全部清掉
        for(HefuServer server : task.getFromServer()){
            processOne(server, task.getRoles().get(server.getServerId()));
            server.getDb().execute("update rankplayer set arenaRank = 0");
        }
        task.getToServer().getDb().execute("update rankplayer set arenaRank = 0");
        WriteLog("rankplayer 表处理完毕！");
    }

    private void processOne(HefuServer server, HashMap<Long,String[]> map)  throws SQLException {
        PreparedStatement update = server.getDb().createPreStat("update rankplayer set name = ? where roleid= ?");
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