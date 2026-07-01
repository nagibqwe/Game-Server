/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.txpushsdk.manager;

import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import com.game.server.GameServer;
import com.game.txpushsdk.messageEvent.PushMessEventHandler;
import com.game.txpushsdk.messageEvent.PushMessEventTranslator;
import com.game.txpushsdk.script.IPushMessageScript;
import com.game.txpushsdk.struct.PushMessInfo;
import com.lmax.disruptor.dsl.ProducerType;
import game.core.disruptor.DisruptorNoOrderPoolExecutor;
import game.core.disruptor.TaskEvent;
import game.core.disruptor.TaskEventFactory;
import game.core.script.IScript;

/**
 * 向安卓与IOS设备推送消息
 */
public class PushMessManager {

    public static IPushMessageScript deal() throws Exception {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.PushMessageBaseScript);
        if (is instanceof IPushMessageScript) {
            return (IPushMessageScript) is;
        }
        throw new Exception("没有实现推送消息！");
    }
    private static DisruptorNoOrderPoolExecutor<TaskEvent<PushMessInfo>, Integer, PushMessInfo> pushMessExc = null;

    public static void start() {
        if (GameServer.getInstance().IsFightServer()) {
            return;
        }
        TaskEventFactory<PushMessInfo> tef = new TaskEventFactory<>();
        PushMessEventTranslator fsmet = new PushMessEventTranslator();
        PushMessEventHandler fsmh = new PushMessEventHandler();
        pushMessExc = new DisruptorNoOrderPoolExecutor<>(tef, 8 * 1024, ProducerType.MULTI, fsmh, 2, fsmet);
        pushMessExc.start();
    }

    public static void stop() {
        if (GameServer.getInstance().IsFightServer()) {
            return;
        }
        pushMessExc.stop();
    }

    /**
     * 给指定的设备推送消息
     *
     * @param roleId 角色ID值，用来区别发往那个用户
     * @param type 设备类型 1 是安卓， 2是IOS
     * @param title　信息标题　（安卓需要）
     * @param context 信息内容
     */
    public static void PushMessage(long roleId, int type, String title, String context) {
        if (pushMessExc == null) {
            return;
        }
        PushMessInfo pmi = new PushMessInfo();
        pmi.setBigtype(1);
        pmi.setContext(context);
        pmi.setDectype(type);
        pmi.setTag(Long.toString(roleId));
        pmi.setTitle(title);
        pushMessExc.publishEvent(0, pmi);
    }

    /**
     * 向所有的设备推送消息
     *
     * @param type 设备类型 1 是安卓， 2是IOS
     * @param title　信息标题　（安卓需要）
     * @param context 信息内容
     */
    public static void PushAllMessage(int type, String title, String context) {
        if (pushMessExc == null) {
            return;
        }
        PushMessInfo pmi = new PushMessInfo();
        pmi.setBigtype(2);
        pmi.setContext(context);
        pmi.setDectype(type);
        pmi.setTag("");
        pmi.setTitle(title);
        pushMessExc.publishEvent(1, pmi);
    }

    /**
     * 向所有的设备推送消息
     *
     * @param title　信息标题　（安卓需要）
     * @param context 信息内容
     */
    public static void PushBackEndAllMessage(String title, String context) {
        if (pushMessExc == null) {
            return;
        }
        PushMessInfo pmi = new PushMessInfo();
        pmi.setBigtype(3);
        pmi.setContext(context);
        pmi.setDectype(3);
        pmi.setTag("");
        pmi.setTitle(title);
        pushMessExc.publishEvent(1, pmi);
    }
}
