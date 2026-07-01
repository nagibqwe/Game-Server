/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.redpacket.script;

import com.game.guild.structs.Guild;
import com.game.player.structs.Player;
import com.game.redpacket.structs.RedPacketEnum;
import game.core.script.IScript;
import game.message.RedPacketMessage.ReqClickRedpacket;
import game.message.RedPacketMessage.ReqGetRedPacketInfo;
import game.message.RedPacketMessage.ReqRedpacketList;
import game.message.RedPacketMessage.ReqSendMineRechargeRedpacket;
import game.message.RedPacketMessage.ReqSendRedpacket;

/**
 * 红包脚本处理接口
 *
 * @author xuchangming <xysoko@qq.com>
 */
public interface IRedPacketScript extends IScript {

    /**
     * 点击红包
     * @param player
     * @param mess
     */
    void OnReqClickRedpacket(Player player, ReqClickRedpacket mess);

    /**
     * 获取红包信息
     * @param player
     * @param mess
     */
    void OnReqGetRedPacketInfo(Player player, ReqGetRedPacketInfo mess);

    /**
     * 获取红包列表
     * @param player
     * @param mess
     */
    void OnReqRedpacketList(Player player, ReqRedpacketList mess);

    /**
     * 发红包
     * @param player
     * @param mess
     */
    void OnReqSendRedpacket(Player player, ReqSendRedpacket mess);

    /**
     * 请求发送我的充值红包
     * @param player
     * @param mess
     */
    void OnReqSendMineRechargeRedpacket(Player player, ReqSendMineRechargeRedpacket mess);

    /**
     * 充值发红包
     * @param player
     * @param rItemId
     * @param gold
     */
    void rechargeSendRedpacket(Player player, int rItemId, int gold);

    /**
     * 对红包进行处理删除
     */
    void tick();

    /**
     * 玩家的登陆
     * @param player
     */
    void playerLogin(Player player);

    /**
     * 系统发红包给公会
     * @param player
     * @param guild
     * @param num
     * @param bindGoldNum
     * @return
     */
    boolean sendRedpacket(Player player, Guild guild, int num, int bindGoldNum);

    /**
     * 发红包
     */
    void createRedpacket(Player player, RedPacketEnum redPacketEnum);

    /**
     * 清除过期红包
     */
    void clearExpireRedpacket();

    /**
     * 加入仙盟
     * @param player
     */
    void joinGuild(Player player);
}
