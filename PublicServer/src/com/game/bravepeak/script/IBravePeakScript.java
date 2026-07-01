package com.game.bravepeak.script;

import com.data.bean.Cfg_Clone_map_Bean;
import com.game.zone.structs.ZoneTeam;
import game.core.script.IScript;
import io.netty.channel.ChannelHandlerContext;

public interface IBravePeakScript extends IScript {
    boolean enterBravePeakRoom(ChannelHandlerContext context, ZoneTeam zt, int modelId, Cfg_Clone_map_Bean bean);
}
