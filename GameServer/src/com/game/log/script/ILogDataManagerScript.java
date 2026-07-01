package com.game.log.script;

import com.game.log.db.ItemCountLog;
import game.core.script.IScript;

import java.util.Collection;

public interface ILogDataManagerScript extends IScript {

    /**
     * 开始（程序运行时执行）
     */
    public void start();

    /**
     * 停服时执行
     */
    public void stop();

    /**
     * 物品变更
     * @param serverId 服务id
     * @param modelId 道具id
     * @param type 类型
     * @param name 名称
     * @param oldNum 旧的数量
     * @param afterNum 新的数量
     */
    public void onItemChange(int serverId,int modelId, int type, String name, long oldNum, long afterNum);

    /**
     * 跨天处理
     */
    public void crossDay();

    public void saveAllByInterval(Collection<ItemCountLog> items);
}
