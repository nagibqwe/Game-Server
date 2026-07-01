package game.core.map;

/**
 * 坐标点
 */
public class Position {
    /**
     * 只用于需要一个POSITION来占位的环境下调用
     * 此静态对象不能请不要使用setx, sety去改变坐标
     */
    public final static Position ZEROPOS = new Position(0, 0);

    private float x = 0.0f;
    private float y = 0.0f;

    public Position() {
    }

    public Position(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public int ceilX() {
        return (int) x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public int ceilY() {
        return (int) y;
    }

    public void setY(float y) {
        this.y = y;

    }

    //加法
    public Position add(Position add) {
        this.x += add.getX();
        this.y += add.getY();
        return this;
    }

    public Position add_builder(Position add) {
        return new Position(x + add.getX(), y + add.getY());
    }

    //减法九 零一起 玩 www.901 75 .com
    public Position dec(Position add) {
        this.x -= add.getX();
        this.y -= add.getY();
        return this;
    }

    public Position dec_builder(Position add) {
        return new Position(x - add.getX(), y - add.getY());
    }

    //乘法
    public Position pro(float value) {
        this.x *= value;
        this.y *= value;
        return this;
    }

    public Position pro_builder(float value) {
        return new Position(x * value, y * value);
    }

    public void rand(int rand) {
        rand = (int) Math.pow(10, rand);
        this.x = (float) Math.round(x * rand) / rand;
        this.y = (float) Math.round(y * rand) / rand;
    }

    /**
     * 是否是同一个格子
     *
     * @param pos
     * @return
     */
    public boolean compare(Position pos) {
        return (int) this.x == (int) pos.getX() && (int) this.y == (int) pos.getY();
    }

    /**
     * 是否是同一个格子
     *
     * @param x
     * @param y
     * @return
     */
    public boolean compare(int x, int y) {
        return (int) this.x == x && (int) this.y == y;
    }

    @Override
    public String toString() {
        return "{" + x + "," + y + "}";
    }
}
