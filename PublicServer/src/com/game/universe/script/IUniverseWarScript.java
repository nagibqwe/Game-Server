package com.game.universe.script;

import com.data.bean.Cfg_Daily_Bean;
import game.core.script.IScript;
import game.message.CommandMessage;
import game.message.MSG_UniverseMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * 太虚战场脚本
 */
public interface IUniverseWarScript extends IScript {

    /**
     * 创建房间
     */
    void createRoom(Cfg_Daily_Bean bean, int index);

    /**
     * 删除房间
     */
    void removeRoom(Cfg_Daily_Bean bean);

    /**
     * 打开数据面板
     * @param context
     * @param messInfo
     */
    void openPanel(ChannelHandlerContext context, MSG_UniverseMessage.G2PReqUniverseWarPanel messInfo);

    /**
     * 进入地图
     * @param context
     * @param messInfo
     */
    void enterDaily(ChannelHandlerContext context, MSG_UniverseMessage.G2PEnterDaily messInfo);

    /**
     * 开启战场准备的阻挡
     */
    void openReadyBlock();

    void onG2PSynGuildBattleInfo(ChannelHandlerContext context, CommandMessage.G2PSynGuildBattleInfo messInfo);
}
