
package game.core.map;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * @author lw
 */
public interface IMapObject extends Serializable {
    /**
     * 获得唯一标识
     *
     * @return
     */
    long getId();

    /**
     * 获得当前线
     *
     * @return
     */
    int gainLine();

    /**
     * 获取当前地图配置表ID
     *
     * @return
     */
    int gainMapModelId();

    /**
     * 获得当前地图id
     *
     * @return
     */
    long gainMapId();

    /**
     * 获取当前对象坐标
     *
     * @return
     */
    Position gainCurPos();

    /**
     * 可见？
     *
     * @param player
     * @return
     */
    boolean canSee(IMapObject player);

    /**
     * 获取任务隐藏id集合
     *
     * @return
     */
    HashMap<Integer, List<Integer>> gainHideTaskIds();
}
