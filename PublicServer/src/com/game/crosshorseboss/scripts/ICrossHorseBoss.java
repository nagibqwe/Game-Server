package com.game.crosshorseboss.scripts;

import game.core.script.IScript;
import game.message.CrossHorseBossMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by cxl on 2021/4/14.
 */
public interface ICrossHorseBoss extends IScript {


    /**
     *初始化
     */
    void initData();

    /**
     *时间管理
     */
    void tick();


    /**
     * 请求面板信息
     * @param context
     * @param msg
     */
    void onG2PReqCrossHorseBossPanel(ChannelHandlerContext context,CrossHorseBossMessage.G2PReqCrossHorseBossPanel msg);

    /**
     * 请求进入副本
     * @param context
     * @param msg
     */
    void onG2PReqEnterHorseBoss(ChannelHandlerContext context , CrossHorseBossMessage.G2PReqEnterHorseBoss msg);


    /**
     * 怪物死亡
     * @param context
     * @param msg
     */
    void onF2PReqMonsterDie(ChannelHandlerContext context,CrossHorseBossMessage.F2PReqCrossHorseBossDie msg);


    /**
     *请求关注boss
     */
    void onG2PReqFollowCrossHorseBoss(ChannelHandlerContext context,CrossHorseBossMessage.G2PReqFollowCrossHorseBoss msg);


}
