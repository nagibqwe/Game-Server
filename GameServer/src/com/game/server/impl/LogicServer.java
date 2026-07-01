package com.game.server.impl;

import com.game.server.filter.HandlerFilter;
import game.core.thread.TimerThread;


public class LogicServer extends MapServer {

    public LogicServer(ThreadGroup group, String name, TimerThread timerThread, int index) {
        super(group, name, timerThread, index);
    }

    @Override
    protected void init() {
        this.addCommandFilter(new HandlerFilter());
    }


}
