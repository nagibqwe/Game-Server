package com.gm.project.gmtool.hefu.tool.tables.game;

import com.gm.project.gmtool.hefu.entiry.HefuServer;
import com.gm.project.gmtool.hefu.tool.tables.interfaces.BaseTableHandler;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author gaozhaoguang
 * @desc 角色登录信息,删除掉From同样用户的信息
 * @date Created on 2021/1/18 22:04
 **/
public class roleloginfo  extends BaseTableHandler {

    //冲突的用户ID列表
    HashMap<Integer,List<Long>> conflictUsers = new HashMap<>();

    @Override
    public void doBefore(Object args) throws SQLException {
        WriteLog("开始处理 roleloginfo 表... ... ");
        conflictUsers.clear();
        List<Long> toUserIDs = new ArrayList<>();
        List<List<String>> rs = task.getToServer().getDb().query("SELECT userId FROM roleloginfo",1);
        for (List<String> r : rs) {
            String userID = r.get(0);
            if (userID == null) {
                continue;
            }
            toUserIDs.add(Long.valueOf(userID));
        }
        for(HefuServer server : task.getFromServer()){
            List<List<String>> rsFrom = server.getDb().query("SELECT userId FROM roleloginfo",1);
            List<Long> tmp = new ArrayList<>();
            conflictUsers.put(server.getServerId(),tmp);
            for (List<String> r : rsFrom) {
                String userID = r.get(0);
                if (userID == null) {
                    continue;
                }
                Long uid = Long.valueOf(userID);
                if(toUserIDs.contains(uid)){
                    tmp.add(uid);
                }else{
                    toUserIDs.add(uid);
                }
            }
            WriteLog("roleloginfo 发现冲突的用户ID数量:" + server.getServerId() +":"+ tmp.size());
        }
    }

    @Override
    public void doProcess(Object args) throws SQLException {
        for(HefuServer server : task.getFromServer()){
            processOne(server, conflictUsers.get(server.getServerId()));
        }
        WriteLog("roleloginfo 表处理完毕！");
    }

    private void processOne(HefuServer server, List<Long> list) throws SQLException {
        PreparedStatement delete = server.getDb().createPreStat("DELETE FROM roleloginfo WHERE userId = ?");
        int count = 0;
        for(Long id : list){
            delete.setLong(1,id);
            delete.addBatch();
            count++;
            if(count % 7200==0){
                delete.executeBatch();
                server.getDb().commit();
                delete.clearBatch();
            }
        }
        delete.executeBatch();
        server.getDb().commit();
        delete.clearBatch();
        delete.close();
    }
}
