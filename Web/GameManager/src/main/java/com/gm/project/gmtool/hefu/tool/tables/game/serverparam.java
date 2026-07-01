package com.gm.project.gmtool.hefu.tool.tables.game;

import com.game.util.VersionUpdateUtil;
import com.gm.project.gmtool.hefu.entiry.HefuServer;
import com.gm.project.gmtool.hefu.tool.tables.interfaces.BaseTableHandler;
import com.gm.project.gmtool.utils.JsonUtils;
import com.gm.project.gmtool.utils.TypeReference;

import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gaozhaoguang
 * @desc 服务器参数,对于冲突数据,默认删除From,特殊的进行特殊处理. 不冲突的话不管
 * @date Created on 2021/1/18 22:04
 **/
public class serverparam  extends BaseTableHandler {

    HashMap<Integer,HashMap<String,String>> fromParams = new HashMap<>();
    HashMap<String,String> toParams = new HashMap<>();
    @Override
    public void doBefore(Object args) throws SQLException {
        WriteLog("开始处理 serverparam 表... ... ");
        List<List<String>> rs = task.getToServer().getDb().query("SELECT paramkey,paramvalue FROM serverparam",2);
        for (List<String> r : rs) {
            toParams.put(r.get(0), r.get(1));
        }

        for(HefuServer server : task.getFromServer()){
            List<List<String>> rsFrom = server.getDb().query("SELECT paramkey,paramvalue FROM serverparam",2);
            HashMap<String,String> tmp = new HashMap<>();
            fromParams.put(server.getServerId(),tmp);
            for(List<String> r : rsFrom){
                tmp.put(r.get(0),r.get(1));
            }
        }
    }

    @Override
    public void doProcess(Object args) throws SQLException {
        //1.先把合并的处理好
        for(HefuServer server : task.getFromServer()){
            processMerge(server);
        }

        //2.处理to数据
        for(String key : toParams.keySet()){
            switch (key){
                case "ServerRoleNameTest":
                case "ServerPkKingRoleID":
                case "ServerTreasureHunt":
                case "ServerAuction":
                case "CangbaogeRecord":
                case "LuckyCardRecord":
                case "guildBattleRate":
                case "guildBattleWin":
                case "UpdateNoticeData":
                case "CangbaogeExChangeData":
                case "FestivalSignTotal": {
                    WriteLog("serverparam:清理目标库关键字:" +key);
                    task.getToServer().getDb().execute("DELETE FROM serverparam WHERE paramkey = '" + key + "'");
                    break;
                }
                case "WeddingNum":
                case "QuestionnaireData":
                case "auctionRoleIDData":
                case "ServerRevel":{
                    WriteLog("serverparam:保存参数值到目标库:" + key);
                    task.getToServer().getDb().execute("UPDATE serverparam set paramvalue = '" + toParams.get(key) + "' WHERE paramkey = '" + key + "'");
                    break;
                }
            }
        }
        //3.处理from的数据库数据清理掉
        task.clearFromData("serverparam");
        WriteLog("serverparam 表处理完毕！");
    }

    /**
     * 处理合并的参数
     * @param server 合并的源数据库
     */
    private void processMerge(HefuServer server){
        HashMap<String, String> source = fromParams.get(server.getServerId());
        HashMap<String, String> target = toParams;
        Integer sid = server.getServerId();
        for(String key:source.keySet()){
            switch (key){
                case "WeddingNum": {
                    //当前服务器结婚的人的数量,这里直接相加
                    Integer val = Integer.parseInt(source.get(key));
                    if(target.containsKey(key)) {
                        val +=Integer.parseInt(target.get(key));
                    }
                    target.put(key,String.valueOf(val));
                    WriteLog("serverparam:服务器结婚的人的数量,这里直接相加:" + key + "=" + val +"========="+sid);
                    break;
                }
                case "QuestionnaireData":
                    //有奖问卷数据
                    try {
                        mergeParam(source,target,key,new TypeReference<ConcurrentHashMap<Long, Long>>(){});
                        WriteLog("serverparam:有奖问卷数据直接合并!"+ key +"========="+sid);

                    } catch (Exception e) {
                        WriteLog("有奖问卷数据错误"+ key +"========="+sid);
                    }
                    break;
                case "auctionRoleIDData":
                    //拍卖行 活动 拍卖 -玩家分红列表
                    try {
                        mergeParam(source,target,key,new TypeReference<ConcurrentHashMap<Long, List<Long>>>(){});
                        WriteLog("serverparam:拍卖行 活动 拍卖 -玩家分红列表直接合并!"+ key+"========="+sid);

                    } catch (Exception e) {
                        WriteLog("记录仙盟拍卖的分红处理错误"+ key +"========="+sid);
                    }
                    break;
                case "ServerRevel":
                    //开服狂欢充值排行榜
                    try {
                        mergeParam(source,target,key,new TypeReference<ConcurrentHashMap<Long,Integer>>(){});
                        WriteLog("serverparam:开服狂欢充值排行榜直接合并!"+ key+"========="+sid);

                    } catch (Exception e) {
                        WriteLog("开服狂欢充值排行榜处理错误"+ key +"========="+sid);
                    }
                    break;
            }
        }
    }

    /**
     * 合并处理通过Json保存的数据结构
     * @param from
     * @param to
     * @param key
     * @param tr
     * @param <T>
     * @throws Exception
     */
    private <T extends AbstractMap> void mergeParam(HashMap<String,String> from,HashMap<String,String> to, String key, TypeReference<T> tr) throws Exception {
        T val = JsonUtils.parseObject(VersionUpdateUtil.dataLoad(from.get(key)), tr);
        if(to.containsKey(key)) {
            T tmp = JsonUtils.parseObject(VersionUpdateUtil.dataLoad(to.get(key)), tr);
            val.putAll(tmp);
        }
        to.put(key,VersionUpdateUtil.dataSave(JsonUtils.toJSONString(val)));
    }


}