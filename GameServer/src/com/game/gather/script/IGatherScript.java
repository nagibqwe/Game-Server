package com.game.gather.script;

import com.data.bean.Cfg_Gather_Bean;
import com.game.map.structs.MapObject;
import com.game.structs.Gather;
import game.core.map.Position;

/**
 * 处理采集相关的接口
 * Created by zcd on 2018/2/8.
 */
public interface IGatherScript {
    /**
     * 创建一个采集物并放入地图
     * @param map
     * @param bean
     * @param position
     */
    Gather createGather(MapObject map, Cfg_Gather_Bean bean, Position position);
}
