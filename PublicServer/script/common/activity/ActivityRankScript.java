/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common.activity;

import com.game.activity.script.IActivityRank;
import com.game.activity.struct.SortRank;
import com.game.db.bean.ActivityRankBean;
import com.game.manager.Manager;
import com.game.script.ScriptEnum;
import com.game.utils.MessageUtils;
import game.message.BackendMessage;
import game.message.BackendMessage.CrossRankData;
import game.message.BackendMessage.P2GResCrossRankData;
import game.message.BackendMessage.P2GResCrossRankIsReceive;
import io.netty.channel.ChannelHandlerContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author zhaibiao
 */
public class ActivityRankScript implements IActivityRank{

    //发送数据过来参加排行
    @Override
    public void OnG2PReqCrossRank(BackendMessage.G2PReqCrossRank mess) {
        long activityId = mess.getId();
        long roleId = mess.getRoleId();
        int fv = mess.getFuntionV();
        int type = mess.getType();
        int rankData = mess.getRankDate();
        //这个活动存在，看看不是这个Id
        ActivityRankBean ard = new ActivityRankBean();
        ard.setFuntionV(fv);
        ard.setId(activityId);
        ard.setRankDate(rankData);
        ard.setRoleId(roleId);
        ard.setType(type);
        ard.setServerId(mess.getServerId());
        ard.setName(mess.getName());
        ard.setPlat(mess.getPlat());
        ard.setSerial(mess.getSerial());
        ActivityRankBean bean = Manager.activityManager.getDao().selectById(ard);
        if(!Manager.activityManager.getExist_activity().containsKey(type)){
            Manager.activityManager.getExist_activity().put(type, 0L);
        }
        long id = Manager.activityManager.getExist_activity().get(type);
        if (activityId != id) {
                //该类型的活Id不一样，删除老的活动Id，保存新的
            //移除内存
            Manager.activityManager.getActivityRank().remove(id);
            //设置新Id
            Manager.activityManager.getExist_activity().put(type, activityId);
            //放入新的数据
            List<ActivityRankBean> list = new ArrayList<>();
            list.add(ard);
            Manager.activityManager.getActivityRank().put(activityId, list);
            //删除数据库，带id的所有数据
            Manager.activityManager.getDao().delete(id);
        } else {
            List<ActivityRankBean> list = Manager.activityManager.getActivityRank().get(activityId);
            boolean isHave  =  true;
            for(ActivityRankBean arb : list){
                if(arb.getRoleId() == roleId){
                    arb.setRankDate(ard.getRankDate());
                    isHave = false;
                    break;
                }
            }
            if(isHave){
                list.add(ard);
            }
            //排行
            Collections.sort(list, new SortRank());
            //保存数据
        }
        if (bean == null) {
            Manager.activityManager.getDao().insert(ard);
        } else {
            bean.setRankDate(ard.getRankDate());
            bean.setSerial(ard.getSerial());
            Manager.activityManager.getDao().update(bean);

        }
    }

    @Override
    public void OnG2PReqCrossRankData(BackendMessage.G2PReqCrossRankData mess) {
        //获取玩家的数据
        long id = mess.getId();
        int type = mess.getType();
        long roleId = mess.getRoleId();
        
        List<ActivityRankBean> arbList = new ArrayList<>();
        int top = 0;
        if(Manager.activityManager.getExist_activity().containsKey(type)){
           long oldId =  Manager.activityManager.getExist_activity().get(type);
           if(id == oldId){
               List<ActivityRankBean> list = Manager.activityManager.getActivityRank().get(id);
               if(!list.isEmpty()){
                   if(list.size()>50){
                       arbList = list.subList(0, 20);
                   }else{
                       arbList.addAll(list);
                   }
                   for(ActivityRankBean arb : list){
                       if(arb.getRoleId()==roleId){
                           top = list.indexOf(arb);
                           break;
                       }
                   }
               }
            }
        }
        //返回的协议
        P2GResCrossRankData.Builder rankData = P2GResCrossRankData.newBuilder();
        
        if(!arbList.isEmpty()){
            int index = 0;
            for(ActivityRankBean ar : arbList){
                rankData.addData(buildCrossRankData(ar,++index));
            }
            rankData.setRank(top);
            rankData.setRoleId(roleId);
            rankData.setType(type);
            ChannelHandlerContext socket = Manager.gameServerManager.GetSession(mess.getPlat(), mess.getServerId());
            MessageUtils.send_to_game(socket, P2GResCrossRankData.MsgID.eMsgID_VALUE, rankData.build().toByteArray());
        }        
    }

    private CrossRankData buildCrossRankData(ActivityRankBean arb,int top){
        CrossRankData.Builder crd = CrossRankData.newBuilder();
        crd.setId(arb.getId());
        crd.setTop(top);
        crd.setName(arb.getName());
        crd.setServerId(arb.getServerId());
        return crd.build();
    }

    @Override
    public void OnG2PReqCrossRankDataIsReceive(BackendMessage.G2PReqCrossRankDataIsReceive mess) {
          //获取玩家的数据
        long id = mess.getId();
        int type = mess.getType();
        long roleId = mess.getRoleId();
        int top = 0;
        if(Manager.activityManager.getExist_activity().containsKey(type)){
           long oldId =  Manager.activityManager.getExist_activity().get(type);
           if(id == oldId){
               List<ActivityRankBean> list = Manager.activityManager.getActivityRank().get(id);
               if(!list.isEmpty()){
                   for(ActivityRankBean arb : list){
                       if(arb.getRoleId()==roleId){
                           top = list.indexOf(arb)+1;
                           break;
                       }
                   }
               }
            }
        }
        //返回的协议
        P2GResCrossRankIsReceive.Builder rankData = P2GResCrossRankIsReceive.newBuilder();
        rankData.setTop(top);
        rankData.setRoleId(roleId);
        rankData.setIsR(top > 0);
        rankData.setActivityId(id);
        ChannelHandlerContext socket = Manager.gameServerManager.GetSession(mess.getPlat(), mess.getServerId());
        MessageUtils.send_to_game(socket, P2GResCrossRankIsReceive.MsgID.eMsgID_VALUE, rankData.build().toByteArray());    
    }
    
    
    @Override
    public int getId() {
        return ScriptEnum.ActivityScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

}
