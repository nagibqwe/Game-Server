package com.game.structs;

import com.data.CfgManager;
import com.data.bean.Cfg_Gather_Bean;
import com.data.struct.ReadArray;
import com.game.manager.Manager;
import com.game.map.structs.MapGps;
import com.game.player.structs.Player;
import game.core.map.IMapObject;
import game.core.map.Position;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * 采集物
 */
public class Gather extends GameObject implements IMapObject {

    private static final Logger log = LogManager.getLogger(Gather.class);
    //采集物配置表ID
    private int modelId;
    //采集物名字
    private String name;

    protected MapGps curGps = new MapGps();

    //地点编号
    private int no=0;

    public int getMapModelId() {
        return curGps.getModelId();
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name + "_" + modelId + "_" + curGps.getPos();
    }

    @Override
    public Position gainCurPos() {
        return curGps.getPos();
    }

    public void setCurPos(Position curPos) {
        curGps.setPos(curPos);
    }

    @Override
    public int gainLine() {
        return curGps.getLine();
    }

    @Override
    public int gainMapModelId() {
        return curGps.getModelId();
    }

    @Override
    public long gainMapId() {
        return curGps.getMapId();
    }

    @Override
    public boolean canSee(IMapObject player) {
        if(player instanceof  Player) {
            return Manager.monsterManager.taskIsShow().canSee((Player) player, this);
        }else {
            return true;
        }
    }

    /**
     * 获取任务隐藏id集合
     *
     * @return
     */
    @Override
    public HashMap<Integer, List<Integer>> gainHideTaskIds() {
        final Cfg_Gather_Bean gatherBean = CfgManager.getCfg_Gather_Container().getValueByKey(this.getModelId());

        if (gatherBean.getTakHinde().size() == 0){
            return null;
        }
        HashMap<Integer, List<Integer>> reulst = new HashMap<>(16);
        for (int i = 0; i < gatherBean.getTakHinde().size(); i++) {
            ReadArray<Integer> readArray = gatherBean.getTakHinde().get(i);
            if (reulst.containsKey(readArray.get(0))) {
                reulst.get(readArray.get(0)).add(readArray.get(1));
            } else {
                List<Integer> temp = new ArrayList<>();
                temp.add(readArray.get(1));
                reulst.put(readArray.get(0), temp);
            }
        }
        return reulst;
    }

    public void setMapId(long id) {
        curGps.setMapId(id);
    }

    public void setLine(int lineId) {
        curGps.setLine(lineId);
    }

    public void setMapModelId(int mapModelId) {
        curGps.setModelId(mapModelId);
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    @Override
    public void release() {

    }
}
