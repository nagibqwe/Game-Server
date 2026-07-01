package game.core.map;

/**
 *
 * 坐标点
 */
public class Position
{
    /**
     * 只用于需要一个POSITION来占位的环境下调用
     */
    public final static Position ZEROPOS = new Position();

    private float x = 0.0f;
    private float y = 0.0f;

    public Position()
    {
    }

    public Position(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public float getX()
    {
        return x;
    }

    public void setX(float x)
    {
        this.x = x;
    }

    public float getY()
    {
        return y;
    }

    public void setY(float y)
    {
        this.y = y;

    }

    /**
     * 是否是同一个格子
     *
     * @param pos
     * @return
     */
    public boolean compare(Position pos)
    {
        return (int) this.x == (int) pos.getX() && (int) this.y == (int) pos.getY();
    }

    @Override
    public String toString()
    {
        return "{" + x + "," + y + "}";
    }
}
