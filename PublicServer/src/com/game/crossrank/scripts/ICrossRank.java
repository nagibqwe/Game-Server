package com.game.crossrank.scripts;

import com.game.db.bean.CrossRankBean;
import com.game.gameserver.structs.ServerInfo;
import game.core.script.IScript;
import game.message.CrossRankMessage;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by cxl on 2020/4/9.
 */
public interface ICrossRank  extends IScript {

    /**
     * 排行
     */
   void crossRankSort();

    /**
     * 游戏服同步过来的玩家数据
     */
    void onReqG2PSyncCrossRankInfo(ChannelHandlerContext context,CrossRankMessage.ReqG2PSyncCrossRankInfo messInfo);

    /**
     * 请求跨服排行
     */
    void onReqG2PCrossRankInfo(ChannelHandlerContext context,CrossRankMessage.ReqG2PCrossRankInfo messInfo);

    /**
     * 写入数据
     */
    void updateSql();


    /**
     * 计算跨服世界等级
     */
    void calcAllCrossWorldLv();

    /**
     * 计算单个服务器
     */
    void calcSingleServerWorldLv(ServerInfo serverInfo);


   /**
    * 合服后跨服排行榜修复
    * @param serverInfo
    */
   void onRepairCrossRank(ServerInfo serverInfo);

}
