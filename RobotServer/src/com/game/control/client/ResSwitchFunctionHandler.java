package com.game.control.client;

import com.game.player.structs.Player;
import game.core.command.PlayerHandler;
import game.core.message.RMessage;
import game.message.BackendMessage;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 功能开关
 * @Auther: gouzhongliang
 * @Date: 2021/8/10 17:25
 */
public class ResSwitchFunctionHandler extends PlayerHandler<BackendMessage.ResSwitchFunction> {

    @Override
    public void handler(RMessage mess, Player player, BackendMessage.ResSwitchFunction data) {
        ConcurrentHashMap<Integer, Boolean> state =  player.getFunctionState();
        for(BackendMessage.FuncOpenInfo info : data.getSwitchFuncListList()){
            state.put(info.getId(), info.getState() == 0 ? false : true);
        }
    }
}
