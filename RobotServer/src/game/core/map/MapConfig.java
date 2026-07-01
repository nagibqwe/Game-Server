package game.core.map;

public class MapConfig
{

    /* 服务器ID值 */
    private int serverId;
    /*线ID值*/
    private int lineId;

    /*地图实例ID值*/
    private int mapId;

    /*地图配置ID值， 有些地图是可以共用一张地图数据的*/
    private int mapModelId;

    public int getServerId()
    {
        return serverId;
    }

    public void setServerId(int serverId)
    {
        this.serverId = serverId;
    }

    public int getLineId()
    {
        return lineId;
    }

    public void setLineId(int lineId)
    {
        this.lineId = lineId;
    }

    public int getMapId()
    {
        return mapId;
    }

    public void setMapId(int mapId)
    {
        this.mapId = mapId;
    }

    public int getMapModelId()
    {
        return mapModelId;
    }

    public void setMapModelId(int mapModelId)
    {
        this.mapModelId = mapModelId;
    }
}
