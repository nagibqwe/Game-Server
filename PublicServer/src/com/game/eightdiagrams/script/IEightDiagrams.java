package com.game.eightdiagrams.script;

import com.game.gameserver.structs.ServerInfo;
import game.core.script.IScript;
import game.message.EightDiagramsMessage;
import io.netty.channel.ChannelHandlerContext;


/**
 * Created by cxl on 2019/9/20.
 */
public interface IEightDiagrams extends IScript {


    /**
     * 活动开启
     */
    void eightDiagramsStart();
    /**
     * 结束
     */
    void eightDiagramsOver();

    /**
     * 服务器划分
     */
    void eightDiagramsServerMarkOff();

    //准备阶段
    void preparationPhase();

    /**
     * 周期结算
     */
    void periodSettle();

    /**
     * 打开主面板
     */
    void G2PReqEightDiagramsPanel(ChannelHandlerContext context, EightDiagramsMessage.G2PReqEightDiagramsPanel messInfo);

    /**
     * 请求排行数据
     */
    void G2PReqRankPanel(ChannelHandlerContext context, EightDiagramsMessage.G2PReqRankPanel messInfo);

    /**
     *   //请求当前城市 数据信息
     */
    void G2PReqTickMapInfo(ChannelHandlerContext context,EightDiagramsMessage.G2PReqTickMapInfo messInfo);
    /**
     *   进入地图
     */
    void G2PReqEnterEightCityMap(ChannelHandlerContext context,EightDiagramsMessage.G2PReqEnterEightCityMap messInfo );

    /**
     *   成功进入地图
     */
    void F2PResEnterMapSucc(ChannelHandlerContext context, EightDiagramsMessage.F2PResEnterMapSucc message);
    /**
     *   击杀BOSS
     */
    void F2PKillBoss(ChannelHandlerContext context ,EightDiagramsMessage.F2PKillBoss message);
    /**
     *   玩家退出城市
     */
    void F2PPlayerOutCity(ChannelHandlerContext context, EightDiagramsMessage.F2PPlayerOutCity message);


    void F2PSendOverCityInfo(EightDiagramsMessage.F2PSendOverCityInfo message);

    void  loadPeriodRank();

    /**
     * 八级阵图合服修复
     * @param info
     */
    void onEightDiagramRepair(ServerInfo info);


}
