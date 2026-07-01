package common.gather;

import com.data.bean.Cfg_Gather_Bean;
import com.game.gather.script.IGatherScript;
import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Gather;
import game.core.map.Position;
import game.core.script.IScript;
import game.core.util.IDConfigUtil;

/**
 * @author admin
 */
public class GatherManagerScript implements IScript, IGatherScript {
    /**
     * 获取scriptId
     */
    @Override
    public int getId() {
        return ScriptEnum.GatherManagerBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    /**
     * 创建一个采集物并放入地图
     *
     * @param map
     * @param bean
     * @param position
     */
    @Override
    public Gather createGather(MapObject map, Cfg_Gather_Bean bean, Position position) {
        Gather gather = new Gather();
        gather.setId(IDConfigUtil.getLogId());
        gather.setModelId(bean.getId());
        gather.setCurPos(position);
        gather.setMapId(map.getId());
        gather.setLine(map.getLineId());
        gather.setMapModelId(map.getMapModelId());
        gather.setName(bean.getName());

        //进入地图
        Manager.mapManager.manager().onEnterMap(gather);
        return gather;
    }
}
