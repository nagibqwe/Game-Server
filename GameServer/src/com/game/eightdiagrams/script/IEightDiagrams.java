package com.game.eightdiagrams.script;

import com.game.player.structs.Player;
import game.message.EightDiagramsMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by 542 on 2019/9/27.
 */
public interface IEightDiagrams {


    void ReqEightDiagramsPanel(Player player, EightDiagramsMessage.ReqEightDiagramsPanel message);

    void  ReqRankPanel(Player player, EightDiagramsMessage.ReqRankPanel message);

    void  ReqTickMapInfo(Player player, EightDiagramsMessage.ReqTickMapInfo message);

    void ReqEnterEightCityMap(Player player, EightDiagramsMessage.ReqEnterEightCityMap message);

    void P2FRepChangeCityMap(EightDiagramsMessage.P2FRepChangeCityMap message);

    void P2GSendEightCityRward(EightDiagramsMessage.P2GSendEightCityRward message);

    void P2FSendEightCityInfo(EightDiagramsMessage.P2FSendEightCityInfo message);

    void P2FReqTickRankPanel(EightDiagramsMessage.P2FReqTickRankPanel message);

    void G2FReqRankPanel(ChannelHandlerContext context,EightDiagramsMessage.G2FReqRankPanel message);

    void F2PSendOverCityInfo();

    void G2FReqEightDiagramsPanel(ChannelHandlerContext context,EightDiagramsMessage.G2FReqEightDiagramsPanel message);

    void eightDiagramsTimerLoop();

    /**
     * //玩家对BOSS伤害
     */
    void playerToBossHurt(long hurt,int groupID,int cityID,long bossCurHP,Player player);

    /**
     * 杀死玩家
     */
    void killPlayer(int groupID,Player player);

    /**
     * 杀死BOSS
     */
    void killBoss(Player player ,int cityID,int groupID,int colorCamp);
    /**
     * 玩家退出
     */
     void playerOutCity(Player player,int cityID,int groupID);
    /**
     * 玩家进入城市
     */
    void enterMapSucc(Player player,int cityID,int groupID);

    void P2FReqRankPanel(EightDiagramsMessage.P2FReqRankPanel message);

    void P2FReqEightDiagramsPanel(EightDiagramsMessage.P2FReqEightDiagramsPanel message);


    void EightDiagramsChangeState(boolean isOpen);

    /**
     * 上线
     * @param player
     */
    void online(Player player);

}
