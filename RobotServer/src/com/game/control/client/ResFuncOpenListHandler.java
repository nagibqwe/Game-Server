package com.game.control.client;

import com.game.player.structs.Player;
import game.core.command.PlayerHandler;
import game.core.message.RMessage;
import game.message.BackendMessage;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: gouzhongliang
 * @Date: 2021/10/16 11:21
 */
public class ResFuncOpenListHandler extends PlayerHandler<BackendMessage.ResFuncOpenList> {

    @Override
    public void handler(RMessage mess, Player player, BackendMessage.ResFuncOpenList data) {
        player.getFunctionState();
        ConcurrentHashMap<Integer, Boolean> state = new ConcurrentHashMap<>();
        for(BackendMessage.FuncOpenInfo info : data.getFuncOpenListList()){
            state.put(info.getId(), info.getState() == 0 ? false: true);
        }
    }
}
