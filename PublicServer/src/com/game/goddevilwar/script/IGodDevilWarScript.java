package com.game.goddevilwar.script;

import com.data.bean.Cfg_Clone_map_Bean;
import com.game.zone.structs.ZoneTeam;
import game.core.script.IScript;
import io.netty.channel.ChannelHandlerContext;

public interface IGodDevilWarScript extends IScript {

    boolean enterFightRoom(ChannelHandlerContext context,ZoneTeam zoneTeam, Cfg_Clone_map_Bean bean);

}
