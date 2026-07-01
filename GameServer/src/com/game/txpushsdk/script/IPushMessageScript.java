package com.game.txpushsdk.script;

import com.game.txpushsdk.struct.PushMessInfo;
import game.core.script.IScript;

/**
 * 发送推送的接口处理
 */
public interface IPushMessageScript extends IScript {

    void dealPush(PushMessInfo pmi);
}
