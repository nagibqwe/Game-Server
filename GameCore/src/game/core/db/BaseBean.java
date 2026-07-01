package game.core.db;

public class BaseBean
{
    // 条件九 零一起玩 www.9017  5.com
    private transient Object where;

    private long dealTime;

    public Object getWhere()
    {
        return where;
    }

    public void setWhere(Object where)
    {
        this.where = where;
    }

    public long getDealTime()
    {
        return dealTime;
    }

    public void setDealTime(long dealTime)
    {
        this.dealTime = dealTime;
    }

}
