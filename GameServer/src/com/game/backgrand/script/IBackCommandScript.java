package com.game.backgrand.script;

import game.message.BackendMessage;
import io.netty.channel.Channel;

import java.util.Map;

/**
 * 后台命令过来的执行函数
 *
 * @author soko <xuchangming@haowan123.com>
 */
public interface IBackCommandScript {

    /**
     * 接收后台过来的命令协议
     *
     * @param session session
     * @param cmd     cmd
     */
    void dealBackCommand(Channel session, Map<String, Object> cmd);

    /**
     * gm文档生成
     * @param parm
     */
    String gmBuildGmDoc(Map<String,Object> parm);

    /**
     * gm发送单个活动
     * @param cmdMap
     * @return
     */
    String gmActivitySendMess(Map<String, Object> cmdMap);

    /**
     * 公共服通知游戏服同步封号、白名单等数据
     * @param messInfo
     */
    void P2GNoticeSynData(BackendMessage.P2GNoticeSynData messInfo);
}
