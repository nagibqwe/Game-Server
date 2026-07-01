package com.game.ninedaysfocused.script;

import com.game.player.structs.Player;
import game.message.NineDaysFocusedMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by 542 on 2019/7/22.
 */
public interface INineDaysFocused {

    /**
     * 报名进入活动战场
     */
   public  void OnReqNineDaysFocusedEnter(Player player);
    /**
     * 取消报名
     */
   public  void OnReqNineDaysFocusedCancelEnter(Player player);

   public  void onStart();

   public  void onOver();

    /**
     * 任务奖励领取
     */
   public void  getTaskReward(Player player ,int taskID);

    /**
     * 刷新奖励数据
     */
   public void  onF2GSynchroRewardData(NineDaysFocusedMessage.F2GSynchrodata data);


    /**
     * 刷新任务数据
     */
    public void  onF2GSynchroTaskData(NineDaysFocusedMessage.F2GSynchrotask data);

}
